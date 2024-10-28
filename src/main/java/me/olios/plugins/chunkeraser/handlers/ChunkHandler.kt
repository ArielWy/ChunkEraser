package me.olios.plugins.chunkeraser.handlers

import me.olios.plugins.chunkeraser.ChunkEraser
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.*
import org.bukkit.entity.Player

class ChunkHandler(private val plugin: ChunkEraser) {
    private val config = plugin.config

    private val player: Player? = Bukkit.getPlayer("_olios")

    // Main entry point to process chunks based on config settings
    fun processChunks() {
        val iterations = getIterations()

        repeat(iterations) {
            if (config.getBoolean("General.forAllPlayers"))
                processChunksForAllPlayers()
            else processChunksForRandomPlayer()

            playPlayerSound() // Play sound for all players after chunk erase
        }
    }

    // Get number of iterations, incrementing if required
    private fun getIterations(): Int {
        return if (config.getBoolean("General.increaseEachTime")) {
            incrementIterations()
        } else {
            1
        }
    }

    // Process chunks for all online players
    private fun processChunksForAllPlayers() {
        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
            val chunk = getRandomChunkForPlayer(onlinePlayer)

            notifyPlayers(chunk)
            playChunkSound(chunk)
            deleteChunk(chunk)

            sendDebugMessage(player, chunk)
        }
    }

    // Process chunks for one random player
    private fun processChunksForRandomPlayer() {
        val randomPlayer = Bukkit.getOnlinePlayers().random()
        val chunk = getRandomChunkForPlayer(randomPlayer)

        notifyPlayers(chunk)
        playChunkSound(chunk)
        deleteChunk(chunk)

        sendDebugMessage(player, chunk)
    }

    // Increment iterations and save to config
    private fun incrementIterations(): Int {
        val currentIterations = config.getInt("General.iterations")
        val newIterations = currentIterations + 1
        config.set("General.iterations", newIterations)
        plugin.saveConfig()
        return newIterations
    }

    // Get a random chunk for a player
    private fun getRandomChunkForPlayer(player: Player): Chunk {
        val loadedChunks: Set<Chunk> = getLoadedChunks(player)
        return loadedChunks.random()
    }

    // Get a set of loaded chunks within the specified distance
    private fun getLoadedChunks(player: Player): Set<Chunk> {
        val chunkDistance = config.getInt("General.playerChunkDistance").takeIf { it != 0 }
            ?: return setOf(player.chunk)
        sendViewDistance(chunkDistance)

        return getPlayerLoadedChunks(player, chunkDistance)
    }

    // Get chunks loaded by a specific player
    private fun getPlayerLoadedChunks(player: Player, chunkDistance: Int): Set<Chunk> {
        val loadedChunks = mutableSetOf<Chunk>()
        val playerChunk = player.location.chunk
        val world = player.world

        for (x in -chunkDistance..chunkDistance) {
            for (z in -chunkDistance..chunkDistance) {
                val chunk = world.getChunkAt(playerChunk.x + x, playerChunk.z + z)
                loadedChunks.add(chunk)
            }
        }
        return loadedChunks
    }

    // Delete all blocks in a chunk by setting them to AIR
    private fun deleteChunk(chunk: Chunk) {
        val world = chunk.world
        for (x in 0..15) {
            for (y in world.minHeight..world.maxHeight) {
                for (z in 0..15) {
                    val block = chunk.getBlock(x, y, z)
                    if (block.type != Material.AIR) {
                        block.type = Material.AIR
                    }
                }
            }
        }
    }

    private fun notifyPlayers(chunk: Chunk) {
        if (!config.getBoolean("General.notifyPlayers")) return

        val chunkX = chunk.x.toString()
        val chunkZ = chunk.z.toString()

        val message: String = config.getString("Messages.chunkErasedNotification").toString()
        val resolver = TagResolver.builder()
            .resolver(Placeholder.parsed("chunkx", chunkX))
            .resolver(Placeholder.parsed("chunkz", chunkZ))
            .build()
        val messageComponent = MiniMessage.miniMessage().deserialize(message, resolver)

        Bukkit.broadcast(messageComponent)
    }

    private fun playPlayerSound() {
        if (!config.getBoolean("Sound.enabled")) return

        val soundTypeString = config.getString("Sound.soundType")!!
        val soundType: Sound = Sound.valueOf(soundTypeString)

        if (!config.getBoolean("Sound.allPlayers")) return

        Bukkit.getOnlinePlayers().forEach { player ->
            player.playSound(player, soundType, 1.0f, 1.0f)
        }
    }

    private fun playChunkSound(chunk: Chunk) {
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

        sendDebugMessage(player, message = "Play sound at ${surfaceLoc.x}, ${surfaceLoc.z}. volume: $volume, pitch: $pitch")
    }



    // Set blocks in a chunk to LIGHT_BLUE_WOOL for debugging purposes
    private fun chunkDebugging(chunk: Chunk) {
        for (x in 0..15) {
            for (z in 0..15) {
                val block = chunk.getBlock(x, 100, z)
                block.type = Material.LIGHT_BLUE_WOOL
            }
        }
    }

    // Send the chunk distance to the player for debugging
    private fun sendViewDistance(chunkDistance: Int) {
        player?.sendMessage("Chunk distance: $chunkDistance")
    }

    // Send a debug message with chunk coordinates
    private fun sendDebugMessage(player: Player?, chunk: Chunk? = null, message: String = "") {
        if (chunk != null)
            player?.sendMessage("ChunkX: ${chunk.x}, ChunkZ: ${chunk.z}")
        else player?.sendMessage(message)
    }
}