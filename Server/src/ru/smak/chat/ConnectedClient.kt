package ru.smak.chat

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import kotlin.concurrent.thread

class ConnectedClient(private val clientSocket: Socket) {

    companion object{
        private val clients = mutableListOf<ConnectedClient>()
    }

    init{
        clients.add(this)
    }

    private var stop = false
    private val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
    private val writer = PrintWriter(clientSocket.getOutputStream())
    val isAlive
        get() = !stop

    fun start(){
        thread {
            while (!stop) {
                val data = readData()
                sendToAll(data)
            }
        }
    }

    private fun sendToAll(data: String) {
        clients.forEach { it.send(data) }
    }

    private fun send(data: String){
        try {
            writer.apply {
                println(data)
                flush()
            }
        } catch (_: Throwable){
            stop()
        }
    }

    fun stop(){
        stop = true
        clientSocket.close()
        clients.removeIf { !it.isAlive }
    }

    fun readData(): String = reader.readLine() ?: ""

}