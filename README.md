ChunkEraser
========

**ChunkEraser** plugin for Spigot/Paper.  Creates an exciting Minecraft minigame by randomly erasing chunks around players.

## Overview

**ChunkEraser** gradually *erase* the world around players, forcing them to adapt or fall into the void. After each cooldown, the plugin picks chunks within a configured radius and removes all blocks in them (often with a visual effect or sound). Players are alerted via chat messages, boss bars or sounds before chunks vanish. Administrators can configure how the erasing works, including the radius, erase pattern, timers, messages, sounds, logging, and visual indicators, making the game highly customizable and replayable.

## How it Works

- **Countdown:** The plugin runs on a cooldown timer (in seconds). During the countdown, players may see an action bar or boss bar showing time remaining until the next event.
- **Erasing:** When the timer expires, ChunkEraser deletes chunks around each player. Depending on the mode, it might remove one random chunk or all chunks within a given radius, shape or pattern. This causes terrain to collaps often with particles and sound effects.
- **Player Impact:** Any player standing on an erased chunk may fall into lower terrain or the void, adding danger and excitement. After each erase, the timer resets (or increases) for the next event.
- **Alerts:** The plugin can broadcast chat messages and play sounds right before or after chunks are erased (e.g. “Warning: Chunks will vanish in 10 seconds!”). It may also use a boss bar to visually show the countdown. These alerts often use placeholders (like player name or time) for dynamic info.

<image placeholder>

## Installation  
1. **Download the Plugin:** Get the latest ChunkEraser `.jar` file from the [Releases](#) page.
2. **Add to Plugins Folder:** Place the `.jar` in your server’s `/plugins/` directory.
3. **Restart the Server:** Restart or reload your server to generate the configuration.
4. **Enable the Plugin:** In `config.yml`, set `enabled: true`, or run `/chunkeraser enable`.


## Configuration

Edit `plugins/ChunkEraser/config.yml` to customize the game:

enabled (boolean): Turn ChunkEraser on or off.

radius (integer): Chunk radius around each player. Chunks within this radius can be erased.

cooldown (integer): Seconds between erase events.

mode (string): Erasing pattern. For example, "random" to remove a single random chunk each time, or "area" to erase all chunks within the radius.

messages: Section to customize player alerts. Includes keys like prefix, countdown, and erase. Message text can include colors and placeholders.

sounds: (optional) Configure sound effects. E.g. a sound to play when warning players or when chunks are erased.

visual: (optional) Enable boss bar or action bar displays for the countdown. You can set bossbar: true to show a boss bar with a title (configurable in messages).

logging: (optional) Options to log erase events to console or file for admins.