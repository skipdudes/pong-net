package net.pong

import java.io.PrintStream
import java.net.ServerSocket

class Server() : Thread() {
    var serverSocket: ServerSocket? = null
    var serverPort: Int = 8080
    var message: String = "SERVER RESPONSE"

    override fun run() {
        try {
            serverSocket = ServerSocket(serverPort)

            while (true) {
                val socket = serverSocket!!.accept()
                val outputStream = socket!!.getOutputStream()
                val printStream = PrintStream(outputStream)
                printStream.print(message)
                printStream.close()
                outputStream.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}