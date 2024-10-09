package me.olios.plugins.chunkeraser.handlers

import me.olios.plugins.chunkeraser.ChunkEraser
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player

class BossBarHandler(private val plugin: ChunkEraser) {
    private lateinit var timerTask: TimerTask
    private var bossBar: BossBar? = null
    private val config = plugin.config
    private val intervalTime: Long = config.getLong("General.timer") * 20

    private val player: Player? = Bukkit.getPlayer("_olios")

    constructor(plugin: ChunkEraser, timerTask: TimerTask) : this(plugin) {
        this.timerTask = timerTask
    }

    fun setTimerTask(timerTask: TimerTask) {
        this.timerTask = timerTask
    }

    fun createBossBar() {
        if (!isEnabled()) return

        val title: String = config.getString("BossBar.message")?.replace("<timer>", timerTask.timer.toString(), true) ?: timerTask.timer.toString()
        val barColor: String = config.getString("BossBar.color") ?: "BLUE"
        val barStyle: String = config.getString("BossBar.style") ?: "SOLID"

        bossBar = Bukkit.createBossBar(title, BarColor.valueOf(barColor), BarStyle.valueOf(barStyle))
        bossBar?.progress = 0.0

        Bukkit.getOnlinePlayers().forEach { player -> bossBar?.addPlayer(player) }
        player?.sendMessage("CREATE BOSS-BAR")
    }

    fun updateBossBar() {
        if (!isEnabled()) return

        val title: String = config.getString("BossBar.title")?.replace("<timer>", timerTask.timer.toString(), true) ?: timerTask.timer.toString()
        bossBar?.setTitle(title)

        // Calculate progress
        val progress = if (timerTask.timer > 0) (intervalTime - timerTask.timer * 20).toDouble() / intervalTime else 1.0
        bossBar?.progress = progress.coerceIn(0.0, 1.0)

        Bukkit.getOnlinePlayers().forEach { player -> bossBar?.addPlayer(player) }

        player?.sendMessage("UPDATE BOSS-BAR: Progress = $progress")
    }


    fun removeBossBar() {
        if (bossBar != null) {
            bossBar?.removeAll()
            bossBar = null
            player?.sendMessage("REMOVE BOSS-BAR: Boss bar removed")
        } else {
            player?.sendMessage("REMOVE BOSS-BAR: No boss bar to remove")
        }
    }

    fun isEnabled(): Boolean {
        return config.getBoolean("BossBar.enabled")
    }

}
