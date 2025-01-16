package me.olios.plugins.chunkeraser.handlers

import me.olios.plugins.chunkeraser.ChunkEraser
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player

class BossBarHandler(private val plugin: ChunkEraser) {
    private var bossBar: BossBar? = null
    private val config = plugin.config
    private val intervalTime: Long = config.getLong("General.cooldown") * 20

    fun createBossBar(timer: Long) {
        if (!isEnabled()) return

        val title: String = config.getString("BossBar.title")?.replace("%TIMER%", timer.toString(), true) ?: timer.toString()
        val barColor: String = config.getString("BossBar.color") ?: "BLUE"
        val barStyle: String = config.getString("BossBar.style") ?: "SOLID"

        bossBar = Bukkit.createBossBar(title, BarColor.valueOf(barColor), BarStyle.valueOf(barStyle))
        bossBar?.progress = 0.0

        Bukkit.getOnlinePlayers().forEach { player -> bossBar?.addPlayer(player) }
    }

    fun updateBossBar(timer: Long) {
        if (!isEnabled()) return

        val title: String = config.getString("BossBar.title")?.replace("%TIMER%", timer.toString(), true) ?: timer.toString()
        bossBar?.setTitle(title)

        // Calculate progress
        val progress = if (timer > 0) (intervalTime - timer * 20).toDouble() / intervalTime else 1.0
        bossBar?.progress = progress.coerceIn(0.0, 1.0)

        Bukkit.getOnlinePlayers().forEach { player -> bossBar?.addPlayer(player) }
    }

    fun removeBossBar() {
        if (bossBar == null) return
        bossBar?.removeAll()
        bossBar = null
    }

    private fun isEnabled(): Boolean {
        return config.getBoolean("BossBar.enabled")
    }
}
