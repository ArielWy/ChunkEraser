package me.olios.plugins.chunkeraser.handlers

import me.olios.plugins.chunkeraser.ChunkEraser
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class ActionBarHandler(private val plugin: ChunkEraser) {
    private val config = plugin.config

    private val player: Player? = Bukkit.getPlayer("_olios")


    fun sendActionBar(timer: Long) {
        if (!isEnabled()) return

        val message = config.getString("ActionBar.message").toString()
        val resolver = TagResolver.resolver(Placeholder.parsed("timer", timer.toString()))
        val messageComponent = MiniMessage.miniMessage().deserialize(message, resolver)

        Bukkit.getOnlinePlayers().forEach { player ->
            player.sendActionBar(messageComponent)
        }

        player?.sendMessage("Send ActionBar")
    }

    fun isEnabled(): Boolean {
        return config.getBoolean("ActionBar.enabled")
    }

}
