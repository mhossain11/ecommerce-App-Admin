package com.faysalh.pkartadmin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.faysalh.pkartadmin.R
import com.faysalh.pkartadmin.adapter.AllOrderAdapter
import com.faysalh.pkartadmin.databinding.ActivityAllOrderBinding
import com.faysalh.pkartadmin.model.AllOrderModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class AllOrderActivity : AppCompatActivity() {
    lateinit var binding: ActivityAllOrderBinding
    lateinit var list:ArrayList<AllOrderModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list=ArrayList()

        Firebase.firestore.collection("allOrders").get()
            .addOnSuccessListener {
            list.clear()
            for (doc in it){
                val data = doc.toObject(AllOrderModel::class.java)
                list.add(data)
            }
                binding.recyclerView.adapter = AllOrderAdapter(this,list)
        }







    }
}