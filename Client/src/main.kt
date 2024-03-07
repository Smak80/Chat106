import ru.smak.chat.net.Client
import ru.smak.chat.ui.ConsoleUi

fun main() {
    val cui = ConsoleUi().apply {
        start()
    }
    Client().apply {
        cui.addStopListener { stop() }
        cui.addDataReadyListener { send(it) }
        addServerDataReceivedListener { cui.showInfo(it) }
        start()
    }
}