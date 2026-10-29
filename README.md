# 🗡️ Arthur - The story of a knight

A 2D action-adventure game developed from scratch in Java, featuring classic RPG elements. Explore dungeons, avoid traps, fight hordes of orcs, and face the Orc King in an epic boss battle!

## ✨ Main Features
* **Three distinct levels:** 
  * *Level 1:* Introduction to enemies and exploration.
  * *Level 2:* The Ice Chamber (custom sliding mechanic) filled with deadly traps (spikes, blades, arrows).
  * *Level 3:* Boss Fight (The Orc King's Arena).
* **Advanced Combat Mechanics:** Enemies (Slimes) can apply status effects to the player (Poison / Slow), and fire enemies explode upon death.
* **Save / Load System:** Progress (health, score, time, remaining enemies, gate and chest states) is persistently saved using a `.properties` file.
* **SQLite Database:** A *Leaderboard* system that saves players' scores, achieved times, and names.
* **Visual Systems:** Fog of War (hides the unexplored map), UI effects (HUD) for altered states (e.g., Drunk).

## 🎮 Controls
* **W, A, S, D / Arrows:** Character movement
* **SPACE:** Sword attack
* **ESC:** Pause / Main Menu
* *Menus can be navigated using the mouse.*

## 🛠️ Prerequisites and Installation
To run this project, you will need:
* **Java Development Kit (JDK):** Version 8 or newer.

### How to run the game:
1. Clone or download this repository.
2. Open the project in your preferred IDE (IntelliJ IDEA, Eclipse, etc.).
3. Ensure the `res` folder (textures, sounds) is marked as a *Resources Root*.
4. Run the main class `Main.java`.

## 🏗️ Architecture and Code (Technical Details)
The project utilizes solid Object-Oriented Programming (OOP) principles:
* **Inheritance and Polymorphism:** Entities inherit from the base `Creature` -> `Enemy` class, allowing for the easy creation of varied enemies (`OrcBoss`, `FireSlime`).
* **Factory Pattern:** Enemies are generated through an `EnemyFactory` to centralize and simplify spawning logic.
* **State Machine:** The game flow (Menu, Playing, Pause, Win, Leaderboard) is managed through a *Game States* system.

## 🎨 Credits and Resources
* Textures and animations: https://itch.io/game-assets/tag-sprite-sheet and https://craftpix.net/categorys/sprites/
* Developers: Botez Ilie & Sava Teofan