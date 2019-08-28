package com.example.firsttestapp

import android.os.CountDownTimer
import java.sql.Time

class Timer {
    var PlayerControl: String = ""
    var TimerLength: Long = 180000

    var PlayerTimer = object : CountDownTimer(this.TimerLength, 1000) {
        override fun onTick(l: Long) {

        }

        override fun onFinish() {

        }
    }
}