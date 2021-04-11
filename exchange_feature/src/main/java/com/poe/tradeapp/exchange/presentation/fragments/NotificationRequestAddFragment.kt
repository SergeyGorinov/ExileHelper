package com.poe.tradeapp.exchange.presentation.fragments

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
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.poe.tradeapp.core.presentation.FragmentScopes
import com.poe.tradeapp.core.presentation.getTransparentProgressDialog
import com.poe.tradeapp.core.presentation.hideKeyboard
import com.poe.tradeapp.core.presentation.scopedViewModel
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.databinding.FragmentNotificationRequestAddBinding
import com.poe.tradeapp.exchange.presentation.ItemsSearchViewModel
import com.poe.tradeapp.exchange.presentation.adapters.ItemsSearchFieldAdapter
import com.poe.tradeapp.exchange.presentation.models.SuggestionItem
import kotlinx.coroutines.launch

class NotificationRequestAddFragment : BottomSheetDialogFragment() {

    private val viewModel by scopedViewModel<ItemsSearchViewModel>(
        FragmentScopes.EXCHANGE_FEATURE.scopeId,
        FragmentScopes.EXCHANGE_FEATURE
    )

    private var viewBinding: FragmentNotificationRequestAddBinding? = null

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
        return inflater.inflate(R.layout.fragment_notification_request_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentNotificationRequestAddBinding.bind(view)

        val progressDialog = requireActivity().getTransparentProgressDialog()

        viewBinding?.let { binding ->
            setupCurrencyList(binding.itemsList)
            binding.addRequest.setOnClickListener {
                val wantItem =
                    (binding.itemsList.adapter as? ItemsSearchFieldAdapter)?.selectedItem
                if (wantItem == null) {
                    Toast.makeText(requireActivity(), "Select item", Toast.LENGTH_LONG).show()
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
                    val result = viewModel.sendNotificationRequest(wantItem, amount)
                    progressDialog.hide()
                    if (result) {
                        Toast.makeText(
                            requireActivity(),
                            "Notification request added successfully",
                            Toast.LENGTH_LONG
                        ).show()
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
        viewBinding = null
    }

    private fun setupCurrencyList(list: AutoCompleteTextView) {
        list.typeface = ResourcesCompat.getFont(requireActivity(), R.font.fontinsmallcaps)
        list.setAdapter(
            ItemsSearchFieldAdapter(
                requireActivity(),
                R.layout.dropdown_item,
                viewModel.itemGroups
            )
        )
        list.setOnClickListener { (it as? AutoCompleteTextView)?.showDropDown() }
        list.setOnItemClickListener { adapterView, _, position, _ ->
            val selectedItem = adapterView.getItemAtPosition(position)
            val adapter = adapterView.adapter
            if (selectedItem is SuggestionItem) {
                if (adapter is ItemsSearchFieldAdapter) {
                    adapter.selectedItem = selectedItem
                }
                viewBinding?.root?.let {
                    requireActivity().hideKeyboard(it)
                }
                list.setSelection(0)
            }
        }
    }

    companion object {
        fun newInstance() = NotificationRequestAddFragment()
    }
}