package mmorpg.map.room;

import java.util.ArrayList;
import java.util.HashMap;
import mmorpg.camera.Camera;
import mmorpg.common.Placeable;
import mmorpg.enemies.Enemy;
import mmorpg.enemies.WallEnemy;
import mmorpg.map.Map;
import mmorpg.map.room.buildingstrategies.RoomBuildingStrategy;
import mmorpg.map.tiles.DoorTile;
import mmorpg.map.tiles.Tile;
import mmorpg.player.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Barrionuevo Diego
 */
public class Room {

    public static final int DIRECTION_NORTH = 0;
    public static final int DIRECTION_SOUTH = 1;
    public static final int DIRECTION_EAST = 2;
    public static final int DIRECTION_WEST = 3;
    //
    public static final int CORNER_TOP_LEFT = 0;
    public static final int CORNER_TOP_RIGHT = 1;
    public static final int CORNER_BOTTOM_LEFT = 2;
    public static final int CORNER_BOTTOM_RIGHT = 3;
    //
    private Tile[][] room;
    private RoomBuildingStrategy buildingStrategy;
    private final float tileWidth, tileHeight;
    private int roomId, roomWidth, roomHeight;
    //
    private Camera camera;
    private Map map;
    //
    //Displayable objects
    ArrayList<Placeable> objects;
    //las posiciones iniciales de los enemigos
    HashMap<Placeable, Vector2f> initPositions;

    public Room(int roomId, RoomBuildingStrategy buildingStrategy) {
        this.roomId = roomId;
        this.tileWidth = 50;
        this.tileHeight = 50;
        this.buildingStrategy = buildingStrategy;
        this.camera = Camera.getInstance();
        this.objects = new ArrayList<>();
        this.initPositions = new HashMap<>();
        this.build();
    }

    public final void build() {
        this.room = this.buildingStrategy.build();
        this.roomWidth = room[0].length;
        this.roomHeight = room.length;
    }

    public DoorTile putDoor(int tileX, int tileY) {
        //only on borders...
        if ((tileX == 0 || tileX == room[0].length - 1) || (tileY == 0 || tileY == room.length - 1)) {
            if (room[tileY][tileX].getType() != Tile.DOOR_TILE) {
                room[tileY][tileX] = new DoorTile(tileX, tileY, tileWidth, tileHeight);
            }
            return (DoorTile) room[tileY][tileX];
        }
        return null;
    }

