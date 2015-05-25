package mmorpg.mapreader;

/**
 *
 * @author Barrionuevo Diego
 */
public class EnemyRoomFile {

    private EnemyFile enemy;
    private PositionFile position;

    public EnemyRoomFile(EnemyFile enemy, PositionFile position) {
        this.enemy = enemy;
        this.position = position;
    }

    public EnemyFile getEnemy() {
        return enemy;
    }

    public PositionFile getPosition() {
        return position;
    }

}
