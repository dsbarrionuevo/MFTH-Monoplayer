package mmorpg.map;

import mmorpg.map.room.Room;

/**
 *
 * @author Barrionuevo Diego
 */
public interface MapEventListener {

    public void roomChanged(Room currentRoom);
    
}
