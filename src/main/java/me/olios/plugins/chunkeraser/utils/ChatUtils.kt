package me.olios.plugins.chunkeraser.utils

import me.olios.plugins.chunkeraser.ChunkEraser
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ChatUtils() {
    private val plugin = PluginManager.getInstance()
    private val config = plugin.config

    fun checkIfPlayer(sender: CommandSender, sendMessage: Boolean = true): Boolean {
        if (sender is Player) return true
        if (!sendMessage) return true

        val name = sender.name

        var message: String = config.getString("Messages.NoPlayer").toString()
        val placeholder: MutableMap<String, String> = mutableMapOf("%EXECUTER%" to name)
        message = replacePlaceHolder(message, placeholder)

        sender.sendMessage(MiniMessage.miniMessage().deserialize(message))
        return true
    }

    fun checkPerm(sender: CommandSender, perm: String, sendMessage: Boolean = true): Boolean {
        if (sender.hasPermission(perm)) return true
        if (!sendMessage) return false

        var message: String = config.getString("Messages.NoPerm").toString()
        val placeholder: MutableMap<String, String> = mutableMapOf("%PERM%" to perm)
        message = replacePlaceHolder(message, placeholder)

        sender.sendMessage(MiniMessage.miniMessage().deserialize(message))
        return false
    }

    private fun replacePlaceHolder(string: String, mutableMap: MutableMap<String, String>): String {
        var result = string
        for ((key, value ) in mutableMap) {
            result = result.replace(key, value)
        }
        return result
    }
}