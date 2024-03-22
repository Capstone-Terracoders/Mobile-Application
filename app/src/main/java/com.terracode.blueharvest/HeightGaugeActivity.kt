package com.terracode.blueharvest

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.terracode.blueharvest.utils.Notifications
import com.terracode.blueharvest.utils.PreferenceManager
import com.terracode.blueharvest.utils.ReadJSONObject
import kotlin.math.cos
import kotlin.math.sin

@Suppress("PrivatePropertyName")
class HeightGaugeActivity @JvmOverloads constructor(
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
            heightData = getRakeHeight()
        }
    }

    //Constants
    private val BAR_WIDTH_OFFSET = 0.3F
    private val BAR_HEIGHT_OFFSET = 0.8f
    private val BAR_CORNER_RADIUS_OFFSET = 0.1f
    private var NUM_BIG_TICKS = 11
    private var NUM_SMALL_TICKS = 4
    private val ANIMATION_DURATION = 1000L

    //Data
    private var heightData: Double? = null
    private var maxHeight = PreferenceManager.getMaxRPMDisplayedInput()
    private var currentHeight = heightData?.toFloat()

    //Colors && Label sizes
    private val lightGreyColor = ContextCompat.getColor(context, R.color.lightGrey)
    private val blackColor = ContextCompat.getColor(context, R.color.black)
    private val blueBerryColor = ContextCompat.getColor(context, R.color.blueBerry)
    private var labelTextSize = PreferenceManager.getSelectedTextSize()
    private val titleTextSize = PreferenceManager.getSelectedTextSize() + 4f
    private var rakeHeightTitle = ""
    private var heightBarTitleText = ""

    //Notifications
    private val rpmNotificationWarning = Notifications.getMaxRPMReachedNotification()

    //Animator
    private var gaugeAnimator: ObjectAnimator? = null


    private val barPaint = Paint().apply {
        color = blueBerryColor
        style = Paint.Style.FILL
    }

    private val lightGreyBarPaint = Paint().apply {
        color = lightGreyColor
        style = Paint.Style.FILL
    }

    private val blackOutlinePaint = Paint().apply {
        color = blackColor
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private val labelTextPaint = Paint().apply {
        color = blackColor
        textSize = labelTextSize
        textAlign = Paint.Align.LEFT
    }

    // Draw "Rake Height (CM)" text
    private val currentValueTextPaint = Paint().apply {
        color = blackColor
        textSize = PreferenceManager.getSelectedTextSize() // Adjust text size as needed
        textAlign = Paint.Align.LEFT
        isFakeBoldText = true // Make the text bold
        strokeWidth = 2f // Increase stroke width for bold effect
    }

    private val tickPaint = Paint().apply {
        color = blackColor // Use custom color
        style = Paint.Style.STROKE
        strokeWidth = 2f // Adjust the stroke width as needed
        strokeCap = Paint.Cap.ROUND // Set the stroke cap to round to make the ticks rounded
    }

    init {
        //Needle Animation
        gaugeAnimator = ObjectAnimator.ofFloat(
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
            rakeHeightTitle = ContextCompat.getString(context, R.string.currentHeightTitle)
            heightBarTitleText = ContextCompat.getString(context, R.string.heightBarTitle)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val barWidth = width.toFloat() * BAR_WIDTH_OFFSET
        val barHeight = height.toFloat() * BAR_HEIGHT_OFFSET
        val cornerRadius = barWidth * BAR_CORNER_RADIUS_OFFSET

        //StartXCoordinate is defined at top of file
        val startXCoordinate = (width - barWidth) / 2
        val startYCoordinate = (height - barHeight) / 2

        // Draw rounded rectangle for blue bar
        canvas.drawRoundRect(
            startXCoordinate, startYCoordinate,
            startXCoordinate + barWidth, startYCoordinate + barHeight,
            cornerRadius, cornerRadius,
            barPaint
        )

        // Calculate position and dimensions of the horizontal bar
        val horizontalBarWidth = barWidth + 10 //Height indicator width
        val horizontalBarHeight = 15f //Height indicator height
        val horizontalBarX = startXCoordinate - 5 // Centered with the vertical bar
        val horizontalBarY =
            startYCoordinate + (barHeight - horizontalBarHeight) * (1 - currentHeight!! / maxHeight) //NEED TO CHANGE

        // Draw rounded rectangle for horizontal bar
        canvas.drawRoundRect(
            horizontalBarX, horizontalBarY,
            horizontalBarX + horizontalBarWidth, horizontalBarY + horizontalBarHeight,
            cornerRadius, cornerRadius,
            lightGreyBarPaint
        )

        // Draw outline for horizontal bar
        canvas.drawRoundRect(
            horizontalBarX, horizontalBarY,
            horizontalBarX + horizontalBarWidth, horizontalBarY + horizontalBarHeight,
            cornerRadius, cornerRadius,
            blackOutlinePaint
        )

        //Draw Horizontal Ticks
        drawTicksAndLabels(
            canvas,
            startXCoordinate,
            startYCoordinate,
            barHeight
        ) //Change start x coord to not be constant?
    }

    private fun drawTicksAndLabels(canvas: Canvas, startXCoordinate: Float, startYCoordinate: Float, barHeight: Float) {
        val numTicks = NUM_BIG_TICKS + (NUM_BIG_TICKS - 1) * NUM_SMALL_TICKS // Total number of ticks including small ticks
        val tickSpacingInterval = barHeight / (numTicks - 1) // Calculate the interval between each tick
        val bigTickLength = 15f // Length of the bigger ticks
        val bigTickWidth = 5f // Width of the bigger ticks
        val smallTickLength = bigTickLength / 2 // Length of the smaller ticks
        val smallTickWidth = bigTickWidth / 2 // Width of the smaller ticks

        for (i in 0 until numTicks) {
            val isBigTick = i % (NUM_SMALL_TICKS + 1) == 0 // Determine if the tick is a big tick

            val tickLength = if (isBigTick) bigTickLength else smallTickLength
            val tickWidth = if (isBigTick) bigTickWidth else smallTickWidth

            val newXCoordinate = startXCoordinate - 20f
            val newYCoordinate = startYCoordinate + barHeight - i * tickSpacingInterval
            val endXCoordinate = startXCoordinate + tickLength
            val endYCoordinate = newYCoordinate

            canvas.drawLine(newXCoordinate, newYCoordinate, endXCoordinate, endYCoordinate, tickPaint.apply {
                strokeWidth = tickWidth
            })

            // Draw label for big ticks only
            if (isBigTick) {
                // Calculate label position
                val labelText = i.toString()
                val textWidth = labelTextPaint.measureText(labelText)
                canvas.drawText(labelText, newXCoordinate - textWidth - 20, endYCoordinate + labelTextSize / 2, labelTextPaint) // Display label
            }
        }
    }

    @Suppress("unused")
    fun setNeedleRotation(rotation: Float) {
        invalidate()
    }
}






