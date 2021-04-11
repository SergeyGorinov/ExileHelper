package com.poe.tradeapp.presentation.fragments

import android.os.Bundle
import android.view.View
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.poe.tradeapp.R
import com.poe.tradeapp.core.presentation.BaseFragment
import com.poe.tradeapp.currency.presentation.fragments.CurrencyExchangeMainFragment
import com.poe.tradeapp.databinding.FragmentStartBinding

class StartFragment : BaseFragment(R.layout.fragment_start) {

    private lateinit var binding: FragmentStartBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentStartBinding.bind(view)
        binding = getBinding()

        binding.signIn.setOnClickListener {
            router.navigateTo(LoginFragment.newInstance(true))
        }
        binding.signUp.setOnClickListener {
            router.navigateTo(LoginFragment.newInstance(false))
        }
        binding.noAccount.setOnClickListener {
            router.newRootScreen(CurrencyExchangeMainFragment.newInstance())
        }
    }

    companion object {
        fun newInstance() = FragmentScreen { StartFragment() }
    }
}