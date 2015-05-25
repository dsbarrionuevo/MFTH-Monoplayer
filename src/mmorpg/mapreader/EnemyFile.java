package mmorpg.mapreader;

/**
 *
 * @author Barrionuevo Diego
 */
public class EnemyFile {

    private int id;
    private EnemyType enemyType;
    private String name;
    private Animation[] animations;

    public EnemyFile(int id, String name, EnemyType enemyType, Animation[] animations) {
        this.id = id;
        this.name = name;
        this.enemyType = enemyType;
        this.animations = animations;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public EnemyType getEnemyType() {
        return enemyType;
    }

    public Animation[] getAnimations() {
        return animations;
    }

}
