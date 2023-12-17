package com.android.japri.utils

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.android.japri.databinding.FragmentSkillDialogBinding

class SkillDialogFragment : DialogFragment() {

    private var _binding: FragmentSkillDialogBinding? = null
    private val binding get() = _binding!!

    private var optionDialogListener: OnOptionDialogListener? = null

    private val selectedSkills = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSkillDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSave.setOnClickListener {
            binding.apply {
                selectedSkills.clear()

                val checkBoxes = listOf(
                    cbService, cbLogistic, cbSelling, cbEducation, cbMedia, cbCreative,
                    cbTechnician, cbCarpentry, cbFishery, cbAgriculture, cbFarming
                )

                for (checkBox in checkBoxes) {
                    if (checkBox.isChecked) {
                        selectedSkills.add(checkBox.text.toString())
                    }
                }
            }
            optionDialogListener?.onOptionChosen(selectedSkills.toTypedArray())
            dialog?.dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dialog?.cancel()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnOptionDialogListener) {
            this.optionDialogListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.optionDialogListener = null
    }

    interface OnOptionDialogListener {
        fun onOptionChosen(skills: Array<String>?)
    }
}

