package mmorpg.enemies;

import mmorpg.common.Placeable;
import org.newdawn.slick.GameContainer;

/**
 *
 * @author Barrionuevo Diego
 */
public class TimedMovingStrategy extends DefaultMovingStrategy {

    private long durationSameDiretion, timer;

    public TimedMovingStrategy(Placeable mover, float speed, long durationSameDiretion) {
        super(mover, speed);
        this.timer = 0;
        this.durationSameDiretion = durationSameDiretion;
    }

    public TimedMovingStrategy(Placeable mover, float speed) {
        this(mover, speed, ((int) (Math.random() * 6) + 4) * 1000);
    }

    @Override
    public void update(GameContainer container, int delta) {
        super.update(container, delta);
        if (timer > durationSameDiretion) {
            timer = 0;
            this.durationSameDiretion = ((int) (Math.random() * 6) + 4) * 1000;
            super.changeDirection();
        }
        timer += delta;
    }
}
