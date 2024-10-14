package me.olios.plugins.chunkeraser.handlers

import me.olios.plugins.chunkeraser.ChunkEraser
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class TimerTask(private val plugin: ChunkEraser, private val bossBarHandler: BossBarHandler) {
    private val config = plugin.config
    private val chunkHandler = ChunkHandler(plugin)
    private val intervalTime: Long = config.getLong("General.cooldown")

    var timer: Long = 10
    var task: BukkitRunnable? = null

    private val player: Player? = Bukkit.getPlayer("_olios")

    fun startTask() {
        timer = intervalTime
        bossBarHandler.createBossBar()

        player?.sendMessage("start task")

        task = object : BukkitRunnable() {
            override fun run() {
                if (timer == 0L) {
                    bossBarHandler.updateBossBar()
                    chunkHandler.processChunks()
                    restartTask()
                }

                bossBarHandler.updateBossBar()
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
            player?.sendMessage("stop task")
        } else {
            player?.sendMessage("No task to stop")
        }
    }


    fun restartTask() {
        stopTask()
        startTask()

        player?.sendMessage("restart task")
    }

    fun isRunning(): Boolean {
        if (task == null)
            return false
        return true
    }
}