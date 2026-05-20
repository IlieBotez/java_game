package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;

import java.awt.*;

public class MenuButton {
    private Game game;
    private int x, y, width, height;
    private String actionType;
    private boolean isPressed = false;

    public MenuButton(Game game, int x, int y, int width, int height, String actionType) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.actionType = actionType;
    }

    public void Update() {
        boolean mousePressed = game.getMouseManager().isLeftPressed();

        if (hoverArea()) {
            if(mousePressed) {
                isPressed = true;
            }
            else if (isPressed) {
                triggerAction();
                isPressed = false;
            }
        } else {
            isPressed = false;
        }
    }

    private void triggerAction() {
        switch (actionType) {
            case "NEW GAME":
                game.getLevelManager().startTransition(0);
                game.setStartTime(System.currentTimeMillis());
                break;

            case "LOAD GAME":
                game.loadProgress();
                break;

            case "RESUME":
                game.resumeGame();
                game.setCurrentState(Game.GameState.PLAYING);
                break;

            case "SAVE GAME":
                game.saveProgress();
                break;

            case "MAIN MENU":
                game.setCurrentState(Game.GameState.MENU);
                break;

            case "SETTINGS":
                game.setPreviousState(game.getCurrentState()); // Salvăm de unde venim
                game.setCurrentState(Game.GameState.SETTINGS);
                break;

            case "SOUND: ON":
            case "SOUND: OFF":
                game.toggleSound();
                this.actionType = game.isMuted() ? "SOUND: OFF" : "SOUND: ON";
                break;

            case "FULL SCREEN":
            case "WINDOW MODE":
                game.toggleFullscreen();
                this.actionType = game.isFullscreen() ? "WINDOW MODE" : "FULL SCREEN";
                break;

            case "CONTROLS":
                game.setCurrentState(Game.GameState.CONTROLS);
                break;

            case "BACK":
                game.setCurrentState(game.getPreviousState());
                break;

            case "EXIT":
                System.exit(0);
                break;
            case "LEADERBOARD":
                game.setPreviousState(game.getCurrentState());
                game.setCurrentState(Game.GameState.LEADERBOARD);
                break;
            case "SUBMIT SCORE":
                String finalName = game.getPlayerNameInput().toString();
                if (finalName.isEmpty()) finalName = "Unknown Hero";
                int finalScore = game.getPlayer().getScore();
                int finalTime = (int)(game.getElapsedTime() / 1000);
                game.getLeaderBoardManager().addScore(finalName, finalScore, finalTime);
                game.setCurrentState(Game.GameState.LEADERBOARD);
                break;
        }
    }

    public void Draw(Graphics g) {

        if (hoverArea()) {
            g.setColor(new Color(150, 150, 150, 200)); // Gri deschis semi-transparent la hover
        } else {
            g.setColor(new Color(80, 80, 80, 200));    // Gri închis în mod normal
        }
        g.fillRect(x, y, width, height);

        // 2. Desenăm un contur subțire alb
        g.setColor(Color.WHITE);
        g.drawRect(x, y, width, height);

        // 3. Desenăm textul (acțiunea) perfect centrat
        g.setFont(new Font("Arial", Font.BOLD, 22)); // Setăm un font clar
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(actionType);
        int textHeight = fm.getAscent();

        // Calcule pentru centrarea textului în mijlocul dreptunghiului
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height + textHeight) / 2 - 4; // -4 pentru ajustare optică

        g.drawString(actionType, textX, textY);
    }

    private boolean hoverArea() {
        int mX = game.getMouseManager().getMouseX();
        int mY = game.getMouseManager().getMouseY();
        return mX >= x && mX <= x + width && mY >= y && mY <= y + height;
    }
}