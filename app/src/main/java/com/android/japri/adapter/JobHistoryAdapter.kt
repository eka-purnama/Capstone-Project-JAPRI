package com.android.japri.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.japri.R
import com.android.japri.data.response.JobHistoryResponseItem
import com.android.japri.databinding.ItemJobBinding
import com.android.japri.ui.detailjob.DetailJobActivity
import com.android.japri.utils.EXTRA_ID
import com.android.japri.utils.convertTimestamp

class JobHistoryAdapter : ListAdapter<JobHistoryResponseItem, JobHistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

    class MyViewHolder(val binding: ItemJobBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: JobHistoryResponseItem) {
            binding.apply {
                val dateUpload = convertTimestamp(history.createdAt?.seconds, history.createdAt?.nanoSeconds)

                tvJobName.text = history.jobName
                tvJobAddress.text = history.address
                tvJobDate.text = root.context.getString(R.string.job_date_time, history.startDay, history.endDay)
                tvJobTime.text = root.context.getString(R.string.job_date_time, history.startTime, history.endTime)
                tvUploadDate.text = root.context.getString(R.string.upload_date, dateUpload)

                itemView.setOnClickListener {
                    val intentDetail = Intent(it.context, DetailJobActivity::class.java)
                    intentDetail.putExtra(EXTRA_ID, history.id)
                    it.context.startActivity(intentDetail)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<JobHistoryResponseItem>() {
            override fun areItemsTheSame(oldItem: JobHistoryResponseItem, newItem: JobHistoryResponseItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: JobHistoryResponseItem, newItem: JobHistoryResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}