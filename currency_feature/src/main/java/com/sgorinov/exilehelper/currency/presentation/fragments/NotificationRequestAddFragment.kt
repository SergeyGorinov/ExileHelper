package com.sgorinov.exilehelper.currency.presentation.fragments

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sgorinov.exilehelper.core.DI
import com.sgorinov.exilehelper.core.presentation.*
import com.sgorinov.exilehelper.currency.R
import com.sgorinov.exilehelper.currency.databinding.FragmentCurrencyNotificationRequestAddBinding
import com.sgorinov.exilehelper.currency.presentation.CurrencyExchangeViewModel
import com.sgorinov.exilehelper.currency.presentation.adapters.CurrencySearchListAdapter
import com.sgorinov.exilehelper.currency.presentation.models.CurrencyViewData
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class NotificationRequestAddFragment : BottomSheetDialogFragment() {

    private val viewModel by scopedViewModel<CurrencyExchangeViewModel>(
        FragmentScopes.CURRENCY_FEATURE.scopeId,
        FragmentScopes.CURRENCY_FEATURE
    )

    private val settings by DI.inject<ApplicationSettings>()

    private val wantItemId by lazy { requireArguments().getString(WANT_ITEM_ID_KEY) }
    private val haveItemId by lazy { requireArguments().getString(HAVE_ITEM_ID_KEY) }

    private var viewBinding: FragmentCurrencyNotificationRequestAddBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_BaseBottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val bottomSheetId = R.id.design_bottom_sheet
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<View>(bottomSheetId) as FrameLayout
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_currency_notification_request_add,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentCurrencyNotificationRequestAddBinding.bind(view)

        val progressDialog = requireActivity().getTransparentProgressDialog()

        viewBinding?.let { binding ->
            setupCurrencyList(binding.wantCurrencyList, wantItemId)
            setupCurrencyList(binding.haveCurrencyList, haveItemId)
            binding.addRequest.setOnClickListener {
                val wantItem =
                    (binding.wantCurrencyList.adapter as? CurrencySearchListAdapter)?.selectedItem
                val haveItem =
                    (binding.haveCurrencyList.adapter as? CurrencySearchListAdapter)?.selectedItem
                if (wantItem == null || haveItem == null) {
                    Toast.makeText(
                        requireActivity(),
                        "Select both items",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
                val amount = binding.amount.text?.toString()?.toIntOrNull()
                if (amount == null) {
                    Toast.makeText(
                        requireActivity(),
                        "Enter correct amount value",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
                lifecycleScope.launch {
                    progressDialog.show()
                    val result = viewModel.sendNotificationRequest(
                        wantItem,
                        haveItem,
                        amount,
                        settings.league
                    )
                    progressDialog.hide()
                    if (result) {
                        Toast.makeText(
                            requireActivity(),
                            "Notification request added successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        dismiss()
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "Error during notification request adding",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding?.wantCurrencyList?.setAdapter(null)
        viewBinding?.haveCurrencyList?.setAdapter(null)
        viewBinding = null
    }

    private fun setupCurrencyList(list: AutoCompleteTextView, preselectedCurrencyId: String?) {
        val items = viewModel.allCurrencies.flatMap { group -> group.staticItems }
        val preselectedItem = items.firstOrNull { it.id == preselectedCurrencyId }
        val adapter = CurrencySearchListAdapter(
            requireActivity(),
            R.layout.dropdown_image_item,
            items
        )
        list.typeface = ResourcesCompat.getFont(requireActivity(), R.font.fontinsmallcaps)
        list.setAdapter(adapter)
        list.setOnClickListener { (it as? AutoCompleteTextView)?.showDropDown() }
        list.setOnItemClickListener { adapterView, _, position, _ ->
            val selectedItem = adapterView.getItemAtPosition(position)
            if (selectedItem is CurrencyViewData) {
                adapter.selectedItem = selectedItem
                viewBinding?.root?.let {
                    requireActivity().hideKeyboard(it)
                }
                list.setSelection(0)
            }
        }
        adapter.selectedItem = preselectedItem
        list.setText(preselectedItem?.label)
    }

    companion object {
        private const val WANT_ITEM_ID_KEY = "WANT_ITEM_ID_KEY"
        private const val HAVE_ITEM_ID_KEY = "HAVE_ITEM_ID_KEY"

        fun newInstance(
            wantItemId: String? = null,
            haveItemId: String? = null
        ): NotificationRequestAddFragment {
            return NotificationRequestAddFragment().apply {
                arguments = bundleOf(WANT_ITEM_ID_KEY to wantItemId, HAVE_ITEM_ID_KEY to haveItemId)
            }
        }
    }
}