package com.github.bytecat.resource

class Drawable {

    val ic_launcher: String = drawableOf("ic_launcher.png")

    val ic_android: String = drawableOf("ic_android.xml")
    val ic_apple: String = drawableOf("ic_apple.xml")
    val ic_apple_finder: String = drawableOf("ic_apple_finder.xml")
    val ic_cat: String = drawableOf("ic_cat.xml")
    val ic_file_send_outline: String = drawableOf("ic_file_send_outline.xml")
    val ic_message_fast_outline: String = drawableOf("ic_message_fast_outline.xml")
    val ic_microsoft_windows: String = drawableOf("ic_microsoft_windows.xml")
    val ic_send: String = drawableOf("ic_send.xml")
    val img_no_cats: String = drawableOf("img_no_cats.xml")

    fun drawableOf(id: String): String {
        return "drawable/$id"
    }
}