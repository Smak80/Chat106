import ru.smak.chat.Client

fun main() {
    val c = Client().apply {
        start()
        Thread.sleep(5000)
        send("Данные 1")
        Thread.sleep(5000)
        send("Данные 2")
        Thread.sleep(5000)
        send("Данные 3")
    }
    readln()
}