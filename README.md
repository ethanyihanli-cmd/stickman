# Stickman Arena: Shadow Brawl

Stickman Arena: Shadow Brawl is a 2D JavaFX fighting game where the player controls a stickman warrior across three arena levels. The goal is to defeat every enemy in each level, survive enemy attacks, earn score, and clear the full game.

The game uses keyboard movement, melee attacks, a ranged fireball special attack, platform-based movement, enemy patrol and chase behavior, hit particles, sound effects, background music, a score counter, and persistent high-score saving.

## Gameplay

- Move through side-scrolling arena-style levels.
- Jump between ground and floating platforms.
- Fight enemies with close-range attacks.
- Use a fireball projectile for ranged damage.
- Clear all enemies to advance to the next level.
- Complete all three levels to win.
- Restart after victory or game over.

## Controls

| Action | Keys |
| --- | --- |
| Move left | `A` or `Left Arrow` |
| Move right | `D` or `Right Arrow` |
| Jump | `K`, `W`, `Up Arrow`, or `Space` |
| Melee attack | `J` or `S` |
| Fireball special | `L` |
| Restart | `R` |

## Techniques Used

This project is built with Java and JavaFX. The main techniques include:

- **JavaFX application structure**: `MainApp` creates the stage, scene, canvas, input handler, and game loop.
- **Canvas-based rendering**: `GameCanvas` draws the background, platforms, stickmen, projectiles, particles, HUD, victory screen, and game-over screen using `GraphicsContext`.
- **Frame-based game loop**: `GameLoop` uses JavaFX `AnimationTimer` to update the world every frame with delta time.
- **Object-oriented game entities**: shared position, size, and velocity behavior is handled through `GameObject`, while `Player`, `Enemy`, `Projectile`, `Platform`, and `Particle` model specific game objects.
- **Keyboard input tracking**: `InputHandler` stores currently pressed keys in a `HashSet`, which supports smooth multi-key controls.
- **Simple physics**: player and enemies use velocity, gravity, jumping force, platform landing checks, and screen bounds.
- **AABB collision detection**: `CollisionDetector` checks rectangle overlap for player, enemy, attack, and projectile collisions.
- **Enemy AI state behavior**: enemies switch between patrol, chase, and attack states based on distance from the player.
- **Projectile system**: `Projectile` stores velocity, damage, and lifetime. The game loop updates each projectile and removes it when expired.
- **Particle effects**: short-lived particles are spawned on hits to make combat feel more responsive.
- **Audio playback**: `SoundPlayer` loads and plays JavaFX `MediaPlayer` sound effects and looping background music.
- **Score persistence**: `ScoreManager` uses Java `Preferences` to save the best score between runs.
- **Maven packaging**: the project uses Maven with JavaFX dependencies and a shaded final JAR build.

## Project Structure

```text
src/main/java/com/macondo/stickman
├── MainApp.java
├── Launcher.java
├── controller
│   └── GameLoop.java
├── input
│   └── InputHandler.java
├── model
│   ├── CollisionDetector.java
│   ├── Enemy.java
│   ├── GameObject.java
│   ├── Level.java
│   ├── Particle.java
│   ├── Platform.java
│   ├── Player.java
│   └── Projectile.java
├── utils
│   ├── ScoreManager.java
│   └── SoundPlayer.java
└── view
    ├── GameCanvas.java
    └── StickmanDrawer.java
```

Sound assets are stored in:

```text
src/main/resources/com/macondo/stickman/sound
```

## Requirements

- JDK 22 or newer
- Maven, or the included Maven wrapper
- Windows, macOS, or Linux with JavaFX-compatible graphics support

This repository includes Maven wrapper scripts, so Maven does not need to be installed globally.

## How to Run

From the project root:

```powershell
.\mvnw.cmd javafx:run
```

On macOS or Linux:

```bash
./mvnw javafx:run
```

## How to Build the Final JAR

From the project root:

```powershell
.\mvnw.cmd -DskipTests package
```

The final JAR is created at:

```text
target/stickman-arena-1.0-SNAPSHOT-final.jar
```

Run it with:

```powershell
java -jar target\stickman-arena-1.0-SNAPSHOT-final.jar
```

## How Others Can Recreate This Game

1. Create a Maven JavaFX project.
2. Add JavaFX dependencies for `javafx-controls` and `javafx-media`.
3. Create a JavaFX `Application` class that opens a `Stage`, `Scene`, and `Canvas`.
4. Add an input handler that records pressed and released keys.
5. Build a reusable `GameObject` class with `x`, `y`, `width`, `height`, `vx`, and `vy`.
6. Create separate classes for the player, enemies, projectiles, platforms, particles, levels, sound, and scoring.
7. Use `AnimationTimer` as the main loop.
8. In each frame, update input, physics, enemies, projectiles, collisions, particles, level state, and score.
9. Render everything to the JavaFX canvas with `GraphicsContext`.
10. Add sound assets under `src/main/resources` and load them with `MediaPlayer`.
11. Package the game with Maven so it can be distributed as a JAR.

## Important Implementation Ideas

The player and enemies move through simple velocity-based physics. Gravity is applied every frame, then objects are moved according to their velocity. Platform collision is checked by comparing the object position before and after movement, then placing the object on top of the platform when it lands.

Combat uses rectangular hit areas. The melee attack creates a short hit zone in front of the player. Fireballs are `Projectile` objects with a fixed lifetime and damage value. When a projectile overlaps an enemy, the enemy takes damage and hit particles are spawned.

Enemy behavior is intentionally simple and readable. Each enemy patrols between two x positions, chases the player when close enough, attacks when in range, and can jump toward higher platforms when needed.

## Web Preview

The repository includes a self-contained `index.html` preview version for events and quick browser demos:

```text
index.html
```

Open it in a browser, click the preview, and the game starts immediately. This HTML version recreates the main JavaFX gameplay loop with Canvas and JavaScript so people can try the game without installing Java.
