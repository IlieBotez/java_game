package PaooGame.Tiles;

import PaooGame.Graphics.Assets;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile
{
    private static final int NO_TILES   = 1000;
    public static Tile[] tiles          = new Tile[NO_TILES];


    public int customId = -1;
    public static Tile floor_s_tile = new PodeaTile(Assets.tiles.get("floor_s"), 79);
    public static Tile stairs       = new StairsTile(Assets.tiles.get("stairs"), 114);

    // Walls
    public static Tile black_wall   = new WallTile(Assets.tiles.get("black_wall"), 14);

    // Front wall
    public static Tile wallBotLeft  = new WallTile(Assets.tiles.get("wallBotLeft"), 52);
    public static Tile wallBotMid   = new WallTile(Assets.tiles.get("wallBotMid"), 53);
    public static Tile wallBotRight = new WallTile(Assets.tiles.get("wallBotRight"), 54);

    public static Tile wallMidLeft  = new WallTile(Assets.tiles.get("wallMidLeft"), 39);
    public static Tile wallMidMid   = new WallTile(Assets.tiles.get("wallMidMid"), 40);
    public static Tile wallMidRight = new WallTile(Assets.tiles.get("wallMidRight"), 41);

    public static Tile wallTopLeft  = new WallTile(Assets.tiles.get("wallTopLeft"), 26);
    public static Tile wallTopMid   = new WallTile(Assets.tiles.get("wallTopMid"), 27);
    public static Tile wallTopRight = new WallTile(Assets.tiles.get("wallTopRight"), 28);

    public static Tile wallLeft      = new WallTile(Assets.tiles.get("wallLeft"), 13);
    public static Tile wallRight     = new WallTile(Assets.tiles.get("wallRight"), 15);
    public static Tile wallBackLeft  = new WallTile(Assets.tiles.get("wallBackLeft"), 0);
    public static Tile wallBackMid   = new WallTile(Assets.tiles.get("wallBackMid"), 1);
    public static Tile wallBackRight = new WallTile(Assets.tiles.get("wallBackRight"), 2);


    public static Tile wallCornerNW = new WallTile(Assets.tiles.get("wallCornerNW"), 3);
    public static Tile wallCornerNE = new WallTile(Assets.tiles.get("wallCornerNE"), 5);
    public static Tile wallCornerSW = new WallTile(Assets.tiles.get("wallCornerSW"), 42);
    public static Tile wallCornerSE = new WallTile(Assets.tiles.get("wallCornerSE"), 44);
    public static Tile walLNW       = new WallTile(Assets.tiles.get("wallNW"), 16);
    public static Tile walLNE       = new WallTile(Assets.tiles.get("wallNE"), 18);


    public static Tile RoundedCornerWallLeft    = new WallTile(Assets.tiles.get("RoundedCornerWallLeft"), 32);
    public static Tile RoundedCornerWallRight   = new WallTile(Assets.tiles.get("RoundedCornerWallRight"), 34);
    public static Tile RoundedCornerWallLeftUp  = new WallTile(Assets.tiles.get("RoundedCornerWallLeftUp"), 19);
    public static Tile RoundedCornerWallRightUp = new WallTile(Assets.tiles.get("RoundedCornerWallRightUp"), 21);


    public static Tile Stairs_CornerNW = new HigherFloorTile(Assets.tiles.get("Stairs_CornerNW"), 65);
    public static Tile Stairs_CornerNE = new HigherFloorTile(Assets.tiles.get("Stairs_CornerNE"), 67);
    public static Tile Stairs_CornerSW = new HigherFloorTile(Assets.tiles.get("Stairs_CornerSW"), 91);
    public static Tile Stairs_CornerSE = new HigherFloorTile(Assets.tiles.get("Stairs_CornerSE"), 93);
    public static Tile Stairs_SideLeft = new HigherFloorTile(Assets.tiles.get("Stairs_SideLeft"), 78);
    public static Tile Stairs_SideRight = new HigherFloorTile(Assets.tiles.get("Stairs_SideRight"), 80);
    public static Tile Stairs_SideUp   = new HigherFloorTile(Assets.tiles.get("Stairs_SideUp"), 66);
    public static Tile Stairs_SideDown = new HigherFloorTile(Assets.tiles.get("Stairs_SideDown"), 92);
    public static Tile iceTile = new PodeaTile(Assets.tiles.get("iceTile"), 524);
    public static Tile spikeDown = new PodeaTile(Assets.tiles.get("spikeDown"), 317);
    public static final int TILE_WIDTH  = 48;
    public static final int TILE_HEIGHT = 48;

    protected BufferedImage img;
    protected final int id;

    public Tile(BufferedImage image, int idd)
    {
        img = image;
        id = idd;
        if (id >= 0 && id < NO_TILES) {
            tiles[id] = this;
        }
    }

    public void Update() {}

    public void Draw(Graphics g, int x, int y)
    {
        if (img != null) {
            g.drawImage(img, x, y, TILE_WIDTH, TILE_HEIGHT, null);
        }
    }

    public boolean IsSolid() { return false; }
    public boolean IsStairs() { return false; }
    public boolean IsSolidAt(float relativeX, float relativeY) { return IsSolid(); }

    public int GetId() { return id; }
}