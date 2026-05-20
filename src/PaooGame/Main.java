package PaooGame;

public class Main
{
    public static void main(String[] args)
    {
        System.setProperty("sun.java2d.opengl", "true");
        Game paooGame = new Game("PaooGame", 1920, 1080);
        paooGame.StartGame();
    }
}
