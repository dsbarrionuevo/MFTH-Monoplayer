package mmorpg.enemies;

import mmorpg.common.Placeable;
import org.newdawn.slick.GameContainer;

/**
 *
 * @author Barrionuevo Diego
 */
public abstract class MovingStrategy {

    protected Placeable mover;
    protected float speed;

    public MovingStrategy(Placeable mover, float speed) {
        this.mover = mover;
        this.speed = speed;
    }

    public abstract void update(GameContainer container, int delta);

}
