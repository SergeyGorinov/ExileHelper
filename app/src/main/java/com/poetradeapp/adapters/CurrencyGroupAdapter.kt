package com.poetradeapp.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.poetradeapp.R
import com.poetradeapp.http.RequestService
import com.poetradeapp.models.MainViewModel
import com.poetradeapp.models.StaticData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CurrencyGroupAdapter(
    private val items: List<StaticData>,
    private val retrofit: RequestService
) :
    RecyclerView.Adapter<CurrencyGroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyGroupViewHolder {
        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.currency_button, parent, false)

        return CurrencyGroupViewHolder(view, parent.context, retrofit)
    }

    override fun onBindViewHolder(holder: CurrencyGroupViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}

class CurrencyGroupViewHolder(
    itemView: View,
    private val context: Context,
    private val retrofit: RequestService
) :
    RecyclerView.ViewHolder(itemView) {
    private val button: ImageButton = itemView.findViewById(R.id.currencyButton)
    private var item: StaticData? = null
    private var model: MainViewModel = ViewModelProvider(
        (context as FragmentActivity),
        ViewModelProvider.AndroidViewModelFactory(context.application)
    ).get(MainViewModel::class.java)

    init {
        button.setOnClickListener { button ->
            button.isSelected = !button.isSelected
            item?.let {
                if (button.isSelected)
                    model.addCurrency(it.id) else
                    model.removeCurrency(it.id)
            }
        }
    }

    fun bind(item: StaticData) {
        if (this.item == null)
            this.item = item

        this.item?.drawable?.let {
            button.setImageDrawable(it)
        } ?: GlobalScope.launch {
            val byteImage = item?.image?.let { link ->
                retrofit.getStaticImage(link).execute().body()?.bytes()
            }
            byteImage?.let { imageData ->
                this@CurrencyGroupViewHolder.item?.drawable = BitmapDrawable(
                    context.resources,
                    BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                )
                GlobalScope.launch(Dispatchers.Main) {
                    button.setImageDrawable(this@CurrencyGroupViewHolder.item?.drawable)
                }
            }
        }
    }
}