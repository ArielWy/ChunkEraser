package me.olios.plugins.chunkeraser

import me.olios.plugins.chunkeraser.handlers.BossBarHandler
import me.olios.plugins.chunkeraser.handlers.ChunkHandler
import me.olios.plugins.chunkeraser.handlers.TimerTask

object PluginManager {
    private var plugin: ChunkEraser? = null
    var timerTask: TimerTask? = null
    var bossBarHandler: BossBarHandler? = null

    fun initialize(plugin: ChunkEraser) {
        this.plugin = plugin
        // Initialize BossBarHandler without TimerTask
        bossBarHandler = BossBarHandler(plugin)

        // Initialize TimerTask and set it in BossBarHandler
        timerTask = TimerTask(plugin, bossBarHandler!!)
        bossBarHandler!!.setTimerTask(timerTask!!)
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
        ChunkHandler(plugin!!).processChunks()
        restartTimer(true)
    }

    fun isEnabled() {
        if (!plugin!!.config.getBoolean("General.enable")) return

        startTimer()
    }
}
