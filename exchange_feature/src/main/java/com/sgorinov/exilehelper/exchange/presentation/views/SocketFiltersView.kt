package com.sgorinov.exilehelper.exchange.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.sgorinov.exilehelper.core.presentation.SlideUpDownAnimator
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.data.models.Filter
import com.sgorinov.exilehelper.exchange.databinding.SocketFiltersViewBinding
import com.sgorinov.exilehelper.exchange.presentation.models.enums.ViewFilters

internal class SocketFiltersView(ctx: Context, attrs: AttributeSet) :
    BaseExpandableView(ctx, attrs) {

    override val animator: SlideUpDownAnimator

    private var viewBinding: SocketFiltersViewBinding?

    init {
        val view = LayoutInflater.from(ctx).inflate(R.layout.socket_filters_view, this, true)
        viewBinding = SocketFiltersViewBinding.bind(view)
        animator = SlideUpDownAnimator(view)
    }

    override fun setupFields(filter: Filter) {
        viewBinding?.let {
            it.sockets.setupField(filter.getOrCreateField(ViewFilters.SocketFilters.Sockets.id))
            it.links.setupField(filter.getOrCreateField(ViewFilters.SocketFilters.Links.id))
        }
    }

    override fun cleanFields() {
        viewBinding?.let {
            it.sockets.cleanField()
            it.links.cleanField()
        }
    }

    override fun onViewRemoved(view: View?) {
        super.onViewRemoved(view)
        viewBinding = null
    }
}