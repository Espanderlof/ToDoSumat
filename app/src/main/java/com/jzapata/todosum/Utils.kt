package com.jzapata.todosum

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

fun Context.vibrateError() {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = this.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Dos vibraciones cortas
        val vibrationEffect = VibrationEffect.createWaveform(
            longArrayOf(0, 100, 100, 100),  // Tiempo de espera, vibración, espera, vibración
            -1 // No repetir
        )
        vibrator.vibrate(vibrationEffect)
    } else {
        // Para versiones anteriores a Android O
        @Suppress("DEPRECATION")
        vibrator.vibrate(longArrayOf(0, 100, 100, 100), -1)
    }
}