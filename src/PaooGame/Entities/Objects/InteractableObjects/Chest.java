package PaooGame.Entities.Objects.InteractableObjects;

import PaooGame.Entities.Player;
import PaooGame.Game;
import PaooGame.Graphics.Assets;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Chest extends InteractiveEntity {
    private int state = 0;

    public Chest(Game game, int x, int y) {
        super(game, x, y, 48, 48, 10);
    }

    @Override
    protected void onInteract(Player p) {
        if (state == 0) {
            state = 1;
        } else if (state == 1) {
            state = 2;
            p.hasKey = true;
        }
    }

    @Override
    public Rectangle getCollisionBounds() {
        return new Rectangle((int) x+10, (int) y+10,  width-20, height-20);
    }

    @Override
    public void Draw(Graphics g) {
        BufferedImage chestImg = (state == 0) ? Assets.objectsMap.get("chestClosed") : Assets.objectsMap.get("chestOpen");
        if (chestImg != null) {
            g.drawImage(chestImg, (int) x, (int) y, width, height, null);
        }

        if (state == 1 && Assets.objectsMap.get("key") != null) {
            g.drawImage(Assets.objectsMap.get("key"), (int) x + 8, (int) y - 7, 32, 32, null);
        }

        if (isPlayerNear && state < 2) {
            String mesaj = (state == 0) ? "[E] Open" : "[E] take the key";
            g.setColor(Color.BLACK);
            g.drawString(mesaj, (int) x - 3, (int) y - 10);
            g.setColor(Color.WHITE);
            g.drawString(mesaj, (int) x - 5, (int) y - 11);
        }
    }

    public int getState(){return state;}
    public void setState(int state) { this.state = state; }

}