package PaooGame.Entities.Objects.InteractableObjects;

import PaooGame.Entities.Entity;
import PaooGame.Entities.Player;
import PaooGame.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class StaticObjects extends Entity {

    protected BufferedImage image;

    public StaticObjects(Game game, float x, float y, int width, int height, BufferedImage image) {
        super(game, x,y,width,height);
        this.image = image;
    }


    public Rectangle getCollisionBounds() {
        int hitboxHeight = height / 6;
        int offsetY = height - hitboxHeight;

        return new Rectangle((int) x, (int) (y + offsetY - 10 ), width, hitboxHeight-10);
    }

    public  void Update(){

    }

    public void Draw(Graphics g)
    {
        g.drawImage(image, (int) x,(int) y,width, height, null);
    }
}
