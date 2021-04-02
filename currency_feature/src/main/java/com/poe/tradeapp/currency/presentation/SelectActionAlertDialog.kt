package com.poe.tradeapp.currency.presentation

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.poe.tradeapp.currency.R
import com.poe.tradeapp.currency.databinding.SelectActionDialogBinding

internal class SelectActionAlertDialog(
    context: Context,
    private val onRequestItemsAction: () -> Unit,
    private val onSendNotificationRequestAction: () -> Unit
) : AlertDialog(context) {

    private val builder = Builder(context, R.style.AppTheme_AlertDialog)
    private val binding = SelectActionDialogBinding.inflate(LayoutInflater.from(context))
    private val dialog = builder.setView(binding.root).create()

    init {
        binding.requestItemsAction.setOnClickListener {
            onRequestItemsAction()
            dialog.dismiss()
        }
        binding.sendNotificationRequestAction.setOnClickListener {
            onSendNotificationRequestAction()
            dialog.dismiss()
        }
    }

    override fun show() {
        dialog.show()
    }

    override fun dismiss() {
        dialog.dismiss()
    }
}