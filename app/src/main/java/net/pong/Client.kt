package net.pong

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.net.Socket

class Client(val context: Context) : Thread() {
    var dstAddress: String = "0.0.0.0"
    var dstPort: Int = 8080
    var messageInput: String = "NO RESPONSE YET"

    override fun run() {
        try {
            val buffer = ByteArray(1024)

            while (true) {
                val socket = Socket(dstAddress, dstPort)

                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, "Client connected successfully", Toast.LENGTH_SHORT).show()
                }

                val inputStream = socket.getInputStream()
                var count = 0
                if ((inputStream.read(buffer).also { count = it }) != -1) {
                    val byteArrayOutputStream = ByteArrayOutputStream(1024)
                    byteArrayOutputStream.write(buffer, 0, count)
                    messageInput = byteArrayOutputStream.toString("UTF-8")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}