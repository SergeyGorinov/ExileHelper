package com.sgorinov.exilehelper.charts_feature.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sgorinov.exilehelper.charts_feature.R
import com.sgorinov.exilehelper.charts_feature.databinding.OverviewItemBinding
import com.sgorinov.exilehelper.charts_feature.presentation.models.OverviewViewData
import com.squareup.picasso.Picasso

internal class OverviewAdapter(private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<OverviewAdapter.CurrencyOverviewViewHolder>() {

    var selling = true
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val diffUtilCallback = object : DiffUtil.ItemCallback<OverviewViewData>() {
        override fun areItemsTheSame(
            oldItem: OverviewViewData,
            newItem: OverviewViewData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: OverviewViewData,
            newItem: OverviewViewData
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val picassoInstance = Picasso.get()
    private val asyncDiffer = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyOverviewViewHolder {
        return CurrencyOverviewViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.overview_item, parent, false), picassoInstance
        )
    }

    override fun onBindViewHolder(holder: CurrencyOverviewViewHolder, position: Int) {
        val item = asyncDiffer.currentList[position]
        holder.bind(item, selling)
        holder.itemView.setOnClickListener {
            onItemClick(item.id)
        }
    }

    override fun getItemCount() = asyncDiffer.currentList.size

    fun setData(data: List<OverviewViewData>) {
        asyncDiffer.submitList(data)
    }

    internal class CurrencyOverviewViewHolder(
        itemView: View,
        private val picassoInstance: Picasso
    ) : RecyclerView.ViewHolder(itemView) {

        private val viewBinding = OverviewItemBinding.bind(itemView)

        fun bind(item: OverviewViewData, selling: Boolean) {
            viewBinding.label.text = item.name
            viewBinding.leftSideText.text = String.format(
                "%.1f",
                if (selling) 1f else item.buyingListingData.value.toFloat()
            )
            viewBinding.rightSideText.text = String.format(
                "%.1f",
                if (selling) item.sellingListingData?.value?.toFloat() ?: 0f else 1f
            )
            picassoInstance.load(item.icon).fit().into(
                if (selling) viewBinding.leftSideImage else viewBinding.rightSideImage
            )
            picassoInstance.load(R.drawable.chaos_icon).fit().into(
                if (selling) viewBinding.rightSideImage else viewBinding.leftSideImage
            )
            picassoInstance.load(item.icon).fit().into(viewBinding.image)
        }
    }
}