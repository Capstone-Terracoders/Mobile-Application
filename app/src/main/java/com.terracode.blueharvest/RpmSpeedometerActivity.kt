package com.terracode.blueharvest

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.terracode.blueharvest.utils.Notifications
import com.terracode.blueharvest.utils.PreferenceManager
import com.terracode.blueharvest.utils.ReadJSONObject
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Suppress("PrivatePropertyName")
class RpmSpeedometerActivity @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    //Initialize Data
    init {
        //Initialize Preference Manager
        PreferenceManager.init(context)

        //Get data from JSON file (or Bluetooth)
        val sensorData = ReadJSONObject.fromAsset(context, "SensorDataExample.json")
        sensorData?.apply {
            rpmData = getRPM()
        }
    }

    //Constants
    private val DEFAULT_DIAL_WIDTH = 20f
    private val RECT_WIDTH = 10f
    private val DEFAULT_NEEDLE_WIDTH = 10f

    private val START_ANGLE = 135f
    private val SWEEP_ANGLE = 270f
    private var NUM_BIG_TICKS = 6
    private var NUM_SMALL_TICKS = 4
    private var ANIMATION_DURATION = 1000L
    private var RECTANGLE_WIDTH = 375f
    private var RECTANGLE_HEIGHT = 90f

    //Data
    private var rpmData: Double? = null
    private var maxSpeed = PreferenceManager.getMaxRPMDisplayedInput()
    private var currentSpeed = rpmData

    //Res Values
    private val blueBerryColor = ContextCompat.getColor(context, R.color.blueBerry)
    private val blackColor = ContextCompat.getColor(context, R.color.black)
    private val red = ContextCompat.getColor(context, R.color.red)
    private var rakeRPMTitle = ""
    private var titleText = ""
    private val normalTextSize = PreferenceManager.getSelectedTextSize()
    private val titleTextSize = PreferenceManager.getSelectedTextSize() + 4f

    //Notifications
    private val rpmNotificationWarning = Notifications.getMaxRPMReachedNotification()

    //Do we need?
    private var dialWidth: Float = DEFAULT_DIAL_WIDTH
    private var rectWidth: Float = RECT_WIDTH
    private var needleWidth: Float = DEFAULT_NEEDLE_WIDTH



    //Animator
    private var needleRotationAnimator: ObjectAnimator? = null

    // Paint objects
    private val dialPaint = Paint().apply {
        color = blueBerryColor // Use custom color
        style = Paint.Style.STROKE
        strokeWidth = dialWidth // Adjust the stroke width as needed
        strokeCap = Paint.Cap.ROUND // Set the stroke cap to round to make the ticks rounded
    }

    private val rectPaint = Paint().apply {
        color = blueBerryColor // Use custom color
        style = Paint.Style.STROKE
        strokeWidth = rectWidth // Adjust the stroke width as needed
        strokeCap = Paint.Cap.ROUND // Set the stroke cap to round to make the ticks rounded
    }

    private val needlePaint = Paint().apply {
        color = blackColor // Use custom color
        style =
            Paint.Style.FILL_AND_STROKE // Use FILL_AND_STROKE to fill the shape and draw its outline
        strokeWidth = needleWidth // Set the stroke width to make the entire needle thicker
        strokeCap = Paint.Cap.ROUND // Set the stroke cap to round to create rounded ends
    }

    private val tickLabelTextPaint: Paint = Paint().apply {
        color = Color.BLACK
        textSize = normalTextSize
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val rpmValueTextPaint: Paint = Paint().apply {
        color = Color.BLACK
        textSize = normalTextSize
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    // Draw the title "Speedometer"
    private val titleTextPaint = Paint().apply {
        color = Color.BLACK
        textSize = titleTextSize // Adjust text size as needed
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true // Make text bold
    }

    //Initializer for Drawing Speedometer
    init {
        //Needle Animation
        needleRotationAnimator = ObjectAnimator.ofFloat(
            this,
            "needleRotation",
            0f,
            20f
        ).apply {
            duration = ANIMATION_DURATION // Animation duration in milliseconds
            repeatCount = ObjectAnimator.INFINITE // Repeat indefinitely
            repeatMode = ObjectAnimator.REVERSE // Reverse animation direction when repeating
        }

        if (!isInEditMode) {
            rakeRPMTitle = ContextCompat.getString(context, R.string.currentRPMTitle)
            titleText = ContextCompat.getString(context, R.string.speedometerTitle)
        }

            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RpmSpeedometerActivity)
        dialWidth =
            typedArray.getDimension(R.styleable.RpmSpeedometerActivity_dialWidth, DEFAULT_DIAL_WIDTH)
        needleWidth =
            typedArray.getDimension(R.styleable.RpmSpeedometerActivity_needleWidth, DEFAULT_NEEDLE_WIDTH)
        typedArray.recycle()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Declare & initialize ark parameters:
        val radiusOffset = 0.8f
        val titleHeightOffset = 1.2f
        val titleOffset = 1.6f
        val centerXCoordinate = width / 2f      //X-Coordinate of the center of the activity page
        val centerYCoordinate = height / 2f     //Y-Coordinate of the center of the activity page
        val radius = (min(centerXCoordinate, centerYCoordinate) - 40) * radiusOffset

        canvas.drawText(
            titleText,
            centerXCoordinate,
            centerYCoordinate - (radius * titleOffset),
            titleTextPaint)

        canvas.drawArc(
            centerXCoordinate - radius,
            centerYCoordinate - radius,
            centerXCoordinate + radius,
            centerYCoordinate + radius,
            START_ANGLE,
            SWEEP_ANGLE,
            false,
            dialPaint
        )

        drawRangeIndicator(canvas,
            centerXCoordinate,
            centerYCoordinate,
            radius)

        // Draw the speedometer needle
        val speedAngle: Double?
        val angle = if (maxSpeed != 0 && currentSpeed != null) {
            if (currentSpeed!! > maxSpeed){
                speedAngle = maxSpeed.toDouble()
                rpmValueTextPaint.color = red
                PreferenceManager.setNotification(rpmNotificationWarning)
            } else {
                speedAngle = currentSpeed
            }
            START_ANGLE + (speedAngle!! / maxSpeed) * SWEEP_ANGLE
        } else {
            0.0
        }
        val needleLength = radius - 40
        val needleX = centerXCoordinate + needleLength * cos(Math.toRadians(angle)).toFloat()
        val needleY = centerYCoordinate + needleLength * sin(Math.toRadians(angle)).toFloat()
        canvas.drawLine(
            centerXCoordinate,
            centerYCoordinate,
            needleX,
            needleY,
            needlePaint)

        // Draw rounded rectangle for RPM value
        val rpmRectWidth = RECTANGLE_WIDTH
        val rpmRectHeight = RECTANGLE_HEIGHT
        val cornerRadius = 20f
        val rpmRectLeft = centerXCoordinate - (rpmRectWidth / 2)
        val rpmRectTop = centerYCoordinate + (radius * titleHeightOffset)
        val rpmRectRight = rpmRectLeft + rpmRectWidth
        val rpmRectBottom = rpmRectTop + rpmRectHeight
        val rpmRect = RectF(rpmRectLeft, rpmRectTop, rpmRectRight, rpmRectBottom)

        canvas.drawRoundRect(rpmRect, cornerRadius, cornerRadius, rectPaint)

        // Draw RPM value text inside the rounded rectangle
        val rpmText =  "$rakeRPMTitle $currentSpeed"
        val textRect = Rect()
        rpmValueTextPaint.getTextBounds(rpmText, 0, rpmText.length, textRect)
        val textY = centerYCoordinate + (radius * titleHeightOffset) +
                (rpmRectHeight + textRect.height()) / 2
        canvas.drawText(
            rpmText,
            centerXCoordinate,
            textY,
            rpmValueTextPaint)

        needleRotationAnimator?.start()
    }


    private fun drawRangeIndicator(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        val angleInterval = SWEEP_ANGLE / ((NUM_BIG_TICKS - 1) * NUM_SMALL_TICKS) // Calculate the angle interval between each tick
        val bigTickLength = 60f // Length of the bigger ticks
        val bigTickWidth = 20f // Width of the bigger ticks
        val smallTickLength = bigTickLength / 2 // Length of the smaller ticks
        val smallTickWidth = bigTickWidth / 2 // Width of the smaller ticks

        val needleLength = radius - 40 // Adjusted needle length

        for (i in 0 until (NUM_BIG_TICKS * NUM_SMALL_TICKS) - (NUM_SMALL_TICKS-1)) {
            val bigTickIndex = i / NUM_SMALL_TICKS
            val smallTickIndex = i % NUM_SMALL_TICKS

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
                val labelText = ((maxSpeed / 5) * bigTickIndex).toString() // Generate label text
                canvas.drawText(labelText, labelX, labelY, tickLabelTextPaint) // Display label
            }
        }
    }
    @Suppress("unused")
    fun setNeedleRotation(rotation: Float) {
        invalidate()
    }
}


