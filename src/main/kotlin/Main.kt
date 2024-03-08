import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.github.bytecat.ByteCat
import com.github.bytecat.contact.Cat
import com.github.bytecat.contact.CatBook
import com.github.bytecat.utils.IDebugger
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
        if (cats.contains(cat)) {
            return
        }
        cats.add(cat)
    }

    override fun onContactRemove(cat: Cat) {
        cats.remove(cat)
    }

    override fun onContactUpdate(cat: Cat) {
    }
}
private val cats = mutableStateListOf<Cat>()

@Composable
@Preview
fun App() {
    MaterialTheme {
        LazyColumn {
            items(cats) {
                Text(it.name)
            }
        }
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
