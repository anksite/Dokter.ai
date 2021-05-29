package com.dokter.ai.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dokter.ai.R
import com.dokter.ai.databinding.ActivityLoginBinding
import com.dokter.ai.util.Cons
import com.dokter.ai.util.SpHelp
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    @Inject lateinit var mSpHelp : SpHelp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            // Choose authentication providers
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
            )

            // Create and launch sign-in intent
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setLogo(R.drawable.ic_launcher_background) // Set logo drawable
                    .setTheme(R.style.Theme_Dokterai) // Set theme
                    .build(),
                RC_SIGN_IN
            )
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                FirebaseAuth.getInstance().currentUser?.let {
                    val userId = it.uid
                    Log.d("onActivityResult", "userId: $userId")
                    mSpHelp.writeString(Cons.ID_USER, userId)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

            } else {
                Log.d("onActivityResult", "login failed")
                if (response == null) {
                    //the user canceled the sign-in flow using the back button.
                    finish()
                } else {
                    val errorCode = response.error?.errorCode
                    Log.d("errorCode", "errorCode: $errorCode")
                }
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 1000
    }
}