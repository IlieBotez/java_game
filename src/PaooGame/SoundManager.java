package PaooGame;

import javax.sound.sampled.*;
import java.net.URL;

public class SoundManager {
    private Clip clip;

    // Metodă pentru a încărca fișierul .wav
    public void load(String path) {
        try {
            // Căutăm fișierul în resursele proiectului
            URL url = getClass().getResource(path);
            if (url == null) {
                System.err.println("Nu am găsit fișierul audio la: " + path);
                return;
            }

            // Deschidem un flux audio și încărcăm clipul în memorie
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioIn);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Redă sunetul o singură dată (săbii, lovituri etc.)
    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // Derulăm la început
            clip.start();
        }
    }

    // Redă sunetul la nesfârșit
    public void loop() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void setMute(boolean mute) {
        if (clip == null) return;

        try {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            if (mute) {
                gainControl.setValue(gainControl.getMinimum());
            } else {
                gainControl.setValue(0.0f);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare: Driverul audio nu suporta schimbarea volumului.");
        }
    }

    // Oprește sunetul
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}