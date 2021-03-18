package com.poe.tradeapp.presentation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.poe.tradeapp.R
import com.poe.tradeapp.core.presentation.BaseFragment
import com.poe.tradeapp.currency.presentation.fragments.CurrencyExchangeMainFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class LoaderFragment : BaseFragment(R.layout.fragment_loader) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            delay(1500L)
            router.newRootScreen(CurrencyExchangeMainFragment.newInstance())
            (requireActivity() as MainActivity).showBottomNavBarIfNeeded()
        }
    }

    companion object {
        fun newInstance() = FragmentScreen { LoaderFragment() }
    }
}