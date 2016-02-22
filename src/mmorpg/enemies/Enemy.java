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
    protected AnimationHolder animation;
    protected Room room;
    protected float attackForce;
    protected float life;
    protected FollowingStrategy followingStrategy;
    protected MovingStrategy movingStrategy;
    protected int preferredInitState;
    protected int state;

    public Enemy(float speed, Vector2f position, Shape body) {
        super(speed, position, body);
        this.life = 1000; //default
        this.attackForce = 5; //default
        this.preferredInitState = STATE_PATROL;
        this.state = preferredInitState;
        this.animation = new AnimationHolder();
        setupFollowingStrategy();
        setupMovingStrategy();
        setupFollowingParameters(120, 200);
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

    public void injure(float damage) {
        this.life -= damage;
    }

    public void injure(float damage, Vector2f attackerPosition) {
        //this method push away the object by his attacker, the distance could vary on the attack force
        double angle = Math.atan2(attackerPosition.y - position.y, attackerPosition.x - position.x);
        double degrees = angle * (180 / Math.PI) + 180;
        int direction = -1;
        if (degrees >= 45 && degrees < 135) {
            System.out.println("DIRECTION_SOUTH");
            direction = Room.DIRECTION_SOUTH;
        } else if (degrees >= 135 && degrees < 225) {
            System.out.println("DIRECTION_WEST");
            direction = Room.DIRECTION_WEST;
        } else if (degrees >= 225 && degrees < 315) {
            System.out.println("DIRECTION_NORTH");
            direction = Room.DIRECTION_NORTH;
        } else if ((degrees >= 315 && degrees <= 360) || (degrees >= 0 && degrees < 45)) {
            System.out.println("DIRECTION_EAST");
            direction = Room.DIRECTION_EAST;
        }
        System.out.println(degrees);
        float distance = 0.2f;//px
        if (room.canMoveTo(this, direction)) {
            //position.x += Math.cos(angle) * distance * (-1);
            //position.y += Math.sin(angle) * distance * (-1);
            //en lugar de correrlo un angulo, voy a correrlo una direccion, es decir normalizo el angulo
            switch (direction) {
                case (Room.DIRECTION_WEST):
                    position.x -= distance;
                    break;
                case (Room.DIRECTION_EAST):
                    position.x += distance;
                    break;
                case (Room.DIRECTION_NORTH):
                    position.y -= distance;
                    break;
                case (Room.DIRECTION_SOUTH):
                    position.y += distance;
                    break;
            }
        }
        this.injure(damage);
    }

    public boolean isDead() {
        return this.life <= 0;
    }

}
