package com.jzapata.todosum

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

fun Context.vibrateError() {
    val vibrator = getVibrator()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val vibrationEffect = VibrationEffect.createWaveform(
            longArrayOf(0, 100, 100, 100),
            -1
        )
        vibrator.vibrate(vibrationEffect)
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(longArrayOf(0, 100, 100, 100), -1)
    }
}

fun Context.vibrateSuccess() {
    val vibrator = getVibrator()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(50)
    }
}

private fun Context.getVibrator(): Vibrator {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = this.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
}