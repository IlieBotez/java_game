package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;

public class NatureSlime extends Slime {

    public NatureSlime(Game game, float x, float y) {
        super(game, x, y, 4, 2);

        this.boundsX = 16; this.boundsY = 24;
        this.boundsWidth = 32; this.boundsHeight = 16;
        this.patrolSpeed = 0.5f; this.chaseSpeed = 0.9f;

        currentKey = "IDLE_DOWN";

        loadAnimations(
                Assets.anims.get("SLIME1_WALK"),
                Assets.anims.get("SLIME1_RUN"),
                Assets.anims.get("SLIME1_IDLE"),
                Assets.anims.get("SLIME1_ATTACK"),
                Assets.anims.get("SLIME1_HURT"),
                Assets.anims.get("SLIME1_DIE")
        );
    }

    @Override
    protected void applyAttackEffect(Player p) {
        super.applyAttackEffect(p);
        p.applySlow(300);
    }
}