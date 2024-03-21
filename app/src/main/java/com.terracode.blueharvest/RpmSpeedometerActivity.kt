package com.terracode.blueharvest

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.terracode.blueharvest.utils.PreferenceManager
import com.terracode.blueharvest.utils.ReadJSONObject
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class RpmSpeedometerActivity @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    //Initialize the sharedPreferences
    init {
        PreferenceManager.init(context)

        val sensorData = ReadJSONObject.fromAsset(context, "SensorDataExample.json")
        sensorData?.apply {
            rpmData = getRPM()
        }
    }

    private var rpmData: Double? = null
    private val blueBerryColor = ContextCompat.getColor(context, R.color.blueBerry)
    private val blackColor = ContextCompat.getColor(context, R.color.black)

    private val DEFAULT_DIAL_WIDTH = 20f
    private val DEFAULT_NEEDLE_WIDTH = 10f

    private var dialWidth: Float = DEFAULT_DIAL_WIDTH
    private var needleWidth: Float = DEFAULT_NEEDLE_WIDTH

    private var MAX_SPEED = PreferenceManager.getMaxRPMDisplayedInput()
    private var currentSpeed = rpmData


    private val START_ANGLE = 135f
    private val SWEEP_ANGLE = 270f

    private var numBigTicks = 6 //change this value to display different max value
    private var numSmallTicks = 4

    private var needleRotationAnimator: ObjectAnimator? = null

    // Paint objects
    private val dialPaint = Paint().apply {
        color = blueBerryColor // Use custom color
        style = Paint.Style.STROKE
        strokeWidth = 30f // Adjust the stroke width as needed
        strokeCap = Paint.Cap.ROUND // Set the stroke cap to round to make the ticks rounded
    }

    private val needlePaint = Paint().apply {
        color = blackColor // Use custom color
        style =
            Paint.Style.FILL_AND_STROKE // Use FILL_AND_STROKE to fill the shape and draw its outline
        strokeWidth = DEFAULT_NEEDLE_WIDTH // Set the stroke width to make the entire needle thicker
        strokeCap = Paint.Cap.ROUND // Set the stroke cap to round to create rounded ends
    }

    private val textPaint: Paint = Paint().apply {
        color = Color.BLACK
        textSize = PreferenceManager.getSelectedTextSize()
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    init {
        needleRotationAnimator = ObjectAnimator.ofFloat(this, "needleRotation", 0f, 20f).apply {
            duration = 1000 // Animation duration in milliseconds
            repeatCount = ObjectAnimator.INFINITE // Repeat indefinitely
            repeatMode = ObjectAnimator.REVERSE // Reverse animation direction when repeating
        }
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RpmSpeedometer)
        dialWidth =
            typedArray.getDimension(R.styleable.RpmSpeedometer_dialWidth, DEFAULT_DIAL_WIDTH)
        needleWidth =
            typedArray.getDimension(R.styleable.RpmSpeedometer_needleWidth, DEFAULT_NEEDLE_WIDTH)
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Declare & initialize ark parameters:
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(centerX, centerY) - 40

        canvas.drawArc(
            centerX - radius, centerY - radius, centerX + radius, centerY + radius,
            START_ANGLE, SWEEP_ANGLE, false, dialPaint
        )

        drawRangeIndicator(canvas, centerX, centerY, radius)

        // Draw the speedometer needle
        val speedAngle: Double?
        val angle = if (MAX_SPEED != 0 && currentSpeed != null) {
            if (currentSpeed!! > MAX_SPEED){
                speedAngle = MAX_SPEED.toDouble()
            } else {
                speedAngle = currentSpeed
            }
            START_ANGLE + (speedAngle!! / MAX_SPEED) * SWEEP_ANGLE
        } else {
            0.0
        }
        if (angle != null) {
            val needleLength = radius - 40
            val needleX = centerX + needleLength * cos(Math.toRadians(angle)).toFloat()
            val needleY = centerY + needleLength * sin(Math.toRadians(angle)).toFloat()
            canvas.drawLine(centerX, centerY, needleX, needleY, needlePaint)
        } else {
            Log.d("RpmSpeedometerActivity", "Angle for needle is null.")
        }


        textPaint.textSize = PreferenceManager.getSelectedTextSize() // Set text size to 40
        //Change text size to what is in preference manager

        // Draw rounded rectangle for RPM value
        val rpmRectWidth = 400f
        val rpmRectHeight = 150f
        val rpmRectLeft = centerX - rpmRectWidth / 2
        val rpmRectTop = centerY + radius * 0.8f
        val rpmRectRight = rpmRectLeft + rpmRectWidth
        val rpmRectBottom = rpmRectTop + rpmRectHeight
        val cornerRadius = 20f // adjust corner radius as needed
        val rpmRect = RectF(rpmRectLeft, rpmRectTop, rpmRectRight, rpmRectBottom)
        canvas.drawRoundRect(rpmRect, cornerRadius, cornerRadius, dialPaint)

        // Draw RPM value text inside the rounded rectangle
        val rpmText = "Rake RPM $currentSpeed"
        val textRect = Rect()
        textPaint.color = blackColor
        textPaint.getTextBounds(rpmText, 0, rpmText.length, textRect)
        val textY = centerY + radius * 0.8f + (rpmRectHeight + textRect.height()) / 2
        canvas.drawText(rpmText, centerX, textY, textPaint)

        needleRotationAnimator?.start()
    }


    private fun drawRangeIndicator(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        val angleInterval = SWEEP_ANGLE / ((numBigTicks - 1) * numSmallTicks) // Calculate the angle interval between each tick
        val bigTickLength = 60f // Length of the bigger ticks
        val bigTickWidth = 20f // Width of the bigger ticks
        val smallTickLength = bigTickLength / 2 // Length of the smaller ticks
        val smallTickWidth = bigTickWidth / 2 // Width of the smaller ticks

        val needleLength = radius - 40 // Adjusted needle length

        for (i in 0 until (numBigTicks * numSmallTicks) - (numSmallTicks-1)) {
            val bigTickIndex = i / numSmallTicks
            val smallTickIndex = i % numSmallTicks

            val angle = START_ANGLE + i * angleInterval // Calculate the angle for each tick

            val isBigTick = smallTickIndex == 0

            val tickLength = if (isBigTick) bigTickLength else smallTickLength
            val tickWidth = if (isBigTick) bigTickWidth else smallTickWidth

            val endX = centerX + needleLength * cos(Math.toRadians(angle.toDouble())).toFloat()
            val endY = centerY + needleLength * sin(Math.toRadians(angle.toDouble())).toFloat()
            val startX = centerX + (needleLength - tickLength) * cos(Math.toRadians(angle.toDouble())).toFloat()
            val startY = centerY + (needleLength - tickLength) * sin(Math.toRadians(angle.toDouble())).toFloat()

            canvas.drawLine(startX, startY, endX, endY, dialPaint.apply {
                strokeWidth = tickWidth
            })

            // Draw label for big ticks only
            if (isBigTick) {
                // Calculate label position
                val labelRadius = radius + 60 // Radius for placing labels outside the speedometer
                val labelX = centerX + labelRadius * cos(Math.toRadians(angle.toDouble())).toFloat()
                val labelY = centerY + labelRadius * sin(Math.toRadians(angle.toDouble())).toFloat()
                val labelText = ((MAX_SPEED / 5) * bigTickIndex).toString() // Generate label text
                canvas.drawText(labelText, labelX, labelY, textPaint) // Display label
            }
        }
    }



    fun setNeedleRotation(rotation: Float) {
        // Set the rotation angle of the needle
        // Adjust rotation as needed
        // For example:
        // needlePaint.rotation = rotation
        invalidate()
    }
}


