package PaooGame.Entities;

public class Camera {
    private float xOffset, yOffset;
    private float zoom;
    private int screenWidth, screenHeight;

    public Camera(int screenWidth, int screenHeight) {
        this.xOffset = 0;
        this.yOffset = 0;
        this.zoom = 2.0f;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }


    public void centerOnPlayer(Entity entity, int mapWidthPixels, int mapHeightPixels) {


        xOffset = entity.getX() - (screenWidth / 2.0f) / zoom + 32;
        yOffset = entity.getY() - (screenHeight / 2.0f) / zoom + 32;


        float visibleWidth = screenWidth / zoom;
        float visibleHeight = screenHeight / zoom;


        //liomitari ale camerei
        if (xOffset < 0) {
            xOffset = 0;
        } else if (xOffset > mapWidthPixels - visibleWidth) {
            xOffset = mapWidthPixels - visibleWidth;
        }


        if (yOffset < 0) {
            yOffset = 0;
        } else if (yOffset > mapHeightPixels - visibleHeight) {
            yOffset = mapHeightPixels - visibleHeight;
        }
    }


    public float getXOffset() {
        return xOffset;
    }

    public float getYOffset() {
        return yOffset;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }


    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
}