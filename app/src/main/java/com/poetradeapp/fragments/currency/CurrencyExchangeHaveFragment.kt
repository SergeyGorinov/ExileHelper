package com.poetradeapp.fragments.currency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.poetradeapp.R
import com.poetradeapp.adapters.CurrencyListAdapter
import com.poetradeapp.models.ui.StaticGroupViewData
import kotlinx.android.synthetic.main.fragment_currency_exchange_have.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CurrencyExchangeHaveFragment(items: ArrayList<StaticGroupViewData>) : Fragment() {

    private val listAdapter = CurrencyListAdapter(items)
    private val listLayoutManager = LinearLayoutManager(context)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency_exchange_have, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currencyList.apply {
            setItemViewCacheSize(20)
            setHasFixedSize(false)
            layoutManager = listLayoutManager
            adapter = listAdapter
        }
    }
}