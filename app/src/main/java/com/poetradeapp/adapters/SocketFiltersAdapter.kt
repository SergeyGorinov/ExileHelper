package com.poetradeapp.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.poetradeapp.R
import com.poetradeapp.activities.ItemsSearchActivity
import com.poetradeapp.listeners.EnableDisableFilterListener
import com.poetradeapp.listeners.SocketsLinksTextChangedListener
import com.poetradeapp.models.enums.ViewFilters
import com.poetradeapp.models.viewmodels.ItemsSearchViewModel
import com.poetradeapp.ui.SlideUpDownAnimator
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SocketFiltersAdapter : RecyclerView.Adapter<SocketFiltersViewHolder>() {

    override fun getItemCount() = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocketFiltersViewHolder {
        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.filters_socket_item, parent, false)

        return SocketFiltersViewHolder(view)
    }

    override fun onBindViewHolder(holder: SocketFiltersViewHolder, position: Int) = Unit
}

@ExperimentalCoroutinesApi
class SocketFiltersViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    private val enabled: CheckBox = itemView.findViewById(R.id.filterEnabled)
    private val showHide: MaterialButton = itemView.findViewById(R.id.filterShowHideButton)
    private val filterItemsLayout: LinearLayout = itemView.findViewById(R.id.filterItemsLayout)
    private val clearAll: ImageButton = itemView.findViewById(R.id.filterClearAll)
    private val context = itemView.context as ItemsSearchActivity
    private val animator = SlideUpDownAnimator(filterItemsLayout)
    private val filters by lazy {
        ViewModelProvider(
            context,
            ViewModelProvider.AndroidViewModelFactory(context.application)
        ).get(ItemsSearchViewModel::class.java).getItemRequestData().query.filters
    }

    init {
        val filter = filters.socket_filters

        enabled.isChecked = !filter.disabled
        enabled.setOnCheckedChangeListener(EnableDisableFilterListener(filter))

        showHide.setOnClickListener {
            if (filterItemsLayout.visibility == View.VISIBLE)
                animator.slideUp()
            else
                animator.slideDown()
        }

        showHide.text = context.getString(R.string.socket_filter_header)

        itemView.findViewById<TextInputEditText>(R.id.redSocketSocketsFilter)
            .addTextChangedListener(
                SocketsLinksTextChangedListener(
                    ViewFilters.SocketFilters.Sockets,
                    ViewFilters.SocketTypes.R,
                    filters.socket_filters.filters
                )
            )
        itemView.findViewById<TextInputEditText>(R.id.greenSocketSocketsFilter)
            .addTextChangedListener(
                SocketsLinksTextChangedListener(
                    ViewFilters.SocketFilters.Sockets,
                    ViewFilters.SocketTypes.G,
                    filters.socket_filters.filters
                )
            )
        itemView.findViewById<TextInputEditText>(R.id.blueSocketSocketsFilter)
            .addTextChangedListener(
                SocketsLinksTextChangedListener(
                    ViewFilters.SocketFilters.Sockets,
                    ViewFilters.SocketTypes.B,
                    filters.socket_filters.filters
                )
            )
        itemView.findViewById<TextInputEditText>(R.id.whiteSocketSocketsFilter)
            .addTextChangedListener(
                SocketsLinksTextChangedListener(
                    ViewFilters.SocketFilters.Sockets,
                    ViewFilters.SocketTypes.W,
                    filters.socket_filters.filters
                )
            )
        itemView.findViewById<TextInputEditText>(R.id.minSocketSocketsFilter)
            .addTextChangedListener(
                SocketsLinksTextChangedListener(
                    ViewFilters.SocketFilters.Sockets,
                    ViewFilters.SocketTypes.MIN,
                    filters.socket_filters.filters
                )
            )
        itemView.findViewById<TextInputEditText>(R.id.maxSocketSocketsFilter)
            .addTextChangedListener(
                SocketsLinksTextChangedListener(
                    ViewFilters.SocketFilters.Sockets,
                    ViewFilters.SocketTypes.MAX,
                    filters.socket_filters.filters
                )
            )
        itemView.findViewById<TextInputEditText>(R.id.redSocketLinksFilter)
            .addTextChangedListener(
                SocketsLinksTextChangedListener(
                    ViewFilters.SocketFilters.Links,
                    ViewFilters.SocketTypes.R,
                    filters.socket_filters.filters
                )
            )
        itemView.findViewById<TextInputEditText>(R.id.greenSocketLinksFilter)
            .addTextChangedListener(
                SocketsLinksTextChangedListener(
                    ViewFilters.SocketFilters.Links,
                    ViewFilters.SocketTypes.G,
                    filters.socket_filters.filters
                )
            )
        itemView.findViewById<TextInputEditText>(R.id.blueSocketLinksFilter)
            .addTextChangedListener(
                SocketsLinksTextChangedListener(
                    ViewFilters.SocketFilters.Links,
                    ViewFilters.SocketTypes.B,
                    filters.socket_filters.filters
                )
            )
        itemView.findViewById<TextInputEditText>(R.id.whiteSocketLinksFilter)
            .addTextChangedListener(
                SocketsLinksTextChangedListener(
                    ViewFilters.SocketFilters.Links,
                    ViewFilters.SocketTypes.W,
                    filters.socket_filters.filters
                )
            )
        itemView.findViewById<TextInputEditText>(R.id.minSocketLinksFilter)
            .addTextChangedListener(
                SocketsLinksTextChangedListener(
                    ViewFilters.SocketFilters.Links,
                    ViewFilters.SocketTypes.MIN,
                    filters.socket_filters.filters
                )
            )
        itemView.findViewById<TextInputEditText>(R.id.maxSocketLinksFilter)
            .addTextChangedListener(
                SocketsLinksTextChangedListener(
                    ViewFilters.SocketFilters.Links,
                    ViewFilters.SocketTypes.MAX,
                    filters.socket_filters.filters
                )
            )

        val layoutParams = filterItemsLayout.layoutParams
        layoutParams.height = 1
        filterItemsLayout.layoutParams = layoutParams

        filterItemsLayout.measure(
            View.MeasureSpec.makeMeasureSpec(
                Resources.getSystem().displayMetrics.widthPixels,
                View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        animator.setHeight(filterItemsLayout.measuredHeight)
    }
}