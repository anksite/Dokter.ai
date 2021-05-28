package com.dokter.ai.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.dokter.ai.databinding.FragmentAccountBinding
import com.dokter.ai.util.Cons
import com.dokter.ai.util.SpHelp
import com.dokter.ai.view.LoginActivity
import com.dokter.ai.view.viewmodel.NotificationsViewModel
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var notificationsViewModel: NotificationsViewModel

    @Inject lateinit var mSpHelp: SpHelp
    @Inject lateinit var mGlide: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)
        binding = FragmentAccountBinding.inflate(inflater, container, false)

        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            //binding.textNotifications.text = it
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bLogout.setOnClickListener {
            context?.let { ctx ->
                AuthUI.getInstance()
                    .signOut(ctx)
                    .addOnCompleteListener {
                        startActivity(Intent(context, LoginActivity::class.java))
                        activity?.finish()
                    }
            }
        }

        FirebaseAuth.getInstance().currentUser.let{
            binding.apply {
                it?.let {
                    mGlide.load(it.photoUrl).circleCrop().into(this.ivAccount)
                    this.tvName.text = it.displayName
                    this.tvEmail.text = it.email
                }

            }
        }
    }
}