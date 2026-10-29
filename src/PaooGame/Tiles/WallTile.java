
package PaooGame.Tiles;
import PaooGame.Graphics.Assets;

import java.awt.image.BufferedImage;

public class WallTile extends Tile {
    public WallTile(BufferedImage texture, int id) {
        super(texture, id);
    }

    @Override
    public boolean IsSolid() {
        return true;
    }
}