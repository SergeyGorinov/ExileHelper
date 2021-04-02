package com.poe.tradeapp.currency.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.poe.tradeapp.core.DI
import com.poe.tradeapp.core.presentation.ApplicationSettings
import com.poe.tradeapp.core.presentation.FragmentScopes
import com.poe.tradeapp.core.presentation.OnResultsScrollListener
import com.poe.tradeapp.core.presentation.scopedViewModel
import com.poe.tradeapp.currency.R
import com.poe.tradeapp.currency.databinding.FragmentResultBinding
import com.poe.tradeapp.currency.presentation.CurrencyExchangeViewModel
import com.poe.tradeapp.currency.presentation.adapters.CurrencyResultAdapter
import com.poe.tradeapp.currency.presentation.models.CurrencyResultViewItem
import kotlinx.coroutines.launch
import org.koin.core.component.inject

internal class CurrencyExchangeResultFragment : BottomSheetDialogFragment() {

    private val viewModel by scopedViewModel<CurrencyExchangeViewModel>(
        FragmentScopes.CURRENCY_FEATURE.scopeId,
        FragmentScopes.CURRENCY_FEATURE
    )

    private val settings by DI.inject<ApplicationSettings>()

    private var binding: FragmentResultBinding? = null

    var data: List<CurrencyResultViewItem> = listOf()

    private var isLoading = false

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

        val adapter = CurrencyResultAdapter()

        binding?.let { viewBinding ->
            viewBinding.results.layoutManager = LinearLayoutManager(view.context)
            viewBinding.results.adapter = adapter
            viewBinding.results.addOnScrollListener(
                OnResultsScrollListener(viewModel.getTotalResultCount) {
                    if (!isLoading) {
                        isLoading = true
                        viewBinding.results.post {
                            adapter.addLoader()
                        }
                        lifecycleScope.launch {
                            adapter.addData(viewModel.requestResult(settings.league, it))
                            isLoading = false
                        }
                    }
                }
            )
            adapter.asyncDiffer.submitList(data)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        fun newInstance(data: List<CurrencyResultViewItem>): CurrencyExchangeResultFragment {
            return CurrencyExchangeResultFragment().apply { this.data = data }
        }
    }
}