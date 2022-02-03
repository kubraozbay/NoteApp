package com.task.noteapp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.io.ByteArrayOutputStream

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Bitmap?.getByteArrayFromBitmap(): ByteArray? {
    if (this == null) return null
    val byteArrayOutputStream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
}

fun ByteArray?.getBitmapFromByteArray(): Bitmap? {
    return if (this == null) null
    else BitmapFactory.decodeByteArray(
        this,
        0,
        this.size
    )
}