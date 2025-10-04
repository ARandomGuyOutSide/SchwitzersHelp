# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project Overview

SchwitzersHelp is a Minecraft 1.8.9 mod for Hypixel SkyBlock that provides automation features, ESP (Extra Sensory Perception) capabilities, and quality-of-life improvements. The mod is built using:

- **Polyfrost Gradle Toolkit** for multi-version support and build management
- **OneConfig** for configuration UI and settings management
- **Forge modding platform** with Mixin framework for game modification
- **Java 8** compatibility for Minecraft 1.8.9

## Essential Development Commands

### Building and Testing
```powershell
# Build the mod for all target versions
./gradlew build

# Build development version with debugging symbols
./gradlew shadowJar

# Run the mod in development environment (1.8.9)
./gradlew runClient

# Clean build artifacts
./gradlew clean

# Build specific version (if multi-version configured)
./gradlew :1.8.9-forge:build
```

### Debugging and Development
```powershell
# Enable mixin debug output (exports mixin changes to run/.mixin.out/class)
# This is already configured in build.gradle.kts for development runs

# Check dependency versions and conflicts
./gradlew dependencies

# Validate configuration
./gradlew check
```

## Architecture Overview

### Core Module Structure
The mod follows a modular architecture organized by functionality:

- **`SchwitzersHelp.java`** - Main mod class that registers all modules and commands
- **`config/SchwitzerHelpConfig.java`** - Centralized OneConfig-based configuration system
- **`features/`** - Core mod functionality (movement, automation, player utilities)
- **`util/`** - Utility classes (path finding, rotation, rendering, chat)
- **Module directories** - Feature-specific code organized by game area:
  - `bedwars/` - Bed Wars game mode features (ESP, player tracking)
  - `dungeon/` - Dungeon-related features (ghost blocks, mob ESP, chat automation)
  - `mining/` - Mining automation and ESP (titanium detection, coal veins)
  - `slayer/` - Slayer boss features and carry mode
  - `macros/` - Automated action sequences with keybind support
  - `helpStuff/` - PvP and movement assistance features
  - `discord/` - Discord webhook integration for notifications

### Key Architectural Patterns

**Event-Driven Architecture**: All modules extend Minecraft Forge's event system, registered to `MinecraftForge.EVENT_BUS` in the main class.

**Configuration Management**: Centralized through `SchwitzerHelpConfig` singleton with OneConfig annotations for GUI generation and automatic serialization.

**Macro System**: `MacroController` manages keybind-based automation with state tracking and Discord integration. All macros implement the `Macro` interface.

**Path Finding**: Advanced A* algorithm in `PathFinder/PathFinding.java` with wall penalty calculations, freespace evaluation, and diagonal movement support.

**ESP Rendering**: Consistent rendering pattern using `RenderUtil` for world overlay drawing with configurable colors and transparency.

## Key Development Patterns

### Adding New Features
1. Create feature class in appropriate module directory
2. Register with event bus in `SchwitzersHelp.init()`
3. Add configuration options to `SchwitzerHelpConfig.java` with OneConfig annotations
4. Use consistent naming: `isFeatureName()` for booleans, `getFeatureName()` for values

### ESP Feature Pattern
```java
@SubscribeEvent
public void onRenderWorldLast(RenderWorldLastEvent event) {
    if (!config.isFeatureEnabled()) return;
    
    // Calculate render positions
    double renderPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks;
    // ... render logic with RenderUtil.drawFilledBox()
}
```

### Macro Development
- Extend `Macro` abstract class
- Implement state management with `MacroState` enum
- Register in `MacroController` with keybind
- Use `ChatUtil.formatedChatMessage()` for user feedback
- Discord notifications automatically handled by controller

### Mixin Integration
- Mixin configuration in `mixins.examplemod.json`
- Target classes for GUI modifications in `mixin/gui/` directory
- Follow Polyfrost Loom guidelines for version compatibility

## Important Configuration

### Gradle Properties
- `mod_id=examplemod` - Should be updated to match actual mod ID
- `mod_version=0.0.5` - Semantic versioning for releases
- Multi-version support configured for 1.8.9-forge (1.12.2 support commented out)

### OneConfig Integration
- Configuration categories: General, Bedwars, Dungeons, Mining, Help, Minigames, Macros, Slayers, Guild, Discord
- Dependency system for conditional config options
- Automatic GUI generation and JSON serialization

### Development Environment
- DevAuth integration for Minecraft authentication in development
- Mixin debugging enabled with export to `.mixin.out/class`
- OneConfig launch wrapper for legacy Forge compatibility
- No server run configurations (client-only mod)

## Version Management

The mod uses Polyfrost's multi-version system:
- Primary target: Minecraft 1.8.9 with Forge
- Build outputs to `versions/{mcVersion}/build/`
- Version-specific preprocessing with `preprocess` block in `root.gradle.kts`

## Testing and Quality Assurance

### Hypixel SkyBlock Context
- All features are designed for Hypixel SkyBlock server
- Location detection using scoreboard parsing (`ScoreboardUtil`)
- Chat message filtering and automation for server-specific commands
- Failsafe systems to prevent unintended actions outside target environment