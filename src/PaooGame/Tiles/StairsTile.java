package PaooGame.Tiles;
import PaooGame.Graphics.Assets;

import java.awt.*;
import java.awt.image.BufferedImage;

public class StairsTile extends Tile {
    public StairsTile(BufferedImage texture, int id) {
        super(texture, id);
    }

    @Override
    public boolean IsStairs() {
        return true;
    }

    @Override
    public boolean IsSolidAt(float relativeX, float relativeY) {
    int margin = 5;


    if (relativeX < margin || relativeX > (TILE_WIDTH - margin)) {
        return true;
    }
    return false;
    }
}