package PaooGame.Entities;

import java.awt.Graphics;
import java.util.ArrayList;

public class EntityManager {
    private Player player;
    private ArrayList<Enemy> enemies;

    public EntityManager(Player player) {
        this.player = player;
        this.enemies = new ArrayList<>();
    }

    public void addEnemy(Enemy e) {
        enemies.add(e);
    }

    public void Update() {
        // Update jucător
        player.Update();

        // Update inamici
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.Update();
            // ștergem inamicul DOAR dacă e mort ȘI animația lui s-a terminat
            if (e.isDead()) {
                String dieKey = "DIE_" + e.getLastDirection();
                if (e.getAnimations().get(dieKey).IsFinished()) {
                    enemies.remove(i);
                    i--;
                }
            }
        }
    }

    public void Draw(Graphics g) {
        // Desenăm inamicii
        for (Enemy e : enemies) {
            e.Draw(g);
        }

        for (Enemy e : enemies) {
            if (e.health < e.maxHealth && e.health > 0) {
                e.drawHealthBar(g);
            }
        }

        // Desenăm jucătorul deasupra inamicilor (sau invers, depinde de logică)
        player.Draw(g);

    }

    // Getters pentru coliziuni
    public Player getPlayer() { return player; }
    public ArrayList<Enemy> getEnemies() { return enemies; }
}