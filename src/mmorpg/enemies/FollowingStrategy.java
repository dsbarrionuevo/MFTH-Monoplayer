package mmorpg.enemies;

import mmorpg.common.Placeable;
import org.newdawn.slick.GameContainer;

/**
 *
 * @author Barrionuevo Diego
 */
public abstract class FollowingStrategy {

    protected Placeable target;
    protected Placeable chaser;
    protected float speed;

    public abstract void update(GameContainer container, int delta);

    public FollowingStrategy(Placeable chaser, float speed) {
        this.chaser = chaser;
        this.speed = speed;
    }

//    public void render(Placeable chaser, GameContainer gc, Graphics g) {
//        g.setColor(Color.yellow);
//        g.draw(new Circle(chaser.getPosition().x + chaser.getWidth() / 2, chaser.getPosition().y + chaser.getHeight() / 2, radiusVision));
//        g.setColor(Color.red);
//        g.draw(new Circle(chaser.getPosition().x + chaser.getWidth() / 2, chaser.getPosition().y + chaser.getHeight() / 2, radiusLostContact));
//    }
    public void setTarget(Placeable target) {
        this.target = target;
    }

    public Placeable getTarget() {
        return target;
    }

}
