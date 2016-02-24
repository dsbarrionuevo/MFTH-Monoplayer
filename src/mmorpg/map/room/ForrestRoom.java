package mmorpg.map.room;

import mmorpg.map.room.buildingstrategies.ForrestRoomBuildingStrategy;

/**
 *
 * @author Barrionuevo Diego
 */
public class ForrestRoom extends Room {

    public ForrestRoom(int roomId) {
        super(roomId, new ForrestRoomBuildingStrategy(
                //(int) (Math.random() * 10) + 5,
                //(int) (Math.random() * 10) + 5,
                30,30,
                50,
                50
        ));
        //place enemies

        //place items
    }

}
