package PaooGame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {
    private boolean[] keys; // Tablou pentru toate tastele posibile
    private boolean[] lastKeys; // Tablou pentru toate tastele posibile
    public boolean up, down, left, right, run, attack, interact, esc, backspace; // Tastele noastre de interes

    public KeyManager() {
        keys = new boolean[256];
        lastKeys = new boolean[256];
    }

    public void Update() {
        // Mapăm variabilele noastre pe codurile tastelor WASD sau Săgeți
        up = keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP];
        down = keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN];
        left = keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT];
        right = keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT];
        run = keys[KeyEvent.VK_SHIFT];

        attack = keys[KeyEvent.VK_SPACE];
        interact = keys[KeyEvent.VK_E];
        esc = keys[KeyEvent.VK_ESCAPE];
        backspace = keys[KeyEvent.VK_BACK_SPACE];
    }
    public void postUpdate() {
        System.arraycopy(keys, 0, lastKeys, 0, keys.length);
    }

    public boolean isKeyJustPressed(int keyCode) {
        if (keyCode < 0 || keyCode >= keys.length) return false;
        return keys[keyCode] && !lastKeys[keyCode];
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true; // Tasta a fost apăsată
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false; // Tasta a fost eliberată
    }

    @Override
    public void keyTyped(KeyEvent e) { }
}