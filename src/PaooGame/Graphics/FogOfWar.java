package PaooGame.Graphics;

import PaooGame.Entities.Camera;
import PaooGame.Entities.Player;
import java.awt.*;

public class FogOfWar {
    private int[][] fogState;
    private int columns, rows;
    private int tileWidth = 48;
    private int tileHeight = 48;
    private int visionRadius = 2;

    public FogOfWar(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        this.fogState = new int[columns][rows];
    }

    public void Update(Player player) {
        if (player == null) return;

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                if (fogState[x][y] == 2) {
                    fogState[x][y] = 1;
                }
            }
        }

        int playerTileX = (int) ((player.getX() + player.getWidth() / 2) / tileWidth);
        int playerTileY = (int) ((player.getY() + player.getHeight() / 2) / tileHeight);

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                if (distance(playerTileX, playerTileY, x, y) <= visionRadius) {
                    fogState[x][y] = 2;
                }
            }
        }
    }

    private double distance(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void Draw(Graphics g, Camera camera) {

        Color unexploredColor = new Color(15, 15, 15, 240);
        Color exploredColor = new Color(0, 0, 0, 0);
        int startX = Math.max(0, (int) (camera.getXOffset() / tileWidth));
        int startY = Math.max(0, (int) (camera.getYOffset() / tileHeight));
        int endX   = Math.min(columns, startX + (int) (camera.getScreenWidth()  / tileWidth)  + 2);
        int endY   = Math.min(rows,    startY + (int) (camera.getScreenHeight() / tileHeight) + 2);

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {

                if (fogState[x][y] == 0) {
                    g.setColor(unexploredColor);
                    g.fillRect(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
                }
                else if (fogState[x][y] == 1) {
                    g.setColor(exploredColor);
                    g.fillRect(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
                }
            }
        }
    }

    public int[][] getFogState() {
        return fogState;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }
}