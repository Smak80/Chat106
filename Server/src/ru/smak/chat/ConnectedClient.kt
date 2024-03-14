package ru.smak.chat

import ru.smak.chat.net.Communicator
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import kotlin.concurrent.thread

class ConnectedClient(private val clientSocket: Socket) {

    private val communicator: Communicator by lazy {
        Communicator(clientSocket).apply {
            addStopWorkingListener { clients.removeIf { !it.isAlive } }
        }
    }

    companion object{
        private val clients = mutableListOf<ConnectedClient>()
    }

    init{
        clients.add(this)
    }

    val isAlive
        get() = communicator.isAlive

    fun start() = communicator.startDataReceiving{ data ->
        sendToAll(data)
    }

    private fun sendToAll(data: String) {
        clients.forEach { it.send(data) }
    }

    fun send(data: String) = communicator.send(data)

    fun stop() = communicator.stop()

}