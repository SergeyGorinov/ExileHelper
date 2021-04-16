package com.sgorinov.exilehelper.core.data

import android.content.Context
import com.sgorinov.exilehelper.core.data.models.MyObjectBox
import io.objectbox.BoxStore

object ObjectBox {

    lateinit var objectBox: BoxStore

    fun init(context: Context) {
        objectBox = MyObjectBox.builder().androidContext(context.applicationContext).build()
    }
}