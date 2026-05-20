package PaooGame.Entities.Objects.InteractableObjects;

import PaooGame.Entities.Player;
import PaooGame.Game;
import PaooGame.Graphics.Animation;
import PaooGame.Graphics.Assets;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SpikeTrap extends InteractiveEntity {
    private enum TrapState { IDLE, RISING, STAYING, RETRACTING }
    private TrapState currentState = TrapState.IDLE;

    private Animation riseAnim;
    private Animation retractAnim;

    private long stayUpTimer = 0;
    private final long STAY_UP_DURATION = 100;
    private final int DAMAGE_FRAME = 2;

    public SpikeTrap(Game game, int x, int y) {
        super(game, x, y, 48, 48, 0);

        BufferedImage[] frames = Assets.trapsAnim.get("spikes")[0];
        riseAnim = new Animation(100, frames);

        // reverse animation
        BufferedImage[] reverseFrames = new BufferedImage[frames.length];
        for (int i = 0; i < frames.length; i++) {
            reverseFrames[i] = frames[frames.length - 1 - i];
        }
        retractAnim = new Animation(100, reverseFrames);
    }

    @Override
    public void Update() {
        super.Update();
        switch (currentState) {
            case IDLE:
                if (isPlayerNear) {
                    currentState = TrapState.RISING;
                    riseAnim.Reset();
                }
                break;
            case RISING:
                riseAnim.Update();
                // damage only on the last frame
                if (riseAnim.getCurrentFrameIndex() == DAMAGE_FRAME && isPlayerNear) {
                    game.getPlayer().TakeDamage(1);
                }
                if (riseAnim.IsFinished()) {
                    currentState = TrapState.STAYING;
                    stayUpTimer = System.currentTimeMillis();
                }
                break;
            case STAYING:
                if (isPlayerNear && System.currentTimeMillis() % 1000 < 20) {
                    game.getPlayer().TakeDamage(1);
                }
                if (System.currentTimeMillis() - stayUpTimer >= STAY_UP_DURATION) {
                    currentState = TrapState.RETRACTING;
                    retractAnim.Reset();
                }
                break;
            case RETRACTING:
                retractAnim.Update();
                if (retractAnim.IsFinished()) {
                    currentState = TrapState.IDLE;
                }
                break;
        }
    }

    @Override
    public void Draw(Graphics g) {
        BufferedImage currentFrame;
        if (currentState == TrapState.RETRACTING) {
            currentFrame = retractAnim.GetCurrentFrame();
        } else if (currentState == TrapState.IDLE) {
            currentFrame = Assets.trapsAnim.get("spikes")[0][0];
        } else {
            currentFrame = riseAnim.GetCurrentFrame();
        }
        g.drawImage(currentFrame, (int) x, (int) y, width, height, null);
    }

    @Override
    protected void onInteract(Player p) {}
}