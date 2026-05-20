package PaooGame.Entities;

import PaooGame.Game;

import java.awt.*;

public abstract class Enemy extends Creature {

    // Variabile pentru controlul timpului (AI)
    protected long lastAIUpdate;
    protected long aiInterval = 2000; // 2 secunde default pentru decizii
    protected int scoreValue = 10;

    public Enemy(Game game, float x, float y, int width, int height, int health, int attackDamage) {
        // Trimitem datele către Creature
        super(game, x, y, width, height, health, attackDamage);
        lastAIUpdate = System.currentTimeMillis();
    }
    public int getScoreValue() {
        return scoreValue;
    }
    public ObjectCoordinate getCoordinates() {
        return new ObjectCoordinate(x, y);
    }

    // Inamicii pot avea o bară de viață mică deasupra, opțional
    @Override
    public void Draw(Graphics g) {

        super.Draw(g); // Desenează animația
    }
}