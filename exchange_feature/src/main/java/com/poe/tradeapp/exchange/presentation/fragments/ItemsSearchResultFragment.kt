package com.poe.tradeapp.exchange.presentation.fragments

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.poe.tradeapp.core.DI
import com.poe.tradeapp.core.presentation.ApplicationSettings
import com.poe.tradeapp.core.presentation.CenteredImageSpan
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.databinding.FragmentItemsSearchResultBinding
import com.poe.tradeapp.exchange.presentation.ItemsSearchViewModel
import com.poe.tradeapp.exchange.presentation.SeparatorHelper
import com.poe.tradeapp.exchange.presentation.adapters.ItemsResultAdapter
import com.poe.tradeapp.exchange.presentation.models.FetchedItem
import com.poe.tradeapp.exchange.presentation.models.ItemResultViewData
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.component.inject

internal class ItemsSearchResultFragment : BottomSheetDialogFragment() {

    private val viewModel by sharedViewModel<ItemsSearchViewModel>()
    private val settings by DI.inject<ApplicationSettings>()

    private var viewBinding: FragmentItemsSearchResultBinding? = null

    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_items_search_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentItemsSearchResultBinding.bind(view)

        val layoutManager = LinearLayoutManager(context)
        val adapter = ItemsResultAdapter()

        viewBinding?.results?.layoutManager = layoutManager
        viewBinding?.results?.adapter = adapter
        viewBinding?.results?.addOnScrollListener(
            OnResultsScrollListener(viewModel.itemsResultData?.second ?: listOf()) {
                if (!isLoading) {
                    isLoading = true
                    viewBinding?.results?.post {
                        adapter.addLoader()
                    }
                    lifecycleScope.launch {
                        viewModel.fetchPartialResults(settings.league, it)
                        adapter.addFetchedItems(populateResponse(viewModel.itemsResultFetchedData))
                        isLoading = false
                    }
                }
            }
        )
        adapter.addFetchedItems(populateResponse(viewModel.itemsResultFetchedData))
    }

    private fun populateResponse(items: List<ItemResultViewData>): List<FetchedItem> {
        return items.map { currentItem ->
            var hybridItemText: SpannableStringBuilder? = null

            val itemText = SpannableStringBuilder()

            listOfNotNull(
                SeparatorHelper.getSpannableTextGroup(currentItem.itemData.properties, "\n"),
                SeparatorHelper.getSpannableTextGroup(currentItem.itemData.requirements, ", "),
                currentItem.itemData.secDescrText,
                currentItem.itemData.implicitMods,
                currentItem.itemData.explicitMods,
                currentItem.itemData.note
            ).forEach {
                populateItemText(it, itemText, currentItem.frameType)
            }

            if (currentItem.hybridData != null) {
                hybridItemText = SpannableStringBuilder()

                listOfNotNull(
                    SeparatorHelper.getSpannableTextGroup(currentItem.hybridData.properties, "\n"),
                    SeparatorHelper.getSpannableTextGroup(
                        currentItem.hybridData.requirements,
                        ", "
                    ),
                    currentItem.hybridData.secDescrText,
                    currentItem.hybridData.implicitMods,
                    currentItem.hybridData.explicitMods
                ).forEach {
                    populateItemText(it, hybridItemText, currentItem.frameType)
                }
            }

            FetchedItem(
                currentItem.name,
                currentItem.typeLine,
                currentItem.iconUrl,
                currentItem.sockets,
                currentItem.frameType,
                SeparatorHelper.getInfluenceIcons(currentItem),
                itemText,
                currentItem.hybridTypeLine,
                hybridItemText
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun populateItemText(
        properties: Any,
        itemText: SpannableStringBuilder,
        frameType: Int?
    ) {
        if (itemText.isNotEmpty())
            itemText.insertSeparator(SeparatorHelper.getSeparator(frameType))
        when (properties) {
            is List<*> -> {
                itemText.append(properties.joinToString("\n"))
            }
            is String -> {
                itemText.append(properties)
            }
            is SpannableStringBuilder -> {
                itemText.append(properties)
            }
        }
    }

    private fun SpannableStringBuilder.insertSeparator(image: Int) {
        this.appendLine()
        this.append(' ')
        this.setSpan(
            CenteredImageSpan(requireContext(), image),
            this.length - 1,
            this.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        this.appendLine()
    }

    private class OnResultsScrollListener(
        items: List<String>,
        private val onDownloadRequest: (Int) -> Unit
    ) : RecyclerView.OnScrollListener() {

        private var totalCount = items.size

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val totalCurrentItems = recyclerView.layoutManager?.itemCount ?: 0
            if (totalCurrentItems < totalCount) {
                val lastVisiblePosition =
                    (recyclerView.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()
                        ?: 0
                if ((totalCurrentItems - 3) <= lastVisiblePosition) {
                    onDownloadRequest(totalCurrentItems)
                }
            }
        }
    }

    companion object {
        fun newInstance() = ItemsSearchResultFragment()
    }
}