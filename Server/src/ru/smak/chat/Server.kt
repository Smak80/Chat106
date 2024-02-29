package ru.smak.chat

import java.net.ServerSocket
import kotlin.concurrent.thread

class Server(port: Int = 5106) {
    private val serverSocket = ServerSocket(port)

    private var stop = false

    fun start(){
        thread {
            try {
                while (!stop) {
                    ConnectedClient(serverSocket.accept()).start()
                }
            } catch (_: Throwable) {
                println("Работа сервера завершена из-за ошибки :(")
            } finally {
                serverSocket.close()
            }
        }
    }

    fun stop(){
        stop = true
    }
}