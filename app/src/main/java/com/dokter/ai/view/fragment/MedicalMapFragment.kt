package com.dokter.ai.view.fragment

import android.R.attr.x
import android.R.attr.y
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dokter.ai.databinding.FragmentMedicalMapBinding
import com.dokter.ai.view.viewmodel.HomeViewModel


class MedicalMapFragment : Fragment() {
    private lateinit var binding: FragmentMedicalMapBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = FragmentMedicalMapBinding.inflate(inflater, container, false)

        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            //binding.textHome.text = it
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvHospital.setOnClickListener {
            val uri = "geo:0,0?q=hospital"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
        }
    }
}