package ru.smak.chat

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.net.SocketException
import kotlin.concurrent.thread

class Client(val host: String = "localhost", val port: Int = 5106) {

    private var socket: Socket? = null
    private var stop = false

    fun start(){
        stop = false
        thread {
            try {
                socket = Socket(host, port)
                startCommunicating()
            } catch (_: Throwable) {
                println("Ошибка :(")
            } finally {
                socket?.close()
            }
        }
    }

    private fun startCommunicating() {
        while (!stop && socket != null) {
            val data = readData()
            process(data)
        }
    }

    fun readData(): String {
        return socket?.let{
            BufferedReader(InputStreamReader(it.getInputStream())).readLine()
        } ?: ""
    }

    private fun process(data: String){
        println(data)
    }

    fun send(data: String){
        socket?.let{
            PrintWriter(it.getOutputStream()).apply {
                println(data)
                flush()
            }
        }
    }

    fun stop(){
        stop = true
        socket?.close()
    }

}