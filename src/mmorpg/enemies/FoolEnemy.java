package mmorpg.enemies;

import mmorpg.map.room.Room;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Barrionuevo Diego
 */
public class FoolEnemy extends Enemy {

    public FoolEnemy() {
        super(7f, new Vector2f(), new Rectangle(0, 0, 32, 32));
    }

    @Override
    protected void setupFollowingStrategy() {
        this.followingStrategy = new DefaultFollowingStrategy(this, speed);
    }

    @Override
    protected void setupMovingStrategy() {
        this.movingStrategy = new DefaultMovingStrategy(this, speed);
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
            //render radius
            g.setColor(Color.yellow);
            g.draw(new Circle(getPosition().x + getWidth() / 2, getPosition().y + getHeight() / 2, radiusVision));
            g.setColor(Color.red);
            g.draw(new Circle(getPosition().x + getWidth() / 2, getPosition().y + getHeight() / 2, radiusLostContact));
        }
    }

    @Override
    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public Room getRoom() {
        return this.room;
    }

}
