package mmorpg.enemies;

import mmorpg.common.AnimationHolder;
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
public abstract class Enemy extends Movable implements Placeable {

    public static final int STATE_STILL = 0;
    public static final int STATE_PATROL = 1;
    public static final int STATE_FOLLOWING = 2;
    //it is in px, but could be in tiles...
    protected float radiusVision, radiusLostContact;
    //protected Animation walkingFront, walkingBack, walkingLeft, walkingRight;
    protected AnimationHolder animation;
    protected Room room;
    protected float attackForce;
    protected FollowingStrategy followingStrategy;
    protected MovingStrategy movingStrategy;
    protected int preferredInitState;
    protected int state;

    public Enemy(float speed, Vector2f position, Shape body) {
        super(speed, position, body);
        this.attackForce = 5; //default
        this.preferredInitState = STATE_PATROL;
        this.state = preferredInitState;
        this.animation = new AnimationHolder();
        setupFollowingStrategy();
        setupMovingStrategy();
        setupFollowingParameters(40, 200);
    }

    @Override
    public void update(GameContainer container, int delta) {
        Placeable target = followingStrategy.getTarget();
        if (target != null) {
            Vector2f originPosition = room.getOriginPosition();
            Vector2f targetPosition = new Vector2f(target.getPosition().x - originPosition.x + target.getWidth() / 2, target.getPosition().y - originPosition.y + target.getHeight() / 2);
            Vector2f chaserPosition = new Vector2f(getPosition().x - originPosition.x + getWidth() / 2, getPosition().y - originPosition.y + getHeight() / 2);
            float distance = (float) Math.sqrt((targetPosition.x - chaserPosition.x) * (targetPosition.x - chaserPosition.x) + (targetPosition.y - chaserPosition.y) * (targetPosition.y - chaserPosition.y));
            if (state == STATE_STILL || state == STATE_PATROL) {
                if (distance < this.radiusVision) {
                    //System.out.println(System.currentTimeMillis());
                    //chase target, better could be to raise an event and chase take care of how chase the target
                    state = STATE_FOLLOWING;
                }
            } else if (state == STATE_FOLLOWING) {
                if (distance > this.radiusLostContact) {
                    //chaser.stopFollowing();
                    state = preferredInitState;
                }
            }
        }
        if (state == STATE_PATROL) {
            this.movingStrategy.update(container, delta);
        } else if (state == STATE_FOLLOWING) {
            this.followingStrategy.update(container, delta);
        }
        if (state != STATE_STILL) {
            updateAnimation(delta);
        }
    }

    //seria mejor que retorne en lugar de esperar que lo setee dentro
    protected abstract void setupFollowingStrategy();

    protected abstract void setupMovingStrategy();

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

    private void setupFollowingParameters(float radiusVision, float radiusLostContact) {
        this.radiusVision = radiusVision;
        this.radiusLostContact = radiusLostContact;
        //the radius of vision always has to be less than the radius of lost contact
        if (this.radiusVision >= this.radiusLostContact) {
            this.radiusLostContact = this.radiusVision + 1;
        }
    }

    public void setAnimation(AnimationHolder animation) {
        this.animation = animation;
    }

    public AnimationHolder getAnimation() {
        return animation;
    }

    @Override
    public void setRoom(Room room) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Room getRoom() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void updateAnimation(int delta) {
        animation.updateAnimation(delta);
    }

    @Override
    public void setOrientation(int orientation) {
        switch (orientation) {
            case (Room.DIRECTION_WEST):
                graphic = animation.changeAnimation("left");
                break;
            case (Room.DIRECTION_EAST):
                graphic = animation.changeAnimation("right");
                break;
            case (Room.DIRECTION_NORTH):
                graphic = animation.changeAnimation("back");
                break;
            case (Room.DIRECTION_SOUTH):
                graphic = animation.changeAnimation("front");
                break;
        }
    }

}
