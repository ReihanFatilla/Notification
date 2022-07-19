package com.example.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.notification.databinding.ActivityAlertDetailsBinding

class AlertDetails : AppCompatActivity() {
    private lateinit var binding: ActivityAlertDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlertDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.iniText.text = intent.getStringExtra("okeoke")
    }
}