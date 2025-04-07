package com.alvin.geegy.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alvin.geegy.R

class HomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        findViewById<CardView>(R.id.firstAppointment).setOnClickListener {
            Intent(this, AppointmentDetailScreen::class.java).also {
                it.putExtra("APPOINTMENT", 1)
                startActivity(it)
            }
        }

    }
}