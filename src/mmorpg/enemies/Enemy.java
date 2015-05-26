package mmorpg.enemies;

import mmorpg.common.Movable;
import mmorpg.common.Placeable;
import org.newdawn.slick.Animation;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Diego
 */
public abstract class Enemy extends Movable implements Placeable {

    protected Animation walkingFront, walkingBack, walkingLeft, walkingRight;

    public Enemy(float speed, Vector2f position, Shape body) {
        super(speed, position, body);
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
    
    protected void updateAnimation(int delta) {
        if (graphic != null) {
            if (((Animation) graphic).isStopped()) {
                ((Animation) graphic).start();
            }
            ((Animation) graphic).update(delta);
        }
    }

    public void setupAnimations(Animation[] animations) {
        this.walkingFront = animations[0];
        this.walkingBack = animations[1];
        this.walkingLeft = animations[2];
        this.walkingRight = animations[3];
        setGraphic(walkingFront);
        ((Animation) graphic).stop();
    }

}
