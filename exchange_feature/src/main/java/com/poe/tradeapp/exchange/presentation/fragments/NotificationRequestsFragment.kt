package com.poe.tradeapp.exchange.presentation.fragments

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.poe.tradeapp.core.presentation.generateLinearDividerDecoration
import com.poe.tradeapp.core.presentation.models.NotificationRequestViewData
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.databinding.FragmentNotificationRequestsBinding
import com.poe.tradeapp.exchange.presentation.adapters.NotificationRequestsAdapter

internal class NotificationRequestsFragment : BottomSheetDialogFragment() {

    private var viewBinding: FragmentNotificationRequestsBinding? = null

    var items: List<NotificationRequestViewData> = listOf()

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
            viewBinding?.emptyPlaceholder?.visibility = View.GONE
            viewBinding?.notificationRequests?.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = NotificationRequestsAdapter(items)
                addItemDecoration(requireActivity().generateLinearDividerDecoration())
            }
        } else {
            viewBinding?.emptyPlaceholder?.visibility = View.VISIBLE
            viewBinding?.notificationRequests?.visibility = View.GONE
        }
        viewBinding?.addRequest?.setOnClickListener {
            dismiss()
            NotificationRequestAddFragment.newInstance().show(parentFragmentManager, null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding?.notificationRequests?.adapter = null
        viewBinding = null
    }

    companion object {
        fun newInstance(items: List<NotificationRequestViewData>): NotificationRequestsFragment {
            return NotificationRequestsFragment().apply {
                this.items = items
            }
        }
    }
}