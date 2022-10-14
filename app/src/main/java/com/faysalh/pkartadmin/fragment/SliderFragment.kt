package com.faysalh.pkartadmin.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.faysalh.pkartadmin.R
import com.faysalh.pkartadmin.databinding.FragmentSliderBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.protobuf.Internal
import java.util.*


class SliderFragment: Fragment() {

    lateinit var binding :FragmentSliderBinding
     private var imageUrl :Uri? = null
    private lateinit var dialog :Dialog

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

binding = FragmentSliderBinding.inflate(layoutInflater)

        //progress Bar
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)

        binding.imageView.setOnClickListener {

            //Gallery open
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchGalleryActivity.launch(intent )


        }

        binding.UpldBtn.setOnClickListener {

            if (imageUrl!==null){
                uploadImage(imageUrl!!)

            }else{
                Toast.makeText(requireContext(), "Please select Image", Toast.LENGTH_SHORT).show()

            }

        }

        return binding.root
    }

    private fun uploadImage(uri: Uri) {
        dialog.show()
//Upload from a local file
        //image format
        val fileName = UUID.randomUUID().toString()+".jpg"

        val refStorage = FirebaseStorage.getInstance()
            .reference.child("slider/$fileName")

        refStorage.putFile(uri)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    storeData(image.toString())

                }
            }
            .addOnFailureListener{
                dialog.dismiss()

                Toast.makeText(requireContext(), "Something went wrong with storage", Toast.LENGTH_SHORT).show()
            }

    }

    private fun storeData(image : String) {
       //firebase store
        val db = Firebase.firestore

        val data = hashMapOf<String, Any>( "img" to image)

        //store file name
        db.collection("slider").document("item").set(data)

            .addOnSuccessListener {
                dialog.dismiss()
                binding.imageView.setImageDrawable(resources.getDrawable(R.drawable.priview))
                Toast.makeText(requireContext(), "Slider Updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went wrong ", Toast.LENGTH_SHORT).show()
            }
    }

}