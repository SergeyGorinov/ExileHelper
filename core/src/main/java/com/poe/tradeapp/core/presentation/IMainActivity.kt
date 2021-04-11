package com.poe.tradeapp.core.presentation

interface IMainActivity {

    fun getLeagues(): List<String>

    fun goToCurrencyExchange(
        wantItemId: String? = null,
        haveItemId: String? = null,
        firstTime: Boolean = false
    )

    fun goToItemsSearch(
        itemType: String,
        itemName: String?
    )

    fun showBottomNavBarIfNeeded()
    fun signOut()
    fun checkApiConnection()
}