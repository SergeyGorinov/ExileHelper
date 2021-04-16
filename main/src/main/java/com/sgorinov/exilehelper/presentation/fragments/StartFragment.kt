package com.sgorinov.exilehelper.presentation.fragments

import android.os.Bundle
import android.view.View
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.sgorinov.exilehelper.R
import com.sgorinov.exilehelper.core.presentation.BaseFragment
import com.sgorinov.exilehelper.currency.presentation.fragments.CurrencyExchangeMainFragment
import com.sgorinov.exilehelper.databinding.FragmentStartBinding

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