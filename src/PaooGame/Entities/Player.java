package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Animation;
import PaooGame.Graphics.Assets;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import java.awt.*;

public class Player extends Creature {

    private int walkSpeed = 2;        // Viteze de mers
    private int runSpeed = 5;         // și alergat

    private float velX = 0; // Viteza curentă pe orizontală
    private float velY = 0; // Viteza curentă pe verticală

    private final int REACH_UP = 22;
    private final int REACH_DOWN = 25;
    private final int REACH_SIDE = 24;

    private final int SWING_WIDTH_UP = 50;
    private final int SWING_WIDTH_DOWN = 35;
    private final int SWING_HEIGHT_SIDE = 40;

    private long lastAttackTimer = 0;
    private final long attackCooldown = 500;

    private long lastHurtTimer = 0;
    private final long invincibilityCooldown = 1000; // 1 secundă invincibilitate după ce ia damage

    private boolean hasDealtDamage = false; // Flag pentru a preveni multi-hit

    private int drunkTimer = 0;
    private int poisonTimer = 0;
    private int poisonTickCounter = 0;
    private int slowTimer = 0;
    private boolean isDrunk = false;
    private boolean isPoisoned = false;
    private boolean isSlowed = false;
    public boolean hasKey = false;
    private int score = 0;

    public Player(Game game, float x, float y) {

        super(game, x, y, 64, 64, 10, 2);

        this.boundsX = 25;
        this.boundsY = 25;
        this.boundsWidth = 15;
        this.boundsHeight = 18;
        this.speed = walkSpeed;

        initAnimations();
        currentKey = "IDLE_DOWN"; // Cheia de start
    }

    private void initAnimations() {
        // Ordinea din Assets este: 0=DOWN, 1=LEFT, 2=RIGHT, 3=UP
        String[] dirs = {"DOWN", "LEFT", "RIGHT", "UP"};

        for (int i = 0; i < 4; i++) {
            animations.put("WALK_" + dirs[i], new Animation(100, Assets.anims.get("PLAYER_WALK")[i]));
            animations.put("RUN_" + dirs[i], new Animation(70, Assets.anims.get("PLAYER_RUN")[i]));
            animations.put("IDLE_" + dirs[i], new Animation(200, Assets.anims.get("PLAYER_IDLE")[i]));

            animations.put("IDLE_ATTACK_" + dirs[i], new Animation(80, Assets.anims.get("PLAYER_ATTACK")[i]));
            animations.put("WALK_ATTACK_" + dirs[i], new Animation(80, Assets.anims.get("PLAYER_WALK_ATTACK")[i]));
            animations.put("RUN_ATTACK_" + dirs[i], new Animation(80, Assets.anims.get("PLAYER_RUN_ATTACK")[i]));

            animations.put("HURT_" + dirs[i], new Animation(100, Assets.anims.get("PLAYER_HURT")[i]));
            animations.put("DIE_" + dirs[i], new Animation(100, Assets.anims.get("PLAYER_DIE")[i]));
        }
    }

    public void Update() {

        if (isDead) {
            currentKey = "DIE_" + lastDirection;
            if (!animations.get(currentKey).IsFinished()) {
                animations.get(currentKey).Update();
            }
            return;
        }

        checkEnemyContact(); // Verificăm dacă ar călca pe vreun inamic

        boolean moved = handleMovement();
        String baseState = game.GetKeyManager().run ? "RUN" : "WALK";

        if (!moved) {
            baseState = "IDLE";
        }

        handleAttacking(baseState);
        updateAnimationState(baseState);

        animations.get(currentKey).Update();


        if (drunkTimer > 0) {
            drunkTimer--;
            if (drunkTimer == 0) {
                setDrunkness();
            }
        }

        if (slowTimer > 0) {
            slowTimer--;
            if (slowTimer == 0) {
                isSlowed = false;
            }
        }

        if (poisonTimer > 0) {
            poisonTimer--;
            poisonTickCounter++;

            if (poisonTickCounter >= 60) {
                TakeDamage(1);
                poisonTickCounter = 0;
            }

            if (poisonTimer <= 0) {
                isPoisoned = false;
                poisonTimer = 0;
                poisonTickCounter = 0;
            }
        }
    }

    private void checkEnemyContact() {
        // Dacă jucătorul este în perioada de invulnerabilitate, ignorăm coliziunea
        if (System.currentTimeMillis() - lastHurtTimer < invincibilityCooldown) {
            return;
        }

        Rectangle playerHitbox = getCollisionBounds(0, 0);

        for (Enemy e : game.getEntityManager().getEnemies()) {
            if (e.isDead() || e instanceof OrcBoss) continue;

            Rectangle enemyFootHitbox = e.getCollisionBounds(0, 0);
            int footHeight = 20; // Dimensiunea zonei picioarelor în pixeli

            enemyFootHitbox.y = enemyFootHitbox.y + enemyFootHitbox.height - footHeight;
            enemyFootHitbox.height = footHeight;

            if (playerHitbox.intersects(enemyFootHitbox)) {
                TakeDamage(1);
                lastHurtTimer = System.currentTimeMillis();
                break;
            }
        }
    }

