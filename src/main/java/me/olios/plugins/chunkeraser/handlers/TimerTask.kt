package me.olios.plugins.chunkeraser.handlers

import me.olios.plugins.chunkeraser.ChunkEraser
import org.bukkit.scheduler.BukkitRunnable

class TimerTask(private val plugin: ChunkEraser) {
    private val chunkHandler = ChunkHandler(plugin)

    private var task: BukkitRunnable? = null

    fun startTask() {
        val interval = plugin.config.getLong("timer") * 20 // Convert seconds to ticks
        task = object : BukkitRunnable() {
            override fun run() {
                chunkHandler.deleteRandomChunk()
                // bossBarHandler.updateBossBar(plugin.config.getLong("interval"))
            }
        }
        task?.runTaskTimer(plugin, interval, interval)
    }

    fun stopTask() {
        task?.cancel()
        task = null
    }

    fun restartTask() {
        stopTask()
        startTask()
    }
}