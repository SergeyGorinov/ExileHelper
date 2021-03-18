package com.poe.tradeapp.core.presentation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import java.lang.ref.WeakReference

class ScreensRepository {
    private val screens = mutableListOf<WeakReference<FragmentScreen>>()

    fun getScreen(screenKey: String): FragmentScreen? {
        return screens.firstOrNull { it.get()?.screenKey == screenKey }?.get()
    }

    fun addScreen(screen: FragmentScreen) {
        screens.add(WeakReference(screen))
    }
}