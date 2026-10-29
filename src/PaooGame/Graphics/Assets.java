package PaooGame.Graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

public class Assets {
    public static final Map<String, BufferedImage[][]> anims = new HashMap<>();
    public static Map<String, BufferedImage> tiles = new HashMap<>();
    public static Map<String, BufferedImage> objectsMap= new HashMap<>();
    public static BufferedImage menuBackground, tutorialImage;
    public static Map<String, BufferedImage[][]> trapsAnim = new HashMap<>();

    public static void Init() {
        loadPlayerAnims();
        loadOrc1Anims();
        loadOrc2Anims();
        loadOrc3Anims();
        loadSlime1Anims();
        loadSlime2Anims();
        loadSlime3Anims();
        loadNPC();
        loadEnvironment();
    }

    private static void loadPlayerAnims() {
        anims.put("PLAYER_WALK", loadAnim("/textures/HeroTexture/Swordsman_lvl3_Walk_with_shadow.png", 4, 6));
        anims.put("PLAYER_RUN", loadAnim("/textures/HeroTexture/Swordsman_lvl3_Run_with_shadow.png", 4, 8));
        anims.put("PLAYER_IDLE", loadCustomAnim("/textures/HeroTexture/Swordsman_lvl3_Idle_with_shadow.png", new int[]{12, 12, 12, 4}));
        anims.put("PLAYER_ATTACK", loadAnim("/textures/HeroTexture/Swordsman_lvl3_attack_with_shadow.png", 4, 8));
        anims.put("PLAYER_WALK_ATTACK", loadAnim("/textures/HeroTexture/Swordsman_lvl3_Walk_Attack_with_shadow.png", 4, 6));
        anims.put("PLAYER_RUN_ATTACK", loadAnim("/textures/HeroTexture/Swordsman_lvl3_Run_Attack_with_shadow.png", 4, 8));
        anims.put("PLAYER_HURT", loadAnim("/textures/HeroTexture/Swordsman_lvl3_Hurt_with_shadow.png", 4, 5));
        anims.put("PLAYER_DIE", loadAnim("/textures/HeroTexture/Swordsman_lvl3_Death_with_shadow.png", 4, 7));
    }

    private static void loadOrc1Anims() {
        anims.put("ORC1_WALK", loadAnim("/textures/OrcsTexture/Orc1/orc1_walk_with_shadow.png", 4, 6));
        anims.put("ORC1_RUN", loadAnim("/textures/OrcsTexture/Orc1/orc1_run_with_shadow.png", 4, 8));
        anims.put("ORC1_IDLE", loadAnim("/textures/OrcsTexture/Orc1/orc1_idle_with_shadow.png", 4, 4));
        anims.put("ORC1_ATTACK", loadAnim("/textures/OrcsTexture/Orc1/orc1_attack_with_shadow.png", 4, 8));
        anims.put("ORC1_WALK_ATTACK", loadAnim("/textures/OrcsTexture/Orc1/orc1_walk_attack_front _with_shadow.png", 4, 6));
        anims.put("ORC1_RUN_ATTACK", loadAnim("/textures/OrcsTexture/Orc1/orc1_run_attack_front_with_shadow.png", 4, 8));
        anims.put("ORC1_HURT", loadAnim("/textures/OrcsTexture/Orc1/orc1_hurt_with_shadow.png", 4, 6));
        anims.put("ORC1_DIE", loadAnim("/textures/OrcsTexture/Orc1/orc1_death_with_shadow.png", 4, 8));
    }

    private static void loadOrc2Anims() {
        anims.put("ORC2_WALK", loadAnim("/textures/OrcsTexture/Orc2/orc2_walk_with_shadow.png", 4, 6));
        anims.put("ORC2_RUN", loadAnim("/textures/OrcsTexture/Orc2/orc2_run_with_shadow.png", 4, 8));
        anims.put("ORC2_IDLE", loadAnim("/textures/OrcsTexture/Orc2/orc2_idle_with_shadow.png", 4, 4));
        anims.put("ORC2_ATTACK", loadAnim("/textures/OrcsTexture/Orc2/orc2_attack_with_shadow.png", 4, 8));
        anims.put("ORC2_WALK_ATTACK", loadAnim("/textures/OrcsTexture/Orc2/orc2_walk_attack_with_shadow.png", 4, 6));
        anims.put("ORC2_RUN_ATTACK", loadAnim("/textures/OrcsTexture/Orc2/orc2_run_attack_with_shadow.png", 4, 8));
        anims.put("ORC2_HURT", loadAnim("/textures/OrcsTexture/Orc2/orc2_hurt_with_shadow.png", 4, 6));
        anims.put("ORC2_DIE", loadAnim("/textures/OrcsTexture/Orc2/orc2_death_with_shadow.png", 4, 8));
    }
    
