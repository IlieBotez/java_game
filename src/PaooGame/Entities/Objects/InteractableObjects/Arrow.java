package PaooGame.Entities.Objects.InteractableObjects;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import java.awt.*;

public class Arrow {
    private float x, y, speed;
    private int width, height;
    private String direction;
    private float alpha = 1.0f;
    private long startTime;
    private long maxLife = 2000;
    private boolean active = true;

    public Arrow(float x, float y, String direction, long maxLife) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = 7.0f;
        this.width = 32;
        this.height = 32;
        this.maxLife = maxLife;
        this.startTime = System.currentTimeMillis();
    }

    public void Update(Game game) {
        if (!active) return;

        if (direction.equals("DOWN")) y += speed;
        else if (direction.equals("LEFT")) x -= speed;
        else if (direction.equals("RIGHT")) x += speed;
        else if (direction.equals("UP")) y -= speed;


        float tailX = x, tailY = y;

        if (direction.equals("DOWN")) { tailX = x + width / 2; tailY = y; }
        else if (direction.equals("LEFT")) { tailX = x + width; tailY = y + height / 2; }
        else if (direction.equals("RIGHT")) { tailX = x; tailY = y + height / 2; }
        else if (direction.equals("UP")) { tailX = x + width / 2; tailY = y + height; }

        long elapsed = System.currentTimeMillis() - startTime;

        if (elapsed > 200 && isWallAt(game, tailX, tailY)) {
            active = false;
        }
        if (elapsed > maxLife * 0.8f) {
            alpha = 1.0f - ((float)(elapsed - (maxLife * 0.8f)) / (maxLife * 0.2f));
            if (alpha < 0) alpha = 0;
        }
        if (elapsed >= maxLife) active = false;

        Rectangle arrowBounds = new Rectangle((int)x + 10, (int)y + 10, 12, 12);
        if (game.getPlayer().getCollisionBounds(0, 0).intersects(arrowBounds)) {
            game.getPlayer().TakeDamage(1);
            active = false;
        }
    }

    private boolean isWallAt(Game game, float px, float py) {
        for (int i = 0; i < 2; i++) {
            PaooGame.Tiles.Tile t = game.getMap().GetTile((int)px, (int)py, i);
            if (t != null && t.IsSolid()) {
                return true;
            }
        }
        return false;
    }

    public void Draw(Graphics g) {
        if (!active) return;
        Graphics2D g2d = (Graphics2D) g;
        Composite oldComposite = g2d.getComposite();
        java.awt.geom.AffineTransform oldTransform = g2d.getTransform();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.translate(x + width / 2.0, y + height / 2.0);

        //rotate the arrow
        double angle = 0;
        if (direction.equals("LEFT"))
            angle = Math.toRadians(90);
        else if (direction.equals("RIGHT"))
            angle = Math.toRadians(-90);
        else if (direction.equals("UP"))
            angle = Math.toRadians(180);
        g2d.rotate(angle);
        g.drawImage(Assets.objectsMap.get("arrow"), -width / 2, -height / 2, width, height, null);

        g2d.setTransform(oldTransform);
        g2d.setComposite(oldComposite);
    }

    public boolean isActive() { return active; }
}