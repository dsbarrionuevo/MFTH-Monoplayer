package mmorpg;

import java.util.logging.Level;
import java.util.logging.Logger;
import mmorpg.camera.Camera;
import mmorpg.camera.StatsLayer;
import mmorpg.map.Map;
import mmorpg.player.Player;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Barrionuevo Diego
 */
public class GameController {

    private Map map;
    private Player player;
    private StatsLayer statsLayer;

    public void init(GameContainer container) throws SlickException {
        Camera.createCamera(new Vector2f(0f, 0f), container.getWidth(), container.getHeight());
        Camera.getInstance().setPadding(4);
        map = new Map(1);
        player = new Player();
        map.getCurrentRoom().addObject(player, 1, 1);
        map.getCurrentRoom().focusObject(player);
        //
        statsLayer = new StatsLayer();
    }

    public void update(GameContainer container, int delta) throws SlickException {
        map.getCurrentRoom().update(container, delta);
//        ArrayList<Enemy> enemies = map.getCurrentRoom().getEnemies();
//        for (int i = 0; i < enemies.size(); i++) {
//            if(enemies.get(i).collide(player)){
//                enemies.get(i).setVisible(false);
//            }
//        }
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        map.render(container, g);
        statsLayer.render(container, g);
    }

}
