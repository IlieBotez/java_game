package PaooGame.Entities;

import PaooGame.Game;

public class EnemyFactory {

    public static Enemy createEnemy(EnemyType type, Game game, float x, float y) {
        switch (type) {
            case ORC_WARRIOR:
                return new OrcWarrior(game, x, y);
            case ORC_BRUTE:
                return new OrcBrute(game, x, y);
            case ORC_BOSS:
                return new OrcBoss(game, x, y);
            case NATURE_SLIME:
                return new NatureSlime(game, x, y); // Adăugat
            case POISON_SLIME:
                return new PoisonSlime(game, x, y); // Adăugat
            case FIRE_SLIME:
                return new FireSlime(game, x, y);
            default:
                throw new IllegalArgumentException("Tip de inamic necunoscut: " + type);
        }
    }
}