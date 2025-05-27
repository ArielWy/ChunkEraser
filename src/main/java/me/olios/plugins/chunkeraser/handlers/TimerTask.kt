package me.olios.plugins.chunkeraser.handlers

import me.olios.plugins.chunkeraser.ChunkEraser
import me.olios.plugins.chunkeraser.utils.PluginManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class TimerTask() {
    private val plugin = PluginManager.getInstance()
    private val config = plugin.config
    private val chunkHandler = ChunkHandler()
    val actionBarHandler = ActionBarHandler()
    val bossBarHandler = BossBarHandler()
    private val intervalTime: Long = config.getLong("General.cooldown")

    var timer: Long = 10
    var task: BukkitRunnable? = null

    fun startTask() {
        timer = intervalTime
        bossBarHandler.createBossBar(timer)
        actionBarHandler.sendActionBar(timer)

        task = object : BukkitRunnable() {
            override fun run() {
                if (timer == 0L) {
                    bossBarHandler.updateBossBar(timer)
                    actionBarHandler.sendActionBar(timer)
                    chunkHandler.processChunks()
                    restartTask()
                }

                bossBarHandler.updateBossBar(timer)
                actionBarHandler.sendActionBar(timer)
                timer--
            }
        }
        task?.runTaskTimer(plugin, 20, 20)
    }

    fun stopTask() {
        if (task != null) {
            task?.cancel()
            task = null
            bossBarHandler.removeBossBar()
        }
    }


    fun restartTask() {
        stopTask()
        startTask()
    }

    fun isRunning(): Boolean {
        return task != null
    }
}