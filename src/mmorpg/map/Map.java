package mmorpg.map;

import java.util.ArrayList;
import mmorpg.common.Placeable;
import mmorpg.map.buildingstrategies.ImprovedFileMapBuildingStrategy;
import mmorpg.map.buildingstrategies.MapBuildingStrategy;
import mmorpg.map.buildingstrategies.SingleRowMapBuildingStrategy;
import mmorpg.map.room.Room;
import mmorpg.map.tiles.DoorTile;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Diego
 */
public class Map {

    private ArrayList<Room> rooms;
    private ArrayList<MapEventListener> listeners;
    private Room currentRoom;
    private MapBuildingStrategy buildingStrategy;

    public Map(int roomsCount) {
        this.listeners = new ArrayList<>();
        this.buildingStrategy = new SingleRowMapBuildingStrategy(SingleRowMapBuildingStrategy.ORIENTATION_HORIZONTAL, roomsCount, 50, 50);
        //this.buildingStrategy = new ImprovedFileMapBuildingStrategy("res/maps/map1.txt", 50, 50);
    }

    public void build() {
        this.buildingStrategy.build(this);
        this.rooms = this.buildingStrategy.getRooms();
        currentRoom = buildingStrategy.getFirstRoom();
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        this.currentRoom.render(container, g);
    }

    @Deprecated
    public boolean placeObject(Placeable placeable, int room, int tileX, int tileY) {
        placeable.setRoom(this.rooms.get(room));
        return this.rooms.get(room).placeObject(placeable, tileX, tileY);
    }

    public boolean placeObject(Placeable placeable, int tileX, int tileY) {
        placeable.setRoom(currentRoom);
        return this.currentRoom.placeObject(placeable, tileX, tileY);
    }

    public Room getCurrentRoom() {
        return this.currentRoom;
    }

    public void nextRoom(Room currentRoom, DoorTile doorTile, Placeable placeable) {
        DoorTile otherDoor = doorTile.getConnectedTo();
        Room nextRoom = otherDoor.getMyRoom();
        currentRoom.removeObject(placeable);
        nextRoom.addObject(placeable, otherDoor.getTileX(), otherDoor.getTileY());
        changeRoom(nextRoom.getRoomId());
    }

    public void changeRoom(int idNewRoom) {
        this.currentRoom = buildingStrategy.getRoomById(idNewRoom);
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).roomChanged(currentRoom);
        }
    }

    public boolean addListener(MapEventListener listener) {
        return this.listeners.add(listener);
    }

    public boolean removeListener(MapEventListener listener) {
        return this.listeners.remove(listener);
    }
}
