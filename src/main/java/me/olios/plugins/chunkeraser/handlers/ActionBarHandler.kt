package me.olios.plugins.chunkeraser.handlers

import me.olios.plugins.chunkeraser.ChunkEraser
import me.olios.plugins.chunkeraser.utils.PluginManager
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class ActionBarHandler() {
    private val plugin = PluginManager.getInstance()
    private val config = plugin.config

    fun sendActionBar(timer: Long) {
        if (!isEnabled()) return

        val message = config.getString("ActionBar.message").toString().replace("%TIMER%", timer.toString(), ignoreCase = true)
        val messageComponent = MiniMessage.miniMessage().deserialize(message)

        Bukkit.getOnlinePlayers().forEach { player ->
            player.sendActionBar(messageComponent)
        }
    }

    private fun isEnabled(): Boolean {
        return config.getBoolean("ActionBar.enabled")
    }

}
