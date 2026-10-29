package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;

import java.awt.Rectangle;

public class FireSlime extends Slime {

    private boolean hasExploded = false;

    public FireSlime(Game game, float x, float y) {
        super(game, x, y, 5, 1);

        this.boundsX = 16; this.boundsY = 24;
        this.boundsWidth = 32; this.boundsHeight = 16;
        this.patrolSpeed = 0.6f; this.chaseSpeed = 1.2f;

        currentKey = "IDLE_DOWN";

        loadAnimations(
                Assets.anims.get("SLIME3_WALK"),
                Assets.anims.get("SLIME3_RUN"),
                Assets.anims.get("SLIME3_IDLE"),
                Assets.anims.get("SLIME3_ATTACK"),
                Assets.anims.get("SLIME3_HURT"),
                Assets.anims.get("SLIME3_DIE")
        );
    }

    @Override
    protected void applyAttackEffect(Player p) {
        super.applyAttackEffect(p);
    }

    @Override
    protected void handleDeath() {
        super.handleDeath();

        if (animations.get(currentKey).IsFinished() && !hasExploded) {
            triggerExplosion();
            hasExploded = true;
        }
    }

    private void triggerExplosion() {
        Player p = game.getEntityManager().getPlayer();
        if (p == null) return;

        int explosionRadius = 40;

        Rectangle explosionArea = new Rectangle(
                (int) x + boundsX - explosionRadius,
                (int) y + boundsY - explosionRadius,
                boundsWidth + explosionRadius * 2,
                boundsHeight + explosionRadius * 2
        );

        if (explosionArea.intersects(p.getCollisionBounds(0, 0))) {
            p.TakeDamage(1);
        }
    }
}