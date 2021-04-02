package com.poe.tradeapp.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.poe.tradeapp.R
import com.poe.tradeapp.core.presentation.BaseFragment
import com.poe.tradeapp.presentation.MainActivityViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@ExperimentalCoroutinesApi
class LoaderFragment : BaseFragment(R.layout.fragment_loader) {

    private val viewModel by sharedViewModel<MainActivityViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        lifecycleScope.launch {
            viewModel.getRemoteData()
            delay(1500L)
            when {
                user != null && user.isEmailVerified -> {
                    val authToken = suspendCoroutine<String?> { coroutine ->
                        user.getIdToken(false).addOnCompleteListener {
                            coroutine.resume(it.result?.token)
                        }
                    }
                    val messagingToken = suspendCoroutine<String?> { coroutine ->
                        FirebaseMessaging.getInstance().token.addOnCompleteListener {
                            coroutine.resume(it.result)
                        }
                    }
                    if (authToken != null && messagingToken != null) {
                        viewModel.addToken(messagingToken, authToken)
                    }
                    getMainActivity()?.goToCurrencyExchange()
                }
                user != null && !user.isEmailVerified -> {
                    router.newRootScreen(EmailVerificationFragment.newInstance())
                }
                else -> {
                    router.newRootScreen(StartFragment.newInstance())
                }
            }
        }
    }

    companion object {
        fun newInstance() = FragmentScreen { LoaderFragment() }
    }
}