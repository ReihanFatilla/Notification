package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.example.notification.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var notificationManager: NotificationManager
    private var CHANNEL_ID = "channel_id"

    private lateinit var countDownTimer: CountDownTimer
    private val KEY_TEXT_REPLY = "key_text_reply"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(CHANNEL_ID, "Countdown", "This is Description")

        countDownTimer = object : CountDownTimer(3000, 3000){
            override fun onTick(millisUntilFinished: Long) {
                binding.timer.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                displayNotification("Countdown", "this is")
            }

        }

        binding.btnStart.apply {
            countDownTimer.start()
        }

        handleIntent()


    }

    private fun displayNotification(title: String, content: String){


        var replyLabel: String = resources.getString(R.string.reply_label)
        var remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
            setLabel(replyLabel)
            build()
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        intent.putExtra("okeoke", getMessageText(intent))

        var replyPendingIntent: PendingIntent =
            PendingIntent.getActivity(
                applicationContext,
                101,
                intent,
                PendingIntent.FLAG_MUTABLE
            )

        var action: NotificationCompat.Action =
            NotificationCompat.Action.Builder(R.drawable.ic_launcher_background,
                getString(R.string.label), replyPendingIntent)
                .addRemoteInput(remoteInput)
                .build()

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(content)
            .addAction(action)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(replyPendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, builder)
    }

    private fun createNotificationChannel(id: String, name: String, channelDescription: String){
        // validasi notif akan dibuat juga bersion sdk 26+
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance).apply {
                description = channelDescription
            }

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getMessageText(intent: Intent): CharSequence? {
        return RemoteInput.getResultsFromIntent(intent)?.getCharSequence(KEY_TEXT_REPLY)
    }

    private fun handleIntent() {

        val intent = this.intent

        val remoteInput = RemoteInput.getResultsFromIntent(intent)

        if (remoteInput != null) {

            val inputString = remoteInput.getCharSequence(
                KEY_TEXT_REPLY).toString()

            binding.text.text = inputString

        }
    }
}