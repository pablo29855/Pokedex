package com.conoland.pokedex

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.conoland.pokedex.MainActivity
import java.util.Timer
import java.util.TimerTask

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val tarea: TimerTask = object : TimerTask() {
            override fun run() {
                val intent = Intent(this@Splash, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        val tiempo = Timer()
        tiempo.schedule(tarea, 2000)
    }
}