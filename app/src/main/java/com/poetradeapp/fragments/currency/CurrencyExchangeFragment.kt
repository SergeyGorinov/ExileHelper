package com.poetradeapp.fragments.currency

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.poetradeapp.R
import com.google.android.material.tabs.TabLayoutMediator
import com.poetradeapp.FragmentChanger
import com.poetradeapp.MainActivity
import com.poetradeapp.models.MainViewModel
import kotlinx.android.synthetic.main.fragment_currency_exchange.*
import kotlinx.coroutines.*

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
            0 -> return CurrencyExchangeWant(viewModel)
            1 -> return CurrencyExchangeHave(viewModel)
        }
        return CurrencyExchangeWant(viewModel)
    }
}

class CurrencyExchangeFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(MainViewModel::class.java)
    }

    private lateinit var mediator: TabLayoutMediator
    private lateinit var listener: FragmentChanger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflanter = TransitionInflater.from(requireContext())
        exitTransition = inflanter.inflateTransition(R.transition.fragment_fade)
        enterTransition = inflanter.inflateTransition(R.transition.fragment_fade)
    }

    override fun onPause() {
        mediator.detach()
        super.onPause()
    }

    override fun onResume() {
        mediator.attach()
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency_exchange, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currencyExchangeContainer.offscreenPageLimit = 2
        currencyExchangeContainer.adapter = CurrencyExchangeViewPagerAdapter(requireActivity())

        mediator = TabLayoutMediator(currencyExchangeTabs, currencyExchangeContainer) { tab, pos ->
            when (pos) {
                0 -> tab.text = "Want"
                1 -> tab.text = "Have"
            }
        }

        searchCurrency.setOnClickListener {
            GlobalScope.launch {
                viewModel.sendCurrencyExchangeRequest()
            }.invokeOnCompletion {
                listener.changeFragment()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as FragmentChanger
    }
}