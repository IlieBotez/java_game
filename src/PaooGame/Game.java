package PaooGame;


import PaooGame.Entities.*;
import PaooGame.Entities.Objects.InteractableObjects.Chest;
import PaooGame.Entities.Objects.InteractableObjects.Gate;
import PaooGame.Entities.Objects.InteractableObjects.StaticObjects;
import PaooGame.Entities.Objects.InteractableObjects.WineBarrel;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;
import PaooGame.Graphics.FogOfWar;
import PaooGame.Graphics.Map;
import PaooGame.MouseManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Properties;

public class Game implements Runnable {

    private GameWindow wnd;
    private boolean runState;
    private Thread gameThread;
    private BufferStrategy bs;
    private Player player;
    private EntityManager entityManager;
    private LevelManager levelManager;
    private Graphics g;
    private SoundManager backgroundMusic;
    private LeaderBoardManager leaderBoardManager;
    private KeyManager keyManager;
    private MouseManager mouseManager;
    private boolean escPressedLastFrame = false;
    private Map[] maps;
    private Map currentMap;
    private Camera camera;
    public enum GameState{
        MENU,
        PLAYING,
        PAUSE,
        SETTINGS,
        LEADERBOARD,
        WIN,
        CONTROLS
    }

    private GameState currentState = GameState.MENU;
    private GameState previousState = GameState.MENU;
    private boolean isMuted = false;
    private boolean isFullscreen = false;
    private StringBuilder playerNameInput = new StringBuilder("");
    private final int MAX_NAME_LENGTH = 15;
    // Main Menu Buttons
    private MenuButton btnStart, btnLoad, btnSettingsMenu, btnExit, btnLeaderBoard;
    // Pause Menu Buttons
    private MenuButton btnResume, btnSave, btnSettingsPause, btnMainMenu;
    // Settings Menu Buttons
    private MenuButton btnBack, btnControls;
    // Butoanele din Settings
    private MenuButton btnToggleSound, btnToggleFS;
    // Buton din WIN state
    private MenuButton btnSubmit;

    private long startTime;
    private long pauseStart;
    private long winTime;

    public Game(String title, int width, int height) {
        // Obiectul GameWindow este creat initial, dar fereastra e construita in InitGame()
        wnd = new GameWindow(title, width, height);
        runState = false;
    }

    private void initUI() {
        int centerX = (wnd.GetWndWidth() / 2) - 105;

        // Butoane Meniu Principal
        btnStart = new MenuButton(this, centerX, 400, 210, 70, "NEW GAME");
        btnLoad = new MenuButton(this, centerX, 500, 210, 70, "LOAD GAME");
        btnSettingsMenu = new MenuButton(this, centerX, 600, 210, 70, "SETTINGS");
        btnLeaderBoard= new MenuButton(this, centerX, 700, 210, 70, "LEADERBOARD");
        btnExit = new MenuButton(this, centerX, 800, 210, 70, "EXIT");

        // Butoane Meniu Pauză
        btnResume = new MenuButton(this, centerX, 300, 210, 70, "RESUME");
        btnSave = new MenuButton(this, centerX, 400, 210, 70, "SAVE GAME");
        btnSettingsPause = new MenuButton(this, centerX, 500, 210, 70, "SETTINGS");
        btnMainMenu = new MenuButton(this, centerX, 600, 210, 70, "MAIN MENU");

        // Butoane Setări
        btnToggleSound = new MenuButton(this, centerX, 400, 210, 70, "SOUND: ON");
        btnToggleFS = new MenuButton(this, centerX, 500, 210, 70, "FULL SCREEN");
        btnControls = new MenuButton(this, centerX, 600, 210, 70, "CONTROLS");
        btnBack = new MenuButton(this, centerX, 800, 210, 70, "BACK");

        // Buton din WIN state
        btnSubmit = new MenuButton(this, centerX, 600, 210, 70, "SUBMIT SCORE");
    }

