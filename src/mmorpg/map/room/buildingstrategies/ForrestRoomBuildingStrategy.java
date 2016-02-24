package mmorpg.map.room.buildingstrategies;

import mmorpg.map.tiles.BlankTile;
import mmorpg.map.tiles.Tile;
import mmorpg.map.tiles.WallTile;

/**
 *
 * @author Barrionuevo Diego
 */
public class ForrestRoomBuildingStrategy extends RoomBuildingStrategy {

    public ForrestRoomBuildingStrategy(int widthLength, int heightLength, float tileWidth, float tileHeight) {
        super(widthLength, heightLength, tileWidth, tileHeight);
    }

    @Override
    public Tile[][] build() {
        Tile[][] map = new Tile[heightLength][widthLength];
        borderMap(map);
        int circles = (int) (widthLength * heightLength) / 100;
        for (int i = 0; i < circles; i++) {
            int x = (int) (Math.random() * widthLength);
            int y = (int) (Math.random() * heightLength);
            int radius = (int) (Math.random() * circles/2) + 1;
            dropCircle(map, x, y, radius);
        }
        return map;
    }

    private void dropCircle(Tile[][] map, int x, int y, int radius) {
        if (radius <= 0) {
            return;
        }
        paintTile(map, x, y);
        if (y - 1 >= 0) {//up
            dropCircle(map, x, y - 1, radius - 1);
        }
        if (x + 1 <= map[0].length - 1) {//right
            dropCircle(map, x + 1, y, radius - 1);
        }
        if (y + 1 <= map.length - 1) {//down
            dropCircle(map, x, y + 1, radius - 1);
        }
        if (x - 1 >= 0) {//left
            dropCircle(map, x - 1, y, radius - 1);
        }
    }

    private void paintTile(Tile[][] map, int x, int y) {
        if (y >= 0 && y <= map.length - 1 && x >= 0 && x <= map[0].length - 1) {
            map[y][x] = new WallTile(x, y, tileWidth, tileHeight);
        }
    }

}
