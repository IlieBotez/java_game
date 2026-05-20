package PaooGame.Entities.Objects.InteractableObjects;

import PaooGame.Entities.Player;
import PaooGame.Game;
import PaooGame.Graphics.Animation;
import PaooGame.Graphics.Assets;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class BladeTrap extends InteractiveEntity {
    private Animation anim;

    private Shape rotatedBlade1;
    private Shape rotatedBlade2;

    public BladeTrap(Game game, int x, int y) {
        super(game, x, y, 144, 144, 0);

        this.boundsX =65;
        this.boundsY = 60;
        this.boundsWidth = 15;
        this.boundsHeight = 65;

        BufferedImage[][] rawFrames = Assets.trapsAnim.get("bladeTrap");
        BufferedImage[] frames = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = rawFrames[i][0];
        }
        anim = new Animation(500, frames);
    }

    @Override
    public void Update() {
        anim.Update();

        double angle = anim.getCurrentFrameIndex() * (-22.5);

        // blade parameters
        int bladeLength = 100;
        int bladeThickness = 2;

        // center the blades
        int lengthOffset = (width - bladeLength) / 2;
        int thicknessOffset = (height - bladeThickness) / 2;

        // horizontal blade
        Rectangle blade1 = new Rectangle((int)x + lengthOffset, (int)y + thicknessOffset, bladeLength, bladeThickness);

        // vertical blade
        Rectangle blade2 = new Rectangle((int)x + thicknessOffset, (int)y + lengthOffset, bladeThickness, bladeLength);

        AffineTransform at = AffineTransform.getRotateInstance(
                Math.toRadians(angle),
                x + width/2.0,
                y + height/2.0
        );

        rotatedBlade1 = at.createTransformedShape(blade1);
        rotatedBlade2 = at.createTransformedShape(blade2);

        Player p = game.getPlayer();
        if (p != null) {
            Rectangle playerHitbox = p.getCollisionBounds(0, 0);
            if (rotatedBlade1.intersects(playerHitbox) || rotatedBlade2.intersects(playerHitbox)) {
                p.TakeDamage(1);
            }
        }
    }

    @Override
    public void Draw(Graphics g) {
        // blade animation
        g.drawImage(anim.GetCurrentFrame(), (int) x, (int) y, width, height, null);
        Graphics2D g2d = (Graphics2D) g;

    }

    @Override
    protected void onInteract(Player p) {}
}