    private void InitGame() {
        wnd.BuildGameWindow();

        // Inițializare Input
        keyManager = new KeyManager();
        mouseManager = new MouseManager();
        wnd.GetCanvas().addKeyListener(keyManager);
        wnd.GetCanvas().addMouseListener(mouseManager);
        wnd.GetCanvas().addMouseMotionListener(mouseManager);
        wnd.GetCanvas().setFocusable(true);
        wnd.GetCanvas().requestFocus();

        // Inițializare Resurse și Hărți
        Assets.Init();
        maps = new Map[3];
        maps[0] = new Map("/Maps/Level1.json");
        maps[1] = new Map("/Maps/Level2.json");
        maps[2] = new Map("/Maps/Level3.json");
        currentMap = maps[0];
        // Inițializare Manageri și Entități
        player = new Player(this, 445, 285);
        levelManager = new LevelManager(this, player);
        entityManager = new EntityManager(player);
        camera = new Camera(1920, 1080);
        leaderBoardManager = new LeaderBoardManager();
        // Inițializare Muzică
        backgroundMusic = new SoundManager();
        backgroundMusic.load("/audio/soundtrack.wav");
        backgroundMusic.loop();
        initUI();
        startTime = System.currentTimeMillis();
    }

    public boolean isLocationOccupied(Rectangle newBounds) {
        // Verificăm dacă se suprapune cu jucătorul
        if (newBounds.intersects(player.getCollisionBounds(0, 0))) {
            return true;
        }

        // Verificăm dacă se suprapune cu orice inamic deja spawnat
        for (Enemy e : entityManager.getEnemies()) {
            if (newBounds.intersects(e.getCollisionBounds(0, 0))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        InitGame();

        int fps = 60;
        double timePerTick = 1000000000.0 / fps; // Nanosecunde per cadru
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();

        while (runState) {

            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            lastTime = now;

            // Dacă a trecut destul timp pentru un "tick" (update logic)
            if (delta >= 1) {
                Update(); // Actualizăm starea jocului
                Draw();   // Randăm imaginea
                delta--;
            }

            // Reducem consumul de CPU (evităm busy-waiting)
            try {
                // Lăsăm procesorul să facă și altceva pentru o milisecundă
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        StopGame(); // Oprim thread-ul corect
    }

    public synchronized void StartGame() {
        if (!runState) {
            runState = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    public synchronized void StopGame() {
        if (runState) {
            runState = false;
            try {
                gameThread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    public long getElapsedTime() {
        if (currentState == GameState.PAUSE) {
            // Dacă e pauză, returnăm timpul scurs până la momentul apăsării pauzei
            return pauseStart - startTime;
        }
        // Dacă suntem în meniu (înainte de start), returnăm 0
        if (currentState == GameState.MENU) {
            return 0;
        }
        if (currentState == GameState.WIN) {  // ← adaugă asta
            return winTime - startTime;
        }
        // Altfel, timpul real minus timpul de start ajustat
        return System.currentTimeMillis() - startTime;
    }
    private void Update() {
        keyManager.Update();

        if (levelManager != null) {
            levelManager.Update();
        }

        if (keyManager.esc) {
            if (!escPressedLastFrame) {
                if (currentState == GameState.PLAYING) {
                    pauseGame(); // Apelăm metoda centralizată
                } else if (currentState == GameState.PAUSE) {
                    resumeGame(); // Apelăm metoda centralizată
                }
            }
            escPressedLastFrame = true;
        } else {
            escPressedLastFrame = false;
        }

        // Logică separată clar pe stări
        switch (currentState) {
            case MENU:
                btnStart.Update();
                btnLoad.Update();
                btnSettingsMenu.Update();
                btnExit.Update();
                btnLeaderBoard.Update();
                break;
            case LEADERBOARD:
                btnBack.Update();
                if (keyManager.esc) currentState = GameState.MENU;
                break;

            case PAUSE:
                btnResume.Update();
                btnSave.Update();
                btnSettingsPause.Update();
                btnMainMenu.Update();
                break;

            case SETTINGS:
                btnToggleSound.Update();
                btnToggleFS.Update();
                btnControls.Update();
                btnBack.Update();
                break;

            case WIN:
                handleNameInput();
                btnSubmit.Update();
                break;

            case CONTROLS:
                if (keyManager.esc) {
                    currentState = GameState.SETTINGS;
                }
                break;

            case PLAYING:
                if (levelManager != null) {
                    levelManager.Update();
                    levelManager.updateEnvironment();
                }
                if (entityManager != null) entityManager.Update();
                if (currentMap != null && player != null){
                  camera.centerOnPlayer(player, currentMap.getWidthInPixels(), currentMap.getHeightInPixels());
                }
                break;
        }
        keyManager.postUpdate();
    }
    public void pauseGame() {
        if (currentState == GameState.PLAYING) {
            pauseStart = System.currentTimeMillis();
            currentState = GameState.PAUSE;
        }
    }
    public void resumeGame() {
        if (currentState == GameState.PAUSE) {
            long pauseDuration = System.currentTimeMillis() - pauseStart;
            startTime += pauseDuration;
            currentState = GameState.PLAYING;
        }
    }
    private void goToMenu() {
        setCurrentState(GameState.MENU);
        setCurrentMap(maps[0]);

        if (entityManager != null) {
            entityManager.getEnemies().clear();
        }
    }
    public void nextLevel()
    {
        int thisLevel = levelManager.getCurrentLevelIndex();
        levelManager.startTransition(thisLevel + 1);
        currentMap = maps[thisLevel + 1];
    }

    private void handleNameInput() {
        // A-Z
        for (int i = KeyEvent.VK_A; i <= KeyEvent.VK_Z; i++) {
            if (keyManager.isKeyJustPressed(i)) {
                if (playerNameInput.length() < MAX_NAME_LENGTH) {
                    playerNameInput.append((char) i);
                }
            }
        }
        // space
        if (keyManager.isKeyJustPressed(KeyEvent.VK_SPACE)) {
            if (playerNameInput.length() < MAX_NAME_LENGTH && playerNameInput.length() > 0) {
                playerNameInput.append(" ");
            }
        }
        // delete
        if (keyManager.isKeyJustPressed(KeyEvent.VK_BACK_SPACE) && playerNameInput.length() > 0) {
            playerNameInput.deleteCharAt(playerNameInput.length() - 1);
        }
    }


    private void Draw() {
        bs = wnd.GetCanvas().getBufferStrategy();
        if (bs == null) {
            wnd.GetCanvas().createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;

        int currentWidth = wnd.GetCanvas().getWidth();
        int currentHeight = wnd.GetCanvas().getHeight();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());

        java.awt.geom.AffineTransform oldTransform = g2d.getTransform();

        if (currentState == GameState.PLAYING || currentState == GameState.PAUSE) {
            g2d.scale(camera.getZoom(), camera.getZoom());
            g2d.translate(-camera.getXOffset(), -camera.getYOffset());

            if (currentMap != null) {
                currentMap.DrawLayer(g, 0, null, camera);
                currentMap.DrawLayer(g, 1, null, camera);
            }
            if (levelManager.getCurrentLevelIndex()==1) {
                levelManager.drawTraps(g);

            }
            ArrayList<Entity> renderList = new ArrayList<>();
            if(levelManager.getStaticObjects() != null) renderList.addAll(levelManager.getStaticObjects());
            if (levelManager.getGate() != null) levelManager.getGate().Draw(g);
            if (levelManager.getChest() != null) renderList.add(levelManager.getChest());
            if (levelManager.getWine() != null) renderList.addAll(levelManager.getWine());
            if (entityManager != null) renderList.addAll(entityManager.getEnemies());
            if(player != null) renderList.add(player);
            if (levelManager.getNpc() != null) renderList.add(levelManager.getNpc());

            renderList.sort((e1, e2) -> {
                float y1 = e1.getY() + e1.getHeight();
                float y2 = e2.getY() + e2.getHeight();
                return Float.compare(y1, y2);
            });

            for (Entity e : renderList) {
                e.Draw(g);
                if (e instanceof Enemy)
                    ((Enemy) e).drawHealthBar(g);
            }


            if (currentMap != null && currentMap.getLayerCount() == 3) {
                currentMap.DrawLayer(g, 2, player, camera);
            }
            if(levelManager.getCurrentLevelIndex()==0)
                g.drawImage(Assets.objectsMap.get("skull"), 2125,1050, 48*2,64*2 , null);

            if (levelManager.getFogOfWar() != null)levelManager.getFogOfWar().Draw(g, camera);

            // Resetează transformările camerei pentru a desena UI pe ecran fix
            g2d.setTransform(oldTransform);

            // Desenare HUD Player
            if (player != null) player.drawHUD(g);

            // Desenare Overlay de Pauză (Efect semi-transparent de întunecare a jocului)
            if (currentState == GameState.PAUSE) {
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, currentWidth, currentHeight);

                // Desenăm butoanele de pauză peste acest overlay
                btnResume.Draw(g);
                btnSave.Draw(g);
                btnSettingsPause.Draw(g);
                btnMainMenu.Draw(g);
            }
        } else {
            // Dacă nu e PLAYING sau PAUSE, sigur e MENU sau SETTINGS, deci setăm camera normală
            g2d.setTransform(oldTransform);
        }

        if (currentState == GameState.MENU) {
            g.drawImage(Assets.menuBackground, 0, 0, currentWidth, currentHeight, null);
            btnStart.Draw(g);
            btnLoad.Draw(g);
            btnSettingsMenu.Draw(g);
            btnExit.Draw(g);
            btnLeaderBoard.Draw(g);
        } else if (currentState == GameState.SETTINGS) {
            g.drawImage(Assets.menuBackground, 0, 0, currentWidth, currentHeight, null);
            btnToggleSound.Draw(g);
            btnToggleFS.Draw(g);
            btnControls.Draw(g);
            btnBack.Draw(g);
        } else if (currentState == GameState.CONTROLS) {
            g.drawImage(Assets.menuBackground, 0, 0, currentWidth, currentHeight, null);

            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, currentWidth, currentHeight);

            int originalW = Assets.tutorialImage.getWidth();
            int originalH = Assets.tutorialImage.getHeight();

            int imgW = 1100;
            int imgH = (imgW * originalH) / originalW;

            int xPos = (currentWidth - imgW) / 2;
            int yPos = (currentHeight - imgH) / 2;

            g.drawImage(Assets.tutorialImage, xPos, yPos, imgW, imgH, null);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Press ESC to return", currentWidth / 2 - 120, yPos + imgH + 40);
        }
        else if (currentState == GameState.WIN) {
            drawWinScreen(g);
        }
        else if (currentState == GameState.LEADERBOARD)
        {
        drawLeaderboard(g);
        }

        if (levelManager != null) {
            levelManager.Draw(g2d, currentWidth, currentHeight);
        }

        bs.show();
        g.dispose();
    }
    private void drawLeaderboard(Graphics g) {
        int W = wnd.GetWndWidth();
        int H = wnd.GetWndHeight();

        //Wallpaper
        g.drawImage(Assets.menuBackground, 0, 0, W, H, null);

        //overlay for the leaderboard
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(100, 100, W - 200, H - 200);

        //title
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.setColor(new Color(255, 215, 0));
        FontMetrics fmTitle = g.getFontMetrics();
        String title = "TOP 10 HEROES";
        int titleX = W / 2 - fmTitle.stringWidth(title) / 2;
        g.drawString(title, titleX, 160);

        //colomn
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.setColor(Color.WHITE);
        int headerY = 220;
        g.drawString("No.",  150, headerY);
        g.drawString("Name", 300, headerY);
        g.drawString("Score", 600, headerY);
        g.drawString("Time", 800, headerY);

        //separator line
        g.setColor(new Color(255, 215, 0));
        g.drawLine(150, headerY + 10, 900, headerY + 10);

        //data from database
        int startY = headerY + 45;
        int spacingY = 45;
        ArrayList<String[]> scores = leaderBoardManager.getTopScores();

        for (int i = 0; i < scores.size(); i++) {
            //
            if (i == 0)      g.setColor(new Color(255, 215, 0));   // gold
            else if (i == 1) g.setColor(new Color(188, 198, 204)); // silver
            else if (i == 2) g.setColor(new Color(205, 127, 50));  // bronze
            else             g.setColor(new Color(169, 169, 169));  // gray

            String[] row = scores.get(i);
            int currentY = startY + (i * spacingY);
            g.drawString(row[0], 150, currentY); // rank
            g.drawString(row[1], 300, currentY); // name
            g.drawString(row[2], 600, currentY); // score
            g.drawString(row[3], 800, currentY); // time
        }

        //back button
        btnBack.Draw(g);
    }
    private void drawWinScreen(Graphics g) {
        g.drawImage(Assets.menuBackground, 0, 0, wnd.GetWndWidth(), wnd.GetWndHeight(), null);

        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());

        //title
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        FontMetrics fm = g.getFontMetrics();
        String victoryText = "VICTORY!";
        g.drawString(victoryText, wnd.GetWndWidth() / 2 - fm.stringWidth(victoryText) / 2, 200);

        //statistics
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        g.drawString("Final Score: " + player.getScore(), wnd.GetWndWidth()/2 - 100, 300);
        g.drawString("Time: " + leaderBoardManager.formatTime((int)(getElapsedTime()/1000)), wnd.GetWndWidth()/2 - 80, 350);

        //Your name
        g.drawString("Enter Your Name:", wnd.GetWndWidth()/2 - 120, 450);
        g.setColor(Color.GRAY);
        g.fillRect(wnd.GetWndWidth()/2 - 150, 480, 300, 50);
        g.setColor(Color.WHITE);
        g.drawRect(wnd.GetWndWidth()/2 - 150, 480, 300, 50);
        g.drawString(playerNameInput.toString() + "|", wnd.GetWndWidth()/2 - 140, 515);

        btnSubmit.Draw(g);
    }
    public void saveProgress() {
        SaveManager.save(
                player.getX(),
                player.getY(),
                player.getHealth(),
                player.getScore(),
                getElapsedTime(),
                player.isDrunk(),
                player.getDrunkTimer(),
                player.isHasKey(),
                player.isDead(),
                levelManager.getCurrentLevelIndex(),
                entityManager.getEnemies(),
                levelManager.getFogOfWar(),
                levelManager.getGate(),
                levelManager.getChest(),
                levelManager.getWine()
        );
    }

    public void loadProgress() {
        Properties props = SaveManager.load();
        if (props == null) {
            System.out.println("Nu s-a găsit niciun fișier de salvare.");
            return;
        }

        // 1. Load Level
        int level = Integer.parseInt(props.getProperty("currentLevel"));
        levelManager.loadLevelDirect(level);

        // 2. Player Data
        player.setX(Float.parseFloat(props.getProperty("playerX")));
        player.setY(Float.parseFloat(props.getProperty("playerY")));
        player.setHealth(Integer.parseInt(props.getProperty("playerHealth")));
        player.setScore(Integer.parseInt(props.getProperty("playerScore")));
        player.setDrunk(Boolean.parseBoolean(props.getProperty("playerIsDrunk")));
        player.setHasKey(Boolean.parseBoolean(props.getProperty("playerHasKey")));


        int savedDrunkTimer = Integer.parseInt(props.getProperty("playerDrunkTimer"));
        if (savedDrunkTimer > 0) {
            player.setDrunk(false);
            player.startDrunk(savedDrunkTimer);
        } else {
            player.setDrunk(false);
            player.setDrunkTimer(0);
        }

        boolean isDead = Boolean.parseBoolean(props.getProperty("playerIsDead"));
        player.setDead(isDead);

        if (!isDead) {
            player.setCurrentKey("IDLE_DOWN");
        } else {
            player.setCurrentKey("DIE_" + player.getLastDirection());
        }

        // 3. Timer
        long savedElapsed = Long.parseLong(props.getProperty("elapsedTime"));
        this.startTime = System.currentTimeMillis() - savedElapsed;

        // 4. Chest
        if (props.containsKey("chest_x")) {
            int cx = (int) Float.parseFloat(props.getProperty("chest_x"));
            int cy = (int) Float.parseFloat(props.getProperty("chest_y"));
            int chestState = Integer.parseInt(props.getProperty("chest_open"));
            Chest chest = new Chest(this, cx, cy);
            chest.setState(chestState);
            levelManager.setChest(chest);

        }

        // 5. Gate
        Gate gate = levelManager.getGate();
        if (gate != null && props.containsKey("gate_open")) {
            gate.setOpen(Boolean.parseBoolean(props.getProperty("gate_open")));

        }

        // 6. WineBarrel
        levelManager.getWine().clear();
        int wineCount = Integer.parseInt(props.getProperty("wineCount"));
        for (int i = 0; i < wineCount; i++) {
            int wx = (int) Float.parseFloat(props.getProperty("wine_" + i + "_x"));
            int wy = (int) Float.parseFloat(props.getProperty("wine_" + i + "_y"));
            WineBarrel wb = new WineBarrel(this, wx, wy);
            wb.setState(Integer.parseInt(props.getProperty("wine_" + i + "_state")));
            levelManager.setWine(wb);
        }

        // 7. Enemies
        entityManager.getEnemies().clear();
        int enemyCount = Integer.parseInt(props.getProperty("enemyCount"));
        for (int i = 0; i < enemyCount; i++) {
            float ex = Float.parseFloat(props.getProperty("enemy_" + i + "_x"));
            float ey = Float.parseFloat(props.getProperty("enemy_" + i + "_y"));
            String type = props.getProperty("enemy_" + i + "_type");

            EnemyType eType;
            switch(type) {
                case "ORC_WARRIOR": eType = EnemyType.ORC_WARRIOR; break;
                case "ORC_BRUTE": eType = EnemyType.ORC_BRUTE; break;
                case "ORC_BOSS": eType = EnemyType.ORC_BOSS; break;
                case "NATURE_SLIME": eType = EnemyType.NATURE_SLIME; break;
                case "POISON_SLIME": eType = EnemyType.POISON_SLIME; break;
                case "FIRE_SLIME": eType = EnemyType.FIRE_SLIME; break;
                default: eType = EnemyType.ORC_WARRIOR; // Fallback de siguranță
            }

            Enemy newEnemy = EnemyFactory.createEnemy(eType, this, ex, ey);
            entityManager.addEnemy(newEnemy);
        }

        // 8. Fog of War
        FogOfWar fogOfWar= levelManager.getFogOfWar();
        String fogData = props.getProperty("fogData");

        if (fogData != null) {
            if (fogOfWar == null) {
                fogOfWar = new FogOfWar(currentMap.getWidthInPixels() / 48, currentMap.getHeightInPixels() / 48);
            }

            int[][] state = fogOfWar.getFogState();
            int index = 0;
            for (int y = 0; y < fogOfWar.getRows(); y++) {
                for (int x = 0; x < fogOfWar.getColumns(); x++) {
                    if (index < fogData.length()) {
                        state[x][y] = Character.getNumericValue(fogData.charAt(index));
                        index++;
                    }
                }
            }
        }

        setCurrentState(GameState.PLAYING);
    }

    public void toggleSound() {
        isMuted = !isMuted;
        if (backgroundMusic != null) {
            backgroundMusic.setMute(isMuted);
        }
    }

    public void toggleFullscreen() {
        isFullscreen = !isFullscreen;
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        if (isFullscreen) {
            // Trecem la Fullscreen
            wnd.getFrame().dispose(); // Distrugem temporar fereastra pentru a schimba stilul
            wnd.getFrame().setUndecorated(true);
            gd.setFullScreenWindow(wnd.getFrame());
            wnd.getFrame().setVisible(true);
        } else {
            // Revenim la 1920x1080 (Windowed)
            gd.setFullScreenWindow(null);
            wnd.getFrame().dispose();
            wnd.getFrame().setUndecorated(false);
            wnd.getFrame().setSize(1920, 1080);
            wnd.getFrame().setLocationRelativeTo(null);
            wnd.getFrame().setVisible(true);
        }
    }

    public boolean isMuted() {
        return isMuted;
    }

    public boolean isFullscreen() {
        return isFullscreen;
    }

    public void setPreviousState(GameState state) {
        this.previousState = state;
    }

    public GameState getPreviousState() {
        return previousState;
    }
    public GameState getCurrentState() {
        return currentState;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public KeyManager GetKeyManager() {
        return keyManager;
    }
    public void setCurrentState(GameState state) {
        if (state == GameState.WIN) {
            winTime = System.currentTimeMillis();
        } else if (state == GameState.PLAYING && currentState == GameState.MENU) {
            playerNameInput.setLength(0);
        }
        this.currentState = state;
    }

    public void setCurrentMap(Map map) {
        this.currentMap = map;
    }

    public Map getMap() {
        return currentMap;
    }

    public Player getPlayer() {
        return player;
    }
    public LevelManager getLevelManager() {
        return levelManager;
    }

    public Map[] getMaps() {
        return maps;
    }

    public MouseManager getMouseManager() {
        return mouseManager;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public LeaderBoardManager getLeaderBoardManager()
    {
        return leaderBoardManager;
    }
    public StringBuilder getPlayerNameInput() {
        return playerNameInput;
    }
}