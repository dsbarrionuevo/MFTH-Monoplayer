package mmorpg.items;

import mmorpg.common.Drawable;
import mmorpg.common.Placeable;
import mmorpg.map.room.Room;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Diego
 */
public class Treasure extends Drawable implements Placeable {

    private Room room;

    //
    public Treasure() {
        super(new Vector2f(), new Circle(0, 0, 10));
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.yellow);
        this.body.setX(position.x);
        this.body.setY(position.y);
        g.fill(body);
        g.setColor(Color.orange);
        g.draw(body);
    }

    @Override
    public void setPosition(Vector2f position) {
        this.position = position;
    }

    @Override
    public Vector2f getPosition() {
        return this.position;
    }

    @Override
    public float getWidth() {
        return this.width;
    }

    @Override
    public float getHeight() {
        return this.height;
    }

    @Override
    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public Room getRoom() {
        return this.room;
    }

    @Override
    public void update(GameContainer container, int delta) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
