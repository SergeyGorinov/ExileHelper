package com.poe.tradeapp.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.terrakok.cicerone.*
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.github.terrakok.cicerone.androidx.TransactionInfo
import com.poe.tradeapp.MyFirebaseMessaging
import com.poe.tradeapp.R
import com.poe.tradeapp.charts_feature.presentation.ChartsMainFragment
import com.poe.tradeapp.core.DI
import com.poe.tradeapp.currency.presentation.fragments.CurrencyExchangeMainFragment
import com.poe.tradeapp.databinding.ActivityMainBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.component.inject

@ExperimentalCoroutinesApi
class MainActivity : FragmentActivity() {

    private val navigatorHolder by DI.inject<NavigatorHolder>()
    private val router by DI.inject<Router>()

    private lateinit var viewBinding: ActivityMainBinding

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == MyFirebaseMessaging.currencyMessage) {
                Log.d(
                    "CURRENCY PAYLOAD",
                    intent.getStringExtra(MyFirebaseMessaging.currencyMessagePayload) ?: ""
                )
            }
        }
    }

    private val navigator = object : AppNavigator(this@MainActivity, R.id.mainContainer) {

        override fun forward(command: Forward) {
            val screen = command.screen
            if (screen is FragmentScreen) {
                val type =
                    if (command.clearContainer) TransactionInfo.Type.REPLACE else TransactionInfo.Type.ADD
                commitNewFragmentScreenWithCommand(command, screen, type, true)
            }
        }

        override fun replace(command: Replace) {
            val screen = command.screen
            if (screen is FragmentScreen) {
                if (localStackCopy.isNotEmpty()) {
                    fragmentManager.popBackStack()
                    val removed = localStackCopy.removeAt(localStackCopy.lastIndex)
                    commitNewFragmentScreenWithCommand(command, screen, removed.type, true)
                } else {
                    commitNewFragmentScreenWithCommand(
                        command,
                        screen,
                        TransactionInfo.Type.REPLACE,
                        false
                    )
                }
            }
        }

        private fun commitNewFragmentScreenWithCommand(
            command: Command,
            screen: FragmentScreen,
            type: TransactionInfo.Type,
            addToBackStack: Boolean
        ) {
            val fragment = screen.createFragment(fragmentFactory)
            val transaction = fragmentManager.beginTransaction()
            transaction.setReorderingAllowed(true)
            setupFragmentTransactionWithCommand(
                command,
                transaction
            )
            when (type) {
                TransactionInfo.Type.ADD -> transaction.add(containerId, fragment, screen.screenKey)
                TransactionInfo.Type.REPLACE -> transaction.replace(
                    containerId,
                    fragment,
                    screen.screenKey
                )
            }
            if (addToBackStack) {
                val transactionInfo = TransactionInfo(screen.screenKey, type)
                transaction.addToBackStack(transactionInfo.toString())
                localStackCopy.add(transactionInfo)
            }
            transaction.commit()
        }

        private fun setupFragmentTransactionWithCommand(
            command: Command,
            fragmentTransaction: FragmentTransaction
        ) {
            when (command) {
                is Forward -> {
                    fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.slide_out,
                        R.anim.pop_in,
                        R.anim.pop_out
                    )
                }
                is Back -> {
                    fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.slide_out,
                        R.anim.pop_in,
                        R.anim.pop_out
                    )
                }
                is Replace -> {
                    fragmentTransaction.setCustomAnimations(
                        R.animator.fade_in,
                        R.animator.fade_out
                    )
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        router.replaceScreen(LoaderFragment.newInstance())
        viewBinding.bottomNavBar.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener when (it.itemId) {
                R.id.currencyExchangeMenu -> {
                    router.newRootScreen(CurrencyExchangeMainFragment.newInstance())
                    true
                }
                R.id.chartsMenu -> {
                    router.newRootScreen(ChartsMainFragment.newInstance())
                    true
                }
                else -> {
                    false
                }
            }
        }
        viewBinding.bottomNavBar.setOnNavigationItemReselectedListener {
            return@setOnNavigationItemReselectedListener
        }
//        FirebaseMessaging.getInstance().token.addOnCompleteListener {
//            if (!it.isSuccessful) {
//                return@addOnCompleteListener
//            }
//            val token = it.result ?: return@addOnCompleteListener
//            lifecycleScope.launch(Dispatchers.IO) {
//                viewModel.sendRequest(
//                    UserRequest(token, "chaos", "alt", 5f)
//                )
//            }
//        }
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
        val intentFilter = IntentFilter().apply {
            addAction(MyFirebaseMessaging.currencyMessage)
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    fun showBottomNavBarIfNeeded() {
        if (viewBinding.bottomNavBar.visibility == View.GONE) {
            viewBinding.bottomNavBar.visibility = View.VISIBLE
        }
    }
}