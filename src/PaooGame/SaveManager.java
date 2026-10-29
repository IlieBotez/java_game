package PaooGame;

import PaooGame.Entities.*;
import PaooGame.Entities.Objects.InteractableObjects.Chest;
import PaooGame.Entities.Objects.InteractableObjects.Gate;
import PaooGame.Entities.Objects.InteractableObjects.WineBarrel;
import PaooGame.Graphics.FogOfWar;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class SaveManager {
    private static final String SAVE_FILE = "savegame.properties";

    public static void save(float pX, float pY, int pHealth, int score, long elapsedTime, boolean isDrunk, int drunkTimer, boolean hasKey, boolean isDead,
                            int level, ArrayList<Enemy> enemies, FogOfWar fow,
                            Gate gate, Chest chest, ArrayList<WineBarrel> wineBarrels) {
        Properties props = new Properties();

        // 1. player data
        props.setProperty("playerX", String.valueOf(pX));
        props.setProperty("playerY", String.valueOf(pY));
        props.setProperty("playerHealth", String.valueOf(pHealth));
        props.setProperty("playerScore", String.valueOf(score));
        props.setProperty("playerIsDrunk", String.valueOf(isDrunk));
        props.setProperty("playerDrunkTimer", String.valueOf(drunkTimer));
        props.setProperty("playerHasKey", String.valueOf(hasKey));
        props.setProperty("elapsedTime", String.valueOf(elapsedTime));
        props.setProperty("currentLevel", String.valueOf(level));

        // 2. enemy data
        props.setProperty("enemyCount", String.valueOf(enemies.size()));
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            String type = "UNKNOWN";

            if (e instanceof OrcWarrior) type = "ORC_WARRIOR";
            else if (e instanceof OrcBrute) type = "ORC_BRUTE";
            else if (e instanceof OrcBoss) type = "ORC_BOSS";
            else if (e instanceof NatureSlime) type = "NATURE_SLIME";
            else if (e instanceof PoisonSlime) type = "POISON_SLIME";
            else if (e instanceof FireSlime) type = "FIRE_SLIME";

            props.setProperty("enemy_" + i + "_type", type);
            props.setProperty("enemy_" + i + "_x", String.valueOf(e.getX()));
            props.setProperty("enemy_" + i + "_y", String.valueOf(e.getY()));
            props.setProperty("enemy_" + i + "_hp", String.valueOf(e.getHealth()));
        }

        // 3. the gate and the chest
        if (gate != null) {
            props.setProperty("gate_open", String.valueOf(gate.getIsOpen()));
        }
        if (chest != null) {
            props.setProperty("chest_x", String.valueOf(chest.getX()));
            props.setProperty("chest_y", String.valueOf(chest.getY()));
            props.setProperty("chest_open", String.valueOf(chest.getState()));
        }

        // 4. Wine barrels
        props.setProperty("wineCount", String.valueOf(wineBarrels.size()));
        for (int i = 0; i < wineBarrels.size(); i++) {
            WineBarrel wb = wineBarrels.get(i);
            props.setProperty("wine_" + i + "_x", String.valueOf(wb.getX()));
            props.setProperty("wine_" + i + "_y", String.valueOf(wb.getY()));
            props.setProperty("wine_" + i + "_state", String.valueOf(wb.getState()));
        }

        // 5. Fog of War
        if (fow != null) {
            StringBuilder fogBuilder = new StringBuilder();
            int[][] state = fow.getFogState();
            for (int y = 0; y < fow.getRows(); y++) {
                for (int x = 0; x < fow.getColumns(); x++) {
                    fogBuilder.append(state[x][y]);
                }
            }
            props.setProperty("fogData", fogBuilder.toString());
        }

        try (OutputStream out = new FileOutputStream(SAVE_FILE)) {
            props.store(out, "Game Save Data");
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static Properties load() {
        Properties props = new Properties();
        File file = new File(SAVE_FILE);
        if (!file.exists()) return null;
        try (InputStream in = new FileInputStream(file)) {
            props.load(in);
        } catch (IOException e) { e.printStackTrace(); }
        return props;
    }
}