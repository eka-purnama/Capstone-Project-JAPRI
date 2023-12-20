package com.android.japri.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.japri.R
import com.android.japri.data.response.JasaResponseItem
import com.android.japri.databinding.ItemJasaBinding
import com.android.japri.ui.detailjasa.DetailJasaActivity
import com.android.japri.utils.EXTRA_ID
import com.android.japri.utils.loadImageOrPlaceholder

class JasaAdapter : ListAdapter<JasaResponseItem, JasaAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemJasaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val jasa = getItem(position)
        holder.bind(jasa)
    }

    class MyViewHolder(val binding: ItemJasaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(jasa: JasaResponseItem) {
            binding.apply {

                userPhoto.loadImageOrPlaceholder(jasa.photoUrl.toString())
                tvUsername.text = jasa.username
                tvName.text = jasa.name
                tvUserDescription.text = jasa.personalData?.deskripsi

                val rating: Float = when (val avgRating: Any? = jasa.rating?.averageRating) {
                    is Number -> avgRating.toFloat()
                    is Boolean -> if (avgRating) 0f else 0f
                    else -> 0f
                }
                ratingBar.rating = rating

                val combinedSkills: String? = jasa.personalData?.skill?.joinToString(", ")
                tvUserSkill.text = root.context.getString(R.string.user_skill, combinedSkills)

                itemView.setOnClickListener {
                    val intentDetail = Intent(it.context, DetailJasaActivity::class.java)
                    intentDetail.putExtra(EXTRA_ID, jasa.id)
                    it.context.startActivity(intentDetail)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<JasaResponseItem>() {
            override fun areItemsTheSame(oldItem: JasaResponseItem, newItem: JasaResponseItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: JasaResponseItem, newItem: JasaResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}