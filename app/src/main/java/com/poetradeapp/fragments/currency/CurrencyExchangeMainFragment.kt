package com.poetradeapp.fragments.currency

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.poetradeapp.R
import com.poetradeapp.MainActivity
import com.poetradeapp.models.MainViewModel
import kotlinx.android.synthetic.main.fragment_currency_exchange_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CurrencyExchangeViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            fragmentActivity,
            ViewModelProvider.AndroidViewModelFactory(fragmentActivity.application)
        ).get(MainViewModel::class.java)
    }

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return CurrencyExchangeWantFragment(viewModel)
            1 -> return CurrencyExchangeHaveFragment(viewModel)
        }
        return CurrencyExchangeWantFragment(viewModel)
    }
}

class CurrencyExchangeMainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private val currencyFragmentExchange = CurrencyExchangeFragment()
    val currencyFragmentResult = CurrencyResultFragment()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency_exchange_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchCurrency.setOnClickListener {
            GlobalScope.launch {
                viewModel.sendCurrencyExchangeRequest()
            }.invokeOnCompletion {
                GlobalScope.launch(Dispatchers.Main) {
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .hide(currencyFragmentExchange)
                        .add(R.id.currencyExchangeContainer, currencyFragmentResult)
                        .show(currencyFragmentResult)
                        .commit()
                }
            }
        }
        (context as MainActivity).supportFragmentManager
            .beginTransaction()
            .add(R.id.currencyExchangeContainer, currencyFragmentExchange)
            .commit()
    }

    fun hideResults() {
        (context as MainActivity).supportFragmentManager
            .beginTransaction()
            .remove(currencyFragmentResult)
            .show(currencyFragmentExchange)
            .commit()
    }
}