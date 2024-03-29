package com.sgorinov.exilehelper.core.presentation

import android.app.Dialog
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.github.terrakok.cicerone.Router
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sgorinov.exilehelper.core.DI
import com.sgorinov.exilehelper.core.R
import com.sgorinov.exilehelper.core.databinding.MenuLayoutBinding
import org.koin.core.component.inject

abstract class BaseFragment(resId: Int) : Fragment(resId), IBaseFragment {

    protected val router by DI.inject<Router>()
    protected val settings by DI.inject<ApplicationSettings>()

    protected val savedState = bundleOf()

    private lateinit var menuDialog: Dialog

    override var viewBinding: ViewBinding? = null

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    inline fun <reified T : ViewBinding> getBinding() = viewBinding as T

    protected fun getMainActivity() = requireActivity() as? IMainActivity

    protected fun showMenu() {
        val leagues = getMainActivity()?.getLeagues() ?: listOf()
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
            binding.accountData.visibility = View.VISIBLE
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