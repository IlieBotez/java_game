package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Animation;
import PaooGame.Graphics.Assets;
import java.awt.*;
import java.util.Random;

public class NPC extends Creature {
    private Random rand;
    private long lastInteractTime = 0;
    private final long interactCooldown = 1000;

    public NPC(Game game, float x, float y) {
        super(game, x, y, 64, 64, 10000, 0);
        this.rand = new Random();

        this.boundsX = 16;
        this.boundsY = 16;
        this.boundsWidth = 32;
        this.boundsHeight = 32;

        if (Assets.anims.containsKey("NPC_IDLE")) {
            animations.put("IDLE", new Animation(250, Assets.anims.get("NPC_IDLE")[0]));
        }
        currentKey = "IDLE";
    }

    @Override
    public void Update() {
        if (animations.containsKey(currentKey)) {
            animations.get(currentKey).Update();
        }

        if (game.GetKeyManager().interact) {
            checkInteraction();
        }
    }

    private void checkInteraction() {
        Player p = game.getPlayer();
        if (p == null) return;

        if (System.currentTimeMillis() - lastInteractTime < interactCooldown) {
            return;
        }

        Rectangle pBounds = p.getCollisionBounds(0, 0);
        Rectangle npcBounds = getCollisionBounds(0, 0);

        Rectangle interactZone = new Rectangle(
                npcBounds.x - 40,
                npcBounds.y - 40,
                npcBounds.width + 80,
                npcBounds.height + 80
        );

        if (interactZone.intersects(pBounds)) {
            triggerGamble(p);
            lastInteractTime = System.currentTimeMillis();
        }
    }

    private void triggerGamble(Player p) {
        boolean giveHealth = rand.nextBoolean();

        if (giveHealth) {
            if (p.getHealth() < p.getMaxHealth()) {
                p.setHealth(p.getHealth() + 1);
                System.out.println("You won 1HP! Your new health: " + p.getHealth());
            } else {
                System.out.println("NPC wanted to give you life, but you are already at full HP!");
            }
        } else {
            p.TakeDamage(1);
            System.out.println("Unlucky! You lost 1 HP! Your new health: " + p.getHealth());
        }
    }

    @Override
    public void Draw(Graphics g) {
        if (animations.containsKey(currentKey)) {
            g.drawImage(animations.get(currentKey).GetCurrentFrame(), (int) x, (int) y, width, height, null);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        int textX = (int) x + (width / 2) - 10;
        int textY = (int) y + 10;
        g.drawString("[E]", textX, textY);
    }
}