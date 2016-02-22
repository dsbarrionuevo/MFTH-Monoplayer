package mmorpg.enemies;

import mmorpg.common.Placeable;
import mmorpg.map.room.Room;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Barrionuevo Diego
 */
public class DefaultMovingStrategy extends MovingStrategy {

    private int currentDirection;

    public DefaultMovingStrategy(Placeable mover, float speed) {
        super(mover, speed);
        this.currentDirection = (int) (Math.random() * 4);
    }

    @Override
    public void update(GameContainer container, int delta) {
        boolean stopMoving = !move(currentDirection, delta);
        if (stopMoving) {
            //change direction
            this.changeDirection();
        }
    }

    public void changeDirection() {
        int oldDirection = this.currentDirection;
        do {
            this.currentDirection = (int) (Math.random() * 4);
        } while (this.currentDirection == oldDirection);
    }

    public void changeDirection(int direction) {
        this.currentDirection = direction;
    }

    public void changeToOppositeDirection() {
        int oppositeDirection = -1;
        switch (this.currentDirection) {
            case (Room.DIRECTION_EAST):
                oppositeDirection = Room.DIRECTION_WEST;
                break;
            case (Room.DIRECTION_WEST):
                oppositeDirection = Room.DIRECTION_EAST;
                break;
            case (Room.DIRECTION_NORTH):
                oppositeDirection = Room.DIRECTION_SOUTH;
                break;
            case (Room.DIRECTION_SOUTH):
                oppositeDirection = Room.DIRECTION_NORTH;
                break;
        }
        this.changeDirection(oppositeDirection);
    }

    private boolean move(int direction, int delta) {
        Vector2f position = mover.getPosition();
        Room room = mover.getRoom();
        float moveFactor = speed * (delta / 100f);
        Vector2f prevPosition = new Vector2f(position.x, position.y);
        if (room.canMoveTo(mover, direction)) {
            switch (direction) {
                case (Room.DIRECTION_WEST):
                    position.x -= moveFactor;
                    //graphic = walkingLeft;
                    break;
                case (Room.DIRECTION_EAST):
                    position.x += moveFactor;
                    //graphic = walkingRight;
                    break;
                case (Room.DIRECTION_NORTH):
                    position.y -= moveFactor;
                    //graphic = walkingBack;
                    break;
                case (Room.DIRECTION_SOUTH):
                    position.y += moveFactor;
                    //graphic = walkingFront;
                    break;
            }
        }
        //updateAnimation(delta);
        //return false when stop moving
        return !(prevPosition.x == position.x && prevPosition.y == position.y);
    }

}
