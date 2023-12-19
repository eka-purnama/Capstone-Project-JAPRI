package com.android.japri.utils

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.japri.R
import com.android.japri.adapter.SkillAdapter
import com.android.japri.databinding.FragmentSkillDialogBinding

class SkillDialogFragment : DialogFragment() {

    private var _binding: FragmentSkillDialogBinding? = null
    private val binding get() = _binding!!

    private var optionDialogListener: OnOptionDialogListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSkillDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val skills = resources.getStringArray(R.array.list_skill)
        val adapter = SkillAdapter(skills)

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerViewSkills.layoutManager = layoutManager
        binding.recyclerViewSkills.adapter = adapter

        binding.btnSave.setOnClickListener {
            optionDialogListener?.onOptionChosen(adapter.getSelectedSkills())
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
