package com.poetradeapp.fragments.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.poetradeapp.R
import com.poetradeapp.activities.ItemsSearchActivity
import com.poetradeapp.adapters.ItemFiltersListAdapter
import com.poetradeapp.models.Enums
import kotlinx.android.synthetic.main.fragment_item_exchange_filters.*

class ItemsSearchFiltersFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_exchange_filters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val divider = DividerItemDecoration(view.context, RecyclerView.VERTICAL)
        ContextCompat.getDrawable(view.context, R.drawable.table_column_divider)
            ?.let { divider.setDrawable(it) }

        filtersList.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                (requireActivity() as ItemsSearchActivity).closeLoader()
                filtersList.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        filtersList.layoutManager = LinearLayoutManager(view.context)
        filtersList.adapter = ItemFiltersListAdapter(Enums.Filters.values())
        filtersList.setItemViewCacheSize(10)
        filtersList.addItemDecoration(divider)
    }
}