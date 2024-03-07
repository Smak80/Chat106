package ru.smak.chat.ui

interface UI {
    fun showMessage(sender:String,message:String)

    fun showInfo(info:String)

    fun addDataReadyListener(l:(String)->Unit)

    fun removeDataReadyListener(l:(String)->Unit)
}