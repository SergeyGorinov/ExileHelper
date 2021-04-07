package com.poe.tradeapp.core.presentation

import android.app.Dialog
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.github.terrakok.cicerone.Router
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.poe.tradeapp.core.DI
import com.poe.tradeapp.core.R
import com.poe.tradeapp.core.databinding.MenuLayoutBinding
import org.koin.core.component.inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

abstract class BaseFragment(resId: Int) : Fragment(resId), IBaseFragment {

    protected val router by DI.inject<Router>()
    protected val settings by DI.inject<ApplicationSettings>()

    protected suspend fun getMessagingToken() = suspendCoroutine<String?> { coroutine ->
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            coroutine.resume(it.result)
        }
    }

    protected suspend fun getAuthToken() = suspendCoroutine<String?> { coroutine ->
        Firebase.auth.currentUser?.getIdToken(false)
            ?.addOnCompleteListener {
                coroutine.resume(it.result?.token)
            } ?: coroutine.resume(null)
    }

    private lateinit var menuDialog: Dialog

    override var viewBinding: ViewBinding? = null

    private val toolbar by lazy { viewBinding?.root?.findViewById<MaterialToolbar>(R.id.toolbar) }
    private val progressBar by lazy {
        viewBinding?.root?.findViewById<LinearProgressIndicator>(R.id.toolbarProgressBar)
    }

    override fun onResume() {
        super.onResume()
        toolbar?.setNavigationOnClickListener {
            showMenu()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    inline fun <reified T : ViewBinding> getBinding() = viewBinding as T

    protected fun getMainActivity() = requireActivity() as? IMainActivity

    protected fun toggleProgressBar(show: Boolean) {
        if (show) {
            progressBar?.show()
        } else {
            progressBar?.hide()
        }
    }

    private fun showMenu() {
        val leagues = getMainActivity()?.leagues ?: listOf()
        val view = View.inflate(requireActivity(), R.layout.menu_layout, null)
        val binding = MenuLayoutBinding.bind(view)
        binding.leagueSelector.adapter =
            ArrayAdapter(requireActivity(), R.layout.spinner_item, leagues)
        binding.leagueSelector.setSelection(leagues.indexOf(settings.league))
        binding.leagueSelector.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    settings.league = leagues[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }

        menuDialog = Dialog(requireActivity(), R.style.AppTheme_DialogMenu)

        Firebase.auth.currentUser?.let {
            binding.signOut.visibility = View.VISIBLE
            binding.signedInEmail.visibility = View.VISIBLE
            binding.signedInEmail.text = it.email
            binding.signOut.setOnClickListener {
                AlertDialog.Builder(requireActivity(), R.style.AppTheme_AlertDialog)
                    .setPositiveButton("Yes") { dialog, _ ->
                        dialog.dismiss()
                        menuDialog.dismiss()
                        getMainActivity()?.signOut()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setTitle("Sign out")
                    .setMessage("Are You really want to sign out from account?")
                    .show()
            }
        }

        menuDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        menuDialog.setContentView(view)
        menuDialog.window?.setGravity(Gravity.TOP or Gravity.START)
        menuDialog.window?.attributes?.height = requireActivity().window.attributes.height
        menuDialog.window?.attributes?.width = 300.dp
        menuDialog.window?.attributes?.dimAmount = 0f
        menuDialog.show()
    }
}