package mmorpg.enemies;

import mmorpg.common.Placeable;

/**
 *
 * @author Barrionuevo Diego
 */
public interface Chaser extends Placeable {
    
    public void startFollowing();
    public void stopFollowing();

}
