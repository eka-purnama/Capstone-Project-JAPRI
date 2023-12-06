package com.android.japri.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.japri.data.JobFields
import com.android.japri.databinding.ItemJobFieldBinding

class JobFieldsAdapter : ListAdapter<JobFields, JobFieldsAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemJobFieldBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class MyViewHolder(val binding: ItemJobFieldBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(jobField: JobFields){
            binding.tvJobField.text = jobField.jobFieldName

            itemView.setOnClickListener {
//                val intentDetail = Intent(it.context, DetailUserActivity::class.java)
//                intentDetail.putExtra(DetailUserActivity.USERNAME, "${user.login}")
//                intentDetail.putExtra(DetailUserActivity.AVATAR_URL, "${user.avatarUrl}")
//                intentDetail.putExtra(DetailUserActivity.HTML_URL, "${user.htmlUrl}")
//                it.context.startActivity(intentDetail)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<JobFields>() {
            override fun areItemsTheSame(oldItem: JobFields, newItem: JobFields): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: JobFields, newItem: JobFields): Boolean {
                return oldItem == newItem
            }
        }
    }
}