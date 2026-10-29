package PaooGame.Entities;

import PaooGame.Game;

import java.awt.*;

public abstract class Entity {

    protected Game game;
    protected float x, y;          // Coordonatele
    protected int width, height; // Dimensiunea pe ecran (hitbox)
    protected int boundsX, boundsY, boundsWidth, boundsHeight;

    public Entity(Game game, float x, float y, int width, int height) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }

    public Rectangle getCollisionBounds(float offsetX, float offsetY) {
        // Returnează un dreptunghi care reprezintă corpul "real" al entității
        return new Rectangle((int) (x + boundsX + offsetX), (int) (y + boundsY + offsetY), boundsWidth, boundsHeight);
    }

    // Orice entitate trebuie să se poată da update și desena
    public abstract void Update();
    public abstract void Draw(Graphics g);

    // --- GETTERS ---
    // Utili pentru Cameră sau Coliziuni externe
    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    // --- SETTERS ---
    // Utili dacă vrei să teleportezi o entitate
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }


}