package com.terracode.blueharvest

import com.terracode.blueharvest.utils.objects.Notifications
//import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.REVERSE
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.StaticLayout
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.terracode.blueharvest.utils.PreferenceManager
import android.text.Layout
import android.text.TextPaint


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
    }


    //Constants
    private val BAR_WIDTH_OFFSET = 0.3F
    private val BAR_HEIGHT_OFFSET = 0.8f
    private val BAR_CORNER_RADIUS_OFFSET = 0.1f

    private val HEIGHT_INDICATOR_HEIGHT = 15f
    private val LABEL_POSITION_OFFSET = 20f

    private var NUM_BIG_TICKS = 11
    private var NUM_SMALL_TICKS = 4
    private val BIG_TICK_LENGTH = 15f
    private val BIG_TICK_WIDTH = 5f

    private val ANIMATION_DURATION = 20000L
    //Change this back to 2000L for quicker animation speed


    //Data
    private var maxHeight = PreferenceManager.getMaxHeightDisplayedInput()
    private var minHeight = PreferenceManager.getMinRakeHeightInput()
    private var optimalHeightRange = PreferenceManager.getOptimalHeightRangeInput()
    private var optimalHeight = PreferenceManager.getOptimalRakeHeight()
    private var currentHeight = PreferenceManager.getRakeHeight()
    private val heightUpperRange = optimalHeight?.plus(optimalHeightRange)
    private val heightLowerRange = optimalHeight?.minus(optimalHeightRange)
    private val unitToggle = PreferenceManager.getSelectedUnit()

    //Colors
    private val lightGreyColor = ContextCompat.getColor(context, R.color.lightGrey)
    private val blackColor = ContextCompat.getColor(context, R.color.black)
    private val blueBerryColor = ContextCompat.getColor(context, R.color.blueBerry)
    private val redColor = ContextCompat.getColor(context, R.color.red)
    private val greenColor = ContextCompat.getColor(context, R.color.green)
    private val darkGreenColor = ContextCompat.getColor(context, R.color.dark_green)


    //Labels
    private var labelTextSize = PreferenceManager.getSelectedTextSize()
    private var currentHeightTitle = context.getString(R.string.currentHeightTitle)
    private var rakeHeightTitle = ""
    private var heightBarTitleText = ""

    //Notifications
    private val heightAboveMaxNotificationWarning =
        Notifications.getMaxHeightReachedNotification(context)
    private val heightBelowMinNotificationWarning =
        Notifications.getMinHeightReachedNotification(context)
    private val heightBelowZeroNotificationError =
        Notifications.getHeightBelowZeroNotification(context)

    //Animator
    //private var gaugeAnimator: ObjectAnimator? = null

    //Create different paints for various components
    private val barPaint = Paint().apply {
        color = blueBerryColor
        style = Paint.Style.FILL
    }

    private val lightGreyBarPaint = Paint().apply {
        color = lightGreyColor
        style = Paint.Style.FILL
    }

    private val redMinValuePaint = Paint().apply {
        color = redColor
        style = Paint.Style.FILL
    }

    private val greenOptimalValuePaint = Paint().apply {
        color = greenColor
        style = Paint.Style.FILL
    }

    private val darkGreenTextPaint = TextPaint().apply {
        color = darkGreenColor
        style = Paint.Style.FILL
        strokeWidth = 2f // Adjust the width of the stroke as needed
        isAntiAlias = true // Enable anti-aliasing for smoother edges
        textSize = labelTextSize
        isFakeBoldText = true // Make the text bold
    }

    private val redTextPaint = TextPaint().apply {
        color = redColor
        style = Paint.Style.FILL
        strokeWidth = 2f // Adjust the width of the stroke as needed
        isAntiAlias = true // Enable anti-aliasing for smoother edges
        textSize = labelTextSize
        isFakeBoldText = true // Make the text bold
    }

    private val blackTextPaint = TextPaint().apply {
        color = blackColor
        style = Paint.Style.FILL
        strokeWidth = 2f // Adjust the width of the stroke as needed
        isAntiAlias = true // Enable anti-aliasing for smoother edges
        textSize = labelTextSize
        isFakeBoldText = true // Make the text bold
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
        //Old Needle Animation
//        gaugeAnimator = currentHeight?.let {
//            ObjectAnimator.ofFloat(
//                this,
//                "needleRotation",
//                it
//            ).apply {
//                duration = ANIMATION_DURATION // Animation duration in milliseconds
//                repeatCount = ObjectAnimator.INFINITE // Repeat indefinitely
//                repeatMode = ObjectAnimator.REVERSE // Reverse animation direction when repeating
//            }
//        }

        //Symposium Needle Animation
        val rpmAnimator = ValueAnimator.ofFloat(0F, maxHeight).apply {
            duration = ANIMATION_DURATION
            repeatCount = INFINITE
            repeatMode = REVERSE
            addUpdateListener { valueAnimator ->
                currentHeight = valueAnimator.animatedValue as Float
                PreferenceManager.setCurrentHeight(currentHeight)
                invalidate() // Redraw the view to reflect the updated RPM
            }
        }
        rpmAnimator.start()

        if (optimalHeight!! > maxHeight) {
            optimalHeight = maxHeight
        }

        //Allows us to still see the preview in the split screen
        if (!isInEditMode) {
            rakeHeightTitle = ContextCompat.getString(context, R.string.currentHeightTitle)
            heightBarTitleText = ContextCompat.getString(context, R.string.heightBarTitle)
            currentHeightTitle = context.getString(R.string.currentHeightTitle)
        }
    }

    override fun onDraw(canvas: Canvas) {
        //Draw canvas
        super.onDraw(canvas)

        //Logic for notifications:
        if (currentHeight > maxHeight) {
            PreferenceManager.setNotification(heightAboveMaxNotificationWarning)
            currentHeight = maxHeight
        } else if (currentHeight < minHeight && currentHeight > 0) {
            PreferenceManager.setNotification(heightBelowMinNotificationWarning)
        } else if (currentHeight < 0) {
            PreferenceManager.setNotification(heightBelowZeroNotificationError)
            currentHeight = 0f
        }

        //Create values for the blue bar width, height, and corner radius
        val barWidth = width.toFloat() * BAR_WIDTH_OFFSET
        val barHeight = height.toFloat() * BAR_HEIGHT_OFFSET
        val cornerRadius = barWidth * BAR_CORNER_RADIUS_OFFSET

        //Calculate total number of ticks and the space between them
        val numTicks = NUM_BIG_TICKS + (NUM_BIG_TICKS - 1) * NUM_SMALL_TICKS
        val tickSpacingInterval = barHeight / (numTicks - 1)

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

        // Draw black outline for blue bar
        canvas.drawRoundRect(
            startXCoordinate,
            startYCoordinate,
            endXCoordinate,
            endYCoordinate,
            cornerRadius,
            cornerRadius,
            blackOutlinePaint
        )

        //Values to figure out position of lower and upper range ticks
        val upperTickRangeValue = optimalHeight!! + optimalHeightRange
        val lowerTickRangeValue = optimalHeight!! - optimalHeightRange

        val upperTickHeightRatio = (upperTickRangeValue) / maxHeight // Height ratio
        val lowerTickHeightRatio = (lowerTickRangeValue) / maxHeight // Height ratio

        //Error logic for if optimal value && range > maxHeight
        val upperTickYCoordinate = if (upperTickRangeValue > maxHeight) {
            startYCoordinate
        } else {
            startYCoordinate + barHeight - (upperTickHeightRatio * barHeight)
        }

        val lowerTickYCoordinate = if (lowerTickRangeValue < 0) {
            endYCoordinate
        } else {
            startYCoordinate + barHeight - (lowerTickHeightRatio * barHeight)
        }

        //Draw rounded rectangle for optimal rake height
        canvas.drawRoundRect(
            startXCoordinate,
            upperTickYCoordinate,
            endXCoordinate,
            lowerTickYCoordinate,
            cornerRadius,
            cornerRadius,
            greenOptimalValuePaint
        )

        // Draw black outline for optimal rake height
        canvas.drawRoundRect(
            startXCoordinate,
            upperTickYCoordinate,
            endXCoordinate,
            lowerTickYCoordinate,
            cornerRadius,
            cornerRadius,
            blackOutlinePaint
        )

        //Y-Coordinate for the top left coordinate for the minimum safety height
        val safetyValueRatio = minHeight / maxHeight // Height ratio
        var minHeightStartYCoordinate =
            startYCoordinate + barHeight - (safetyValueRatio * barHeight)

        if (minHeight > maxHeight) {
            minHeightStartYCoordinate = startYCoordinate
        }

        //Draw rounded rectangle for safety min height
        canvas.drawRoundRect(
            startXCoordinate,
            minHeightStartYCoordinate,
            endXCoordinate,
            endYCoordinate,
            cornerRadius,
            cornerRadius,
            redMinValuePaint
        )

        // Draw black outline for safety min height
        canvas.drawRoundRect(
            startXCoordinate,
            minHeightStartYCoordinate,
            endXCoordinate,
            endYCoordinate,
            cornerRadius,
            cornerRadius,
            blackOutlinePaint
        )

        //Draw Horizontal Ticks
        drawTicksAndLabels(
            canvas,
            startXCoordinate,
            startYCoordinate,
            barHeight,
            numTicks,
            tickSpacingInterval
        )

        //Y-Coordinate for the top left coordinate for the height indicator
        val heightRatio = currentHeight / maxHeight // Height ratio
        val heightIndicatorStartYCoordinate =
            startYCoordinate + barHeight - (heightRatio * barHeight) - (HEIGHT_INDICATOR_HEIGHT / 2)

        //Y-Coordinate for the bottom right coordinate for the height indicator
        val heightIndicatorEndXCoordinate = startXCoordinate + barWidth
        val heightIndicatorEndYCoordinate =
            heightIndicatorStartYCoordinate + HEIGHT_INDICATOR_HEIGHT

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

        // Draw black outline for height indicator
        canvas.drawRoundRect(
            startXCoordinate,
            heightIndicatorStartYCoordinate,
            heightIndicatorEndXCoordinate,
            heightIndicatorEndYCoordinate,
            cornerRadius,
            cornerRadius,
            blackOutlinePaint
        )

        // Draw the current height value title box
        val currentValueText = String.format("%.1f", currentHeight) // Format to one decimal place
        val currentHeightText: String = if (unitToggle) {
            "$currentHeightTitle \n $currentValueText cm"
        } else {
            "$currentHeightTitle \n $currentValueText in"
        }

        //Current value text color logic - Height
        val textColor: TextPaint = if (currentHeight > maxHeight || currentHeight < minHeight) {
            redTextPaint
        } else if (currentHeight > heightLowerRange!! && currentHeight < heightUpperRange!!) {
            darkGreenTextPaint
        } else {
            blackTextPaint
        }

// Calculate current value text coordinates
        val currentValueTextXCoordinate = endXCoordinate + 50f
        val currentValueTextYCoordinate = startYCoordinate + (barHeight / 2)
        val currentValueTextWidth = width - currentValueTextXCoordinate.toInt()


// Create StaticLayout for text wrapping
        val textLayout = StaticLayout.Builder.obtain(currentHeightText, 0, currentHeightText.length, textColor, currentValueTextWidth)
            .setAlignment(Layout.Alignment.ALIGN_CENTER) // Update alignment to ALIGN_CENTER
            .setLineSpacing(0f, 1f)
            .setIncludePad(true)
            .build()


// Draw the text using StaticLayout
        canvas.save()
        canvas.translate(currentValueTextXCoordinate, currentValueTextYCoordinate)
        textLayout.draw(canvas)
        canvas.restore()

        // Draw the background drawable for the title box
        val textBoxPadding = 20f

        val drawable = ContextCompat.getDrawable(context, R.drawable.blue_bordered_box)
        drawable?.setBounds(
            currentValueTextXCoordinate.toInt(),                             // Box start x
            (currentValueTextYCoordinate - textBoxPadding).toInt(),                            // Adjusted top to match text
            width,                                                          // Box end x
            (currentValueTextYCoordinate + textLayout.height + textBoxPadding).toInt()      // Bottom below the current value text
        )
        drawable?.draw(canvas)
    }

    //Function to draw the ticks and the labels for the big ticks
    private fun drawTicksAndLabels(
        canvas: Canvas,
        startXCoordinate: Float,
        startYCoordinate: Float,
        barHeight: Float,
        numTicks: Int,
        tickSpacingInterval: Float
    ) {

        //Initialize tick length and width
        val smallTickLength = BIG_TICK_LENGTH / 2     // Length of the smaller ticks
        val smallTickWidth = BIG_TICK_WIDTH / 2       // Width of the smaller ticks

        for (i in 0 until numTicks) {
            val isBigTick = i % (NUM_SMALL_TICKS + 1) == 0 // Determine if the tick is a big tick

            //Determine tick length and width according to what tick it is
            val tickLength = if (isBigTick) BIG_TICK_LENGTH else smallTickLength
            val tickWidth = if (isBigTick) BIG_TICK_WIDTH else smallTickWidth

            val tickXCoordinate = startXCoordinate - LABEL_POSITION_OFFSET
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
                val tickValue =
                    ((maxHeight * 10) / (numTicks - 1) * i) / 10 // Using integer arithmetic to maintain accuracy
                val labelText = String.format("%.1f", tickValue) // Format to one decimal place

                val textWidth = labelTextPaint.measureText(labelText)

                //Tick Label coordinates
                val labelXCoordinate = tickXCoordinate - textWidth - LABEL_POSITION_OFFSET
                val labelYCoordinate = tickYCoordinate + (labelTextSize / 2)

                // Display label
                canvas.drawText(
                    labelText,
                    labelXCoordinate,
                    labelYCoordinate,
                    labelTextPaint
                )
            }
        }
    }
}






