package com.github.bytecat

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.github.bytecat.contact.Cat
import com.github.bytecat.contact.CatBook
import com.github.bytecat.utils.IDebugger
import com.github.bytecat.vm.CatBookViewModel
import java.awt.event.WindowEvent
import java.awt.event.WindowFocusListener

private val debuggerDefault by lazy {
    object : IDebugger {
        override fun onBroadcastReady() {
            println("onBroadcastReady")
        }

        override fun onBroadcastReceived(fromIp: String, data: ByteArray) {
            println("onBroadcastReceived fromIp=$fromIp text=${String(data)}")
        }

        override fun onContactAdd(cat: Cat) {
            println("onContactAdd ${cat.name}")
        }

        override fun onContactRemove(cat: Cat) {
            println("onContactRemove ${cat.name}")
        }

        override fun onMessageReady() {
            println("onMessageReady")
        }

        override fun onMessageReceived(fromIp: String, data: ByteArray) {
            println("onMessageReceived fromIp=$fromIp text=${String(data)}")
        }
    }
}

private val catCallback by lazy {
    object : ByteCat.Callback {
        override fun onReady(myCat: Cat) {
            catBookVM.myCat.value = myCat
        }
        override fun onCatMessage(cat: Cat, text: String) {
            println("Receive message from ${cat.name}: $text")
        }
    }
}

private val byteCat by lazy {
    object : ByteCat() {
        override val debugger: IDebugger
            get() = debuggerDefault
    }.apply {
        setCallback(catCallback)
    }
}

private val catBookCallback = object : CatBook.Callback {
    override fun onContactAdd(cat: Cat) {
        catBookVM.addCat(cat)
    }

    override fun onContactRemove(cat: Cat) {
        catBookVM.removeCat(cat)
    }

    override fun onContactUpdate(cat: Cat) {
    }
}

private val catBookVM = CatBookViewModel()

@Composable
@Preview
fun App() {
    MaterialTheme(
        colors = themeColors()
    ) {
        MainView(catBookVM) {

        }
        /*Image(
            painter = painterResource("drawable/ic_apple.xml"),
            contentDescription = ""
        )*/
    }
}

fun main() = application {

    val windowFocusListener = object : WindowFocusListener {
        override fun windowGainedFocus(e: WindowEvent?) {
            println("windowGainedFocus")
            byteCat.refresh()
        }

        override fun windowLostFocus(e: WindowEvent?) {
            println("windowLostFocus")
        }
    }

    byteCat.catBook.registerCallback(catBookCallback)
    byteCat.startup()
    Window(
        title = "ByteCat Desktop",
        resizable = false,
        state = rememberWindowState(
            width = 320.dp, height = 480.dp,
            position = WindowPosition(Alignment.Center)
        ),
        onCloseRequest = {
            byteCat.catBook.unregisterCallback(catBookCallback)
            byteCat.shutdown()
            exitApplication()
        }
    ) {
        this.window.addWindowFocusListener(windowFocusListener)
        App()
    }
}
