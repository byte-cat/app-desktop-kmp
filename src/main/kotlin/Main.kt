import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.bytecat.ByteCat
import com.github.bytecat.IDebugger
import com.github.bytecat.contact.CatBook
import com.github.bytecat.contact.Contact

private val debuggerDefault by lazy {
    object : IDebugger {
        override fun onBroadcastReady() {
            println("onBroadcastReady")
        }

        override fun onBroadcastReceived(fromIp: String, data: ByteArray) {
            println("onBroadcastReceived fromIp=$fromIp text=${String(data)}")
        }

        override fun onContactAdd(contact: Contact) {
            println("onContactAdd ${contact.id}")
        }

        override fun onContactRemove(contact: Contact) {
            println("onContactRemove ${contact.id}")
        }

        override fun onMessageReady() {
            println("onMessageReady")
        }

        override fun onMessageReceived(fromIp: String, data: ByteArray) {
            println("onMessageReceived fromIp=$fromIp text=${String(data)}")
        }
    }
}
private val byteCat by lazy {
    object : ByteCat() {
        override val debugger: IDebugger
            get() = debuggerDefault
    }
}

private val catBookCallback = object : CatBook.Callback {
    override fun onContactAdd(contact: Contact) {
        if (cats.contains(contact)) {
            return
        }
        cats.add(contact)
    }

    override fun onContactRemove(contact: Contact) {
        cats.remove(contact)
    }
}
private val cats = mutableStateListOf<Contact>()

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
    byteCat.catBook.registerCallback(catBookCallback)
    byteCat.startup()
    Window(title = "ByteCat Desktop", onCloseRequest = {
        byteCat.catBook.unregisterCallback(catBookCallback)
        byteCat.shutdown()
        exitApplication()
    }) {
        App()
    }
}
