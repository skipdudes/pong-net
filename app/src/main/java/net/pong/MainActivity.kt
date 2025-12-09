package net.pong

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {

    private var server: Server? = null
    private var client: Client? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val mainLayout = findViewById<ConstraintLayout>(R.id.mainView)
        val panel = findViewById<View>(R.id.startPanel)
        val ipInput = findViewById<EditText>(R.id.ipInput)
        val btnServer = findViewById<Button>(R.id.btnStartServer)
        val btnClient = findViewById<Button>(R.id.btnStartClient)
        val drawView = DrawView(this)

        // Server
        btnServer.setOnClickListener {
            server = Server().apply {
                message = "SERVER RUNNING"
                start()
            }

            panel.visibility = View.GONE
            mainLayout.addView(drawView)
            Toast.makeText(this, "Server started successfully", Toast.LENGTH_SHORT).show()
        }

        // Client
        btnClient.setOnClickListener {
            val ip = ipInput.text.toString().trim()

            if (ip.isNotEmpty()) {
                client = Client(this).apply {
                    dstAddress = ip
                    start()
                }

                panel.visibility = View.GONE
                mainLayout.addView(drawView)
            }
        }
    }
}