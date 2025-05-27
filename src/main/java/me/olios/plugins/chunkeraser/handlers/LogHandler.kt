package me.olios.plugins.chunkeraser.handlers

import me.olios.plugins.chunkeraser.ChunkEraser
import me.olios.plugins.chunkeraser.utils.PluginManager
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.time.LocalDateTime
import kotlin.math.log


class LogHandler() {
    private val plugin = PluginManager.getInstance()
    private val config = plugin.config

    fun logChunkEnabled(chunk: Chunk, player: Player) {
        if (!config.getBoolean("Logs.enabled", false)) return

        val defaultPath = "plugins/ChunkEraser/logs.txt"
        val logfilePath: String = config.getString("Logs.logFilePath", defaultPath)?: defaultPath
        val logFile = validateLogFile(logfilePath)

        logErasedChunk(logFile, chunk, player)
    }

    private fun validateLogFile(logfilePath: String): File {
        val logFile = File(logfilePath)

        try {
            if (!logFile.exists()) {
                logFile.parentFile.mkdirs()
                logFile.createNewFile()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return logFile
    }

    private fun logErasedChunk(logFile: File, chunk: Chunk, player: Player) {
        val logEntry: String = getLogEntry(chunk, player)

        try {
            BufferedWriter(FileWriter(logFile,true)).use { writer ->
                writer.write(logEntry)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getLogEntry(chunk: Chunk, player: Player): String {
        val defaultLogEntry= "[%TIME%] Erased chunk at %CHUNK.X%, %CHUNK.Z% in world %CHUNK.WORLD%"
        var logEntry: String = config.getString("Logs.logEntry", defaultLogEntry)?: defaultLogEntry
        val placeholders = mapOf<String, String>(
            "%CHUNK.X%" to chunk.x.toString(),
            "%CHUNK.Z%" to chunk.z.toString(),
            "%CHUNK.WORLD%" to chunk.world.name,
            "%TIME%" to LocalDateTime.now().toString(),
            "%PLAYER%" to player.name
        )

        for ((placeholder, value) in placeholders) {
            logEntry = logEntry.replace(placeholder, value)
        }
        return logEntry
    }
}