package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;

public class OrcBoss extends Orc{

    private long lastSummonTimer;
    private final long summonCooldown = 10000;

    private boolean isSummoning = false;
    private boolean hasSummonedThisCycle = false;

    public OrcBoss(Game game, float x, float y) {
        super(game, x, y, 20, 0);

        this.width = 128;
        this.height = 128;
        this.boundsX = 32;
        this.boundsY = 32;
        this.boundsWidth = 64;
        this.boundsHeight = 64;

        this.scoreValue = 200;

        currentKey = "IDLE_DOWN";

        loadAnimations(
                Assets.anims.get("ORC3_IDLE"),
                Assets.anims.get("ORC3_IDLE"),
                Assets.anims.get("ORC3_IDLE"),
                Assets.anims.get("ORC3_ATTACK"),
                Assets.anims.get("ORC3_ATTACK"),
                Assets.anims.get("ORC3_ATTACK"),
                Assets.anims.get("ORC3_HURT"),
                Assets.anims.get("ORC3_DIE")
        );

        lastSummonTimer = System.currentTimeMillis();
    }

    @Override
    public void Update() {

        if (isDead) { handleDeath(); return; }
        if (isHurt) { handleHurt(); return; }

        if (!isSummoning) {
            currentKey = "IDLE_DOWN";

            if (System.currentTimeMillis() - lastSummonTimer > summonCooldown) {
                isSummoning = true;
                hasSummonedThisCycle = false;
                animations.get("IDLE_ATTACK_DOWN").Reset();
            }
        } else {
            currentKey = "IDLE_ATTACK_DOWN";

            int frame = animations.get(currentKey).getCurrentFrameIndex();
            if (frame >= 4 && !hasSummonedThisCycle) {
                spawnSlimes();
                hasSummonedThisCycle = true;
            }

            if (animations.get(currentKey).IsFinished()) {
                isSummoning = false;
                lastSummonTimer = System.currentTimeMillis();
                currentKey = "IDLE_DOWN";
            }
        }

        if (animations.containsKey(currentKey)) {
            animations.get(currentKey).Update();
        }
    }

    private void spawnSlimes() {
        EnemyType[] slimes = {EnemyType.NATURE_SLIME, EnemyType.POISON_SLIME, EnemyType.FIRE_SLIME};

        for (EnemyType currentType: slimes) {
            boolean spawned = false;
            int attempts = 0;

            while (!spawned && attempts < 10) {
                float spawnX = this.x + (this.width / 2) + (random.nextBoolean() ? 1 : -1) * (50 + random.nextInt(80));
                float spawnY = this.y + (this.height / 2) + (random.nextBoolean() ? 1 : -1) * (50 + random.nextInt(80));

                Enemy slime = EnemyFactory.createEnemy(currentType, game, spawnX, spawnY);

                if (slime.canMove(spawnX, spawnY) && !game.isLocationOccupied(slime.getCollisionBounds(0, 0))) {
                    game.getEntityManager().addEnemy(slime);
                    spawned = true;
                }
                attempts++;
            }
        }
    }

    @Override
    protected void handleDeath() {
        super.handleDeath();

        if (animations.get(currentKey).IsFinished()) {
            game.setCurrentState(Game.GameState.WIN);
        }
    }

    @Override
    protected void checkAttacks(Player p) {
        // Rămâne gol, pentru că boss-ul nu atacă player-ul
    }
}