    private boolean handleMovement() {

        float oldX = x;
        float oldY = y;
        float targetSpeed = game.GetKeyManager().run ? runSpeed : walkSpeed;

        if (isSlowed) { targetSpeed *= 0.6f; }
        if (onStairs()) { targetSpeed *= 0.75f; }

        boolean isIceLevel = (game.getLevelManager().getCurrentLevelIndex() == 1);

        float friction = isIceLevel ? 0.95f : 0.5f;
        float acceleration = isIceLevel ? 0.15f : 1.0f;

        if (isDrunk) {
            acceleration = -acceleration;
        }

        if (game.GetKeyManager().up) {
            velY -= acceleration;
            lastDirection = "UP";
        }
        if (game.GetKeyManager().down) {
            velY += acceleration;
            lastDirection = "DOWN";
        }
        if (game.GetKeyManager().left) {
            velX -= acceleration;
            lastDirection = "LEFT";
        }
        if (game.GetKeyManager().right) {
            velX += acceleration;
            lastDirection = "RIGHT";
        }

        if (!game.GetKeyManager().up && !game.GetKeyManager().down) {
            velY *= friction;
        }
        if (!game.GetKeyManager().left && !game.GetKeyManager().right) {
            velX *= friction;
        }

        if (velX > targetSpeed) velX = targetSpeed;
        if (velX < -targetSpeed) velX = -targetSpeed;
        if (velY > targetSpeed) velY = targetSpeed;
        if (velY < -targetSpeed) velY = -targetSpeed;

        if (Math.abs(velX) < 0.1f) velX = 0;
        if (Math.abs(velY) < 0.1f) velY = 0;

        float nextX = x + velX;
        float nextY = y + velY;

        if (canMove(nextX, y)) {
            x = nextX;
        } else {
            velX = 0;
        }

        if (canMove(x, nextY)) {
            y = nextY;
        } else {
            velY = 0;
        }

        return (x != oldX || y != oldY);
    }

    private void handleAttacking(String baseState) {
        if (game.GetKeyManager().attack && !isAttacking && !isHurt) {
            if (System.currentTimeMillis() - lastAttackTimer >= attackCooldown) {
                isAttacking = true;
                hasDealtDamage = false;
                lastAttackTimer = System.currentTimeMillis();
                animations.get(baseState + "_ATTACK_" + lastDirection).Reset();
            }
        }

        if (isAttacking) {
            checkAttacks();
        }
    }

    private void checkAttacks() {

        // Dacă am lovit deja în acest swing, nu mai verificăm restul cadrelor
        if (hasDealtDamage) return;

        Animation anim = animations.get(currentKey);
        if (anim == null) return;

        int totalFrames = anim.getFrameCount();
        int frame = anim.getCurrentFrameIndex();

        boolean isImpactFrame = (totalFrames == 8) ? (frame >= 3 && frame <= 5) : (frame >= 2 && frame <= 4);
        if (!isImpactFrame) return;

        // Calculăm Hitbox-ul de Atac
        Rectangle body = getCollisionBounds(0, 0);
        int overlap = 15;

        switch (lastDirection) {
            case "UP":
                attackRect.setBounds(
                        body.x + body.width / 2 - SWING_WIDTH_UP / 2, // Centrat pe lățimea specifică UP
                        body.y - REACH_UP + overlap,
                        SWING_WIDTH_UP,
                        REACH_UP
                );
                break;
            case "DOWN":
                attackRect.setBounds(
                        body.x + body.width / 2 - SWING_WIDTH_DOWN / 2, // Centrat pe lățimea specifică DOWN
                        body.y + body.height - overlap,
                        SWING_WIDTH_DOWN,
                        REACH_DOWN
                );
                break;
            case "LEFT":
                attackRect.setBounds(
                        body.x - REACH_SIDE + overlap,
                        body.y + body.height / 2 - SWING_HEIGHT_SIDE / 2, // Centrat pe înălțimea specifică SIDE
                        REACH_SIDE,
                        SWING_HEIGHT_SIDE
                );
                break;
            case "RIGHT":
                attackRect.setBounds(
                        body.x + body.width - overlap,
                        body.y + body.height / 2 - SWING_HEIGHT_SIDE / 2,
                        REACH_SIDE,
                        SWING_HEIGHT_SIDE
                );
                break;
        }

        // Verificăm coliziunea cu inamicii
        for (Enemy e : game.getEntityManager().getEnemies()) {
            if (attackRect.intersects(e.getCollisionBounds(0, 0))) {
                boolean wasDeadBefore = e.isDead();

                e.TakeDamage(this.attackDamage);

                if (!wasDeadBefore && e.isDead()) {
                    addScore(e.getScoreValue()); //
                }
                hasDealtDamage = true; // „Încuiem” atacul până la următoarea apăsare de SPACE
                break; // Oprim bucla dacă vrem să lovim un singur inamic per swing
            }
        }
    }

