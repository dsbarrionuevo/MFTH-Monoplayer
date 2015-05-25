package mmorpg.enemies;

import org.newdawn.slick.GameContainer;

/**
 *
 * @author Barrionuevo Diego
 */
public class SmartWallEnemy extends WallEnemy {

    private long durationSameDiretion, timer;

    public SmartWallEnemy() {
        super();
        this.timer = 0;
        this.durationSameDiretion = ((int) (Math.random() * 6) + 4) * 1000;
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
