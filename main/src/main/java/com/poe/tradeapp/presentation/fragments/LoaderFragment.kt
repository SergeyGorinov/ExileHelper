package com.poe.tradeapp.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.google.firebase.auth.FirebaseAuth
import com.poe.tradeapp.R
import com.poe.tradeapp.core.presentation.BaseFragment
import com.poe.tradeapp.presentation.MainActivityViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
class LoaderFragment : BaseFragment(R.layout.fragment_loader) {

    private val viewModel by sharedViewModel<MainActivityViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        lifecycleScope.launch {
            viewModel.getLeagues()
            delay(1500L)
            when {
                user != null && user.isEmailVerified -> {
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