    private static void loadOrc3Anims() {
        anims.put("ORC3_IDLE", loadAnim("/textures/OrcsTexture/Orc3/orc3_idle_with_shadow.png", 4, 4));
        anims.put("ORC3_ATTACK", loadAnim("/textures/OrcsTexture/Orc3/orc3_attack_with_shadow.png", 4, 8));
        anims.put("ORC3_HURT", loadAnim("/textures/OrcsTexture/Orc3/orc3_hurt_with_shadow.png", 4, 6));
        anims.put("ORC3_DIE", loadAnim("/textures/OrcsTexture/Orc3/orc3_death_with_shadow.png", 4, 8));
    }

    private static void loadSlime1Anims() {
        anims.put("SLIME1_WALK", loadAnim("/textures/SlimesTexture/Slime1/Slime1_Walk_with_shadow.png",4, 8));
        anims.put("SLIME1_RUN", loadAnim("/textures/SlimesTexture/Slime1/Slime1_Run_with_shadow.png", 4, 8));
        anims.put("SLIME1_IDLE", loadAnim("/textures/SlimesTexture/Slime1/Slime1_Idle_with_shadow.png", 4, 6));
        anims.put("SLIME1_ATTACK", loadAnim("/textures/SlimesTexture/Slime1/Slime1_Attack_with_shadow.png", 4, 10));
        anims.put("SLIME1_HURT", loadAnim("/textures/SlimesTexture/Slime1/Slime1_Hurt_with_shadow.png", 4, 5));
        anims.put("SLIME1_DIE", loadAnim("/textures/SlimesTexture/Slime1/Slime1_Death_with_shadow.png", 4, 10));
    }

    private static void loadSlime2Anims() {
        anims.put("SLIME2_WALK", loadAnim("/textures/SlimesTexture/Slime2/Slime2_Walk_with_shadow.png",4, 8));
        anims.put("SLIME2_RUN", loadAnim("/textures/SlimesTexture/Slime2/Slime2_Run_with_shadow.png", 4, 8));
        anims.put("SLIME2_IDLE", loadAnim("/textures/SlimesTexture/Slime2/Slime2_Idle_with_shadow.png", 4, 6));
        anims.put("SLIME2_ATTACK", loadAnim("/textures/SlimesTexture/Slime2/Slime2_Attack_with_shadow.png", 4, 11));
        anims.put("SLIME2_HURT", loadAnim("/textures/SlimesTexture/Slime2/Slime2_Hurt_with_shadow.png", 4, 5));
        anims.put("SLIME2_DIE", loadAnim("/textures/SlimesTexture/Slime2/Slime2_Death_with_shadow.png", 4, 10));
    }

    private static void loadSlime3Anims() {
        anims.put("SLIME3_WALK", loadAnim("/textures/SlimesTexture/Slime3/Slime3_Walk_with_shadow.png",4, 8));
        anims.put("SLIME3_RUN", loadAnim("/textures/SlimesTexture/Slime3/Slime3_Run_with_shadow.png", 4, 8));
        anims.put("SLIME3_IDLE", loadAnim("/textures/SlimesTexture/Slime3/Slime3_Idle_with_shadow.png", 4, 6));
        anims.put("SLIME3_ATTACK", loadAnim("/textures/SlimesTexture/Slime3/Slime3_Attack_with_shadow.png", 4, 9));
        anims.put("SLIME3_HURT", loadAnim("/textures/SlimesTexture/Slime3/Slime3_Hurt_with_shadow.png", 4, 5));
        anims.put("SLIME3_DIE", loadAnim("/textures/SlimesTexture/Slime3/Slime3_Death_with_shadow.png", 4, 10));
    }

