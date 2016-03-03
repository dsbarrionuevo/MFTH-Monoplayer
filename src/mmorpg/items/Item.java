package mmorpg.items;

import mmorpg.common.Drawable;
import mmorpg.common.Placeable;
import mmorpg.map.room.Room;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Barrionuevo Diego
 */
public abstract class Item extends Drawable implements Placeable, Catchable {

    protected Room room;
    protected Catcher catcher;

    public Item(Vector2f position, Shape body, boolean visible) {
        super(position, body, visible);
    }

    @Override
    public void setPosition(Vector2f position) {
        this.position = position;
        this.body.setX(position.x);
        this.body.setY(position.y);
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
        if (catcher != null && isVisible()) {
            if (catcher.getDrawable().collide(this)) {
                catcher.catches(this);
                setVisible(false);
            }
        }
    }

    public void setCatcher(Catcher catcher) {
        this.catcher = catcher;
    }

    @Override
    public void setOrientation(int orientation) {
    }

}
