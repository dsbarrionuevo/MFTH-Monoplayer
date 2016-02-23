package mmorpg.items;

import mmorpg.common.Drawable;

/**
 *
 * @author Barrionuevo Diego
 */
public interface Catcher {

    public void catches(Catchable catchable);
    
    public Drawable getDrawable();
    
}
