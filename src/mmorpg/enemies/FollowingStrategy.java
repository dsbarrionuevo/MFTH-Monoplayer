package mmorpg.enemies;

import mmorpg.common.Placeable;
import mmorpg.map.room.Room;
import mmorpg.map.tiles.Tile;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Barrionuevo Diego
 */
public abstract class FollowingStrategy {

    //it is in px, but could be in tiles...
    protected float radiusVision, radiusLostContact;
    protected Placeable target;
    protected Chaser chaser;

    public FollowingStrategy(Chaser chaser, float radiusVision, float radiusLostContact) {
        this.chaser = chaser;
        this.radiusVision = radiusVision;
        this.radiusLostContact = radiusLostContact;
        //the radius of vision always has to be less than the radius of lost contact
        if (this.radiusVision >= this.radiusLostContact) {
            this.radiusLostContact = this.radiusVision + 1;
        }
    }

    public FollowingStrategy(Chaser chaser) {
        this(chaser, 200, 100);
    }

    public void render(Placeable chaser, GameContainer gc, Graphics g) {
        g.setColor(Color.yellow);
        g.draw(new Circle(chaser.getPosition().x + chaser.getWidth() / 2, chaser.getPosition().y + chaser.getHeight() / 2, radiusVision));
        g.setColor(Color.red);
        g.draw(new Circle(chaser.getPosition().x + chaser.getWidth() / 2, chaser.getPosition().y + chaser.getHeight() / 2, radiusLostContact));
    }

    public void update(GameContainer gc, int delta) {
        if (target != null) {
            //no hacerlo a cada instante...
            Room room = chaser.getRoom();

            Vector2f originPosition = room.getOriginPosition();
            Vector2f targetPosition = new Vector2f(target.getPosition().x - originPosition.x + target.getWidth() / 2, target.getPosition().y - originPosition.y + target.getHeight() / 2);
            Vector2f chaserPosition = new Vector2f(chaser.getPosition().x - originPosition.x + chaser.getWidth() / 2, chaser.getPosition().y - originPosition.y + chaser.getHeight() / 2);
            float distance = (float) Math.sqrt((targetPosition.x - chaserPosition.x) * (targetPosition.x - chaserPosition.x) + (targetPosition.y - chaserPosition.y) * (targetPosition.y - chaserPosition.y));
            if (distance < this.radiusVision) {
                //chase target, better could be to raise an event and chase take care of how chase the target
                chaser.startFollowing();
                //System.out.println(System.currentTimeMillis());
                this.follow(gc, delta);
            }
            if (distance > this.radiusLostContact) {
                //raise event of lost contact for target to stop the chasing
                //chaser.stopFollowing();
            }
        }
    }

    public void follow(GameContainer gc, int delta) {
//        Vector2f targetPosition = new Vector2f(target.getPosition().x + target.getWidth() / 2, target.getPosition().y + target.getHeight() / 2);
//        Vector2f chaserPosition = new Vector2f(chaser.getPosition().x + chaser.getWidth() / 2, chaser.getPosition().y + chaser.getHeight() / 2);
//        float distance = (float) Math.sqrt((targetPosition.x - chaserPosition.x) * (targetPosition.x - chaserPosition.x) - (targetPosition.y - chaserPosition.y) * (targetPosition.y - chaserPosition.y));
//        int direction = Room.DIRECTION_WEST;
        float moveFactor = 7f * (delta / 100f);
        Room room = chaser.getRoom();
        Tile tileTarget = room.getCurrentTile(target);
        Tile tileChaser = room.getCurrentTile(chaser);
        
            System.out.println(tileTarget.getId());
            System.out.println(tileChaser.getId());
        if (tileTarget.getTileX() < tileChaser.getTileX()) {
            if (room.canMoveTo(chaser, Room.DIRECTION_WEST)) {
                chaser.getPosition().x -= moveFactor;
            }
        } else if (tileTarget.getTileX() > tileChaser.getTileX()) {
            if (room.canMoveTo(chaser, Room.DIRECTION_EAST)) {
                chaser.getPosition().x += moveFactor;
            }
        } else if (tileTarget.getTileX() == tileChaser.getTileX()) {
            if (tileTarget.getTileY() < tileChaser.getTileY()) {
                if (room.canMoveTo(chaser, Room.DIRECTION_NORTH)) {
                    chaser.getPosition().y -= moveFactor;
                }
            } else if (tileTarget.getTileY() > tileChaser.getTileY()) {
                if (room.canMoveTo(chaser, Room.DIRECTION_SOUTH)) {
                    chaser.getPosition().y += moveFactor;
                }
            } else if (tileTarget.getTileY() == tileChaser.getTileY()) {
                //chaser.stopFollowing();
            }
        }
        if (tileTarget.getTileX() == tileChaser.getTileX() && (tileTarget.getTileY() == tileChaser.getTileY())) {
            //chaser.stopFollowing();
            System.out.println("FOUND");
        }
    }

    public void setTarget(Placeable target) {
        this.target = target;
    }

}
