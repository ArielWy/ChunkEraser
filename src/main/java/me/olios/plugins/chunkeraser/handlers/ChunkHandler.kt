package me.olios.plugins.chunkeraser.handlers

import me.olios.plugins.chunkeraser.ChunkEraser
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Material

class ChunkHandler(private val plugin: ChunkEraser) {
    private val config = plugin.config

    fun deleteRandomChunk() {
        // get the random chunk
        val chunks = getPlayerLoadedChunks()
        val chunk = chunks.random()

        // delete the chunk blocks
        deleteChunk(chunk)
    }

    private fun getPlayerLoadedChunks(): Set<Chunk> {
        val loadedChunks = mutableSetOf<Chunk>()
        for (player in Bukkit.getOnlinePlayers()) {
            val viewDistance = Bukkit.getServer().viewDistance
            val playerChunk = player.location.chunk
            val world = player.world

            for (x in -viewDistance..viewDistance) {
                for (z in -viewDistance..viewDistance) {
                    val chunk = world.getChunkAt(playerChunk.x + x, playerChunk.z + z)
                    loadedChunks.add(chunk)
                }
            }
        }
        return loadedChunks
    }


    fun deleteChunk(chunk: Chunk) {
        val world = chunk.world

        // set all the chunk blocks to air
        for (x in 0..15) {
            for (y in world.minHeight..world.maxHeight) {
                for (z in 0..15) {
                    val block = chunk.getBlock(x, y, z)
                    block.type = Material.AIR
                }
            }
        }
    }
}