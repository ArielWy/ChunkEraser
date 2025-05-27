package me.olios.plugins.chunkeraser.utils

import me.olios.plugins.chunkeraser.ChunkEraser
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class NotificationManager() {
    private val plugin = PluginManager.getInstance()
    private val config = plugin.config

    fun broadcastGlobalMessage(chunk: Chunk) {
        if (!config.getBoolean("General.notifyPlayers")) return

        val chunkX = chunk.x.toString()
        val chunkZ = chunk.z.toString()

        var message: String = config.getString("Messages.chunkErasedNotification").toString()
        val placeholder: MutableMap<String, String> = mutableMapOf("%CHUNK.X%" to chunkX, "%CHUNK.Z%" to chunkZ)
        message = replacePlaceHolder(message, placeholder)
        Bukkit.broadcast(MiniMessage.miniMessage().deserialize(message))
    }

    private fun replacePlaceHolder(string: String, mutableMap: MutableMap<String, String>): String {
        var result = string
        for ((key, value ) in mutableMap) {
            result = result.replace(key, value)
        }
        return result
    }

    fun playPlayerSound() {
        if (!config.getBoolean("Sound.enabled")) return

        val soundTypeString = config.getString("Sound.soundType")!!
        val soundType: Sound = Sound.valueOf(soundTypeString)

        if (!config.getBoolean("Sound.allPlayers")) return

        Bukkit.getOnlinePlayers().forEach { player ->
            player.playSound(player, soundType, 1.0f, 1.0f)
        }
    }

    fun playChunkSound(chunk: Chunk) {
        // Check if sound is enabled in the config
        if (!config.getBoolean("Sound.enabled")) return

        // Get the sound type from the config
        val soundTypeString = config.getString("Sound.soundType")
        if (soundTypeString.isNullOrEmpty()) {
            plugin.logger.severe("Sound type string is null or empty in config.")
            return
        }

        // Parse the sound type
        val soundType: Sound
        try {
            soundType = Sound.valueOf(soundTypeString)
        } catch (e: IllegalArgumentException) {
            plugin.logger.severe("Invalid sound type specified: $soundTypeString")
            return
        }

        // Get the volume and pitch from the config
        val volume = config.getDouble("Sound.volume", 1.0).toFloat()
        val pitch = config.getDouble("Sound.pitch", 1.0).toFloat()

        // Calculate the location to play the sound
        val chunkCenter = Location(chunk.world, chunk.x * 16 + 8.0, 64.0, chunk.z * 16 + 8.0)
        val surfaceLoc: Location = chunk.world.getHighestBlockAt(chunkCenter).location

        // Play the sound at the calculated location
        chunk.world.playSound(surfaceLoc, soundType, volume, pitch)

    }
}