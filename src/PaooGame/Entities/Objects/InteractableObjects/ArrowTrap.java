package PaooGame.Entities.Objects.InteractableObjects;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class ArrowTrap extends InteractiveEntity {
    private long lastFireTime;
    private long fireRate = 2000;
    private String shootAtDirection;
    private long maxArrowLife;
    private ArrayList<Arrow> arrows = new ArrayList<>();

    public ArrowTrap(Game game, int x, int y, String shootAtDirection, long maxArrowLife, long fireRate) {
        super(game, x, y, 48, 48, 0);
        this.shootAtDirection = shootAtDirection;
        this.lastFireTime = System.currentTimeMillis();
        this.maxArrowLife = maxArrowLife;
        this.fireRate = fireRate;
        if (shootAtDirection.equals("LEFT") || shootAtDirection.equals("RIGHT"))
        {
            this.width = 36;
            this.height = 36;

        }

    }

    @Override
    public void Update() {
        if (System.currentTimeMillis() - lastFireTime > fireRate) {
            arrows.add(new Arrow(x, y, shootAtDirection, maxArrowLife));
            lastFireTime = System.currentTimeMillis();
        }
        for (int i = 0; i < arrows.size(); i++) {
            Arrow a = arrows.get(i);
            a.Update(game);
            if (!a.isActive()) {
                arrows.remove(i);
                i--;
            }
        }
    }

    @Override
    public void Draw(java.awt.Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        switch (shootAtDirection){
            case "DOWN":
                g.drawImage(Assets.objectsMap.get("wallHoleUp"), (int)x, (int)y, width, height, null);
                break;
            case "LEFT":
                AffineTransform oldTransform = g2d.getTransform();
                g2d.translate(x + width / 2.0, y + height / 2.0);
                g2d.rotate(Math.toRadians(180));
                g.drawImage(Assets.objectsMap.get("wallHoleSides"), -width / 2, -height / 2, width, height, null);
                g2d.setTransform(oldTransform);
                break;
            case "RIGHT":
                g.drawImage(Assets.objectsMap.get("wallHoleSides"), (int)x, (int)y, width, height, null);
                break;
            default:
                break;


        }
        for (Arrow a : arrows) {
            a.Draw(g);
        }
    }

    @Override
    protected void onInteract(PaooGame.Entities.Player p) {}
}