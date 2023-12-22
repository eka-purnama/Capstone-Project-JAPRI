package com.android.japri.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.japri.data.JobField
import com.android.japri.databinding.ItemJobFieldBinding
import com.android.japri.ui.jobfield.JobFieldActivity
import com.android.japri.utils.EXTRA_NAME

class JobFieldsAdapter : ListAdapter<JobField, JobFieldsAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemJobFieldBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class MyViewHolder(val binding: ItemJobFieldBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(jobField: JobField){
            binding.tvJobField.text = jobField.jobFieldName

            itemView.setOnClickListener {
                val intent = Intent(it.context, JobFieldActivity::class.java)
                intent.putExtra(EXTRA_NAME, jobField.jobFieldName)
                it.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<JobField>() {
            override fun areItemsTheSame(oldItem: JobField, newItem: JobField): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: JobField, newItem: JobField): Boolean {
                return oldItem == newItem
            }
        }
    }
}