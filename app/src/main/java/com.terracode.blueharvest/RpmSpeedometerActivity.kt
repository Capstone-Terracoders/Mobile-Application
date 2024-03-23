package com.terracode.blueharvest

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
    //Default widths
    private val DEFAULT_STROKE_WIDTH = 20f
    private val DEFAULT_NEEDLE_WIDTH = 10f

    //Offsets for adjusting length of needle
    private val NEEDLE_LENGTH_OFFSET = 40f

    //Radius offset for placing labels outside the speedometer
    private val LABEL_OFFSET = 60f

    //Length of the bigger ticks (will also impact size of small ticks)
    private val BIG_TICK_LENGTH = 40f
    private val BIG_TICK_WIDTH = 15f

    //Angles of arc
    private val START_ANGLE = 135f
    private val SWEEP_ANGLE = 270f

    //Number of ticks - both small and big
    private var NUM_BIG_TICKS = 6
    private var NUM_SMALL_TICKS = 5

    //Animation duration in milliseconds
    private var ANIMATION_DURATION = 1000L

    //Data
    private var rpmData: Double? = null
    private var maxRpm = PreferenceManager.getMaxRPMDisplayedInput()
    private var currentRpm = rpmData

    //Colors
    private val blueBerryColor = ContextCompat.getColor(context, R.color.blueBerry)
    private val blackColor = ContextCompat.getColor(context, R.color.black)

    //Labels
    private val normalTextSize = PreferenceManager.getSelectedTextSize()

    //Notifications
    private val rpmNotificationWarning = Notifications.getMaxRPMReachedNotification()

    //Animator
    private var needleRotationAnimator: ObjectAnimator? = null

    // Paint objects
    private val dialAndTickPaint = Paint().apply {
        color = blueBerryColor
        style = Paint.Style.STROKE
        strokeWidth = DEFAULT_STROKE_WIDTH
        strokeCap = Paint.Cap.ROUND // Set the stroke cap to round to make the ticks rounded
    }

    private val needlePaint = Paint().apply {
        color = blackColor
        style =
            Paint.Style.FILL_AND_STROKE // Use FILL_AND_STROKE to fill the shape and draw its outline
        strokeWidth = DEFAULT_NEEDLE_WIDTH
        strokeCap = Paint.Cap.ROUND // Set the stroke cap to round to create rounded ends
    }

    private val tickLabelTextPaint: Paint = Paint().apply {
        color = Color.BLACK
        textSize = normalTextSize
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    //Initializer for Drawing Speedometer
    init {
        //Needle Animation
        needleRotationAnimator = ObjectAnimator.ofFloat(
            this,
            "needleRotation",
            0f,
            20f                                         //WUTTTTT
        ).apply {
            duration = ANIMATION_DURATION // Animation duration in milliseconds
            repeatCount = ObjectAnimator.INFINITE // Repeat indefinitely
            repeatMode = ObjectAnimator.REVERSE // Reverse animation direction when repeating
        }
    }


    override fun onDraw(canvas: Canvas) {
        //Draw canvas
        super.onDraw(canvas)

        // Declare & initialize ark parameters:
        val radiusOffset = 0.8f
        val centerXCoordinate = width / 2f      //X-Coordinate of the center of the activity page
        val centerYCoordinate = height / 2f     //Y-Coordinate of the center of the activity page
        val radius = min(centerXCoordinate, centerYCoordinate) * radiusOffset

        //Create top-left coordinates of the arc
        val arcStartXCoordinate = centerXCoordinate - radius
        val arcStartYCoordinate = centerYCoordinate - radius

        //Create the bottom-right coordinates of the arc
        val arcEndXCoordinate = centerXCoordinate + radius
        val arcEndYCoordinate = centerYCoordinate + radius

        //Draw the arc for the speedometer
        canvas.drawArc(
            arcStartXCoordinate,
            arcStartYCoordinate,
            arcEndXCoordinate,
            arcEndYCoordinate,
            START_ANGLE,
            SWEEP_ANGLE,
            false,
            dialAndTickPaint
        )

        //Function to draw the speedometer ticks and labels for RPM
        drawSpeedometerTicksAndLabels(canvas,
            centerXCoordinate,
            centerYCoordinate,
            radius)

        // Initialize rpm value
        val rpm: Double?

        //Sets the angle of the needle based off the currentSpeed and handles currentRpm > maxRpm
        val angle = if (maxRpm != 0 && currentRpm != null) {
            if (currentRpm!! > maxRpm){
                rpm = maxRpm.toDouble()
                //Allows us to continue seeing the preview in split mode
                if (!isInEditMode) {
                    PreferenceManager.setNotification(rpmNotificationWarning)
                }
            } else {
                rpm = currentRpm
            }
            START_ANGLE + (rpm!! / maxRpm) * SWEEP_ANGLE
        } else {
            0.0
        }

        //Sets up needle length/ending coordinates
        val needleLength = radius - NEEDLE_LENGTH_OFFSET
        val needleLengthXCoordinate = centerXCoordinate + needleLength * cos(Math.toRadians(angle)).toFloat()
        val needleLengthYCoordinate = centerYCoordinate + needleLength * sin(Math.toRadians(angle)).toFloat()

        //Draw the speedometer needle
        canvas.drawLine(
            centerXCoordinate,
            centerYCoordinate,
            needleLengthXCoordinate,
            needleLengthYCoordinate,
            needlePaint)

        needleRotationAnimator?.start()
    }


    private fun drawSpeedometerTicksAndLabels(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        //Declare the interval between each tick
        val angleInterval = SWEEP_ANGLE / ((NUM_BIG_TICKS - 1) * NUM_SMALL_TICKS)

        //Declare small tick length && width
        val smallTickLength = BIG_TICK_LENGTH / 2
        val smallTickWidth = BIG_TICK_WIDTH / 3

        //Declare needle length
        val needleLength = radius - NEEDLE_LENGTH_OFFSET

        //Create all the ticks
        for (tick in 0 until (NUM_BIG_TICKS * NUM_SMALL_TICKS) - (NUM_SMALL_TICKS-1)) {
            //Declare if tick is big or small based off index
            val bigTickIndex = tick / NUM_SMALL_TICKS
            val smallTickIndex = tick % NUM_SMALL_TICKS
            val isBigTick = smallTickIndex == 0

            //Tick angle
            val angle = START_ANGLE + tick * angleInterval // Calculate the angle for each tick

            //Determine what tick length and width to use
            val tickLength = if (isBigTick) BIG_TICK_LENGTH else smallTickLength
            val tickWidth = if (isBigTick) BIG_TICK_WIDTH else smallTickWidth

            //Declare top-left coordinate of tick
            val startTickXCoordinate = centerX + (needleLength - tickLength) * cos(Math.toRadians(angle.toDouble())).toFloat()
            val startTickYCoordinate = centerY + (needleLength - tickLength) * sin(Math.toRadians(angle.toDouble())).toFloat()

            //Declare bottom-right coordinate of tick
            val endTickXCoordinate = centerX + needleLength * cos(Math.toRadians(angle.toDouble())).toFloat()
            val endTickYCoordinate = centerY + needleLength * sin(Math.toRadians(angle.toDouble())).toFloat()

            //Draw the tick
            canvas.drawLine(
                startTickXCoordinate,
                startTickYCoordinate,
                endTickXCoordinate,
                endTickYCoordinate,
                dialAndTickPaint.apply {
                strokeWidth = tickWidth
            })

            // Draw label for big ticks only
            if (isBigTick) {
                //Calculate label position
                val labelRadius = radius + LABEL_OFFSET
                val labelXCoordinate = centerX + labelRadius * cos(Math.toRadians(angle.toDouble())).toFloat()
                val labelYCoordinate = centerY + labelRadius * sin(Math.toRadians(angle.toDouble())).toFloat()

                //Generate label text
                val labelText = ((maxRpm / 5) * bigTickIndex).toString()

                // Draw label
                canvas.drawText(
                    labelText,
                    labelXCoordinate,
                    labelYCoordinate,
                    tickLabelTextPaint)
            }
        }
    }
    @Suppress("unused")
    fun setNeedleRotation(rotation: Float) {
        invalidate()
    }
}


