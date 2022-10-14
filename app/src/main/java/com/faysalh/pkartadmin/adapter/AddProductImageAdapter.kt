package com.faysalh.pkartadmin.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.faysalh.pkartadmin.databinding.ImageItemBinding

class AddProductImageAdapter(private val list: ArrayList<Uri>):RecyclerView.Adapter<AddProductImageAdapter.AddProductImageviewHolder>() {
  inner class AddProductImageviewHolder(val binding:ImageItemBinding) :RecyclerView.ViewHolder(binding.root)



    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddProductImageviewHolder {
     val binding = ImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AddProductImageviewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddProductImageviewHolder, position: Int) {
        holder.binding.itemImg.setImageURI(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}


