package com.sgorinov.exilehelper.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.google.firebase.auth.FirebaseAuth
import com.sgorinov.exilehelper.R
import com.sgorinov.exilehelper.core.presentation.BaseFragment
import com.sgorinov.exilehelper.presentation.MainActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoaderFragment : BaseFragment(R.layout.fragment_loader) {

    private val viewModel by sharedViewModel<MainActivityViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser

        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    viewModel.getRemoteData()
                }
            } catch (e: Exception) {
                val dialog = AlertDialog.Builder(requireActivity(), R.style.AppTheme_AlertDialog)
                    .setTitle("Error")
                    .setMessage("Cannot download necessary data. Exiting...")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setOnDismissListener {
                        requireActivity().finishAndRemoveTask()
                    }
                    .show()
                delay(3000L)
                dialog.dismiss()
                return@launch
            }
            when {
                user != null && user.isEmailVerified -> {
                    getMainActivity()?.goToCurrencyExchange(firstTime = true)
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