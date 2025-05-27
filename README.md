ChunkEraser
========

**ChunkEraser** plugin for Spigot/Paper.  Creates an exciting Minecraft minigame by randomly erasing chunks around players.

## Overview

**ChunkEraser** gradually *erase* the world around players, forcing them to adapt or fall into the void. After each cooldown, the plugin picks chunks within a configured radius and removes all blocks in them (often with a visual effect or sound). Players are alerted via chat messages, boss bars or sounds before chunks vanish. Administrators can configure how the erasing works, including the radius, erase pattern, timers, messages, sounds, logging, and visual indicators, making the game highly customizable and replayable.

## How it Works

- **Countdown:** The plugin runs on a cooldown timer (in seconds). During the countdown, players may see an action bar or boss bar showing time remaining until the next event.
- **Erasing:** When the timer expires, ChunkEraser deletes chunks around each player. Depending on the mode, it might remove one random chunk or all chunks within a given radius, shape or pattern. This causes terrain to collapse or disappear, often with particles and sound effects.
- **Player Impact:** Any player standing on an erased chunk may fall into lower terrain or the void, adding danger and excitement. After each erase, the timer resets (or increases) for the next event.
- **Alerts:** The plugin can broadcast chat messages and play sounds right before or after chunks are erased (e.g. “Warning: Chunks will vanish in 10 seconds!”). It may also use a boss bar to visually show the countdown. These alerts often use placeholders (like player name or time) for dynamic info.

<image placeholder>

## Installation  
1. **Download the Plugin:** Get the latest ChunkEraser `.jar` file from the [Releases](#) page.
2. **Add to Plugins Folder:** Place the `.jar` in your server’s `/plugins/` directory.
3. **Restart the Server:** Restart or reload your server to generate the configuration.
4. **Enable the Plugin:** In `config.yml`, set `enabled: true`, or run `/chunkeraser enable`.


## Configuration  
The `config.yml` is generated after the first run in `plugins/ChunkEraser/`. Below are key options (default values shown):

```yaml
General:
  enabled: false
  cooldown: 10
  playerChunkDistance: 10
  forAllPlayers: false
  increaseEachTime: false
  notifyPlayers: true
  iterations: 0

BossBar:
  enabled: true
  title: "%TIMER% seconds are left"
  color: BLUE
  style: SOLID

ActionBar:
  enabled: false
  message: "<blue>Chunks being erased in <gold>%TIMER%</gold> seconds!</blue>"

Messages:
  chunkErasedNotification: "<aqua>A chunk has been <hover:show_text:'<gold>at</gold> <green>%CHUNK.X%</green>, <green>%CHUNK.Z%</green>'>erased!</hover></aqua>"
  NoPerm: "<red>You lack the permission <dark_red>%PERM%</dark_red>!</red>"
  NoPlayer: "<red>You can't do that through the <dark_red>%EXECUTER%</dark_red>!</red>"

Sound:
  enabled: true
  soundType: "ENTITY_ENDER_DRAGON_GROWL"
  volume: 10.0
  pitch: 1.0
  allPlayers: false

Logs:
  enabled: true
  logEntry: "[%TIME%] Erased chunk at {%CHUNK.X%, %CHUNK.Z%} in world {%CHUNK.WORLD%} near player: {%PLAYER%}\n"
  logFilePath: "plugins/ChunkEraser/logs.txt"
```
