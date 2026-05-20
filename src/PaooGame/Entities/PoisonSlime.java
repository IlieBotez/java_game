package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;

public class PoisonSlime extends Slime {

    public PoisonSlime(Game game, float x, float y) {
        super(game, x, y, 5, 1);

        this.boundsX = 16; this.boundsY = 24;
        this.boundsWidth = 32; this.boundsHeight = 16;
        this.patrolSpeed = 0.4f; this.chaseSpeed = 1.0f;

        currentKey = "IDLE_DOWN";

        loadAnimations(
                Assets.anims.get("SLIME2_WALK"),
                Assets.anims.get("SLIME2_RUN"),
                Assets.anims.get("SLIME2_IDLE"),
                Assets.anims.get("SLIME2_ATTACK"),
                Assets.anims.get("SLIME2_HURT"),
                Assets.anims.get("SLIME2_DIE")
        );
    }

    @Override
    protected void applyAttackEffect(Player p) {
        super.applyAttackEffect(p);
        p.applyPoison(120);
    }
}