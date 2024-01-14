package com.example.comp.ui.util

import android.content.Context

//TODO via chatgpt, check if there is a decent built in way to do this

// "    Use the Function in Your Activity or Fragment: To use this function, call it from your activity or fragment, passing the appropriate context and resource ID."
// val fileContents = readTextFileFromResource(this, R.raw.your_text_file)
fun readTextFileFromResource(context: Context, resourceId: Int): String {
    return context.resources.openRawResource(resourceId).use { inputStream ->
        inputStream.bufferedReader().use {
            it.readText()
        }
    }
}