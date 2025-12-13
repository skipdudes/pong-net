package net.pong

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    private var client: Client? = null
    private var server: Server? = null

    private var posX = 0
    private var posY = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val layout = findViewById<ConstraintLayout>(R.id.mainView)
        val panel = findViewById<View>(R.id.startPanel)
        val ipInput = findViewById<EditText>(R.id.ipInput)
        val btnServer = findViewById<Button>(R.id.btnStartServer)
        val btnClient = findViewById<Button>(R.id.btnStartClient)
        //val drawView = DrawView(this)

        // Client
        btnClient.setOnClickListener {
            val ip = ipInput.text.toString().trim()

            if (ip.isNotEmpty()) {
                // Connect
                client = Client(this, ip) { msg ->
                    Log.d("CLIENT", "Received: $msg")
                }
                client!!.start()

                // Run game (client mode)
                panel.visibility = View.GONE
                //layout.addView(drawView)

            } else {
                Toast.makeText(this, "Enter server IP address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

        // Server
        btnServer.setOnClickListener {
            // Start
            server = Server {
                updateData()
                "$posX;$posY"
            }
            server!!.start()

            // Run game (server mode)
            panel.visibility = View.GONE
            //layout.addView(drawView)
            Toast.makeText(this, "Server started successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateData() {
        // todo: Delete this, now it's just for testing
        posX++
        posY++
    }
}