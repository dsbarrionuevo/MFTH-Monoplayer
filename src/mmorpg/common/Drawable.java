package mmorpg.common;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Diego
 */
public abstract class Drawable {

    protected Vector2f position;
    protected boolean visible;
    protected Shape body;
    protected float width, height;
    //
    protected Renderable graphic;

    public Drawable(Vector2f position, Shape body, boolean visible) {
        this.position = position;
        this.visible = visible;
        this.body = body;
        this.width = body.getWidth();
        this.height = body.getHeight();
        this.graphic = null;//must set a defaut renderable as resource
    }

    public Drawable(Vector2f position, Shape body) {
        this(position, body, true);
    }

    public abstract void render(GameContainer gc, Graphics g);

    public boolean collide(Drawable target) {
        return this.getBody().intersects(target.getBody());
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
        this.body.setX(position.x);
        this.body.setY(position.y);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Shape getBody() {
        return body;
    }

    public void setBody(Shape body) {
        this.body = body;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Renderable getGraphic() {
        return graphic;
    }

    public void setGraphic(Renderable graphic) {
        this.graphic = graphic;
    }

}
