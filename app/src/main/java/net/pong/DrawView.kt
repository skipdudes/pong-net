package net.pong

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class DrawView : View {

    private val paintPaddle = Paint().apply {
        color = Color.BLACK
    }

    private val paintBall = Paint().apply {
        color = Color.RED
    }

    private val paintBackground = Paint().apply {
        color = Color.LTGRAY
    }

    // wymiary paletek i piłki
    private val paddleWidth = 40f
    private val paddleHeight = 200f
    private val ballRadius = 30f

    // pozycje
    private var leftY = 0f
    private var rightY = 0f
    private var ballX = 0f
    private var ballY = 0f

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // tło
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackground)

        // inicjalizacja pozycji (robi się tylko raz)
        if (ballX == 0f && ballY == 0f) {
            leftY = height / 2f - paddleHeight / 2f
            rightY = height / 2f - paddleHeight / 2f
            ballX = width / 2f
            ballY = height / 2f
        }

        // lewa paletka
        canvas.drawRect(
            50f, leftY,
            50f + paddleWidth, leftY + paddleHeight,
            paintPaddle
        )

        // prawa paletka
        canvas.drawRect(
            width - 50f - paddleWidth, rightY,
            width - 50f, rightY + paddleHeight,
            paintPaddle
        )

        // piłeczka
        canvas.drawCircle(ballX, ballY, ballRadius, paintBall)
    }
}