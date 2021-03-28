package com.poe.tradeapp.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.poe.tradeapp.R
import com.poe.tradeapp.core.presentation.BaseFragment
import com.poe.tradeapp.databinding.FragmentEmailVerificationBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EmailVerificationFragment : BaseFragment(R.layout.fragment_email_verification) {

    private var afterRegister = true

    private lateinit var binding: FragmentEmailVerificationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentEmailVerificationBinding.bind(view)
        binding = getBinding()

        sendEmailVerificationLink()
        binding.resendVerification.setOnClickListener {
            sendEmailVerificationLink()
        }
    }

    private fun sendEmailVerificationLink() {
        binding.resendVerification.isEnabled = false
        binding.resendVerification.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.disabledButtonTextColor
            )
        )
        Firebase.auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
            if (it.isSuccessful) {
                lifecycleScope.launch {
                    for (i in RESEND_TIMEOUT until 0 step -1) {
                        binding.resendVerificationTimeout.text =
                            getString(R.string.resend_timeout_text, i)
                        delay(1000L)
                    }
                    binding.resendVerification.isEnabled = true
                    binding.resendVerification.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.primaryTextColor
                        )
                    )
                }
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Email verification link send failed!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (afterRegister) {
            afterRegister = false
            return
        }
        val user = Firebase.auth.currentUser
        user?.reload()?.addOnCompleteListener {
            if (it.isSuccessful && user.isEmailVerified) {
                router.newRootScreen(LoginFragment.newInstance(true))
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Email was not verified yet!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    companion object {
        private const val RESEND_TIMEOUT = 30

        fun newInstance() = FragmentScreen { EmailVerificationFragment() }
    }
}