package com.dokter.ai.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dokter.ai.databinding.FragmentDashboardBinding
import com.dokter.ai.view.BMICalculateActivity
import com.dokter.ai.view.ChooseSymptomActivity
import com.dokter.ai.viewmodel.DashboardViewModel

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textDashboard.text = it
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bDiagnosis.setOnClickListener {
            startActivity(Intent(context, ChooseSymptomActivity::class.java))
        }

        binding.bBMI.setOnClickListener {
            startActivity(Intent(context, BMICalculateActivity::class.java))
        }
    }
}