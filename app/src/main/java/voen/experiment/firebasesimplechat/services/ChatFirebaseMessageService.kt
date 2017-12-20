package voen.experiment.firebasesimplechat.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import voen.experiment.firebasesimplechat.section.main.MainActivity

/**
 * Created by voen on 12/8/17.
 */
class ChatFirebaseMessageService : FirebaseMessagingService() {
    private val CHANNEL_ID = "my_channel"
    private val NOTIF_ID = 1212

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
//        Log.d("voen","message : ${remoteMessage?.from}")
        remoteMessage?.data?.let {
            it.forEach{ Log.d("voen","key : ${it.key} = ${it.value}") }
        }
        remoteMessage?.notification?.let {
            Log.d("voen","notification : body : ${it.body} | title : ${it.title} | tag : ${it.tag} | sound : ${it.sound}")

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(it.title)
                    .setContentText(it.body)

            val resIntent = Intent(this, MainActivity::class.java)
            val taskStack = TaskStackBuilder.create(this)
            taskStack.addParentStack(MainActivity::class.java)
            taskStack.addNextIntent(resIntent)

            val pendingIntent = taskStack.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            builder.setContentIntent(pendingIntent)

            val notifManager = getSystemService(Context.NOTIFICATION_SERVICE)
            (notifManager as NotificationManager).notify(NOTIF_ID, builder.build())
        }
    }
}