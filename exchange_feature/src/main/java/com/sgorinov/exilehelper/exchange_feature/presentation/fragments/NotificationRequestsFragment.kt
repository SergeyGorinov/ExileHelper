package com.sgorinov.exilehelper.exchange_feature.presentation.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sgorinov.exilehelper.core.presentation.FragmentScopes
import com.sgorinov.exilehelper.core.presentation.generateCustomDividerDecoration
import com.sgorinov.exilehelper.core.presentation.getTransparentProgressDialog
import com.sgorinov.exilehelper.core.presentation.models.NotificationRequestViewData
import com.sgorinov.exilehelper.core.presentation.scopedViewModel
import com.sgorinov.exilehelper.exchange_feature.R
import com.sgorinov.exilehelper.exchange_feature.databinding.FragmentNotificationRequestsBinding
import com.sgorinov.exilehelper.exchange_feature.presentation.ItemsSearchViewModel
import com.sgorinov.exilehelper.exchange_feature.presentation.SwipeToDeleteCallback
import com.sgorinov.exilehelper.exchange_feature.presentation.adapters.NotificationRequestsAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class NotificationRequestsFragment : BottomSheetDialogFragment() {

    private val viewModel by scopedViewModel<ItemsSearchViewModel>(
        FragmentScopes.EXCHANGE_FEATURE.scopeId,
        FragmentScopes.EXCHANGE_FEATURE
    )

    private var viewBinding: FragmentNotificationRequestsBinding? = null
    private var progressDialog: AlertDialog? = null

    var items: MutableList<NotificationRequestViewData> = mutableListOf()

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
        return inflater.inflate(R.layout.fragment_notification_requests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentNotificationRequestsBinding.bind(view)
        if (items.isNotEmpty()) {
            val adapter = NotificationRequestsAdapter(items)
            val backgroundColor = ColorDrawable(
                ContextCompat.getColor(requireActivity(), R.color.secondaryColor)
            )
            val icon = ContextCompat.getDrawable(requireActivity(), R.drawable.clear_24)
            val itemSwipeHelper = ItemTouchHelper(
                SwipeToDeleteCallback(backgroundColor, icon!!) {
                    lifecycleScope.launch {
                        val item = adapter.deleteItem(it)
                        val itemId = item.id
                        if (itemId == null) {
                            adapter.restoreItem(it, item)
                            Toast.makeText(
                                requireActivity(),
                                "Item has no remote id",
                                Toast.LENGTH_LONG
                            ).show()
                            return@launch
                        }
                        if (!viewModel.removeRequest(itemId)) {
                            adapter.restoreItem(it, item)
                            Toast.makeText(
                                requireActivity(),
                                "Error during notification removing",
                                Toast.LENGTH_LONG
                            ).show()
                            return@launch
                        }
                        if (adapter.itemCount == 0) {
                            viewBinding?.emptyPlaceholder?.visibility = View.VISIBLE
                            viewBinding?.notificationRequests?.visibility = View.GONE
                        }
                    }
                }
            )
            viewBinding?.emptyPlaceholder?.visibility = View.GONE
            viewBinding?.notificationRequests?.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(requireActivity())
                this.adapter = adapter
                addItemDecoration(
                    requireActivity().generateCustomDividerDecoration(
                        R.drawable.colored_list_divider,
                        0
                    )
                )
            }
            itemSwipeHelper.attachToRecyclerView(viewBinding?.notificationRequests)
        } else {
            viewBinding?.emptyPlaceholder?.visibility = View.VISIBLE
            viewBinding?.notificationRequests?.visibility = View.GONE
        }
        viewBinding?.addRequest?.setOnClickListener {
            dismiss()
            NotificationRequestAddFragment.newInstance().show(parentFragmentManager, null)
        }
    }

    override fun onResume() {
        super.onResume()
        progressDialog = requireActivity().getTransparentProgressDialog()
        lifecycleScope.launchWhenResumed {
            viewModel.viewLoadingState.collect {
                if (it) {
                    progressDialog?.show()
                } else {
                    progressDialog?.hide()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding?.notificationRequests?.adapter = null
        viewBinding = null
        progressDialog = null
    }

    companion object {
        fun newInstance(items: List<NotificationRequestViewData>): NotificationRequestsFragment {
            return NotificationRequestsFragment().apply {
                this.items = items.toMutableList()
            }
        }
    }
}