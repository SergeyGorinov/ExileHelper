package com.sgorinov.exilehelper.presentation.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.sgorinov.exilehelper.R
import com.sgorinov.exilehelper.core.presentation.BaseFragment
import com.sgorinov.exilehelper.currency_feature.presentation.fragments.CurrencyExchangeMainFragment
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
        binding.info.setOnClickListener {
            val popUpView = View.inflate(requireActivity(), R.layout.info_popup, null)
            val popUp = PopupWindow(requireActivity()).apply {
                contentView = popUpView
                height = WindowManager.LayoutParams.WRAP_CONTENT
                width = WindowManager.LayoutParams.WRAP_CONTENT
                isOutsideTouchable = true
            }
            popUp.showAsDropDown(it, 0, it.height, Gravity.START or Gravity.BOTTOM)
        }
    }

    companion object {
        fun newInstance() = FragmentScreen { StartFragment() }
    }
}