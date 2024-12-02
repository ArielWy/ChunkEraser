package me.olios.plugins.chunkeraser.handlers

import me.olios.plugins.chunkeraser.ChunkEraser
import me.olios.plugins.chunkeraser.utils.NotificationManager
import org.bukkit.*
import org.bukkit.entity.Player

class ChunkHandler(private val plugin: ChunkEraser) {
    private val config = plugin.config

    // Main entry point to process chunks based on config settings
    fun processChunks() {
        val iterations = getIterations()

        repeat(iterations) {
            if (config.getBoolean("General.forAllPlayers"))
                processChunksForAllPlayers()
            else processChunksForRandomPlayer()

            NotificationManager(plugin).playPlayerSound() // Play sound for all players after chunk erase
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

            processChunkErase(chunk, onlinePlayer) // delete the chunk
        }
    }

    // Process chunks for one random player
    private fun processChunksForRandomPlayer() {
        val randomPlayer = Bukkit.getOnlinePlayers().random()
        val chunk = getRandomChunkForPlayer(randomPlayer)

        processChunkErase(chunk, randomPlayer) // delete the chunk
    }

    // call the necessary functions when erasing a chunk
    private fun processChunkErase(chunk: Chunk, player: Player) {
        LogHandler(plugin).logChunkEnabled(chunk) // log to the log file if enabled
        NotificationManager(plugin).broadcastGlobalMessage(chunk) // send messages to the players
        NotificationManager(plugin).playChunkSound(chunk) // play sound
        deleteChunk(chunk) // delete the chunk
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
}