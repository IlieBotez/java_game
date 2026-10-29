package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;

public class OrcWarrior extends Orc {

    public OrcWarrior(Game game, float x, float y) {
        // Constructor: game, x, y, width, height, health (10 pt Orc1)
        super(game, x, y, 10, 1);

        this.boundsX = 16; this.boundsY = 10;
        this.boundsWidth = 32; this.boundsHeight = 32;
        this.patrolSpeed = 0.5f; this.chaseSpeed = 1.0f;

        currentKey = "IDLE_DOWN";

        loadAnimations(
                Assets.anims.get("ORC1_WALK"),
                Assets.anims.get("ORC1_RUN"),
                Assets.anims.get("ORC1_IDLE"),
                Assets.anims.get("ORC1_ATTACK"),
                Assets.anims.get("ORC1_WALK_ATTACK"),
                Assets.anims.get("ORC1_RUN_ATTACK"),
                Assets.anims.get("ORC1_HURT"),
                Assets.anims.get("ORC1_DIE")
        );
    }
}
