package me.olios.plugins.chunkeraser.commands

import me.olios.plugins.chunkeraser.ChunkEraser
import me.olios.plugins.chunkeraser.utils.PluginManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class ChunkCommand(): CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, p1: Command, p2: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) return false

        val subCommand = SubCommandManager.getCommand(args[0])
        if (subCommand == null) {
            sender.sendMessage("§cUnknown subcommand: ${args[0]}")
            return true
        }

        return subCommand.execute(sender, args.sliceArray(1 until args.size))
    }


    override fun onTabComplete(
        sender: CommandSender,
        p1: Command,
        p2: String,
        args: Array<out String>?
    ): MutableList<String>? {

        val subCommands = SubCommandManager.getAllCommands().filter { sender.hasPermission("ChunkEraser.$it") }

        if (args == null || args.isEmpty()) return subCommands.toMutableList()

        val subCommand = SubCommandManager.getCommand(args[0])
        if (args.size == 1) {
            return subCommands
                .filter { it.startsWith(args[0], ignoreCase = true) }
                .toMutableList()
        }

        return subCommand?.tabComplete(sender, args.sliceArray(1 until args.size))?.toMutableList()
    }

}