    private static void loadNPC() {
        anims.put("NPC_IDLE", loadAnim("/textures/NpcTexcture/Vampires1_Idle_with_shadow.png", 1, 4));
    }

    private static void loadEnvironment() {
        SpriteSheet walls_floor = new SpriteSheet(ImageLoader.LoadImage("/textures/DungeonTexture/walls_floor.png"), 16, 16);
        SpriteSheet objects = new SpriteSheet(ImageLoader.LoadImage("/textures/DungeonTexture/Objects.png"), 16, 16);
        SpriteSheet other_object = new SpriteSheet(ImageLoader.LoadImage("/textures/TrapsAndObjectsTexture/Other_objects.png"), 16, 16);
        SpriteSheet supplies_objects = new SpriteSheet(ImageLoader.LoadImage("/textures/TrapsAndObjectsTexture/supplies_objects.png"),16,16);
        SpriteSheet spike_trap = new SpriteSheet(ImageLoader.LoadImage("/textures/DungeonTexture/spike_trap.png"),16,16);
        SpriteSheet arrow_trap = new SpriteSheet(ImageLoader.LoadImage("/textures/TrapsAndObjectsTexture/Arrow.png"), 16, 16);
        addTile(tiles,"floor_s", walls_floor, 1, 6);
        addTile(tiles,"stairs", walls_floor, 10, 8);

        addTile(tiles,"black_wall", walls_floor, 1, 1);

        addTile(tiles,"wallBotLeft", walls_floor, 0, 4);
        addTile(tiles,"wallBotMid", walls_floor, 1, 4);
        addTile(tiles,"wallBotRight", walls_floor, 2, 4);

        addTile(tiles,"wallMidLeft", walls_floor, 0, 3);
        addTile(tiles,"wallMidMid", walls_floor, 1, 3);
        addTile(tiles,"wallMidRight", walls_floor, 2, 3);

        addTile(tiles,"wallTopLeft", walls_floor, 0, 2);
        addTile(tiles,"wallTopMid", walls_floor, 1, 2);
        addTile(tiles,"wallTopRight", walls_floor, 2, 2);

        addTile(tiles,"wallLeft", walls_floor, 0, 1);
        addTile(tiles,"wallRight", walls_floor, 2, 1);
        addTile(tiles,"wallBackLeft", walls_floor, 0, 0);
        addTile(tiles,"wallBackMid", walls_floor, 1, 0);
        addTile(tiles,"wallBackRight", walls_floor, 2, 0);

        addTile(tiles,"wallCornerNW", walls_floor, 3, 0);
        addTile(tiles,"wallCornerNE", walls_floor, 5, 0);
        addTile(tiles,"wallCornerSW", walls_floor, 3, 3);
        addTile(tiles,"wallCornerSE", walls_floor, 5, 3);
        addTile(tiles,"wallNW", walls_floor, 3, 1);
        addTile(tiles,"wallNE", walls_floor, 5, 1);

        addTile(tiles,"RoundedCornerWallLeft", walls_floor, 6, 2);
        addTile(tiles,"RoundedCornerWallLeftUp", walls_floor, 6, 1);
        addTile(tiles,"RoundedCornerWallRight", walls_floor, 8, 2);
        addTile(tiles,"RoundedCornerWallRightUp", walls_floor, 8, 1);

        addTile(tiles,"Stairs_CornerNW", walls_floor, 0, 5);
        addTile(tiles,"Stairs_CornerNE", walls_floor, 2, 5);
        addTile(tiles,"Stairs_CornerSW", walls_floor, 0, 7);
        addTile(tiles,"Stairs_CornerSE", walls_floor, 2, 7);
        addTile(tiles,"Stairs_SideLeft", walls_floor, 0, 6);
        addTile(tiles,"Stairs_SideUp", walls_floor, 1, 5);
        addTile(tiles,"Stairs_SideRight", walls_floor, 2, 6);
        addTile(tiles,"Stairs_SideDown", walls_floor, 1, 7);
        tiles.put("iceTile", ImageLoader.LoadImage("/textures/DungeonTexture/ice_tile.png"));
        addTile(tiles, "spikeDown", spike_trap, 0, 0);
        addTile(objectsMap,"closedGate", walls_floor, 7, 21, 2, 2);
        addTile(objectsMap,"openedGate", walls_floor, 3, 21, 2, 2);
        addTile(objectsMap,"closedSteelGate", walls_floor, 5, 21, 2, 2);

        addTile(objectsMap,"unusedWineBarrel", other_object, 0, 3, 2, 3);
        addTile(objectsMap,"usedWineBarrel", other_object, 2, 3, 2, 3);

        addTile(objectsMap,"key", objects, 3, 0);
        addTile(objectsMap,"chestClosed", objects, 8, 0, 2, 2);
        addTile(objectsMap,"chestOpen", objects, 10, 0, 2, 2);
        addTile(objectsMap,"redBanner", other_object,10,3, 2, 3 );
        addTile(objectsMap,"skull", other_object,7,0, 3, 3 );
        addTile(objectsMap,"shelfWithSupplies", supplies_objects,3,0, 3, 4 );
        addTile(objectsMap,"emptyShelf", supplies_objects,0,0, 3, 4 );
        addTile(objectsMap, "weaponRack", supplies_objects,3,23,3, 3);
        addTile(objectsMap, "emptyWeaponRack", supplies_objects,0,23,3, 3);
        objectsMap.put("drunk", ImageLoader.LoadImage("/textures/Buffs & Debuffs/poison.png"));
        objectsMap.put("poison", ImageLoader.LoadImage("/textures/Buffs & Debuffs/Icons_16.png"));
        objectsMap.put("slow", ImageLoader.LoadImage("/textures/Buffs & Debuffs/Icons_10.png"));
        menuBackground = ImageLoader.LoadImage("/textures/BackgroundImageMenu/menu_background.png");
        trapsAnim.put("spikes", loadAnim("/textures/DungeonTexture/spike_trap.png", 1, 3, 16));
        trapsAnim.put("bladeTrap", loadAnim("/textures/TrapsAndObjectsTexture/Rotating_blades.png",4,1,48));
        addTile(objectsMap, "arrow", arrow_trap, 1, 0, 1, 2);
        addTile(objectsMap, "wallHoleUp", arrow_trap, 0, 0);
        addTile(objectsMap, "wallHoleSides", arrow_trap, 0, 1);

        tutorialImage = ImageLoader.LoadImage(("/textures/BackgroundImageMenu/tutorial.png"));
    }

