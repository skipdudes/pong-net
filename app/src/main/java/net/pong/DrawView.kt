package net.pong

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawView : View {

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

    private val pointerToSide = mutableMapOf<Int, Side>()
    enum class Side { LEFT, RIGHT }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Background
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackground)

        // Position initialization (only once)
        if (ballX == 0f && ballY == 0f) {
            leftY = height / 2f - paddleHeight / 2f
            rightY = height / 2f - paddleHeight / 2f
            ballX = width / 2f
            ballY = height / 2f
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
                        Side.LEFT -> leftY = y - paddleHeight / 2
                        Side.RIGHT -> rightY = y - paddleHeight / 2
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