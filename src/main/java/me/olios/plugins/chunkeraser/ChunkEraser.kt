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
        getCommand("chunkeraser")?.setExecutor(ChunkCommand())

        SubCommandManager.registerCommand("start", StartCommand())
        SubCommandManager.registerCommand("stop", StopCommand())
        SubCommandManager.registerCommand("restart", RestartCommand())
        SubCommandManager.registerCommand("erase", EraseCommand())
        SubCommandManager.registerCommand("reload", ReloadCommand())
    }
}