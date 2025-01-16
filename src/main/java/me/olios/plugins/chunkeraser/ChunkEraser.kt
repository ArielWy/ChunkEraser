package me.olios.plugins.chunkeraser

import me.olios.plugins.chunkeraser.commands.ChunkCommand
import me.olios.plugins.chunkeraser.commands.SubCommandManager
import me.olios.plugins.chunkeraser.commands.subcommands.*
import me.olios.plugins.chunkeraser.utils.PluginManager
import org.bukkit.plugin.java.JavaPlugin

class ChunkEraser : JavaPlugin() {

    override fun onEnable() {
        saveDefaultConfig()

        registerCommands()

        PluginManager.initialize(this)
        PluginManager.isEnabled()
    }

    override fun onDisable() {
        PluginManager.stopTimer()
    }

    private fun registerCommands() {
        getCommand("chunkeraser")?.setExecutor(ChunkCommand(this))

        SubCommandManager.registerCommand("start", StartCommand(this))
        SubCommandManager.registerCommand("stop", StopCommand(this))
        SubCommandManager.registerCommand("restart", RestartCommand(this))
        SubCommandManager.registerCommand("erase", EraseCommand(this))
        SubCommandManager.registerCommand("reload", ReloadCommand(this))
    }
}