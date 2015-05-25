package mmorpg.debug;

import mmorpg.common.Drawable;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Barrionuevo Diego
 */
public class TimedMessage extends Drawable {

    private String message;

    private long duration, timer;

    public TimedMessage(Vector2f position, String message) {
        super(position, new Rectangle(0, 0, 0, 0));
        this.message = message;
        this.timer = 0;
        this.duration = 3 * 1000;
    }

    public void update(int delta) {
        if (this.timer > this.duration) {
            visible = false;
        }
        this.timer += delta;
    }

    public void changeMessage(String newMessage) {
        this.message = newMessage;
        this.visible = true;
        //reset timer
        this.timer = 0;
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        if (this.isVisible()) {
            g.setColor(Color.yellow);
            g.drawString(message, position.x, position.y);
        }
    }

}
