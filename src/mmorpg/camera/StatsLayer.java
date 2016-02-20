package mmorpg.camera;

import mmorpg.common.Drawable;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Barrionuevo Diego
 */
public class StatsLayer {
    
    private Drawable lifeBar;
    
    public StatsLayer() {
        lifeBar = new Drawable(new Vector2f(10, 10), new Rectangle(0, 0, 200, 10)) {
            
            @Override
            public void render(GameContainer gc, Graphics g) {
                this.body.setX(position.x);
                this.body.setY(position.y);
                g.setColor(Color.green);
                g.fill(body);
                g.setColor(Color.black);
                g.draw(body);
            }
        };
    }
    
    public void render(GameContainer gc, Graphics g) {
        this.lifeBar.render(gc, g);
    }
    
}
