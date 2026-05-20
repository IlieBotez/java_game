package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Animation;
import java.awt.*;
import java.util.Random;

public class Orc extends Enemy{
    protected Random random;
    protected int dirX = 0, dirY = 0;

    protected boolean hasDealtDamage = false;
    protected long attackCooldown = 1500;
    protected long lastAttackTimer = 0;

    protected float patrolSpeed;      // Viteza de patrulare (mers)
    protected float chaseSpeed;       // Viteza de urmărire (alergat)

    public Orc(Game game, float x, float y, int health, int damage) {
        super(game, x, y, 64, 64, health, damage);
        this.random = new Random();
        this.scoreValue = 50;
        this.patrolSpeed = 0.6f;
        this.chaseSpeed = 1.0f;
    }

    protected void loadAnimations(java.awt.image.BufferedImage[][] walk,
                                  java.awt.image.BufferedImage[][] run,
                                  java.awt.image.BufferedImage[][] idle,
                                  java.awt.image.BufferedImage[][] attack,
                                  java.awt.image.BufferedImage[][] walkAttack,
                                  java.awt.image.BufferedImage[][] runAttack,
                                  java.awt.image.BufferedImage[][] hurt,
                                  java.awt.image.BufferedImage[][] die) {

        String[] dirs = {"DOWN", "UP", "LEFT", "RIGHT"};

        for (int i = 0; i < 4; i++) {
            animations.put("WALK_" + dirs[i], new Animation(100, walk[i]));
            animations.put("RUN_" + dirs[i], new Animation(120, run[i]));
            animations.put("IDLE_" + dirs[i], new Animation(200, idle[i]));
            animations.put("IDLE_ATTACK_" + dirs[i], new Animation(80, attack[i]));
            animations.put("WALK_ATTACK_" + dirs[i], new Animation(80, walkAttack[i]));
            animations.put("RUN_ATTACK_" + dirs[i], new Animation(80, runAttack[i]));
            animations.put("HURT_" + dirs[i], new Animation(120, hurt[i]));
            animations.put("DIE_" + dirs[i], new Animation(100, die[i]));
        }
    }

    @Override
    public void Update() {
        // Verificări prioritare (Moarte/Rănire)
        if (isDead) { handleDeath(); return; }
        if (isHurt) {handleHurt(); return;}

        // Calcul distanță către țintă
        Player target = game.getEntityManager().getPlayer();
        float distance = getDistanceTo(target);

        // Decizia AI (Stări)
        decideBehavior(target, distance);

        // Execuție Mișcare
        if (!isAttacking) {
            applyMovement();
        }

        // Actualizare Vizuală
        updateAnimationKey(distance);
        if (animations.containsKey(currentKey)) {
            animations.get(currentKey).Update();
        }
    }

    protected void handleDeath() {
        currentKey = "DIE_" + lastDirection;
        if (!animations.get(currentKey).IsFinished()) {
            animations.get(currentKey).Update();
        }
    }

    protected void handleHurt() {
        currentKey = "HURT_" + lastDirection;
        if (animations.get(currentKey).IsFinished()) {
            isHurt = false;
        }
        animations.get(currentKey).Update();
    }

    private void decideBehavior(Player p, float distance) {
        float stoppingDistance = 35.0f;

        if (distance <= stoppingDistance) {
            executeAttack(p);
        } else if (distance < 150) {
            executeChase(p, distance);
        } else {
            executePatrol();
        }
    }

    private void executeAttack(Player p) {
        dirX = 0; dirY = 0;

        if (!isAttacking && System.currentTimeMillis() - lastAttackTimer > attackCooldown) {
            isAttacking = true;
            hasDealtDamage = false;
            lastAttackTimer = System.currentTimeMillis();
            animations.get("IDLE_ATTACK_" + lastDirection).Reset();
        }
    }

    private void executeChase(Player p, float distance) {
        isAttacking = false;
        this.speed = chaseSpeed;

        // Delay pentru primul atac (0.5s)
        if (distance > 50) {
            lastAttackTimer = System.currentTimeMillis() - (attackCooldown - 500);
        }

        float diffX = p.getX() - x;
        float diffY = p.getY() - y;
        followPlayer(diffX, diffY);
    }

    private void executePatrol() {
        isAttacking = false;
        this.speed = patrolSpeed;
        lastAttackTimer = System.currentTimeMillis();

        if (System.currentTimeMillis() - lastAIUpdate > aiInterval) {
            chooseNewDirection();
            lastAIUpdate = System.currentTimeMillis();
        }
    }

