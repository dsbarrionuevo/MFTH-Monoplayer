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

    private LifeBar lifeBar;

    public StatsLayer() {
        lifeBar = new LifeBar();
    }

    public void render(GameContainer gc, Graphics g) {
        lifeBar.render(gc, g);
    }

    public void updateLifeBar(float lifePoints) {
        lifeBar.updateLifeBar(lifePoints);
    }

    public void setMaxLife(float maxLife) {
        this.lifeBar.maxLife = maxLife;
    }

    class LifeBar {

        private Drawable lifeBar;
        private float currentWidth, maxWidth;
        protected float maxLife;

        public LifeBar() {
            maxWidth = 200;
            currentWidth = maxWidth;
            lifeBar = new Drawable(new Vector2f(10, 10), new Rectangle(0, 0, maxWidth, 10)) {

                @Override
                public void render(GameContainer gc, Graphics g) {
                    this.body.setX(position.x);
                    this.body.setY(position.y);
                    ((Rectangle) this.body).setWidth(currentWidth);
                    g.setColor(Color.green);
                    g.fill(body);
                    g.setColor(Color.black);
                    ((Rectangle) this.body).setWidth(maxWidth);
                    g.draw(body);
                    g.setColor(Color.white);
                    this.body.setX(position.x - 1);
                    this.body.setY(position.y - 1);
                    ((Rectangle) this.body).setWidth(maxWidth + 1);
                    ((Rectangle) this.body).setHeight(10 + 1);
                    g.draw(body);
                }
            };
        }

        private void updateLifeBar(float lifePoints) {
            if (lifePoints <= 0) {
                currentWidth = 0;
            } else {
                float lifeWidth = (lifePoints * maxWidth) / maxLife;
                currentWidth = lifeWidth;
            }
        }

        public void render(GameContainer gc, Graphics g) {
            lifeBar.render(gc, g);
        }

    }

}
