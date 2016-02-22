package mmorpg.enemies;

import mmorpg.common.Movable;
import mmorpg.common.Placeable;
import mmorpg.map.room.Room;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Diego
 */
public abstract class Enemy extends Movable implements Placeable, Chaser {

    public static final int STATE_STILL = 0;
    public static final int STATE_PATROL = 1;
    public static final int STATE_FOLLOWING = 2;
    protected Animation walkingFront, walkingBack, walkingLeft, walkingRight;
    protected Room room;
    protected float attackForce;
    protected FollowingStrategy followingStrategy;
    protected MovingStrategy movingStrategy;
    protected int state;

    public Enemy(float speed, Vector2f position, Shape body) {
        super(speed, position, body);
        this.attackForce = 5; //default
        this.state = STATE_STILL;
        setupFollowingStrategy();
        setupMovingStrategy();
    }

    @Override
    public void update(GameContainer container, int delta) {
        this.followingStrategy.update(container, delta);
        switch (this.state) {
            case (STATE_PATROL):
                this.movingStrategy.update(container, delta);
                break;
            case (STATE_FOLLOWING):
                //this.followingStrategy.update(container, delta);
                break;
            case (STATE_STILL):
                break;
        }
    }

    //seria mejor que retorne en lugar de esperar que lo setee dentro
    protected abstract void setupFollowingStrategy();

    protected abstract void setupMovingStrategy();

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

    public void setTarget(Placeable target) {
        this.followingStrategy.setTarget(target);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    public float getAttackForce() {
        return attackForce;
    }

    @Override
    public void startFollowing() {
        this.state = STATE_FOLLOWING;
        System.out.println("FOLLOWING");
    }

    @Override
    public void stopFollowing() {
        this.state = STATE_STILL;
        System.out.println("STOP FOLLOWING");
    }
}
