package com.poe.tradeapp.currency.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.poe.tradeapp.currency.R
import com.poe.tradeapp.currency.databinding.FragmentResultBinding
import com.poe.tradeapp.currency.presentation.CurrencyExchangeViewModel
import com.poe.tradeapp.currency.presentation.adapters.CurrencyResultAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

internal class CurrencyExchangeResultFragment : BottomSheetDialogFragment() {

    private val viewModel by sharedViewModel<CurrencyExchangeViewModel>()

    private var binding: FragmentResultBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentResultBinding.bind(view)

        binding?.let {
            it.results.layoutManager = LinearLayoutManager(view.context)
            it.results.adapter = CurrencyResultAdapter(viewModel.currencyResultData) { pos ->
                it.results.post { it.results.adapter?.notifyDataSetChanged() }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        fun newInstance() = CurrencyExchangeResultFragment()
    }
}