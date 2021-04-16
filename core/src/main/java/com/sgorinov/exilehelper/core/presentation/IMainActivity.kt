package com.sgorinov.exilehelper.core.presentation

import android.os.Bundle

interface IMainActivity {

    fun getLeagues(): List<String>

    fun goToCurrencyExchange(
        wantItemId: String? = null,
        haveItemId: String? = null,
        firstTime: Boolean = false,
        withNotificationRequest: Boolean = false
    )

    fun goToItemsSearch(
        itemType: String,
        itemName: String?,
        withNotificationRequest: Boolean = false
    )

    fun saveCurrencyExchangeFragmentState(state: Bundle)
    fun saveItemsSearchFragmentState(state: Bundle)
    fun restoreCurrencyExchangeFragmentState(): Bundle?
    fun restoreItemsSearchFragmentState(): Bundle?
    fun showBottomNavBarIfNeeded()
    fun signOut()
    fun checkApiConnection()
}