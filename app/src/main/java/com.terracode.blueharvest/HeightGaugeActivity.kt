package com.terracode.blueharvest

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.terracode.blueharvest.utils.Notifications
import com.terracode.blueharvest.utils.PreferenceManager
import com.terracode.blueharvest.utils.ReadJSONObject

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

        //Get data from JSON file (or Bluetooth) -- Update with preference manager ticket
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
    private var maxHeight = PreferenceManager.getMaxHeightDisplayedInput().toFloat()
    private var currentHeight = heightData?.toFloat()

    //Colors
    private val lightGreyColor = ContextCompat.getColor(context, R.color.lightGrey)
    private val blackColor = ContextCompat.getColor(context, R.color.black)
    private val blueBerryColor = ContextCompat.getColor(context, R.color.blueBerry)

    //Labels
    private var labelTextSize = PreferenceManager.getSelectedTextSize()
    private var rakeHeightTitle = ""
    private var heightBarTitleText = ""

    //Notifications
    private val heightNotificationWarning = Notifications.getMaxHeightReachedNotification()

    //Animator
    private var gaugeAnimator: ObjectAnimator? = null

    //Create different paints for various components
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

        //Allows us to still see the preview in the split screen
        if (!isInEditMode) {
            rakeHeightTitle = ContextCompat.getString(context, R.string.currentHeightTitle)
            heightBarTitleText = ContextCompat.getString(context, R.string.heightBarTitle)
        }
    }

    override fun onDraw(canvas: Canvas) {
        //Draw canvas
        super.onDraw(canvas)

        //Logic for adding a warning notification if current height > max height
        if (currentHeight!! > maxHeight){
            PreferenceManager.setNotification(heightNotificationWarning)
            currentHeight = maxHeight
        }

        //Create values for the blue bar width, height, and corner radius
        val barWidth = width.toFloat() * BAR_WIDTH_OFFSET
        val barHeight = height.toFloat() * BAR_HEIGHT_OFFSET
        val cornerRadius = barWidth * BAR_CORNER_RADIUS_OFFSET

        //These define the top left coordinate of the blue height bar
        val startXCoordinate = (width - barWidth) / 2
        val startYCoordinate = (height - barHeight) / 2

        //These define the bottom right coordinate of the blue height bar
        val endXCoordinate = startXCoordinate + barWidth
        val endYCoordinate = startYCoordinate + barHeight

        // Draw rounded rectangle for blue bar (hover over drawRoundRect for more info)
        canvas.drawRoundRect(
            startXCoordinate,
            startYCoordinate,
            endXCoordinate,
            endYCoordinate,
            cornerRadius,
            cornerRadius,
            barPaint
        )

        // Calculate position and dimensions of the height indicator rectangle
        val horizontalBarHeight = 15f                   //Height indicator height

        //Starting Y-Coordinate for the top left coordinate for the height indicator
        val heightRatio = currentHeight!! / maxHeight // Height ratio
        val heightIndicatorStartYCoordinate = startYCoordinate + barHeight - (heightRatio * barHeight)

        //Starting Y-Coordinate for the bottom right coordinate for the height indicator
        val heightIndicatorEndXCoordinate = startXCoordinate + barWidth
        val heightIndicatorEndYCoordinate = heightIndicatorStartYCoordinate + horizontalBarHeight

        // Draw rounded rectangle for height indicator
        canvas.drawRoundRect(
            startXCoordinate,
            heightIndicatorStartYCoordinate,
            heightIndicatorEndXCoordinate,
            heightIndicatorEndYCoordinate,
            cornerRadius,
            cornerRadius,
            lightGreyBarPaint
        )

        // Draw outline for horizontal bar
        canvas.drawRoundRect(
            startXCoordinate,
            heightIndicatorStartYCoordinate,
            heightIndicatorEndXCoordinate,
            heightIndicatorEndYCoordinate,
            cornerRadius,
            cornerRadius,
            blackOutlinePaint
        )

        //Draw Horizontal Ticks
        drawTicksAndLabels(
            canvas,
            startXCoordinate,
            startYCoordinate,
            barHeight
        )
    }

    //Function to draw the ticks and the labels for the big ticks
    private fun drawTicksAndLabels(
        canvas: Canvas,
        startXCoordinate: Float,
        startYCoordinate: Float,
        barHeight: Float
    ){
        //Calculate total number of ticks and the space between them
        val numTicks = NUM_BIG_TICKS + (NUM_BIG_TICKS - 1) * NUM_SMALL_TICKS
        val tickSpacingInterval = barHeight / (numTicks - 1)

        //Tick label pixel offset
        val labelPositionOffset = 20f

        //Initialize tick length and width
        val bigTickLength = 15f                     // Length of the bigger ticks
        val bigTickWidth = 5f                       // Width of the bigger ticks
        val smallTickLength = bigTickLength / 2     // Length of the smaller ticks
        val smallTickWidth = bigTickWidth / 2       // Width of the smaller ticks

        for (i in 0 until numTicks) {
            val isBigTick = i % (NUM_SMALL_TICKS + 1) == 0 // Determine if the tick is a big tick

            //Determine tick length and width according to what tick it is
            val tickLength = if (isBigTick) bigTickLength else smallTickLength
            val tickWidth = if (isBigTick) bigTickWidth else smallTickWidth

            val tickXCoordinate = startXCoordinate - labelPositionOffset
            val tickYCoordinate = startYCoordinate + barHeight - (i * tickSpacingInterval)
            val endXCoordinate = startXCoordinate + tickLength

            canvas.drawLine(
                tickXCoordinate,
                tickYCoordinate,
                endXCoordinate,
                tickYCoordinate,
                tickPaint.apply {
                strokeWidth = tickWidth
            })

            // Draw label for big ticks only
            if (isBigTick) {
                // Calculate label position and label value
                val labelText = ((maxHeight/(numTicks-1)) * i).toInt()
                val textWidth = labelTextPaint.measureText(labelText.toString())

                //Tick Label coordinates
                val labelXCoordinate = tickXCoordinate - textWidth - labelPositionOffset
                val labelYCoordinate = tickYCoordinate + (labelTextSize / 2)

                // Display label
                canvas.drawText(
                    labelText.toString(),
                    labelXCoordinate,
                    labelYCoordinate,
                    labelTextPaint)
            }
        }
    }

    @Suppress("unused")
    fun setNeedleRotation(rotation: Float) {
        invalidate()
    }
}






