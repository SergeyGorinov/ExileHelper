package com.sgorinov.exilehelper.presentation.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sgorinov.exilehelper.R
import com.sgorinov.exilehelper.core.presentation.BaseFragment
import com.sgorinov.exilehelper.core.presentation.dp
import com.sgorinov.exilehelper.databinding.FragmentLoginBinding
import com.sgorinov.exilehelper.presentation.MainActivityViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.regex.Pattern

class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private val viewModel by sharedViewModel<MainActivityViewModel>()

    private val isSignIn by lazy { requireArguments().getBoolean(IS_SIGN_IN_KEY, false) }

    private lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentLoginBinding.bind(view)
        binding = getBinding()

        val dialog = AlertDialog
            .Builder(requireContext(), R.style.AppTheme_AlertDialog)
            .setPositiveButton(
                "Ok"
            ) { dialog, _ ->
                dialog.dismiss()
            }

        binding.accept.text = if (isSignIn) {
            getString(R.string.sign_in_text)
        } else {
            getString(R.string.sign_up_text)
        }
        val drawable = ContextCompat.getDrawable(
            requireActivity(),
            if (isSignIn) R.drawable.sign_in_24 else R.drawable.sign_up_24
        )?.apply {
            setBounds(0, 0, 24.dp, 24.dp)
        }
        binding.accept.setCompoundDrawablesRelative(drawable, null, null, null)
        binding.accept.setOnClickListener {
            if (!binding.email.text.isEmailValid()) {
                dialog.setMessage(R.string.email_not_match_alert_text).show()
                return@setOnClickListener
            }
            if (!binding.password.text.isPasswordValid()) {
                dialog.setMessage(R.string.password_not_match_alert_text).show()
                return@setOnClickListener
            }
            if (isSignIn) {
                Firebase.auth.signInWithEmailAndPassword(
                    binding.email.text.toString(),
                    binding.password.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        lifecycleScope.launch {
                            viewModel.addToken()
                            getMainActivity()?.goToCurrencyExchange()
                        }
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "Authentication failed!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                Firebase.auth.createUserWithEmailAndPassword(
                    binding.email.text.toString(),
                    binding.password.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        router.navigateTo(EmailVerificationFragment.newInstance())
                    } else {
                        AlertDialog.Builder(requireActivity(), R.style.AppTheme_AlertDialog)
                            .setTitle("Registration error")
                            .setMessage(it.exception?.message ?: "Failed")
                            .setPositiveButton("OK") { dialogInterface, _ ->
                                dialogInterface.dismiss()
                            }
                            .show()
                    }
                }
            }
        }
    }

    private fun CharSequence?.isEmailValid(): Boolean {
        return !isNullOrBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun CharSequence?.isPasswordValid(): Boolean {
        return !isNullOrBlank() && passwordPattern.matcher(this).matches()
    }

    private val passwordPattern = Pattern.compile("([a-zA-Z0-9]|[!@#$%^&+=]){6,20}")

    companion object {
        private const val IS_SIGN_IN_KEY = "IS_SIGN_IN_KEY"

        fun newInstance(isSignIn: Boolean) = FragmentScreen {
            LoginFragment().apply {
                arguments = bundleOf(IS_SIGN_IN_KEY to isSignIn)
            }
        }
    }
}