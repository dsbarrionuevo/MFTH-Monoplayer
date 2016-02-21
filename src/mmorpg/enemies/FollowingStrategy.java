package mmorpg.enemies;

import mmorpg.common.Placeable;
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

    public FollowingStrategy(float radiusVision, float radiusLostContact) {
        this.radiusVision = radiusVision;
        this.radiusLostContact = radiusLostContact;
        //the radius of vision always has to be less than the radius of lost contact
        if (this.radiusVision >= this.radiusLostContact) {
            this.radiusLostContact = this.radiusVision - 1;
        }
    }

    public FollowingStrategy() {
        this(100, 200);
    }

    public void render(Placeable chaser, GameContainer gc, Graphics g) {
        g.setColor(Color.yellow);
        g.draw(new Circle(chaser.getPosition().x + chaser.getWidth() / 2, chaser.getPosition().y + chaser.getHeight() / 2, radiusVision));
    }

    public void update(Placeable chaser, Placeable target, int delta) {
        //better consider centers...
        Vector2f targetPosition = new Vector2f(target.getPosition().x + target.getWidth() / 2, target.getPosition().y + target.getHeight() / 2);
        Vector2f chaserPosition = new Vector2f(chaser.getPosition().x + chaser.getWidth() / 2, chaser.getPosition().y + chaser.getHeight() / 2);
        float distance = (float) Math.sqrt((targetPosition.x - chaserPosition.x) * (targetPosition.x - chaserPosition.x) - (targetPosition.y - chaserPosition.y) * (targetPosition.y - chaserPosition.y));
        if (distance < this.radiusVision) {
            System.out.println(System.currentTimeMillis());
            //chase target, better could be to raise an event and chase take care of how chase the target
        }
        if (distance > this.radiusLostContact) {
            //raise event of lost contact for target to stop the chasing
        }
    }

}
