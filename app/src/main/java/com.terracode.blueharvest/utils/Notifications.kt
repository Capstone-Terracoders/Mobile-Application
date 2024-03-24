import android.content.Context
import com.terracode.blueharvest.R
import com.terracode.blueharvest.utils.constants.Notification
import com.terracode.blueharvest.utils.constants.NotificationTypes
import java.time.Instant
import java.time.format.DateTimeFormatter

object Notifications {

    fun getMaxRPMReachedNotification(context: Context): Notification {
        val maxRpmReachedNotification = R.string.maxRPMReachedNotification
        return Notification(
            NotificationTypes.WARNING,
            context.getString(maxRpmReachedNotification),
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun getMaxHeightReachedNotification(context: Context): Notification {
        val maxHeightReachedNotification = R.string.maxHeightReachedNotification
        return Notification(
            NotificationTypes.WARNING,
            context.getString(maxHeightReachedNotification),
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun getStartRecordingNotification(context: Context): Notification {
        val startedRecordingNotification = R.string.startedRecordingNotification
        return Notification(
            NotificationTypes.NOTIFICATION,
            context.getString(startedRecordingNotification),
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }

    fun getStopRecordingNotification(context: Context): Notification {
        val stoppedRecordingNotification = R.string.stoppedRecordingNotification
        return Notification(
            NotificationTypes.NOTIFICATION,
            context.getString(stoppedRecordingNotification),
            DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        )
    }
}
