package com.android.japri.ui.photoprofile

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.android.japri.R
import com.android.japri.data.ResultState
import com.android.japri.databinding.ActivityPhotoProfileBinding
import com.android.japri.ui.ViewModelFactory
import com.android.japri.ui.camera.CameraActivity
import com.android.japri.ui.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.android.japri.utils.EXTRA_ID
import com.android.japri.utils.EXTRA_PHOTO_URL
import com.android.japri.utils.loadImage
import com.android.japri.utils.reduceFileImage
import com.android.japri.utils.showLoading
import com.android.japri.utils.showToast
import com.android.japri.utils.uriToFile
import java.io.File

class PhotoProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhotoProfileBinding

    private lateinit var id: String
    private var currentImageUri: Uri? = null
    private var imageUri: Uri? = null
    private lateinit var imageFile: File

    private val viewModel by viewModels<PhotoProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, getString(R.string.permission_granted), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            CAMERA_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_toolbar_photo_profile_activity)

        id = intent.getStringExtra(EXTRA_ID).toString()

        val imageUrl = intent.getStringExtra(EXTRA_PHOTO_URL)
        imageUri = Uri.parse(imageUrl)

        if (imageUrl.toString().isEmpty()){
            binding.userPhoto.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.user_photo))
        } else {
            binding.userPhoto.loadImage(imageUrl)
        }

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(CAMERA_PERMISSION)
        }

        binding.btnEdit.setOnClickListener{
            showImageSourceDialog()
        }

        binding.btnSave.setOnClickListener {
            if (currentImageUri == null){
                showToast(getString(R.string.no_change_image))
            } else {
                currentImageUri?.let { uri ->
                    imageFile = uriToFile(uri, this).reduceFileImage()
                    Log.d("Image File", "showImage: ${imageFile.path}")
                }
                editPhotoProfile(id, imageFile)
            }
        }
    }

    private fun editPhotoProfile(id: String, imageFile: File){
        viewModel.editPhotoProfile(id, imageFile).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        binding.progressIndicator.showLoading(true)
                    }

                    is ResultState.Success -> {
                        result.data.message?.let { showToast(it) }
                        binding.progressIndicator.showLoading(false)
                        finish()
                    }

                    is ResultState.Error -> {
                        showToast(result.error)
                        binding.progressIndicator.showLoading(false)
                    }
                }
            }
        }
    }

    private fun showImageSourceDialog() {
        val options = resources.getStringArray(R.array.select_photo)

        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.select_photo))
            setItems(options) { dialogInterface: DialogInterface, which: Int ->
                when (which) {
                    0 -> openGallery()
                    1 -> openCameraX()
                }
                dialogInterface.dismiss()
            }
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    private fun openGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            imageUri = null
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun openCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            imageUri = null
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.userPhoto.setImageURI(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    }
}