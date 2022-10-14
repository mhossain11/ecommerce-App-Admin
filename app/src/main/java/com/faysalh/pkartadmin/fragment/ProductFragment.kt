package com.faysalh.pkartadmin.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.faysalh.pkartadmin.R
import com.faysalh.pkartadmin.databinding.FragmentProductBinding


class ProductFragment : Fragment() {
    lateinit var binding: FragmentProductBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProductBinding.inflate(layoutInflater)

        binding.floatingActionButton.setOnClickListener {

            findNavController().navigate(R.id.action_productFragment_to_addProductFragment)
        }

        return binding.root
    }

}