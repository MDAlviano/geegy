package com.alvin.geegy.ui.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.alvin.geegy.R
import com.alvin.geegy.http.HttpHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class AppointmentDetailScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_detail_screen)

        findViewById<ImageView>(R.id.bBack).setOnClickListener {
            finish()
        }

        val appointmentId = intent.getIntExtra("APPOINTMENT", 0)

        if (appointmentId > 0) {
            loadAppointmentDetails(1)
        }

    }

    private fun loadAppointmentDetails(appointmentId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val appointmentDetails = HttpHandler.getRequest("appointments/$appointmentId")
                if (appointmentDetails.isNotEmpty()) {
                    lifecycleScope.launch(Dispatchers.Main) {
                        val jsonObject = JSONObject(appointmentDetails)
                        findViewById<TextView>(R.id.tvAppointmentDate).text = jsonObject.getString("appointment_date")
                        findViewById<TextView>(R.id.tvPatientName).text = "Name: ${jsonObject.getString("patient_name")}"
                        findViewById<TextView>(R.id.tvPatientBirthdate).text = "DOB: ${jsonObject.getString("patient_dob")} (${jsonObject.getString("patient_age")} years old)"
                        findViewById<TextView>(R.id.tvPatientAddress).text = "Address: ${jsonObject.getString("patient_address")}"
                        findViewById<TextView>(R.id.tvDoctorName).text = "Name: ${jsonObject.getString("doctor_name")}"
                        findViewById<TextView>(R.id.tvDoctorSpecialist).text = "Specialist: ${jsonObject.getString("doctor_specialist")}"
                        findViewById<TextView>(R.id.tvProblem).text = jsonObject.getString("problems")
                        findViewById<TextView>(R.id.tvSymptoms).text = jsonObject.getString("symptoms")
                        findViewById<TextView>(R.id.tvAction).text = jsonObject.getString("actions")

                        loadImage(jsonObject.getString("teeth_photo"))

                    }
                }
            } catch (e: Exception) {
                lifecycleScope.launch(Dispatchers.Main) {
                    Log.e("loadAppointmentDetails", e.message.toString())
                }
            }
        }
    }

    private fun loadImage(imageName: String) {
        val imageUrl = "http://10.0.2.2:5000/images/$imageName"
        Thread {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doOutput = true

            val inputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(inputStream)

            runOnUiThread {
                findViewById<ImageView>(R.id.iTeethPhoto).setImageBitmap(bitmap)
            }

        }.start()
    }

}