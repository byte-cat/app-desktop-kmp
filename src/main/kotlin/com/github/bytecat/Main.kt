package com.github.bytecat

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.github.bytecat.contact.Cat
import com.github.bytecat.contact.CatBook
import com.github.bytecat.message.Message
import com.github.bytecat.message.MessageBox
import com.github.bytecat.protocol.data.FileRequestData
import com.github.bytecat.resource.R
import com.github.bytecat.utils.IDebugger
import com.github.bytecat.vm.CatBookVM
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

private val messageCallback = object : MessageBox.Callback {
    override fun onMessageReceived(cat: Cat, message: Message<*>) {
        println("onMessageReceived message=$message")
        if (message.data is FileRequestData) {
            val fileReqData = message.data as FileRequestData

            byteCat.acceptFileRequest(cat, fileReqData)
        }
    }
}

private val catBookCallback = object : CatBook.Callback {
    override fun onContactAdd(cat: Cat) {
        catBookVM.addCat(cat)
        MessageBox.obtain(cat).registerCallback(messageCallback)
    }

    override fun onContactRemove(cat: Cat) {
        catBookVM.removeCat(cat)
        MessageBox.obtain(cat).unregisterCallback(messageCallback)
    }

    override fun onContactUpdate(cat: Cat) {
    }
}

private val catBookVM = CatBookVM()

private val chosenCat = mutableStateOf<Cat?>(null)

private val pendingMsg = mutableStateOf("")

@Composable
@Preview
fun App() {
    MaterialTheme(
        colors = themeColors()
    ) {
        if (chosenCat.value != null) {
            Dialog(onDismissRequest = {
                chosenCat.value = null
            }) {
                Column (
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .background(color = myCatTheme().myCatBackgroundColor, shape = RoundedCornerShape(16.dp)),
                    horizontalAlignment = Alignment.End
                ) {
                    TextField(
                        pendingMsg.value,
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        onValueChange = {
                            pendingMsg.value = it
                        }
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_send),
                        modifier = Modifier.size(40.dp)
                            .padding(8.dp)
                            .clickable {
                                byteCat.sendMessage(chosenCat.value!!, pendingMsg.value)
                                pendingMsg.value = ""
                                chosenCat.value = null
                            },
                        contentDescription = ""
                    )
                }
            }
        }
        MainView(catBookVM) {
            chosenCat.value = it
        }
    }
}

fun main() = application {

    val icon = painterResource(resourcePath = R.drawable.ic_launcher)

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
        icon = icon,
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
