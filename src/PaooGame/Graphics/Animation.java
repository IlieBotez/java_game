package PaooGame.Graphics;

import java.awt.image.BufferedImage;

public class Animation {

    private BufferedImage[] frames; // Tabloul cu cadrele animației
    private int speed;              // Viteza (în milisecunde între cadre)
    private int index;              // Indexul cadrului curent
    private long lastTime, timer;   // Auxiliare pentru calculul timpului

    public Animation(int speed, BufferedImage[] frames) {
        this.speed = speed;
        this.frames = frames;
        index = 0;
        timer = 0;
        lastTime = System.currentTimeMillis(); // Luăm timpul de start
    }

    public void Update() {
        // Calculăm cât timp a trecut de la ultima actualizare
        timer += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();

        // Dacă a trecut timpul setat pentru un cadru, trecem la următorul
        if (timer > speed) {
            index++;
            timer = 0;
            // Dacă am ajuns la final, resetăm animația (loop)
            if (index >= frames.length) {
                index = 0;
            }
        }
    }

    public void Reset() {
        index = 0;
        timer = 0;
        lastTime = System.currentTimeMillis(); // Resetăm și timpul de referință
    }

    public boolean IsFinished() {
        // Verificăm dacă indexul a ajuns la ultima imagine din vector
        return index == frames.length - 1;
    }

    public int getCurrentFrameIndex() {
        return index;
    }

    public int getFrameCount() {
        return frames.length;
    } // Returnează totalul de cadre (6 sau 8)

    public BufferedImage GetCurrentFrame() {
        return frames[index];
    }
}
