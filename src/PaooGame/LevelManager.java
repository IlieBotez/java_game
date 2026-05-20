package PaooGame;

import PaooGame.Entities.*;
import PaooGame.Entities.Objects.InteractableObjects.*;

import PaooGame.Graphics.Assets;
import PaooGame.Graphics.FogOfWar;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class LevelManager {
    private Game game;
    private Player player;
    private boolean isTransitioning = false;
    private float transitionAlpha = 0.0f;
    private boolean fadingOut = true;
    private int level;

    private Gate gate;
    private Chest chest;
    private ArrayList<WineBarrel> wine = new ArrayList<>();
    private ArrayList<StaticObjects> objs = new ArrayList<>();
    private NPC npc;

    private ArrayList<SpikeTrap> spikeTraps = new ArrayList<>();
    private ArrayList<BladeTrap> bladeTraps = new ArrayList<>();
    private ArrayList<ArrowTrap> arrowTraps = new ArrayList<>();

    private FogOfWar fogOfWar;
    public LevelManager(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    public void startTransition(int levelIndex) {
        isTransitioning = true;
        fadingOut = true;
        transitionAlpha = 0.0f;
        level = levelIndex;
        game.getEntityManager().getEnemies().clear();
        wine.clear();
        chest = null;
        gate = null;
        if (levelIndex != 0)
            objs.clear();
        spikeTraps.clear();
        bladeTraps.clear();
        arrowTraps.clear();
        fogOfWar = null;
    }

    public void Update() {
        if (!isTransitioning) return;
        float transitionSpeed = 0.02f;
        if (fadingOut) {
            transitionAlpha += transitionSpeed;
            if (transitionAlpha >= 1.0f) {
                transitionAlpha = 1.0f;
                fadingOut = false;
                loadLevel();
            }
        } else {
            transitionAlpha -= transitionSpeed;
            if (transitionAlpha <= 0.0f) {
                transitionAlpha = 0.0f;
                isTransitioning = false;
            }
        }
    }
    public void updateEnvironment() {
        switch (level) {
            case 0:
                updateLevel_1();
                break;
            case 1:
                updateLevel_2();
                break;
            case 2:
                updateLevel_3();
                break;
        }
    }

    private void loadLevel() {
        game.setCurrentState(Game.GameState.PLAYING);
        game.setCurrentMap(game.getMaps()[level]);
        switch (level) {
            case 0:
                setupLevel_1();
                break;
            case 1:
                setupLevel_2();
                break;
            case 2:
                setupLevel_3();
                break;
        }
    }

    public void loadLevelDirect(int levelIndex) {
        this.level = levelIndex;
        game.setCurrentMap(game.getMaps()[levelIndex]);
        game.setCurrentState(Game.GameState.PLAYING);
        game.getEntityManager().getEnemies().clear();
        wine.clear();
        objs.clear();
        spikeTraps.clear();
        bladeTraps.clear();
        arrowTraps.clear();
        fogOfWar = null;
        switch (levelIndex) {
            case 0: setupLevel_1(); break;
            case 1: setupLevel_2(); break;
            case 2: setupLevel_3(); break;
        }
    }

    private void setupLevel_1() {
        fogOfWar = new FogOfWar(game.getMap().getWidthInPixels() / 48, game.getMap().getHeightInPixels() / 48);
        npc = new NPC(game, 2540, 895);

        objs.clear();
        addStaticObjects();

        player.setY(200);
        player.setX(200);
        player.setHealth(10);
        player.setDead(false);
        player.setCurrentKey("IDLE_DOWN");

        ArrayList<ObjectCoordinate> chestAndWine = new ArrayList<>();
        loadCoodinates1(chestAndWine);
        wine.clear();
        game.getEntityManager().getEnemies().clear();

        Random random = new Random();
        int randomPositionIndex = random.nextInt(chestAndWine.size());
        ObjectCoordinate randomPosition = chestAndWine.remove(randomPositionIndex);
        chest = new Chest(game, (int) randomPosition.x, (int) randomPosition.y);
        gate = new Gate(game, 2832, 96, false);

        for (int i = 0; i < 2; i++) {
            randomPositionIndex = random.nextInt(chestAndWine.size());
            randomPosition = chestAndWine.remove(randomPositionIndex);
            wine.add(new WineBarrel(game, (int) randomPosition.x, (int) randomPosition.y));
        }

        EntityManager entityManager = game.getEntityManager();

        entityManager.addEnemy(EnemyFactory.createEnemy(EnemyType.ORC_WARRIOR, game, 318f, 775f));
        entityManager.addEnemy(EnemyFactory.createEnemy(EnemyType.ORC_WARRIOR, game, 645f, 1875f));
        entityManager.addEnemy(EnemyFactory.createEnemy(EnemyType.ORC_BRUTE, game, 95f, 1828f));
        entityManager.addEnemy(EnemyFactory.createEnemy(EnemyType.ORC_WARRIOR, game, 1710f, 2655f));
        entityManager.addEnemy(EnemyFactory.createEnemy(EnemyType.ORC_BRUTE, game, 1050f, 3111f));
        entityManager.addEnemy(EnemyFactory.createEnemy(EnemyType.ORC_BRUTE, game, 3190f, 3097f));
        entityManager.addEnemy(EnemyFactory.createEnemy(EnemyType.ORC_WARRIOR, game, 2580f, 2673f));
        entityManager.addEnemy(EnemyFactory.createEnemy(EnemyType.ORC_BRUTE, game, 2870f, 1998f));
        entityManager.addEnemy(EnemyFactory.createEnemy(EnemyType.ORC_WARRIOR, game, 2156f, 1808f));
        entityManager.addEnemy(EnemyFactory.createEnemy(EnemyType.ORC_BRUTE, game, 1274f, 2197f));
        entityManager.addEnemy(EnemyFactory.createEnemy(EnemyType.ORC_WARRIOR, game, 1329f, 1568f));
        entityManager.addEnemy(EnemyFactory.createEnemy(EnemyType.ORC_BRUTE, game, 984f, 260f));
        entityManager.addEnemy(EnemyFactory.createEnemy(EnemyType.ORC_WARRIOR, game, 2031f, 555f));
        entityManager.addEnemy(EnemyFactory.createEnemy(EnemyType.ORC_BRUTE, game, 2655f, 1509f));

    }

    public void updateLevel_1()
    {
        if (fogOfWar != null) fogOfWar.Update(player);
        if (chest != null) chest.Update();
        if (gate != null) gate.Update();
        if (wine != null) {
            for (WineBarrel i : wine) i.Update();
        }
        if (npc != null) npc.Update();
    }

    public void setupLevel_2()
    {
        player.setY(2000);
        player.setX(200);
        gate = new Gate(game, 144, 96, true);
        loadSpikesFromMap();
        //second chamber
        bladeTraps.add(new BladeTrap(game, 720, 1179));
        bladeTraps.add(new BladeTrap(game, 860, 1179));
        bladeTraps.add(new BladeTrap(game, 1010, 1179));
        //third chamber
        bladeTraps.add(new BladeTrap(game,1950, 1510 ));
        bladeTraps.add(new BladeTrap(game,1950, 1600 ));
        bladeTraps.add(new BladeTrap(game,2050, 1555 ));

        //first chamber
        arrowTraps.add(new ArrowTrap(game, 100, 760, "DOWN", 2000, 1500));
        arrowTraps.add(new ArrowTrap(game, 250, 760, "DOWN", 1000, 750));
        arrowTraps.add(new ArrowTrap(game, 350, 760, "DOWN", 1000, 750));
        arrowTraps.add(new ArrowTrap(game, 712, 760, "DOWN", 1500, 1000));
        arrowTraps.add(new ArrowTrap(game, 992, 760, "DOWN", 1500, 1000));
        arrowTraps.add(new ArrowTrap(game, 1135, 760, "DOWN", 1500, 1000));
        arrowTraps.add(new ArrowTrap(game, 852, 760, "DOWN", 1500, 1000));

        //second chamber
        arrowTraps.add(new ArrowTrap(game, 680, 1607, "RIGHT", 2000, 1000));
        arrowTraps.add(new ArrowTrap(game, 680, 1707, "RIGHT", 2000, 1000));
        arrowTraps.add(new ArrowTrap(game, 680, 1807, "RIGHT", 2000, 1000));
        arrowTraps.add(new ArrowTrap(game, 680, 1907, "RIGHT", 2000, 1000));
        arrowTraps.add(new ArrowTrap(game, 730, 2285, "UP", 2000, 1000));

        //third chamber - right side
        arrowTraps.add(new ArrowTrap(game, 2256, 290, "DOWN", 3500, 600));
        arrowTraps.add(new ArrowTrap(game, 2201, 2270, "UP", 3500, 600));
        //third chamber - left side
        arrowTraps.add(new ArrowTrap(game, 1770, 633, "DOWN", 1500, 800));
        arrowTraps.add(new ArrowTrap(game, 1820, 1090, "UP", 1500, 1000));
        arrowTraps.add(new ArrowTrap(game, 1860, 633, "DOWN", 1500, 700));
        arrowTraps.add(new ArrowTrap(game, 1930, 1090, "UP", 1500, 900));

        //final chamber
        arrowTraps.add(new ArrowTrap(game, 70, 270, "RIGHT", 3500, 700));
        arrowTraps.add(new ArrowTrap(game, 70, 370, "RIGHT", 3500, 750));
        arrowTraps.add(new ArrowTrap(game, 70, 470, "RIGHT", 3500, 800));
        arrowTraps.add(new ArrowTrap(game, 280, 140, "DOWN", 3000, 800));
        arrowTraps.add(new ArrowTrap(game, 380, 140, "DOWN", 3000, 750));
        arrowTraps.add(new ArrowTrap(game, 480, 140, "DOWN", 3000, 700));
        arrowTraps.add(new ArrowTrap(game, 580, 140, "DOWN", 3000, 650));
        arrowTraps.add(new ArrowTrap(game, 680, 140, "DOWN", 3000, 700));
        arrowTraps.add(new ArrowTrap(game, 780, 140, "DOWN", 3000, 750));
        arrowTraps.add(new ArrowTrap(game, 880, 140, "DOWN", 3000, 800));
        arrowTraps.add(new ArrowTrap(game, 980, 140, "DOWN", 3000, 750));
        arrowTraps.add(new ArrowTrap(game, 1080, 140, "DOWN", 3000, 700));
        arrowTraps.add(new ArrowTrap(game, 1180, 140, "DOWN", 3000, 650));
        arrowTraps.add(new ArrowTrap(game, 1280, 140, "DOWN", 3000, 700));
        arrowTraps.add(new ArrowTrap(game, 1380, 140, "DOWN", 3000, 750));
        arrowTraps.add(new ArrowTrap(game, 1480, 140, "DOWN", 3000, 800));
        arrowTraps.add(new ArrowTrap(game, 1580, 140, "DOWN", 3000, 750));

    }

    public void updateLevel_2()
    {
        if (isTransitioning) return;

        for (SpikeTrap spikeTrap : spikeTraps) {
            spikeTrap.Update();
        }
        for(BladeTrap bladeTrap: bladeTraps)
            bladeTrap.Update();
        for (ArrowTrap trap : arrowTraps) trap.Update();

        if(gate != null) {
            gate.Update();
        }
    }

    public void setupLevel_3()
    {
        player.setY(200);
        player.setX(1000);
        player.setHealth(10);

        fogOfWar = new FogOfWar(game.getMap().getWidthInPixels() / 48, game.getMap().getHeightInPixels() / 48);

        game.getEntityManager().getEnemies().clear();
        EntityManager entityManager = game.getEntityManager();

        float bossX = 1135f;
        float bossY = 1150f;
        Enemy boss = EnemyFactory.createEnemy(EnemyType.ORC_BOSS, game, bossX, bossY);

        if (boss != null) {
            entityManager.addEnemy(boss);
        }
    }

    private void updateLevel_3() {
        if (fogOfWar != null) fogOfWar.Update(player);
    }

    public void Draw(Graphics2D g2d, int screenWidth, int screenHeight) {
        if (isTransitioning) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transitionAlpha));
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, screenWidth, screenHeight);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }

    private void loadCoodinates1(ArrayList<ObjectCoordinate> chestAndWine) {
        chestAndWine.add(new ObjectCoordinate(112, 1532));
        chestAndWine.add(new ObjectCoordinate(888, 2998));
        chestAndWine.add(new ObjectCoordinate(3168, 2298));
        chestAndWine.add(new ObjectCoordinate(3192, 1800));
        chestAndWine.add(new ObjectCoordinate(1340, 184));
        chestAndWine.add(new ObjectCoordinate(1358, 1478));
        chestAndWine.add(new ObjectCoordinate(3194, 2052));
        chestAndWine.add(new ObjectCoordinate(2348, 1784));
        chestAndWine.add(new ObjectCoordinate(3178, 180));
        chestAndWine.add(new ObjectCoordinate(1300, 2286));
    }

    private void loadSpikesFromMap() {
        int mapWidth = game.getMap().getWidth();
        int mapHeight = game.getMap().getHeight();

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                PaooGame.Tiles.Tile t = game.getMap().GetTile(x * 48, y * 48, 1);

                if (t != null && (t.GetId() == 317 || t.GetId() == 318)) {
                    spikeTraps.add(new SpikeTrap(game, x * 48, y * 48));
                }
            }
        }
    }

    public ArrayList<SpikeTrap> getSpikeTraps() {
        return spikeTraps;
    }

    public void drawTraps(Graphics g) {
        if (isTransitioning && fadingOut && transitionAlpha > 0.8f) return;

        for (SpikeTrap trap : spikeTraps) {
            trap.Draw(g);
        }
        for(BladeTrap bladeTrap: bladeTraps)
            bladeTrap.Draw(g);
        for (ArrowTrap trap : arrowTraps) trap.Draw(g);

    }

    public void addStaticObjects()
    {
        objs.add(createStaticObject(2720, 192, 32*3, 48*3, Assets.objectsMap.get("redBanner")));
        objs.add(createStaticObject(2950, 192, 32*3, 48*3, Assets.objectsMap.get("redBanner")));
        objs.add(createStaticObject(1200-96, 3025, 48*2, 64*2, Assets.objectsMap.get("shelfWithSupplies")));
        objs.add(createStaticObject(1200, 3025, 48*2, 64*2, Assets.objectsMap.get("shelfWithSupplies")));
        objs.add(createStaticObject(1200+96, 3025, 48*2, 64*2, Assets.objectsMap.get("shelfWithSupplies")));
        objs.add(createStaticObject(1200+2*96, 3025, 48*2, 64*2, Assets.objectsMap.get("shelfWithSupplies")));
        objs.add(createStaticObject(1200+3*96, 3025, 48*2, 64*2, Assets.objectsMap.get("emptyShelf")));
        objs.add(createStaticObject(2225+96, 3025, 48*2, 64*2, Assets.objectsMap.get("weaponRack")));
        objs.add(createStaticObject(2225+2*96, 3025, 48*2, 64*2, Assets.objectsMap.get("weaponRack")));
        objs.add(createStaticObject(2225+3*96, 3025, 48*2, 64*2, Assets.objectsMap.get("weaponRack")));
        objs.add(createStaticObject(2225, 3025, 48*2, 64*2, Assets.objectsMap.get("emptyWeaponRack")));
        objs.add(createStaticObject(144, 96, 96, 96, Assets.objectsMap.get("closedSteelGate")));
    }

    public StaticObjects createStaticObject(int x, int y, int w, int h, BufferedImage image)
    {
        return new StaticObjects(game, x,y,w,h, image);
    }

    public int getCurrentLevelIndex() {
        return level;
    }
    public ArrayList<? extends Entity> getBladeTraps() {
        return bladeTraps;
    }
    public FogOfWar getFogOfWar()
    {
        return fogOfWar;
    }
    public void setFogOfWar(FogOfWar fogOfWar) {
        this.fogOfWar = fogOfWar;
    }
    public Gate getGate() { return gate; }
    public void setGate(Gate g) { this.gate = g; }
    public Chest getChest() { return chest; }
    public void setChest(Chest c) { this.chest = c; }

    public void setWine(WineBarrel b) { wine.add(b); }
    public ArrayList<WineBarrel> getWine() { return wine; }
    public ArrayList<StaticObjects> getStaticObjects() { return objs; }
    public NPC getNpc(){return npc;}
}