    private static void addTile(Map<String, BufferedImage> map,String name, SpriteSheet sheet, int x, int y) {
        map.put(name, sheet.crop(x, y));
    }

    private static void addTile(Map<String, BufferedImage> map, String name, SpriteSheet sheet, int x, int y, int w, int h) {
        map.put(name, sheet.crop(x, y, w, h));
    }
        // Încarcă o matrice perfect uniformă
        private static BufferedImage[][] loadAnim (String path,int rows, int framesPerRow){
            SpriteSheet sheet = new SpriteSheet(ImageLoader.LoadImage(path), 64, 64);
            BufferedImage[][] anim = new BufferedImage[rows][framesPerRow];
            for (int r = 0; r < rows; r++) {
                for (int f = 0; f < framesPerRow; f++) {
                    anim[r][f] = sheet.crop(f, r);
                }
            }
            return anim;
        }

        // Încarcă o matrice unde rândurile au număr diferit de cadre (ex: IDLE la jucător)
        private static BufferedImage[][] loadCustomAnim (String path,int[] framesPerRow){
            SpriteSheet sheet = new SpriteSheet(ImageLoader.LoadImage(path), 64, 64);
            BufferedImage[][] anim = new BufferedImage[framesPerRow.length][];
            for (int r = 0; r < framesPerRow.length; r++) {
                anim[r] = new BufferedImage[framesPerRow[r]];
                for (int f = 0; f < framesPerRow[r]; f++) {
                    anim[r][f] = sheet.crop(f, r);
                }
            }
            return anim;
        }
        private static BufferedImage[][] loadAnim (String path,int rows, int framesPerRow, int size){
        SpriteSheet sheet = new SpriteSheet(ImageLoader.LoadImage(path), size, size);
        BufferedImage[][] anim = new BufferedImage[rows][framesPerRow];
        for (int r = 0; r < rows; r++) {
            for (int f = 0; f < framesPerRow; f++) {
                anim[r][f] = sheet.crop(f, r);
            }
        }
        return anim;
    }
    }