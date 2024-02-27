package com.terracode.blueharvest
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class SpeedometerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val MAX_SPEED = 20f
    private val START_ANGLE = 135f
    private val SWEEP_ANGLE = 270f

    private var currentSpeed = 0f

    private val dialPaint: Paint
    private val needlePaint: Paint

    init {
        dialPaint = Paint().apply {
            color = Color.GRAY
            style = Paint.Style.STROKE
            strokeWidth = 20f
        }

        needlePaint = Paint().apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 10f
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the speedometer dial
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = Math.min(centerX, centerY) - 40
        canvas.drawArc(
            centerX - radius, centerY - radius, centerX + radius, centerY + radius,
            START_ANGLE, SWEEP_ANGLE, false, dialPaint
        )

        // Draw the speedometer needle
        val angle = START_ANGLE + currentSpeed / MAX_SPEED * SWEEP_ANGLE
        val needleLength = radius - 40
        val needleX = centerX + needleLength * Math.cos(Math.toRadians(angle.toDouble())).toFloat()
        val needleY = centerY + needleLength * Math.sin(Math.toRadians(angle.toDouble())).toFloat()
        canvas.drawLine(centerX, centerY, needleX, needleY, needlePaint)
    }

    fun setCurrentSpeed(speed: Float) {
        if (speed in 0f..MAX_SPEED) {
            currentSpeed = speed
            invalidate()
        }
    }
}
