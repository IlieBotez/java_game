package PaooGame.Entities;

import PaooGame.Entities.Objects.InteractableObjects.BladeTrap;
import PaooGame.Entities.Objects.InteractableObjects.StaticObjects;
import PaooGame.Entities.Objects.InteractableObjects.WineBarrel;
import PaooGame.Game;
import PaooGame.Graphics.Animation;
import PaooGame.Tiles.Tile;

import java.awt.*;
import java.util.HashMap;

public abstract class Creature extends Entity {
    // Statistici de bază
    protected float speed;
    protected int attackDamage;
    protected int health, maxHealth;
    // Stări comune
    protected boolean isHurt = false;
    protected boolean isDead = false;
    protected boolean isAttacking = false;

    // Sistemul de animații mutat aici
    protected HashMap<String, Animation> animations;
    protected String currentKey;
    protected String lastDirection = "DOWN";

    protected Rectangle attackRect;

    public Creature(Game game, float x, float y, int width, int height, int health, int attackDamage) {
        super(game, x, y, width, height);


        this.attackDamage = attackDamage; // Setăm puterea de atac
        this.health = health;
        this.maxHealth = health;
        this.attackRect = new Rectangle();
        animations = new HashMap<>();
    }

    // Metoda de damage este acum universală pentru orice creatură
    public void TakeDamage(int damage) {
        if (isDead || isHurt) return;

        health -= damage;
        isHurt = true;
        isAttacking = false;

        // Resetăm animația corespunzătoare pentru a vedea tresărirea de la început
        String hurtKey = "HURT_" + lastDirection;
        if (animations.containsKey(hurtKey)) {
            animations.get(hurtKey).Reset();
        }

        if (health <= 0) {
            health = 0;
            isDead = true;
            animations.get("DIE_" + lastDirection).Reset(); //

        }
    }

    public void drawHealthBar(Graphics g) {
        if (isDead || (health == maxHealth)) return;

        // Calculăm lățimea barei în funcție de procentul de viață
        float healthRatio = (float) health / maxHealth;
        int barWidth = 30;  // Lățimea totală a barei în pixeli
        int barHeight = 5;  // Înălțimea barei

        // Poziționăm bara deasupra capului (centrată orizontal)
        int barX = (int) x + (width / 2) - (barWidth / 2);
        int barY = (int) y - 10;

        // Fundalul barei (negru sau roșu închis)
        g.setColor(new Color(100, 0, 0));
        g.fillRect(barX, barY, barWidth, barHeight);

        // Viața actuală (verde)
        g.setColor(new Color(2, 145, 2));
        g.fillRect(barX, barY, (int) (barWidth * healthRatio), barHeight);

        // Contur (opțional, pentru aspect)
        g.setColor(Color.BLACK);
        g.drawRect(barX, barY, barWidth, barHeight);
    }

    @Override
    public void Draw(Graphics g) {
        // Verificăm dacă există o animație pentru cheia curentă pentru a evita erorile
        if (animations.containsKey(currentKey)) {
            // Desenăm cadrul curent la poziția x, y
            g.drawImage(animations.get(currentKey).GetCurrentFrame(), (int) x, (int) y, width, height, null);
        }
    }

    // Update va fi implementat diferit (tastatură vs AI), deci rămâne abstract
    public abstract void Update();

    protected boolean onStairs() {
        //verificam mijlocul părții de jos a personajului
        int centerX = (int) (x + 32);
        int footY = (int) (y + 64 - 20);

        int numLayers = game.getMap().getLayerCount();


        for (int i = 0; i < numLayers; i++) {
            Tile currentTile = game.getMap().GetTile(centerX, footY, i);

            if (currentTile != null && currentTile.IsStairs()) {
                return true;
            }
        }

        return false;
    }

    public boolean canMove(float nextX, float nextY) {

        //Facem un hitbox la piciorele personajului
        int hitboxWidth = 6;
        int hitboxHeight = 10;
        float offsetX = (64.0f - hitboxWidth)/2;
        float offsetY = 40;


        return !isSolid(nextX + offsetX, nextY + offsetY) &&                //  stânga sus
                !isSolid(nextX + offsetX + hitboxWidth, nextY + offsetY) && // dreapta sus
                !isSolid(nextX + offsetX, nextY + offsetY + hitboxHeight) && // stânga jos
                !isSolid(nextX + offsetX + hitboxWidth, nextY + offsetY + hitboxHeight); // dreapta jos
    }

    private boolean isSolid(float px, float py) {
        int tileX = (int)(px / Tile.TILE_WIDTH);
        int tileY = (int)(py / Tile.TILE_HEIGHT);
        float relX = px - (tileX * Tile.TILE_WIDTH);
        float relY = py - (tileY * Tile.TILE_HEIGHT);

        int numLayers = game.getMap().getLayerCount() - 1;
        for (int i = 0; i < numLayers; i++) {
            Tile t = game.getMap().GetTile((int)px, (int)py, i);
            if (t != null && t.IsSolidAt(relX, relY)) {
                return true;
            }
        }


        if (game.getLevelManager().getChest() != null && game.getLevelManager().getChest().getCollisionBounds().contains(px, py)) {
           return true;
        }


        if (game.getLevelManager().getGate() != null && !game.getLevelManager().getGate().getIsOpen() && game.getLevelManager().getGate().getCollisionBounds().contains(px, py)) {
            return true;
        }


        if (game.getLevelManager().getWine() != null) {
            for (WineBarrel barrel : game.getLevelManager().getWine()) {
                if (barrel.getCollisionBounds().contains(px, py)) {
                    return true;
                }
            }
        }
        if (game.getLevelManager().getStaticObjects() != null) {
            for (StaticObjects obj : game.getLevelManager().getStaticObjects()) {
                if (obj.getCollisionBounds().contains(px, py)) {
                    return true;
                }
            }
        }
        if (game.getLevelManager() != null && game.getLevelManager().getBladeTraps() != null) {
            for (Entity blade : game.getLevelManager().getBladeTraps()) {
                if (blade.getCollisionBounds(0, 0).contains(px, py)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isHurt() {
        return isHurt;
    }

    public HashMap<String, Animation> getAnimations() {
        return animations;
    }

    public String getLastDirection() {
        return lastDirection;
    }
    public void setDead(boolean dead) { this.isDead = dead; }
    public void setCurrentKey(String key) { this.currentKey = key; }
    public void setAttacking(boolean attacking) { this.isAttacking = attacking; }
    public void setHurt(boolean hurt) { this.isHurt = hurt; }
    public int getHealth(){ return health;}
    public int getMaxHealth(){ return  maxHealth;}
    public void setHealth(int health){ this.health = health;}
}

