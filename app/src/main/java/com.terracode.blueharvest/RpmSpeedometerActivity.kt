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
import android.graphics.RectF
import android.graphics.Rect
import android.animation.ObjectAnimator




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
        strokeWidth = 30f // Adjust the stroke width as needed
        strokeCap = Paint.Cap.ROUND // Set the stroke cap to round to make the ticks rounded
    }

    private val needlePaint = Paint().apply {
        color = lightGreyColor // Use custom color
        style = Paint.Style.FILL_AND_STROKE // Use FILL_AND_STROKE to fill the shape and draw its outline
        strokeWidth = 30f // Set the stroke width to make the entire needle thicker
        strokeCap = Paint.Cap.ROUND // Set the stroke cap to round to create rounded ends
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

    private var needleRotationAnimator: ObjectAnimator? = null

    init {
        needleRotationAnimator = ObjectAnimator.ofFloat(this, "needleRotation", 0f, 20f).apply {
            duration = 1000 // Animation duration in milliseconds
            repeatCount = ObjectAnimator.INFINITE // Repeat indefinitely
            repeatMode = ObjectAnimator.REVERSE // Reverse animation direction when repeating
        }
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

        textPaint.textSize = 40f // Set text size to 40


        // Draw current speed text
        if (angle > START_ANGLE + 1 && angle < START_ANGLE + SWEEP_ANGLE - 1) {
            canvas.drawText("$currentSpeed", centerX, centerY + 50, textPaint)
        }
        // Draw rounded rectangle for RPM value
        val rpmRectWidth = 200f
        val rpmRectHeight = 100f
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
        textPaint.color = lightGreyColor
        textPaint.getTextBounds(rpmText, 0, rpmText.length, textRect)
        val textX = centerX
        val textY = centerY + radius * 0.8f + (rpmRectHeight + textRect.height()) / 2
        canvas.drawText(rpmText, textX, textY, textPaint)

        needleRotationAnimator?.start()
    }


    private fun drawRangeIndicator(canvas: Canvas, centerX: Float, centerY: Float, radius: Float, speed: Float, label: String) {
        val numTicks = 5 // Including the original 3 ticks
        val angleInterval = SWEEP_ANGLE / (numTicks - 1) // Calculate the angle interval between each tick
        val startAngle = START_ANGLE // Starting angle at 135 degrees
        val tickLength = 60f // Length of the ticks
        val tickWidth = 50f // Width of the ticks

        val needleLength = radius - 40 // Adjusted needle length

        for (i in 0 until numTicks) {
            val angle = startAngle + i * angleInterval // Calculate the angle for each tick
            val endX = centerX + needleLength * cos(Math.toRadians(angle.toDouble())).toFloat() // Adjusted endpoint X
            val endY = centerY + needleLength * sin(Math.toRadians(angle.toDouble())).toFloat() // Adjusted endpoint Y
            val startX = centerX + (needleLength - tickLength) * cos(Math.toRadians(angle.toDouble())).toFloat() // Adjusted start X
            val startY = centerY + (needleLength - tickLength) * sin(Math.toRadians(angle.toDouble())).toFloat() // Adjusted start Y
            canvas.drawLine(startX, startY, endX, endY, dialPaint.apply {
                strokeWidth = tickWidth
            })

            // Calculate label position
            val labelAngle = startAngle + i * angleInterval // Angle for the label
            val labelRadius = radius + 60 // Radius for placing labels outside the speedometer
            val labelX = centerX + labelRadius * cos(Math.toRadians(labelAngle.toDouble())).toFloat() // Adjusted label X
            val labelY = centerY + labelRadius * sin(Math.toRadians(labelAngle.toDouble())).toFloat() // Adjusted label Y
            val labelText = (i * 5).toString() // Generate label text
            canvas.drawText(labelText, labelX, labelY, textPaint) // Display label
        }
    }




    fun setCurrentSpeed(speed: Float) {
        if (speed in 0f..MAX_SPEED) {
            currentSpeed = speed
            invalidate()
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


