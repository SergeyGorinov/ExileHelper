package com.sgorinov.exilehelper.exchange.presentation.views

import com.sgorinov.exilehelper.core.presentation.SlideUpDownAnimator
import com.sgorinov.exilehelper.exchange.data.models.Filter

internal interface IExpandableView {
    val animator: SlideUpDownAnimator
    fun setupFields(filter: Filter)
    fun cleanFields()
}