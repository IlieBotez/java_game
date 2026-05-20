package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

import java.awt.image.BufferedImage;


public class HigherFloorTile extends Tile
{
    public HigherFloorTile(BufferedImage texture, int id)
    {
        super(texture, id);
    }
    @Override
    public boolean IsSolidAt(float relativeX, float relativeY) {
        int margin = 10;

        switch (id)
        {
            case 78:
                if (relativeX < margin)
                    return true;
                break;
            case 80:
                if (relativeX > (TILE_WIDTH - margin))
                    return true;
                break;
            case 66:
                if(relativeY < margin)
                    return true;
                break;
            case 92:
                if(relativeY >(TILE_HEIGHT - margin))
                    return true;
                break;
            case 65:
                if (relativeX < margin || relativeY < margin)
                    return true;
                break;
            case 67:
                if (relativeX > (TILE_WIDTH - margin) || relativeY < margin)
                    return true;
                break;
            case 91:
                if (relativeX < margin|| relativeY >(TILE_HEIGHT - margin))
                    return true;
                break;
            case 93:
                if (relativeX > (TILE_WIDTH - margin) || relativeY >(TILE_HEIGHT - margin))
                    return true;
                break;



        }
        return false;
    }
}


