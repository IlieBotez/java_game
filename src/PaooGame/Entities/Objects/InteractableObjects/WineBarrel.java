package PaooGame.Entities.Objects.InteractableObjects;

import PaooGame.Entities.Player;
import PaooGame.Game;
import PaooGame.Graphics.Assets;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class WineBarrel extends InteractiveEntity {
    private int state = 0;

    public WineBarrel(Game game, int x, int y) {
        super(game, x, y, 30, 60, 10);
    }

    @Override
    protected void onInteract(Player player) {
        if (state == 0 && player.getHealth() < player.getMaxHealth()) {
            state = 1;
            System.out.println("You have drunk the wine");
            player.setHealth(player.getHealth()+1);
            player.startDrunk(600);
        }
    }
    @Override
    public Rectangle getCollisionBounds() {
        return new Rectangle((int) x, (int) y + height / 2, width, height / 2);
    }

    @Override
    public void Draw(Graphics g) {
        BufferedImage wineBarrelImg = (state == 0) ? Assets.objectsMap.get("unusedWineBarrel") : Assets.objectsMap.get("usedWineBarrel");
        if (wineBarrelImg != null) {
            g.drawImage(wineBarrelImg, (int) x, (int) y, width, height, null);
        }

        if (isPlayerNear && state < 2) {
            String mesaj = (state == 0) ? "[E] Drink some wine" : "You've already drunk the wine!";
            g.setColor(Color.BLACK);
            g.drawString(mesaj, (int) x - 3, (int) y - 10);
            g.setColor(Color.WHITE);
            g.drawString(mesaj, (int) x - 5, (int) y - 11);
        }
    }
    public void setState(int state) { this.state = state; }
    public int getState(){return state;}

}