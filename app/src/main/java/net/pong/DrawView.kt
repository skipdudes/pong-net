package net.pong

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawView : View {

    // Paint
    private val paintPaddle = Paint().apply { color = Color.BLACK }
    private val paintBall = Paint().apply { color = Color.RED }
    private val paintBackground = Paint().apply { color = Color.LTGRAY }

    // Dimensions
    private val paddleWidth = 40f
    private val paddleHeight = 200f
    private val ballRadius = 30f

    // Positions
    private var leftY = 0f
    private var rightY = 0f
    private var ballX = 0f
    private var ballY = 0f

    // Ball velocity
    private val initialSpeed = 10f
    private var ballVX = initialSpeed
    private var ballVY = initialSpeed
    private var bounceForce = 1.2f

    // Pointer mapping
    private val pointerToSide = mutableMapOf<Int, Side>()
    enum class Side { LEFT, RIGHT }

    // Who just lost
    private enum class LoseSide { NONE, LEFT, RIGHT }
    private var lastLoser: LoseSide = LoseSide.NONE

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private val frameRate = 16L

    private val updater = object : Runnable {
        override fun run() {
            updateGame()
            invalidate()
            postDelayed(this, frameRate)
        }
    }

    init { post(updater) }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Background
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackground)

        // First round init
        if (ballX == 0f && ballY == 0f) {
            leftY = height / 2f - paddleHeight / 2f
            rightY = height / 2f - paddleHeight / 2f
            resetBall()
        }

        // Left paddle
        canvas.drawRect(
            50f, leftY,
            50f + paddleWidth, leftY + paddleHeight,
            paintPaddle
        )

        // Right paddle
        canvas.drawRect(
            width - 50f - paddleWidth, rightY,
            width - 50f, rightY + paddleHeight,
            paintPaddle
        )

        // Ball
        canvas.drawCircle(ballX, ballY, ballRadius, paintBall)
    }

    private fun updateGame() {
        if (width == 0 || height == 0) return

        // Move ball
        ballX += ballVX
        ballY += ballVY

        // Top & bottom bounce
        if (ballY - ballRadius < 0) {
            ballY = ballRadius
            ballVY = -ballVY
        }
        if (ballY + ballRadius > height) {
            ballY = height - ballRadius
            ballVY = -ballVY
        }

        // Paddle bounce
        val leftPaddleX = 50f
        if (ballX - ballRadius < leftPaddleX + paddleWidth &&
            ballY > leftY && ballY < leftY + paddleHeight) {
            ballX = leftPaddleX + paddleWidth + ballRadius
            ballVX = -ballVX * bounceForce
        }
        val rightPaddleX = width - 50f - paddleWidth
        if (ballX + ballRadius > rightPaddleX &&
            ballY > rightY && ballY < rightY + paddleHeight) {
            ballX = rightPaddleX - ballRadius
            ballVX = -ballVX * bounceForce
        }

        // Score point and reset
        if (ballX < 0) {
            lastLoser = LoseSide.LEFT
            resetBall()
        }
        if (ballX > width) {
            lastLoser = LoseSide.RIGHT
            resetBall()
        }
    }

    private fun resetBall() {
        when (lastLoser) {

            LoseSide.NONE -> {
                ballX = width / 2f
                ballY = height / 2f
            }

            LoseSide.LEFT -> {
                val paddleX = 50f + paddleWidth + ballRadius + 5f
                ballX = paddleX
                ballY = leftY + paddleHeight / 2f
            }

            LoseSide.RIGHT -> {
                val paddleX = width - 50f - paddleWidth - ballRadius - 5f
                ballX = paddleX
                ballY = rightY + paddleHeight / 2f
            }
        }

        // Reset velocity
        ballVX = if ((0..1).random() == 0) initialSpeed else -initialSpeed
        ballVY = if ((0..1).random() == 0) initialSpeed else -initialSpeed
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {

            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> {
                val index = event.actionIndex
                val pointerId = event.getPointerId(index)
                val x = event.getX(index)

                // Assign finger to paddle based on touch event
                pointerToSide[pointerId] = if (x < width / 2) Side.LEFT else Side.RIGHT
            }

            MotionEvent.ACTION_MOVE -> {
                for (i in 0 until event.pointerCount) {
                    val pointerId = event.getPointerId(i)
                    val y = event.getY(i)

                    when (pointerToSide[pointerId]) {
                        Side.LEFT -> leftY = (y - paddleHeight / 2).coerceIn(0f, height - paddleHeight)
                        Side.RIGHT -> rightY = (y - paddleHeight / 2).coerceIn(0f, height - paddleHeight)
                        null -> {}
                    }
                }
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_CANCEL -> {
                val pointerId = event.getPointerId(event.actionIndex)
                pointerToSide.remove(pointerId)
                performClick()
            }
        }

        invalidate()
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}