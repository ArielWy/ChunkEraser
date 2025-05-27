package me.olios.plugins.chunkeraser.commands.subcommands

import me.olios.plugins.chunkeraser.ChunkEraser
import me.olios.plugins.chunkeraser.commands.SubCommand
import me.olios.plugins.chunkeraser.utils.ChatUtils
import me.olios.plugins.chunkeraser.utils.PluginManager
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ReloadCommand: SubCommand {
    private val plugin = PluginManager.getInstance()
    override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
        if (ChatUtils().checkPerm(sender, "chunkeraser.reload")) {
            plugin.saveDefaultConfig()
            plugin.reloadConfig()
            PluginManager.reload()
        }

        return true
    }

    override fun tabComplete(sender: CommandSender, args: Array<out String>): List<String> {
        return emptyList()
    }
}