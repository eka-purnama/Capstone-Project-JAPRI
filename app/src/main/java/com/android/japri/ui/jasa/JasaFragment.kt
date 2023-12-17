package com.android.japri.ui.jasa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.japri.databinding.FragmentJasaBinding
import com.android.japri.ui.ViewModelFactory
import com.android.japri.utils.EXTRA_USERNAME

class JasaFragment : Fragment() {

    private var _binding: FragmentJasaBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<JasaViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJasaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = arguments?.getString(EXTRA_USERNAME)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.textJasa.text = query
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.textJasa.text = newText
                return true
            }
        })

    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}