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
    private ItemRoomFile[] items;

    public RoomFile(int id, RoomType roomType, EnemyRoomFile[] enemies, ItemRoomFile[] items, int[][] map) {
        this.id = id;
        this.roomType = roomType;
        this.enemies = enemies;
        this.items = items;
        this.map = map;
    }

    public EnemyRoomFile[] getEnemies() {
        return enemies;
    }

    public ItemRoomFile[] getItems() {
        return items;
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
