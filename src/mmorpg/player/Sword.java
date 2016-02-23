package mmorpg.player;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mmorpg.common.AnimationHolder;
import mmorpg.common.Drawable;
import mmorpg.map.room.Room;
import org.newdawn.slick.Animation;
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
public class Sword extends Drawable {

    private AnimationHolder animation;

    public Sword() {
        super(new Vector2f(), new Rectangle(0, 0, 32, 32), true);
        animation = new AnimationHolder();
        setupAnimations();
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.cyan);
        this.body.setX(position.x);
        this.body.setY(position.y);
        ((Rectangle)this.body).setWidth(width);
        ((Rectangle)this.body).setHeight(height);
        if (graphic != null) {
            ((Animation) graphic).draw(body.getX(), body.getY());
        } else {
            g.fill(body);
        }
    }

    private void setupAnimations() {
        try {
            animation = new AnimationHolder();
            ArrayList<String> animationNames = new ArrayList<>();
            animationNames.add("front");
            animationNames.add("back");
            animationNames.add("left");
            animationNames.add("right");
            ArrayList<Animation> animations = new ArrayList<>();
            int duration = 1;
            animations.add(new Animation(new Image[]{
                new Image("res/images/players/sword/front.png")
            }, duration, true));
            animations.add(new Animation(new Image[]{
                new Image("res/images/players/sword/back.png")
            }, duration, true));
            animations.add(new Animation(new Image[]{
                new Image("res/images/players/sword/left.png")
            }, duration, true));
            animations.add(new Animation(new Image[]{
                new Image("res/images/players/sword/right.png")
            }, duration, true));
            animation.setup(animationNames, animations, new int[]{0, 0, 0, 0});
            animation.still();
            setOrientation(Room.DIRECTION_SOUTH);
        } catch (SlickException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setOrientation(int orientation) {
        switch (orientation) {
            case (Room.DIRECTION_WEST):
                graphic = animation.changeAnimation("left");
                break;
            case (Room.DIRECTION_EAST):
                graphic = animation.changeAnimation("right");
                break;
            case (Room.DIRECTION_NORTH):
                graphic = animation.changeAnimation("back");
                break;
            case (Room.DIRECTION_SOUTH):
                graphic = animation.changeAnimation("front");
                break;
        }
    }

    public AnimationHolder getAnimation() {
        return animation;
    }

}
