package ru.smak.chat.net

import java.net.Socket

class Client(host: String = "localhost", port: Int = 5106) {
    private val serverDataReceivedListener = mutableListOf<(String)->Unit>()

    private val communicator: Communicator by lazy {
        Communicator(Socket(host, port))
    }

    fun start(){
        communicator.startDataReceiving { data ->
            serverDataReceivedListener.forEach {
                it(data)
            }
        }
    }

    fun send(data: String) = communicator.send(data)

    fun stop() = communicator.stop()

    fun addServerDataReceivedListener(l:(String)->Unit){
        serverDataReceivedListener.add(l)
    }

    fun removeServerDataReceivedListener(l:(String)->Unit){
        serverDataReceivedListener.remove(l)
    }
}