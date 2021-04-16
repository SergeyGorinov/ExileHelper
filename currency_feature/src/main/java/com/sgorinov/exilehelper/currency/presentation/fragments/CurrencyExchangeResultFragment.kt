package com.sgorinov.exilehelper.currency.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sgorinov.exilehelper.core.DI
import com.sgorinov.exilehelper.core.presentation.ApplicationSettings
import com.sgorinov.exilehelper.core.presentation.FragmentScopes
import com.sgorinov.exilehelper.core.presentation.OnResultsScrollListener
import com.sgorinov.exilehelper.core.presentation.scopedViewModel
import com.sgorinov.exilehelper.currency.R
import com.sgorinov.exilehelper.currency.databinding.FragmentResultBinding
import com.sgorinov.exilehelper.currency.presentation.CurrencyExchangeViewModel
import com.sgorinov.exilehelper.currency.presentation.adapters.CurrencyResultAdapter
import com.sgorinov.exilehelper.currency.presentation.models.CurrencyResultViewItem
import kotlinx.coroutines.launch
import org.koin.core.component.inject

internal class CurrencyExchangeResultFragment : BottomSheetDialogFragment() {

    private val viewModel by scopedViewModel<CurrencyExchangeViewModel>(
        FragmentScopes.CURRENCY_FEATURE.scopeId,
        FragmentScopes.CURRENCY_FEATURE
    )

    private val settings by DI.inject<ApplicationSettings>()

    private val isFullfilable by lazy { requireArguments().getBoolean(IS_FULLFILABLE_KEY, false) }
    private val minimum by lazy { requireArguments().getString(MINIMUM_STOCK_KEY) }

    private var binding: FragmentResultBinding? = null

    var data: List<CurrencyResultViewItem> = listOf()

    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_BaseBottomSheetDialog)
    }

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

        val scrollListener = OnResultsScrollListener(viewModel.getTotalResultCount) {
            if (!isLoading) {
                isLoading = true
                binding?.results?.post {
                    adapter.addLoader()
                }
                lifecycleScope.launch {
                    val result = try {
                        viewModel.requestResult(
                            settings.league,
                            isFullfilable,
                            minimum,
                            it
                        )
                    } catch (e: Exception) {
                        AlertDialog.Builder(requireActivity(), R.style.AppTheme_AlertDialog)
                            .setTitle("Error")
                            .setMessage(e.message ?: "Fetching failed")
                            .setPositiveButton("OK") { dialogInterface, _ ->
                                dialogInterface.dismiss()
                            }
                            .show()
                        emptyList()
                    }
                    adapter.addData(result)
                    isLoading = false
                }
            }
        }

        binding?.let { viewBinding ->
            viewBinding.results.apply {
                layoutManager = LinearLayoutManager(view.context)
                this.adapter = adapter
                addOnScrollListener(scrollListener)
                setItemViewCacheSize(20)
            }
            adapter.addData(data)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.results?.adapter = null
        binding = null
    }

    companion object {
        private const val IS_FULLFILABLE_KEY = "IS_FULLFILABLE_KEY"
        private const val MINIMUM_STOCK_KEY = "MINIMUM_STACK_KEY"

        fun newInstance(
            data: List<CurrencyResultViewItem>,
            isFullfilable: Boolean,
            minimum: String?
        ): CurrencyExchangeResultFragment {
            return CurrencyExchangeResultFragment().apply {
                this.data = data
                arguments = bundleOf(
                    IS_FULLFILABLE_KEY to isFullfilable,
                    MINIMUM_STOCK_KEY to minimum
                )
            }
        }
    }
}