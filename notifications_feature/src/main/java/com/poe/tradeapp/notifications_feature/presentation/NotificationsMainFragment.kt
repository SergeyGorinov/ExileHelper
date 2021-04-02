package com.poe.tradeapp.notifications_feature.presentation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.poe.tradeapp.core.presentation.BaseFragment
import com.poe.tradeapp.core.presentation.getTransparentProgressDialog
import com.poe.tradeapp.notifications_feature.R
import com.poe.tradeapp.notifications_feature.databinding.FragmentNotificationsMainBinding
import com.poe.tradeapp.notifications_feature.presentation.adapters.NotificationRequestsAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class NotificationsMainFragment : BaseFragment(R.layout.fragment_notifications_main) {

    private val viewModel by sharedViewModel<NotificationsViewModel>()

    private lateinit var binding: FragmentNotificationsMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentNotificationsMainBinding.bind(view)
        binding = getBinding()

        val progressDialog = requireActivity().getTransparentProgressDialog()

        lifecycleScope.launchWhenResumed {
            viewModel.viewLoadingState.collect {
                if (it) {
                    progressDialog.show()
                } else {
                    progressDialog.dismiss()
                }
            }
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { tokenTask ->
            if (tokenTask.isSuccessful) {
                val messagingToken = tokenTask.result
                val currentUser = Firebase.auth.currentUser
                if (currentUser != null) {
                    currentUser.getIdToken(false).addOnCompleteListener { authTokenTask ->
                        messagingToken?.let {
                            requestNotifications(it, authTokenTask.result?.token)
                        }
                    }
                } else {
                    messagingToken?.let {
                        requestNotifications(it, null)
                    }
                }
            }
        }
    }

    private fun requestNotifications(messagingToken: String, authToken: String?) {
        lifecycleScope.launch {
            val requestNotifications = viewModel.requestNotifications(messagingToken, authToken)
            binding.notificationRequests.layoutManager = LinearLayoutManager(requireActivity())
            binding.notificationRequests.adapter = NotificationRequestsAdapter(requestNotifications)
        }
    }

    companion object {
        fun newInstance() = FragmentScreen { NotificationsMainFragment() }
    }
}