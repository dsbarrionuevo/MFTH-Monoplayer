package mmorpg.enemies;

import mmorpg.map.room.Room;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Diego
 */
public class WallEnemy extends Enemy {

    private int currentDirection;
    //
    private Room room;

    public WallEnemy() {
        super(7f, new Vector2f(), new Rectangle(0, 0, 32, 32));
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

    @Override
    public void render(GameContainer gc, Graphics g) {
        if (this.isVisible()) {
            g.setColor(Color.red);
            this.body.setX(position.x);
            this.body.setY(position.y);
            if (graphic != null) {
                ((Animation) graphic).draw(body.getX(), body.getY());
            } else {
                g.fill(body);
            }
            this.followingStrategy.render(this, gc, g);
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
        float moveFactor = speed * (delta / 100f);
        Vector2f prevPosition = new Vector2f(position.x, position.y);
        if (room.canMoveTo(this, direction)) {
            switch (direction) {
                case (Room.DIRECTION_WEST):
                    position.x -= moveFactor;
                    graphic = walkingLeft;
                    break;
                case (Room.DIRECTION_EAST):
                    position.x += moveFactor;
                    graphic = walkingRight;
                    break;
                case (Room.DIRECTION_NORTH):
                    position.y -= moveFactor;
                    graphic = walkingBack;
                    break;
                case (Room.DIRECTION_SOUTH):
                    position.y += moveFactor;
                    graphic = walkingFront;
                    break;
            }
        }
        updateAnimation(delta);
        //return false when stop moving
        return !(prevPosition.x == position.x && prevPosition.y == position.y);
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public Room getRoom() {
        return this.room;
    }
}
