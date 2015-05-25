package mmorpg.common;

import mmorpg.map.room.Room;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Diego
 */
public interface Placeable {

    public void setPosition(Vector2f position);

    public Vector2f getPosition();

    public float getWidth();

    public float getHeight();

    public void setRoom(Room room);

    public Room getRoom();

    //

    public void update(GameContainer container, int delta);

    public void render(GameContainer gc, Graphics g);

}/*
public abstract class Placeable extends Drawable {

    protected Vector2f position;
    protected Room room;

    public Placeable(Vector2f position, Shape body, boolean visible) {
        super(position, body, visible);
    }

    public float getWidth() {
        return super.getWidth();
    }

    public float getHeight() {
        return super.getHeight();
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

}*/
