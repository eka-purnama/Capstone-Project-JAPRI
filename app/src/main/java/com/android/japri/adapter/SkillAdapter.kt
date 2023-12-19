package com.android.japri.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.android.japri.R

class SkillAdapter(private val skills: Array<String>) : RecyclerView.Adapter<SkillAdapter.ViewHolder>() {

    private val selectedSkills = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_skill_checbox, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val skill = skills[position]
        holder.bind(skill)
    }

    override fun getItemCount(): Int {
        return skills.size
    }

    fun getSelectedSkills(): Array<String> {
        return selectedSkills.toTypedArray()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.job_check_box)

        fun bind(skill: String) {
            checkBox.text = skill
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedSkills.add(skill)
                } else {
                    selectedSkills.remove(skill)
                }
            }
        }
    }
}

