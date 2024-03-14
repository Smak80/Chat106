package ru.smak.chat.net

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import kotlin.concurrent.thread

class Client(val host: String = "localhost", val port: Int = 5106) {
    private val serverDataReceivedListener = mutableListOf<(String)->Unit>()
    private var socket: Socket? = null
    private var stop = false

    fun start(){
        stop = false
        thread {
            try {
                socket = Socket(host, port)
                startCommunicating()
            } catch (_: Throwable) {
                println("До свидания!")
            } finally {
                socket?.close()
            }
        }
    }

    private fun startCommunicating() {
        while (!stop && socket != null) {
            val data = readData()
            serverDataReceivedListener.forEach {
                it(data)
            }
        }
    }

    fun readData(): String {
        return socket?.let{
            BufferedReader(InputStreamReader(it.getInputStream())).readLine()
        } ?: ""
    }

    fun send(data: String){
        try {
            socket?.let {
                PrintWriter(it.getOutputStream()).apply {
                    println(data)
                    flush()
                }
            }
        } catch(_: Throwable){}
    }

    fun stop(){
        stop = true
        socket?.close()
    }

    fun addServerDataReceivedListener(l:(String)->Unit){
        serverDataReceivedListener.add(l)
    }

    fun removeServerDataReceivedListener(l:(String)->Unit){
        serverDataReceivedListener.remove(l)
    }
}