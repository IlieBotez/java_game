package PaooGame.Entities.Objects.InteractableObjects;

import PaooGame.Entities.Entity;
import PaooGame.Entities.Player;
import PaooGame.Game;

import java.awt.*;

public abstract class InteractiveEntity extends Entity {

    protected Rectangle interactBounds;
    protected boolean isPlayerNear = false;
    private boolean eWasPressed = false;


    public InteractiveEntity(Game game, int x, int y, int width, int height, int interactRadius) {
        super(game, x,y,width,height);
        this.interactBounds = new Rectangle(x - interactRadius, y - interactRadius, width + interactRadius * 2, height + interactRadius * 2);
    }


    public Rectangle getCollisionBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
    public void Update() {
        Player p = game.getPlayer();
        if (p == null) return;

        if (interactBounds.intersects(p.getCollisionBounds(0, 0))) {
            isPlayerNear = true;
            boolean eIsPressed = game.GetKeyManager().interact;

            if (eIsPressed && !eWasPressed) {
                onInteract(p);
            }
            eWasPressed = eIsPressed;
        } else {
            isPlayerNear = false;
            eWasPressed = false;
        }
    }

    protected abstract void onInteract(Player p);
    public abstract void Draw(Graphics g);
}
