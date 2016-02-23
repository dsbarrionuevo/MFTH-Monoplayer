package mmorpg.player;

/**
 *
 * @author Barrionuevo Diego
 */
public interface PlayerEventListener {

    public void playerDead();

    public void treasureFound();

    public void lifeChanged(float life);

}
