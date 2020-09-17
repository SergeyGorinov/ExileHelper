package com.poetradeapp.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.poetradeapp.R
import com.poetradeapp.http.RequestService
import com.poetradeapp.models.MainViewModel
import com.poetradeapp.models.StaticData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrencyGroupAdapter(
    private val context: Context,
    private val items: List<StaticData>,
    private val retrofit: RequestService
) :
    RecyclerView.Adapter<CurrencyGroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyGroupViewHolder {
        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.currency_button, parent, false)

        return CurrencyGroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyGroupViewHolder, position: Int) {

        items[position].drawable?.let {
            holder.bindTo(it, context)
        } ?: run {
            GlobalScope.launch {
                val byteImage = items[position].image?.let { link ->
                    retrofit.getStaticImage(link).execute().body()?.bytes()
                }
                val image = byteImage?.let {
                    BitmapDrawable(context.resources, BitmapFactory.decodeByteArray(it, 0, it.size))
                }
                items[position].drawable = image
                withContext(Dispatchers.Main) {
                    image?.let { holder.bindTo(it, context) }
                }
            }
        }
    }

    override fun getItemCount() = items.size
}

class CurrencyGroupViewHolder : RecyclerView.ViewHolder {
    private val button: ImageButton

    constructor(itemView: View) : super(itemView) {
        button = itemView.findViewById(R.id.currencyButton)
    }

    fun bindTo(drawable: Drawable, context: Context) {
        button.setImageDrawable(drawable)
        button.setOnClickListener {
            ViewModelProvider(
                (context as AppCompatActivity),
                ViewModelProvider.AndroidViewModelFactory(context.application)
            ).get(MainViewModel::class.java).setSelectedButton(button)
        }
    }
}