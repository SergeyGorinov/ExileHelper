package com.poe.tradeapp.currency.presentation

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.poe.tradeapp.currency.R
import com.poe.tradeapp.currency.databinding.EnterNotifyValueDialogBinding

internal class EnterNotifyValueAlertDialog(
    buyingItemName: String,
    payingItemName: String,
    context: Context,
    private val onAccept: (Int) -> Unit
) : AlertDialog(context) {

    private val builder = Builder(context, R.style.AppTheme_AlertDialog)
    private val binding = EnterNotifyValueDialogBinding.inflate(LayoutInflater.from(context))
    private val dialog = builder
        .setView(binding.root)
        .setPositiveButton("Accept") { dialog, _ ->
            val value = binding.payingValue.text?.toString()?.toIntOrNull()
            if (value != null) {
                dialog.dismiss()
                onAccept(value)
            } else {
                Toast.makeText(context, "Enter valid value!", Toast.LENGTH_LONG).show()
            }
        }
        .setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        .create()

    init {
        binding.buyingItemRightSide.text = buyingItemName
        binding.payingItemRightSide.text = payingItemName
    }

    override fun show() {
        dialog.show()
    }

    override fun dismiss() {
        dialog.dismiss()
    }
}