package PaooGame.Entities.Objects.InteractableObjects;

import PaooGame.Entities.Player;
import PaooGame.Game;
import PaooGame.Graphics.Assets;
import java.awt.*;
import java.awt.image.BufferedImage;
import PaooGame.LevelManager;

public class Gate extends InteractiveEntity {
    private boolean isOpen = false;

    public Gate(Game game, int x, int y, boolean isOpen) {
        super(game, x, y, 96, 96, 20);
        this.isOpen = isOpen;
    }

    @Override
    protected void onInteract(Player p) {
        if (!isOpen) {
            if (p.hasKey) {
                isOpen = true;
                p.hasKey = false;
            }
        }
        else
        {
            if (!p.hasKey)
            {
                game.nextLevel();
            }
        }
    }

    @Override
    public void Draw(Graphics g) {

        BufferedImage gateImg;
        String mesaj = "";
        gateImg = isOpen ? Assets.objectsMap.get("openedGate") : Assets.objectsMap.get("closedGate");

        if (gateImg != null) {
            g.drawImage(gateImg, (int) x, (int) y, width, height, null);
        }
        if (isPlayerNear && !isOpen) {
            if (game.getPlayer().hasKey)
                mesaj = "[E] Open the gate";

            g.setColor(Color.BLACK);
            g.drawString(mesaj, (int) x - 20, (int) y - 10);
            g.setColor(Color.WHITE);
            g.drawString(mesaj, (int) x - 21, (int) y - 11);
        }
    }

    public boolean getIsOpen(){return isOpen;}
    public void setOpen(boolean open) { this.isOpen= open; }
}