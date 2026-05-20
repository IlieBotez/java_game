package PaooGame.Graphics;

import PaooGame.Entities.Camera;
import com.google.gson.Gson;
import PaooGame.Tiles.Tile;
import java.awt.*;
import java.io.InputStreamReader;
import java.io.Reader;

public class Map {
    private TiledMap mapData;

    public Map(String path) {
        try {
            Gson gson = new Gson();
            var is = getClass().getResourceAsStream(path);

            if (is == null) {
                System.err.println("Nu am găsit fișierul la calea: " + path);
                return;
            }

            Reader reader = new InputStreamReader(is);
            mapData = gson.fromJson(reader, TiledMap.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void DrawLayer(Graphics g, int layerIndex, PaooGame.Entities.Player player, Camera camera) {
        if (mapData == null || mapData.layers == null || layerIndex >= mapData.layers.length) {
            return;
        }

        Layer layer = mapData.layers[layerIndex];
        Graphics2D g2d = (Graphics2D) g;


        AlphaComposite originalComposite = (AlphaComposite) g2d.getComposite();
        AlphaComposite transparentComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
        int startX = Math.max(0, (int) (camera.getXOffset() / Tile.TILE_WIDTH));
        int startY = Math.max(0, (int) (camera.getYOffset() / Tile.TILE_HEIGHT));
        int endX   = Math.min(layer.width,  startX + (int) (camera.getScreenWidth()  / Tile.TILE_WIDTH)  + 2);
        int endY   = Math.min(layer.height, startY + (int) (camera.getScreenHeight() / Tile.TILE_HEIGHT) +2);


        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                int tileId = layer.data[x + y * layer.width];

                if (tileId > 0) {
                    Tile t = Tile.tiles[tileId - 1];
                    if (t != null) {

                        if (layerIndex == 2 && player != null ) {

                            float tileCenterX = (x * Tile.TILE_WIDTH) + (Tile.TILE_WIDTH / 2.0f);
                            float tileCenterY = (y * Tile.TILE_HEIGHT) + (Tile.TILE_HEIGHT / 2.0f);


                            float playerCenterX = player.getX() + 32;
                            float playerCenterY = player.getY() + 32;


                            double distance = Math.hypot(tileCenterX - playerCenterX, tileCenterY - playerCenterY);


                            int radius = 75;

                            if (distance < radius) {
                                g2d.setComposite(transparentComposite);
                            } else {
                                g2d.setComposite(originalComposite);
                            }
                        }


                        t.Draw(g, x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);


                        g2d.setComposite(originalComposite);
                    }
                }
            }
        }
    }


    public int getLayerCount() {
        if (mapData == null || mapData.layers == null) return 0;
        return mapData.layers.length;
    }


    public Tile GetTile(int x, int y, int layerIndex) {
        if (mapData == null || mapData.layers == null || layerIndex >= mapData.layers.length) {
            return null;
        }

        int tx = x / Tile.TILE_WIDTH;
        int ty = y / Tile.TILE_HEIGHT;


        if (tx < 0 || ty < 0 || tx >= mapData.width || ty >= mapData.height) {
            return Tile.tiles[0];
        }

        int id = mapData.layers[layerIndex].data[tx + ty * mapData.width];

        if (id == 0) return null;

        return Tile.tiles[id - 1];
    }


    public int getWidthInPixels() {
        if (mapData == null) return 0;
        return mapData.width * Tile.TILE_WIDTH;
    }

    public int getHeightInPixels() {
        if (mapData == null) return 0;
        return mapData.height * Tile.TILE_HEIGHT;
    }

    public int getWidth() {
        if (mapData == null) return 0;
        return mapData.width;
    }

    public int getHeight() {
        if (mapData == null) return 0;
        return mapData.height;
    }
}