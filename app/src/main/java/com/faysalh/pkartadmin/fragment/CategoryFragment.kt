package com.faysalh.pkartadmin.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.faysalh.pkartadmin.R
import com.faysalh.pkartadmin.adapter.CategoryAdapter
import com.faysalh.pkartadmin.databinding.FragmentCategoryBinding
import com.faysalh.pkartadmin.model.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList


class CategoryFragment : Fragment() {
    lateinit var binding : FragmentCategoryBinding
    private var imageUrl : Uri? = null
    private lateinit var dialog : Dialog

    //Gallery Image pick & Show
    val launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == Activity.RESULT_OK){
            imageUrl = it.data!!.data
            binding.imageView.setImageURI(imageUrl)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCategoryBinding.inflate(layoutInflater)

        //progress Bar
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)
        getData()
        binding.imageView.setOnClickListener {

            //Gallery open
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchGalleryActivity.launch(intent)

        }

        binding.button5.setOnClickListener {
            
            validateData(binding.categoryName.text.toString())


        }


        return binding.root
    }

    private fun getData() {
        val list = ArrayList<CategoryModel>()
        Firebase.firestore.collection("categories")
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents){
                    val data = doc.toObject(CategoryModel::class.java)
                    list.add(data!!)
                }
                binding.categoryRecycler.adapter= CategoryAdapter(requireContext(),list)

            }
    }

    private fun validateData(categoryName: String) {
        if (categoryName.isEmpty()){
            Toast.makeText(requireContext(), "Please provide category name", Toast.LENGTH_SHORT).show()
        }else if (imageUrl == null){
            Toast.makeText(requireContext(), "Please select image", Toast.LENGTH_SHORT).show()
        }else{
            uploadImage(categoryName)
        }
    }

    private fun uploadImage(categoryName: String) {
        dialog.show()

        //image format
        val fileName = UUID.randomUUID().toString()+".jpg"

        val refStorage = FirebaseStorage.getInstance().reference.child("category/$fileName")
        refStorage.putFile(imageUrl!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    storeData(image.toString(),categoryName)

                }
            }
            .addOnFailureListener{
                dialog.dismiss()

                Toast.makeText(requireContext(), "Something went wrong with storage", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(Url: String, categoryName: String) {
        //firebase store
        val db = Firebase.firestore

        val data = hashMapOf<String,Any>("img" to Url, "cat" to categoryName)

        //store file name
        db.collection("categories").add(data)

            .addOnSuccessListener {
                dialog.dismiss()
                binding.imageView.setImageDrawable(resources.getDrawable(R.drawable.priview))
                binding.categoryName.text= null
                getData()
                Toast.makeText(requireContext(), "category Added", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went wrong ", Toast.LENGTH_SHORT).show()
            }
    }

}