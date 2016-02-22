package mmorpg.enemies;

import mmorpg.common.Placeable;
import mmorpg.map.room.Room;
import mmorpg.map.tiles.Tile;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Barrionuevo Diego
 */
public class DefaultFollowingStrategy extends FollowingStrategy {

    public DefaultFollowingStrategy(Placeable chaser, float speed) {
        super(chaser,speed);
    }

    public void update(GameContainer gc, int delta) {
        if (target != null) {
            float moveFactor = 7f * (delta / 100f);
            Room room = chaser.getRoom();
            Tile tileTarget = room.getCurrentTile(target);
            Tile tileChaser = room.getCurrentTile(chaser);
            //first chase in horizontal
            if (tileTarget.getTileX() < tileChaser.getTileX()) {
                if (room.canMoveTo(chaser, Room.DIRECTION_WEST)) {
                    chaser.getPosition().x -= moveFactor;
                }
            } else if (tileTarget.getTileX() > tileChaser.getTileX()) {
                if (room.canMoveTo(chaser, Room.DIRECTION_EAST)) {
                    chaser.getPosition().x += moveFactor;
                }
            } else if (tileTarget.getTileX() == tileChaser.getTileX()) {
                //then chase in vertical
                if (tileTarget.getTileY() < tileChaser.getTileY()) {
                    if (room.canMoveTo(chaser, Room.DIRECTION_NORTH)) {
                        chaser.getPosition().y -= moveFactor;
                    }
                } else if (tileTarget.getTileY() > tileChaser.getTileY()) {
                    if (room.canMoveTo(chaser, Room.DIRECTION_SOUTH)) {
                        chaser.getPosition().y += moveFactor;
                    }
                }
            }
            if (tileTarget.getTileX() == tileChaser.getTileX() && (tileTarget.getTileY() == tileChaser.getTileY())) {
                //chaser.stopFollowing();
                Vector2f targetPosition = new Vector2f(target.getPosition().x + target.getWidth() / 2, target.getPosition().y + target.getHeight() / 2);
                Vector2f chaserPosition = new Vector2f(chaser.getPosition().x + chaser.getWidth() / 2, chaser.getPosition().y + chaser.getHeight() / 2);
                float distance = (float) Math.sqrt((targetPosition.x - chaserPosition.x) * (targetPosition.x - chaserPosition.x) - (targetPosition.y - chaserPosition.y) * (targetPosition.y - chaserPosition.y));
                if (distance > 5) {
                    double angle = Math.atan2(targetPosition.y - chaserPosition.y, targetPosition.x - chaserPosition.x);
                    chaser.getPosition().x += Math.cos(angle) * moveFactor;
                    chaser.getPosition().y += Math.sin(angle) * moveFactor;
                }
            }
        }
    }

}
