package mmorpg.mapreader;

/**
 *
 * @author Barrionuevo Diego
 */
public class RoomType {

    private int id;
    private String name;
    private TileFile[] tiles;
    private EnemyFile[] enemies;

    public RoomType(int id, String name, TileFile[] tiles, EnemyFile[] enemies) {
        this.id = id;
        this.name = name;
        this.tiles = tiles;
        this.enemies = enemies;
    }

    public TileFile findTileFile(int id) {
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i].getId() == id) {
                return tiles[i];
            }
        }
        return null;
    }

    public EnemyFile findEnemyFile(int id) {
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i].getId() == id) {
                return enemies[i];
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TileFile[] getTiles() {
        return tiles;
    }

    public EnemyFile[] getEnemies() {
        return enemies;
    }

}
