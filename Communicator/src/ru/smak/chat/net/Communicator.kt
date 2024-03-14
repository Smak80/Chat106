package ru.smak.chat.net

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import kotlin.concurrent.thread

class Communicator(private val socket: Socket){

    private val reader: BufferedReader =
        BufferedReader(
            InputStreamReader(socket.getInputStream())
        )
    private val writer: PrintWriter =
        PrintWriter(socket.getOutputStream())

    private val stopWorkingListener = mutableListOf<()->Unit>()

    fun addStopWorkingListener(l: ()->Unit) = stopWorkingListener.add(l)
    fun removeStopWorkingListener(l: ()->Unit) = stopWorkingListener.remove(l)

    private var stop = false
    val isAlive
        get() = !stop

    fun send(data: String){
        try {
            socket.let {
                writer.apply {
                    println(data)
                    flush()
                }
            }
        } catch(_: Throwable){
            stop()
        }
    }

    fun startDataReceiving(onDataReceived: (String)->Unit){
        thread {
            try {
                while (isAlive) {
                    val data = reader.readLine() ?: ""
                    onDataReceived(data)
                }
            } catch (_: Throwable){
                stop()
            }
        }
    }

    fun stop() {
        stop = true
        socket.close()
        stopWorkingListener.forEach { it() }
    }

}