    public void update(GameContainer container, int delta) throws SlickException {
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) != null) {
                objects.get(i).update(container, delta);
            }
        }
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        //when using camera logic, this should be optimized
        for (int i = 0; i < room.length; i++) {
            for (int j = 0; j < room[0].length; j++) {
                room[i][j].render(container, g);
            }
        }
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) != null) {
                objects.get(i).render(container, g);
            }
        }
    }

    public void move(Vector2f moveFactor, Placeable ignorePlaceable) {
        for (int i = 0; i < room.length; i++) {
            for (int j = 0; j < room[0].length; j++) {
                room[i][j].getPosition().x += moveFactor.x;
                room[i][j].getPosition().y += moveFactor.y;
            }
        }
        for (Placeable object : objects) {
            if (ignorePlaceable == null) {
                object.getPosition().x += moveFactor.x;
                object.getPosition().y += moveFactor.y;
            } else {
                if (object != ignorePlaceable) {
                    object.getPosition().x += moveFactor.x;
                    object.getPosition().y += moveFactor.y;
                }
            }
        }
    }

    public void move(Vector2f moveFactor) {
        this.move(moveFactor, null);
    }

    public Tile getTileByPositionInRoom(int tileX, int tileY) {
        return room[tileY][tileX];
    }

    public boolean placeObject(Placeable placeable, int tileX, int tileY) {
        if (tileX < 0 || tileX > room[0].length - 1 || tileY < 0 || tileY > room.length - 1) {
            return false;
        }
        Vector2f newPosition = new Vector2f();
        newPosition.x = tileWidth * tileX + tileWidth / 2 - placeable.getWidth() / 2;
        newPosition.y = tileHeight * tileY + tileHeight / 2 - placeable.getHeight() / 2;
        placeable.setPosition(newPosition);
        //also remember the position
        //setInitPosition(placeable, newPosition);
        return true;
    }

    public Tile getCurrentTile(Placeable placeable) {
        return getTileInCorner(placeable, CORNER_TOP_LEFT);
    }

    private Tile getTileInCorner(Placeable placeable, int corner) {
        Tile foundTile = null;
        Vector2f mainPosition = room[0][0].getPosition();
        //absolute in the room, but is actually relative to the first tile in the upper left corner
        Vector2f absolutePosition = new Vector2f(
                placeable.getPosition().x - mainPosition.x,
                placeable.getPosition().y - mainPosition.y
        );
        switch (corner) {
            case (CORNER_TOP_LEFT)://.:
                //por defecto esta bien
                break;
            case (CORNER_TOP_RIGHT)://:.
                absolutePosition.x = placeable.getPosition().x + placeable.getWidth() - mainPosition.x;
                break;
            case (CORNER_BOTTOM_RIGHT)://:'
                absolutePosition.x = placeable.getPosition().x + placeable.getWidth() - mainPosition.x;
                absolutePosition.y = placeable.getPosition().y + placeable.getHeight() - mainPosition.y;
                break;
            case (CORNER_BOTTOM_LEFT)://':
                absolutePosition.y = placeable.getPosition().y + placeable.getHeight() - mainPosition.y;
                break;
        }
        int tileX = (int) (Math.floor((absolutePosition.x) / tileWidth));
        int tileY = (int) (Math.floor((absolutePosition.y) / tileHeight));
        if (tileX >= 0 && tileX <= room[0].length - 1 && tileY >= 0 && tileY <= room.length - 1) {
            foundTile = room[tileY][tileX];
        }
        return foundTile;
    }

    public Tile findNextTile(Placeable placeable, int direction) {
        return findNextTile(placeable, direction, CORNER_TOP_LEFT);
    }

    public Tile findNextTile(Placeable placeable, int direction, int corner) {
        Tile foundTile = null;
        Tile currentTile = getTileInCorner(placeable, corner);
        if (currentTile != null) {
            int tileX = currentTile.getTileX();
            int tileY = currentTile.getTileY();
            switch (direction) {
                case (DIRECTION_WEST):
                    if (tileX - 1 >= 0) {
                        foundTile = room[tileY][tileX - 1];
                    }
                    break;
                case (DIRECTION_EAST):
                    if (tileX + 1 <= room[0].length - 1) {
                        foundTile = room[tileY][tileX + 1];
                    }
                    break;
                case (DIRECTION_NORTH):
                    if (tileY - 1 >= 0) {
                        foundTile = room[tileY - 1][tileX];
                    }
                    break;
                case (DIRECTION_SOUTH):
                    if (tileY + 1 <= room.length - 1) {
                        foundTile = room[tileY + 1][tileX];
                    }
                    break;
            }
        }
        return foundTile;
    }

    public boolean canMoveTo(Placeable placeable, int direction) {
        Tile nextTile1 = null, nextTile2 = null;
        switch (direction) {
            case (DIRECTION_WEST):
                nextTile1 = findNextTile(placeable, direction, CORNER_TOP_LEFT);
                nextTile2 = findNextTile(placeable, direction, CORNER_BOTTOM_LEFT);
                break;
            case (DIRECTION_EAST):
                nextTile1 = findNextTile(placeable, direction, CORNER_TOP_RIGHT);
                nextTile2 = findNextTile(placeable, direction, CORNER_BOTTOM_RIGHT);
                break;
            case (DIRECTION_NORTH):
                nextTile1 = findNextTile(placeable, direction, CORNER_TOP_LEFT);
                nextTile2 = findNextTile(placeable, direction, CORNER_TOP_RIGHT);
                break;
            case (DIRECTION_SOUTH):
                nextTile1 = findNextTile(placeable, direction, CORNER_BOTTOM_LEFT);
                nextTile2 = findNextTile(placeable, direction, CORNER_BOTTOM_RIGHT);
                break;
        }
        Vector2f position = placeable.getPosition();
        if (nextTile1 != null && nextTile2 != null) {
            if (nextTile1.isWalkable() && nextTile2.isWalkable()) {
                return true;
            } else {
                Tile nextTileNotWalkeable = (nextTile1.isWalkable()) ? nextTile1 : nextTile2;
                switch (direction) {
                    case (DIRECTION_WEST):
                        return (Math.abs((nextTileNotWalkeable.getPosition().x + nextTileNotWalkeable.getWidth()) - position.x) >= 1);
                    case (DIRECTION_EAST):
                        return (Math.abs((nextTileNotWalkeable.getPosition().x) - (position.x)) >= placeable.getWidth() + 2);
                    case (DIRECTION_NORTH):
                        return (Math.abs((nextTileNotWalkeable.getPosition().y + nextTileNotWalkeable.getHeight()) - position.y) >= 1);
                    case (DIRECTION_SOUTH):
                        return (Math.abs((nextTileNotWalkeable.getPosition().y) - (position.y)) >= placeable.getHeight() + 2);
                }
            }
        } else {
            switch (direction) {
                case (DIRECTION_WEST):
                    return (Math.abs((room[0][0].getPosition().x) - (position.x)) >= 1);
                case (DIRECTION_EAST):
                    return (Math.abs((room[0][roomWidth - 1].getPosition().x + room[0][roomWidth - 1].getWidth()) - (position.x)) >= placeable.getWidth() + 2);
                case (DIRECTION_NORTH):
                    return (Math.abs((room[0][0].getPosition().y) - (position.y)) >= 1);
                case (DIRECTION_SOUTH):
                    return (Math.abs((room[roomHeight - 1][0].getPosition().y + room[roomHeight - 1][0].getHeight()) - (position.y)) >= placeable.getHeight() + 2);
            }
        }
        return false;
    }

    public boolean movingInsideCamera(Placeable placeable, float distanceMoving, int direction) {
        Vector2f position = placeable.getPosition();
        boolean allowMoving = true;
        //65 = 50 + 25 - 10
        //   = 50*1 + 50/2 - 20/2
        //   = tileWidth*tilesPadding + tileWidth/2 + placeble.getWidth()/2
        float limit = 0f;
        float definedLimitWidth = tileWidth * camera.getPadding() + tileWidth / 2 - placeable.getWidth() / 2;
        float definedLimitHeight = tileHeight * camera.getPadding() + tileHeight / 2 - placeable.getHeight() / 2;
        Vector2f movement = new Vector2f();
        movement.x = 0;
        movement.y = 0;
        //direction = -1;
        switch (direction) {
            case (DIRECTION_WEST):
                limit = definedLimitWidth;
                if (position.x < limit) {
                    movement.x = distanceMoving;
                    //pleaceble reached the end of the camera padding (screen) so we stop moving the placeable, and instead we move all the room
                    move(movement, placeable);
                    allowMoving = false;
                }
                break;
            case (DIRECTION_EAST):
                limit = camera.getWidth() - definedLimitWidth;
                if (position.x > limit) {
                    movement.x = distanceMoving * (-1);
                    move(movement, placeable);
                    allowMoving = false;
                }
                break;
            case (DIRECTION_NORTH):
                limit = definedLimitHeight;
                if (position.y < limit) {
                    movement.y = distanceMoving;
                    move(movement, placeable);
                    allowMoving = false;
                }
                break;
            case (DIRECTION_SOUTH):
                limit = camera.getHeight() - definedLimitHeight;
                if (position.y > limit) {
                    movement.y = distanceMoving * (-1);
                    move(movement, placeable);
                    allowMoving = false;
                }
                break;
        }
        return allowMoving;
    }

    public void focusObject(Placeable placeable) {
        Vector2f position = this.getCurrentTile(placeable).getPosition();
        Vector2f center = new Vector2f(camera.getWidth() / 2, camera.getHeight() / 2);
        Vector2f finalPosition = new Vector2f(
                center.x - position.getX() - Math.abs(tileWidth / 2),
                center.y - position.getY() - Math.abs(tileHeight / 2)
        );
        move(finalPosition);
    }

    public boolean hitTheDoor(Placeable placeable) {
        if (isFullInsideTile(placeable) && getFullCurrentTile(placeable).getType() == Tile.DOOR_TILE) {
            map.nextRoom(this, (DoorTile) getCurrentTile(placeable), placeable);
            //this.moveToPosition(new Vector2f(0, 0));
            Vector2f initPosition = room[0][0].getPosition();
            Vector2f finalPosition = new Vector2f();
            finalPosition.x = 0 - initPosition.x;
            finalPosition.y = 0 - initPosition.y;
            move(finalPosition);
            return true;
        }
        return false;
    }

    public Tile getFullCurrentTile(Placeable placeable) {
        Tile[] tiles = new Tile[]{
            getTileInCorner(placeable, CORNER_TOP_LEFT),
            getTileInCorner(placeable, CORNER_TOP_RIGHT),
            getTileInCorner(placeable, CORNER_BOTTOM_LEFT),
            getTileInCorner(placeable, CORNER_BOTTOM_RIGHT)
        };
        for (int i = 1; i < tiles.length; i++) {
            //only when all tiles matched with the four corners of the placeable we are sure the placeable is full inside it
            if (tiles[i] != tiles[i - 1]) {
                return null;
            }
        }
        return tiles[0];
    }

    public boolean isFullInsideTile(Placeable placeable) {
        return getFullCurrentTile(placeable) != null;
    }

    @Deprecated
    public boolean isInsideTile(Placeable placeable) {
        Tile tile = getCurrentTile(placeable);
        if (tile == null) {
            return false;
        }
        Vector2f tilePosition = tile.getPosition();
        Vector2f position = placeable.getPosition();
        boolean r = (position.x > tilePosition.x
                && position.y > tilePosition.y
                && position.x < (tilePosition.x + tile.getWidth() - placeable.getWidth())
                && position.y < (tilePosition.y + tile.getHeight() - placeable.getHeight()));
        System.out.println(r);
        return r;
    }

    public Tile[] getTilesOfType(int tileType) {
        Tile[] result = null;
        ArrayList<Tile> foundTiles = new ArrayList<>();
        for (int i = 0; i < this.room.length; i++) {
            for (int j = 0; j < this.room[0].length; j++) {
                Tile currentTile = this.room[i][j];
                if (currentTile.getType() == tileType) {
                    foundTiles.add(currentTile);
                }
            }
        }
        result = new Tile[foundTiles.size()];
        for (int i = 0; i < foundTiles.size(); i++) {
            result[i] = foundTiles.get(i);
        }
        return result;
    }

    public void addObject(Placeable placeable, int tileX, int tileY) {
        placeable.setRoom(this);
        this.placeObject(placeable, tileX, tileY);
        this.objects.add(placeable);
    }

    public void removeObject(Placeable placeable) {
        this.objects.remove(placeable);
    }

    public int getRoomWidth() {
        return roomWidth;
    }

    public int getRoomHeight() {
        return roomHeight;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Room) obj).getRoomId() == this.getRoomId();
    }

    public void setInitPosition(Placeable placeable, Vector2f position) {
        if (this.initPositions.get(placeable) == null) {
            this.initPositions.put(placeable, position);
        } else {
            this.initPositions.get(placeable).x = position.x;
            this.initPositions.get(placeable).y = position.y;
        }
    }

}
