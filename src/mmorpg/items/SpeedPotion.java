package mmorpg.items;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Barrionuevo Diego
 */
public class SpeedPotion extends Item {

    public SpeedPotion() {
        super(new Vector2f(), new Rectangle(0, 0, 36, 36), true);
        try {
            graphic = new Image("res/images/items/speedpotion.png");
        } catch (SlickException ex) {
            Logger.getLogger(Treasure.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.red);
        this.body.setX(position.x);
        this.body.setY(position.y);
        if (graphic != null) {
            ((Image) graphic).draw(body.getX(), body.getY());
        } else {
            g.fill(body);
        }
    }
}
