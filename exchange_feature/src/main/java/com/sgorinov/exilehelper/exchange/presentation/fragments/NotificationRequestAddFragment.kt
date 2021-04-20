package com.sgorinov.exilehelper.exchange.presentation.fragments

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
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.databinding.FragmentItemNotificationRequestAddBinding
import com.sgorinov.exilehelper.exchange.presentation.ItemsSearchViewModel
import com.sgorinov.exilehelper.exchange.presentation.adapters.ItemsSearchFieldAdapter
import com.sgorinov.exilehelper.exchange.presentation.models.SuggestionItem
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class NotificationRequestAddFragment : BottomSheetDialogFragment() {

    private val viewModel by scopedViewModel<ItemsSearchViewModel>(
        FragmentScopes.EXCHANGE_FEATURE.scopeId,
        FragmentScopes.EXCHANGE_FEATURE
    )

    private val settings by DI.inject<ApplicationSettings>()

    private val itemType by lazy { requireArguments().getString(ITEM_TYPE_KEY) }
    private val itemName by lazy { requireArguments().getString(ITEM_NAME_KEY) }

    private var viewBinding: FragmentItemNotificationRequestAddBinding? = null

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
        return inflater.inflate(R.layout.fragment_item_notification_request_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentItemNotificationRequestAddBinding.bind(view)

        val progressDialog = requireActivity().getTransparentProgressDialog()

        viewBinding?.let { binding ->
            setupItemsList(binding.itemsList)
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
                    val result =
                        viewModel.sendNotificationRequest(wantItem, amount, settings.league)
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
        viewBinding = null
    }

    private fun setupItemsList(list: AutoCompleteTextView) {
        val preselectedItem = if (itemType != null && itemName != null) {
            viewModel.itemGroups.flatMap { it.entries }.firstOrNull {
                it.type == itemType && it.name == itemName
            }
        } else {
            viewModel.itemGroups.flatMap { it.entries }.firstOrNull { it.type == itemType }
        }
        val adapter = ItemsSearchFieldAdapter(
            requireActivity(),
            R.layout.dropdown_item,
            viewModel.itemGroups
        ) {
            return@ItemsSearchFieldAdapter
        }
        list.typeface = ResourcesCompat.getFont(requireActivity(), R.font.fontinsmallcaps)
        list.setAdapter(adapter)
        list.setOnClickListener { (it as? AutoCompleteTextView)?.showDropDown() }
        list.setOnItemClickListener { adapterView, _, position, _ ->
            val selectedItem = adapterView.getItemAtPosition(position)
            if (selectedItem is SuggestionItem) {
                adapter.selectedItem = selectedItem
                viewBinding?.root?.let {
                    requireActivity().hideKeyboard(it)
                }
                list.setSelection(0)
            }
        }
        if (preselectedItem != null) {
            adapter.selectedItem = SuggestionItem(
                false,
                preselectedItem.text,
                preselectedItem.type,
                preselectedItem.name
            )
            list.setText(preselectedItem.text)
        }
    }

    companion object {
        private const val ITEM_TYPE_KEY = "ITEM_TYPE_KEY"
        private const val ITEM_NAME_KEY = "ITEM_NAME_KEY"

        fun newInstance(
            itemType: String? = null,
            itemName: String? = null
        ): NotificationRequestAddFragment {
            return NotificationRequestAddFragment().apply {
                arguments = bundleOf(ITEM_TYPE_KEY to itemType, ITEM_NAME_KEY to itemName)
            }
        }
    }
}