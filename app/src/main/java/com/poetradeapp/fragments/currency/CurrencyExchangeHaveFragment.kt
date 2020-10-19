package com.poetradeapp.fragments.currency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.poetradeapp.R
import com.poetradeapp.adapters.CurrencyListAdapter
import com.poetradeapp.models.CurrencyGroupViewData
import kotlinx.android.synthetic.main.fragment_currency_exchange_have.*

class CurrencyExchangeHaveFragment(items: ArrayList<CurrencyGroupViewData>) : Fragment() {

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