    private void applyMovement() {

        if (canMoveWithoutStacking(dirX, dirY)) {
            move(dirX, dirY);
            return;
        }

        // Alunecare pe X: Dacă e blocat diagonal, încercăm să mergem doar pe axa X
        if (dirX != 0 && canMoveWithoutStacking(dirX, 0)) {
            move(dirX, 0);
            return;
        }

        // Alunecare pe Y: Dacă axa X e blocată, încercăm doar pe Y
        if (dirY != 0 && canMoveWithoutStacking(0, dirY)) {
            move(0, dirY);
            return;
        }

        // Dacă e blocat și pe X și pe Y spre jucător
        // Încercăm direcții perpendiculare ca să iasă din mulțime
        if (dirX != 0 && dirY == 0) {
            // Mergea stânga/dreapta, s-a blocat. Încearcă să ocolească pe sus sau pe jos
            if (canMoveWithoutStacking(0, 1)) { move(0, 1); return; }
            if (canMoveWithoutStacking(0, -1)) { move(0, -1); return; }
        }
        else if (dirY != 0 && dirX == 0) {
            // Mergea sus/jos, s-a blocat. Încearcă să ocolească prin stânga sau dreapta
            if (canMoveWithoutStacking(1, 0)) { move(1, 0); return; }
            if (canMoveWithoutStacking(-1, 0)) { move(-1, 0); return; }
        }

        // Dacă absolut toate direcțiile sunt blocate (este înconjurat complet), abia atunci stă pe loc.
        dirX = 0;
        dirY = 0;
    }

    private void updateAnimationKey(float distance) {
        if (isAttacking) {
            String attackType = (speed > 0) ? "WALK_ATTACK_" : "IDLE_ATTACK_";
            currentKey = attackType + lastDirection;
            checkAttacks(game.getEntityManager().getPlayer());

            if (animations.get(currentKey).IsFinished()) {
                isAttacking = false;
            }
        }
        else {
            // RUN dacă distanța e mică, WALK în rest
            String moveType = (distance < 150) ? "RUN_" : "WALK_";
            currentKey = (dirX != 0 || dirY != 0) ? moveType + lastDirection : "IDLE_" + lastDirection;
        }
    }

    protected void checkAttacks(Player p) {
        if (hasDealtDamage) return;

        Animation anim = animations.get(currentKey);
        int frame = anim.getCurrentFrameIndex();

        // Orcii lovesc la cadrul 4 (mijlocul animației de atac)
        if (frame == 4) {
            Rectangle body = getCollisionBounds(0, 0);
            int range = 25;

            // Calculăm unde lovește orcul
            switch (lastDirection) {
                case "UP"       : attackRect.setBounds(body.x, body.y - range, body.width, range); break;
                case "DOWN"     : attackRect.setBounds(body.x, body.y + body.height, body.width, range); break;
                case "LEFT"     : attackRect.setBounds(body.x - range, body.y, range, body.height); break;
                case "RIGHT"    : attackRect.setBounds(body.x + body.width, body.y, range, body.height); break;
            }

            if (attackRect.intersects(p.getCollisionBounds(0, 0))) {
                p.TakeDamage(this.attackDamage); // Jucătorul ia damage!
                hasDealtDamage = true;
            }
        }
    }

    private void followPlayer(float diffX, float diffY) {

        dirX = (Math.abs(diffX) > 2) ? ((diffX > 0) ? 1 : -1) : 0;
        dirY = (Math.abs(diffY) > 2) ? ((diffY > 0) ? 1 : -1) : 0;

        float absX = Math.abs(diffX);
        float absY = Math.abs(diffY);

        if (absX > absY + 15) {
            lastDirection = (dirX > 0) ? "RIGHT" : "LEFT";
        }
        else if (absY > absX + 15) {
            lastDirection = (dirY > 0) ? "DOWN" : "UP";
        }
    }

    // Metodă pentru a preveni "clumping" (stivuirea orcilor)
    private boolean canMoveWithoutStacking(int testDirX, int testDirY) {

        if (testDirX == 0 && testDirY == 0) {
            return true;
        }

        Rectangle nextBounds = getCollisionBounds(testDirX * speed, testDirY * speed);

        for (Enemy e : game.getEntityManager().getEnemies()) {
            if (e == this || e.isDead()) {
                continue;
            }

            if (nextBounds.intersects(e.getCollisionBounds(0, 0))) {
                return false;
            }
        }
        return true;
    }

    private void chooseNewDirection() {

        int chance = random.nextInt(5);
        dirX = 0; dirY = 0;

        switch (chance) {
            case 0: dirY = -1; lastDirection = "UP"; break;
            case 1: dirY = 1;  lastDirection = "DOWN"; break;
            case 2: dirX = -1; lastDirection = "LEFT"; break;
            case 3: dirX = 1;  lastDirection = "RIGHT"; break;
        }
    }

    private float getDistanceTo(Entity target) {
        float diffX = target.getX() - x;
        float diffY = target.getY() - y;
        return (float) Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private void move(int moveX, int moveY) {
        if (moveX != 0 && canMove(x + moveX * speed, y)) {
            x += moveX * speed;
        }

        if (moveY != 0 && canMove(x, y + moveY * speed)) {
            y += moveY * speed;
        }
    }
}
