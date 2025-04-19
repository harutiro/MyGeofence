package net.harutiro.mygeofence.utils

import android.content.Context
import android.os.BatteryManager
import android.os.Handler
import android.os.Looper
import android.util.Log

// コールバックインターフェースを定義
interface EnergyCallback {
    fun onEnergyChecked(energy: Long)
}

class EnergyChecker(private val context: Context) {

    private val handler = Handler(Looper.getMainLooper())
    private val interval: Long = 1000 // 1秒ごとに実行
    private lateinit var callback: EnergyCallback

    private val runnable = object : Runnable {
        override fun run() {
            checkEnergy()
            handler.postDelayed(this, interval)
        }
    }

    fun start(callback: EnergyCallback) {
        this.callback = callback
        handler.post(runnable)
    }

    fun stop() {
        handler.removeCallbacks(runnable)
    }

    private fun checkEnergy() {
        val TAG = "Energy"

        val mBatteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val energy = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)
        Log.d(TAG, "Remaining energy = $energy μAh")

        // コールバックを通じてエネルギー値を返す
        callback.onEnergyChecked(energy)
    }
}