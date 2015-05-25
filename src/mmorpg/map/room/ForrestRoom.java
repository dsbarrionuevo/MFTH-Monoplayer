package mmorpg.map.room;

import mmorpg.map.room.buildingstrategies.BorderRoomBuildingStrategy;
import mmorpg.map.room.buildingstrategies.RoomBuildingStrategy;

/**
 *
 * @author Barrionuevo Diego
 */
public class ForrestRoom extends Room{

    public ForrestRoom(int roomId) {
        super(roomId, new BorderRoomBuildingStrategy(20, 20, 50, 50));
    }

}
