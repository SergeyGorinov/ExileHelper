package com.poe.tradeapp.core.presentation

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

    fun showBottomNavBarIfNeeded()
    fun signOut()
    fun checkApiConnection()
}