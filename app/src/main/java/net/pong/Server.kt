package net.pong

import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

class Server(
    private val onSend: () -> String //todo: Change this
) : Thread() {
    private val port = 8080
    private var running = true

    override fun run() {
        try {
            val serverSocket = ServerSocket(port)
            val client: Socket = serverSocket.accept()
            val writer = PrintWriter(client.getOutputStream(), true)

            while (running) {
                val msg = onSend() //todo: Change this
                writer.println(msg)
                sleep(50) //todo: Change this
            }

            client.close()
            serverSocket.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopServer() {
        running = false
    }
}