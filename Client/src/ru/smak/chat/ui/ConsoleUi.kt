package ru.smak.chat.ui

import kotlin.concurrent.thread

class ConsoleUi : UI {
    private val stopListeners = mutableListOf<()->Unit>()
    private val dataReadyListeners = mutableListOf<(String)->Unit>()
    private var stop = false

    override fun showMessage(sender: String, message: String) {
        println("$sender: $message")
    }

    override fun showInfo(info: String) {
        println(info)
    }

    override fun addDataReadyListener(l: (String) -> Unit) {
        dataReadyListeners.add(l)
    }

    override fun removeDataReadyListener(l: (String) -> Unit) {
        dataReadyListeners.remove(l)
    }

    fun addStopListener(l: () -> Unit) {
        stopListeners.add(l)
    }

    fun removeStopListener(l: () -> Unit) {
        stopListeners.remove(l)
    }

    fun start(){
        stop = false
        thread {
            while (!stop) {
                val data = readln()
                stop = data.uppercase().trim() == "BYE"
                if (stop) stopListeners.forEach { it() }
                dataReadyListeners.forEach { it(data) }
            }
        }
    }

}