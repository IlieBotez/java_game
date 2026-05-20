package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;

public class OrcBrute extends Orc{

    public OrcBrute(Game game, float x, float y) {
        // Constructor: game, x, y, width, height, health (8 pt Orc2)
        super(game, x, y, 6, 2);

        this.boundsX = 16; this.boundsY = 10;
        this.boundsWidth = 32; this.boundsHeight = 32;
        this.patrolSpeed = 0.8f; this.chaseSpeed = 1.5f;

        currentKey = "IDLE_DOWN";

        loadAnimations(
                Assets.anims.get("ORC2_WALK"),
                Assets.anims.get("ORC2_RUN"),
                Assets.anims.get("ORC2_IDLE"),
                Assets.anims.get("ORC2_ATTACK"),
                Assets.anims.get("ORC2_WALK_ATTACK"),
                Assets.anims.get("ORC2_RUN_ATTACK"),
                Assets.anims.get("ORC2_HURT"),
                Assets.anims.get("ORC2_DIE")
        );
    }
}
