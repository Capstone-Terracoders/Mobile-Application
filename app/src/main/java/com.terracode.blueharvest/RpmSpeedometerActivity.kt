package com.terracode.blueharvest
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.cos
import kotlin.math.sin

class RpmSpeedometerActivity @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val blueBerryColor = ContextCompat.getColor(context, R.color.blueBerry)
    private val lightGreyColor = ContextCompat.getColor(context, R.color.lightGrey)

    // Paint objects
    private val dialPaint = Paint().apply {
        color = blueBerryColor // Use custom color
        style = Paint.Style.STROKE
        strokeWidth = 20f
    }

    private val needlePaint = Paint().apply {
        color = lightGreyColor // Use custom color
        style = Paint.Style.STROKE
        strokeWidth = 50f
    }

    private val DEFAULT_DIAL_WIDTH = 20f
    private val DEFAULT_NEEDLE_WIDTH = 10f

    private var dialWidth: Float = DEFAULT_DIAL_WIDTH
    private var needleWidth: Float = DEFAULT_NEEDLE_WIDTH

    private val MAX_SPEED = 20f
    private val START_ANGLE = 135f
    private val SWEEP_ANGLE = 270f

    private var currentSpeed = 0f

    private val textPaint: Paint = Paint().apply {
        color = Color.BLACK
        textSize = 20f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RpmSpeedometer)
        dialWidth = typedArray.getDimension(R.styleable.RpmSpeedometer_dialWidth, DEFAULT_DIAL_WIDTH)
        needleWidth = typedArray.getDimension(R.styleable.RpmSpeedometer_needleWidth, DEFAULT_NEEDLE_WIDTH)
        typedArray.recycle()
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

        // Draw range indicators and labels
        val range1Speed = MAX_SPEED * 0.25f
        val range2Speed = MAX_SPEED * 0.5f
        val range3Speed = MAX_SPEED * 0.75f

        drawRangeIndicator(canvas, centerX, centerY, radius, range1Speed, "0")
        drawRangeIndicator(canvas, centerX, centerY, radius, range2Speed, "5")
        drawRangeIndicator(canvas, centerX, centerY, radius, range3Speed, "10")

        // Draw the speedometer needle
        val angle = START_ANGLE + currentSpeed / MAX_SPEED * SWEEP_ANGLE
        val needleLength = radius - 40
        val needleX = centerX + needleLength * Math.cos(Math.toRadians(angle.toDouble())).toFloat()
        val needleY = centerY + needleLength * Math.sin(Math.toRadians(angle.toDouble())).toFloat()
        canvas.drawLine(centerX, centerY, needleX, needleY, needlePaint)

        // Draw current speed text
        canvas.drawText("$currentSpeed", centerX, centerY + 50, textPaint)
    }

    private fun drawRangeIndicator(canvas: Canvas, centerX: Float, centerY: Float, radius: Float, speed: Float, label: String) {
        val numTicks = 5 // Including the original 3 ticks
        val angleInterval = (180f) / (numTicks - 1) // Calculate the angle between each tick
        val startAngle = 0f // Starting angle at 270 degrees
        val tickLength = 60f // Length of the ticks
        val tickWidth = 50f // Width of the ticks

        val needleLength = radius - 40 // Adjusted needle length

        for (i in 0 until numTicks) {
            val angle = startAngle - i * angleInterval // Calculate the angle for each tick
            val endX = centerX + needleLength * cos(Math.toRadians(angle.toDouble())).toFloat() // Adjusted endpoint X
            val endY = centerY + needleLength * sin(Math.toRadians(angle.toDouble())).toFloat() // Adjusted endpoint Y
            val startX = centerX + (needleLength - tickLength) * cos(Math.toRadians(angle.toDouble())).toFloat() // Adjusted start X
            val startY = centerY + (needleLength - tickLength) * sin(Math.toRadians(angle.toDouble())).toFloat() // Adjusted start Y
            canvas.drawLine(startX, startY, endX, endY, dialPaint.apply {
                strokeWidth = tickWidth
            })
            canvas.drawText(label, endX, endY, textPaint)
        }
    }


    fun setCurrentSpeed(speed: Float) {
        if (speed in 0f..MAX_SPEED) {
            currentSpeed = speed
            invalidate()
        }
    }
}


