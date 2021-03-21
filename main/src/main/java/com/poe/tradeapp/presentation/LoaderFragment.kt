package com.poe.tradeapp.presentation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.poe.tradeapp.R
import com.poe.tradeapp.core.presentation.BaseFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
class LoaderFragment : BaseFragment(R.layout.fragment_loader) {

    private val viewModel by sharedViewModel<MainActivityViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.getLeagues()
            delay(1500L)
            getMainActivity()?.goToCurrencyExchange(null, null)
            getMainActivity()?.showBottomNavBarIfNeeded()
        }
    }

    companion object {
        fun newInstance() = FragmentScreen { LoaderFragment() }
    }
}