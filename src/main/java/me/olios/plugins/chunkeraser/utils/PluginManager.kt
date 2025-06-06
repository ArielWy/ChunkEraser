package me.olios.plugins.chunkeraser.utils

import me.olios.plugins.chunkeraser.ChunkEraser
import me.olios.plugins.chunkeraser.handlers.ChunkHandler
import me.olios.plugins.chunkeraser.handlers.TimerTask

object PluginManager {
    private lateinit var plugin: ChunkEraser
    private var timerTask: TimerTask? = null

    fun getInstance() = plugin

    fun initialize(plugin: ChunkEraser) {
        PluginManager.plugin = plugin

        // Initialize TimerTask and set it in BossBarHandler
        timerTask = TimerTask()
    }

    fun startTimer() {
        timerTask?.startTask()
    }

    fun stopTimer() {
        timerTask?.stopTask()
    }

    fun restartTimer(ifRunning: Boolean = false) {
        if (ifRunning) {
            if (timerTask?.isRunning() == true)
                timerTask?.restartTask()
            else return
        } else timerTask?.restartTask()
    }

    fun removeChunkImmediately() {
        ChunkHandler().processChunks()
        restartTimer(true)
    }

    fun isEnabled() {
        if (!plugin.config.getBoolean("General.enable")) return

        startTimer()
    }

    fun reload() {
        plugin.reloadConfig()
    }
}
