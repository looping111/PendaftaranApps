package com.example.pendaftaranapps.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pendaftaranapps.data.response.DataItem
import com.example.pendaftaranapps.databinding.ItemSiswaBinding

class ListSiswaAdapter: ListAdapter<DataItem, ListSiswaAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(val binding: ItemSiswaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataItem){
            binding.namaSiswa.text = data.nama
            binding.sekolahAsal.text = data.sekolahAsal
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListSiswaAdapter.MyViewHolder {
        val binding = ItemSiswaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListSiswaAdapter.MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)

        val activity = holder.itemView.context as MainActivity
        holder.binding.rvSiswa.setOnClickListener {
            val intent = Intent(activity, AddUpdateActivity::class.java)
            intent.putExtra(AddUpdateActivity.EXTRA_DATA, data)
            activity.startActivity(intent)
        }
    }
    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
             return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}