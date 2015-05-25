package mmorpg.mapreader;

/**
 *
 * @author Barrionuevo Diego
 */
public class RoomFile {

    private int id;
    private RoomType roomType;
    private int[][] map;
    private EnemyRoomFile[] enemies;

    public RoomFile(int id, RoomType roomType, EnemyRoomFile[] enemies, int[][] map) {
        this.id = id;
        this.roomType = roomType;
        this.enemies = enemies;
        this.map = map;
    }

    public EnemyRoomFile[] getEnemies() {
        return enemies;
    }

    public int getId() {
        return id;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public int[][] getMap() {
        return map;
    }

}
