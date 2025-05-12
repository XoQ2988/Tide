# Tide

**Tide** is a lightweight, client-side utility mod for Minecraft 1.20.5 (Fabric), providing a suite of quality-of-life features.

---

## Features

- **Modular System**  
  Load or unload individual features on the fly, with per-module keybinds and settings.

- **Core Modules**
    - **Anti-Break**  
      Warns or prevents breaking tools when durability falls below configurable thresholds.
    - **Auto-Tool**  
      Switches to the best tool in your hotbar for the block you’re mining, then (optionally) switches back.
    - **HUD Display**  
      Single, unified HUD with dynamic placeholders (FPS, ping, coords, speed, and more).

- **Command Interface**
    - **`.help`** – list available commands
    - **`.list`** – show all modules and their on/off state
    - **`.toggle <module>`** – enable or disable a module
    - **`.bind <module>`** / **`.binds`** – assign or list keybinds
    - **`.setting <module> <setting> <value>`** – view or change module settings
    - **`.hud ...`** – view, toggle or edit individual HUD lines

- **Settings & Configuration**
    - All module settings, keybinds, HUD templates and colors persist in `config/tide.json`.
    - Live edit via commands or (future) in-game GUI.

---

## Installation

1. **Fabric Loader**  
   Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.20.5.

2. **Tide Mod**  
   Download the latest `tide-<version>.jar` from the Releases page and place it into your `mods/` folder.

3. **(Optional) Fabric API**  
   Some modules may require Fabric API; if you see missing-class errors, install the matching Fabric API.

4. **Launch Minecraft**  
   Select the Fabric profile and start the game.

---

## Usage

### Opening the Command Bar

Press `.` in chat, then type:

- `.help` to see all commands.
- `.list` to list modules and their status.
- `.toggle anti-break` to enable/disable the Auto-Tool module.
- `.bind auto-tool` then press your desired key to bind that module.
- `.setting anti-break warn-threshold 20` to set the warning threshold to 20%.
- `.hud list` to list all HUD entries.
- `.hud ping toggle false` to hide the ping line.
- `.hud speed template "Speed: {SPEED} m/s"` to change the speed format.

### Config File

All settings are saved to `config/tide.json`.
You can edit this fire directly or use the in-game commands above.