    private void updateAnimationState(String baseState) {
        if (isHurt) {
            currentKey = "HURT_" + lastDirection;
            if (animations.get(currentKey).IsFinished()) isHurt = false;
        } else if (isAttacking) {
            currentKey = baseState + "_ATTACK_" + lastDirection;
            if (animations.get(currentKey).IsFinished()) isAttacking = false;
        } else {
            currentKey = baseState + "_" + lastDirection;
        }
    }

    public void addScore(int value) {
        score += value;
    }


    public void startDrunk(int ticks) {
        if (!isDrunk) {
            isDrunk = true;
        }
        drunkTimer = ticks;
    }

    public void setDrunkness() {
        isDrunk = !isDrunk;
    }

    private void renderEffect(Graphics g, int x, int y, int size, int currentTimer, float maxTicks, Color tintColor) {

        float progress = currentTimer / maxTicks;
        int lineX = x + (int) (size * progress);

        g.setColor(new Color(255, 255, 255, 200));
        g.drawLine(lineX, y, lineX, y + size);

        g.setColor(tintColor);
        g.fillRect(lineX, y, (x + size) - lineX, size);
    }

    private void drawShadowedString(Graphics g, String text, int x, int y, Color textColor) {
        g.setColor(Color.BLACK);
        g.drawString(text, x + 2, y + 2);
        g.setColor(textColor);
        g.drawString(text, x, y);
    }


    public void drawHUD(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int hudX = 20, hudY = 20;

        int panelWidth = 200;
        int panelHeight = 150;

        g.setColor(new Color(0, 0, 0, 160));
        g.fillRoundRect(hudX - 10, hudY - 10, panelWidth, panelHeight, 15, 15);

        g.setColor(new Color(255, 255, 255, 80));
        g.drawRoundRect(hudX - 10, hudY - 10, panelWidth, panelHeight, 15, 15);

        int bw = 160, bh = 18;
        float healthRatio = (float) health / maxHealth;

        g.setColor(new Color(60, 0, 0));
        g.fillRect(hudX, hudY, bw, bh);
        g.setColor(new Color(0, 200, 50));
        g.fillRect(hudX, hudY, (int) (bw * healthRatio), bh);
        g.setColor(Color.WHITE);
        g.drawRect(hudX, hudY, bw, bh);

        g.setFont(new Font("Arial", Font.BOLD, 11));
        g.drawString(health + " / " + maxHealth, hudX + bw / 2 - 20, hudY + 13);

        g.setFont(new Font("Arial", Font.BOLD, 16));

        drawShadowedString(g, "SCORE: " + score, hudX, hudY + 45, new Color(255, 255, 255));

        long elapsed = game.getElapsedTime();
        long seconds = (elapsed / 1000) % 60;
        long minutes = (elapsed / 1000) / 60;
        String timeText = String.format("%02d:%02d", minutes, seconds);
        drawShadowedString(g, "TIME: " + timeText, hudX, hudY + 70, Color.WHITE);

        ArrayList<BufferedImage> buffs = new ArrayList<>();
        if (hasKey) buffs.add(Assets.objectsMap.get("key"));
        if (isDrunk) buffs.add(Assets.objectsMap.get("drunk"));
        if (isPoisoned) buffs.add(Assets.objectsMap.get("poison"));
        if (isSlowed) buffs.add(Assets.objectsMap.get("slow"));

        int currentIconX = hudX;
        int iconSize = 32;
        int iconY = hudY + 85;

        for (BufferedImage icon : buffs) {

            g.setColor(new Color(255, 255, 255, 30));
            g.fillOval(currentIconX, iconY, iconSize, iconSize);

            g.drawImage(icon, currentIconX, iconY, iconSize, iconSize, null);

            if (isDrunk && icon == Assets.objectsMap.get("drunk")) {
                renderEffect(g, currentIconX, iconY, iconSize, drunkTimer, 600.0f, new Color(0, 0, 0, 100));
            } else if (isPoisoned && icon == Assets.objectsMap.get("poison")) {
                renderEffect(g, currentIconX, iconY, iconSize, poisonTimer, 120.0f, new Color(0, 50, 0, 150));
            } else if (isSlowed && icon == Assets.objectsMap.get("slow")) {
                renderEffect(g, currentIconX, iconY, iconSize, slowTimer, 300.0f, new Color(0, 0, 50, 150));
            }
            currentIconX += iconSize + 10;
        }
    }
    public boolean isHasKey() {
        return hasKey;
    }

    public int getScore() {
        return score;
    }

    public boolean isDrunk() {
        return isDrunk;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public void setDrunk(boolean isDrunk) { this.isDrunk = isDrunk; }
    public void setHasKey(boolean hasKey) { this.hasKey = hasKey; }
    public int getDrunkTimer() { return drunkTimer; }
    public void setDrunkTimer(int ticks) { this.drunkTimer = ticks; }

    public void applyPoison(int ticks) {
        this.isPoisoned = true;
        this.poisonTimer = Math.max(this.poisonTimer, ticks);
        this.poisonTickCounter = 0;
    }

    public void applySlow(int ticks) {
        this.isSlowed = true;
        this.slowTimer = Math.max(this.slowTimer, ticks);
    }
}