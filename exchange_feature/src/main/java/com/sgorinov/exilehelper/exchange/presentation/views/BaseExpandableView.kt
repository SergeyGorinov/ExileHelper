package com.sgorinov.exilehelper.exchange.presentation.views

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

abstract class BaseExpandableView(ctx: Context, attrs: AttributeSet) : ConstraintLayout(ctx, attrs),
    IExpandableView