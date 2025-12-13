package net.pong

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket

class Client(
    val context: Context,
    private val dstAddress: String,
    private val onReceive: (String) -> Unit //todo: Change this
) : Thread() {
    private val dstPort = 8080

    override fun run() {
        try {
            val socket = Socket(dstAddress, dstPort)
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, "Connected to the server", Toast.LENGTH_SHORT).show()
            }

            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            while (true) {
                val msg = reader.readLine() ?: break
                onReceive(msg) //todo: Change this
            }

            socket.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}