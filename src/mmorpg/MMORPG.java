package mmorpg;

import java.util.ArrayList;
import mmorpg.map.Map;
import mmorpg.player.Player;
import java.util.logging.Level;
import java.util.logging.Logger;
import mmorpg.camera.Camera;
import mmorpg.camera.StatsLayer;
import mmorpg.common.Placeable;
import mmorpg.enemies.Enemy;
import mmorpg.map.room.Room;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Diego
 */
public class MMORPG extends BasicGame {

    private Map map;
    private Player player;
    private StatsLayer statsLayer;
    private Room currentRoom;

    @Override
    public void keyReleased(int key, char c) {
        super.keyReleased(key, c);
        //debo comprobar que el juego haya iniciado
        //57 space
        if (key == Input.KEY_SPACE && player != null) {
            ArrayList<Placeable> objects = currentRoom.getObjectsNearby(player);
            //podría agregar una caracteristica a cada placeable apra ver si es afecatdo por la esapda, por ejemplo un cofre rompible
            player.attack(objects);
        }
    }

    public MMORPG() throws SlickException {
        super("MMORPG");
        AppGameContainer container = new AppGameContainer(this);
        container.setDisplayMode(800, 600, false);
        container.setShowFPS(false);
        container.start();
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        Camera.createCamera(new Vector2f(0f, 0f), container.getWidth(), container.getHeight());
        Camera.getInstance().setPadding(4);
        map = new Map(1);
        player = new Player();
        currentRoom = map.getCurrentRoom();
        currentRoom.addObject(player, 1, 1);
        ArrayList<Enemy> enemies = currentRoom.getEnemies();
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).setTarget(player);
        }
        currentRoom.focusObject(player);
        //
        statsLayer = new StatsLayer();
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        currentRoom = map.getCurrentRoom();
        currentRoom.update(container, delta);
        //todo el control se hace aquí
        ArrayList<Enemy> enemies = currentRoom.getEnemies();
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (enemy.isDead()) {
                currentRoom.removeObject(enemy);
                continue;
            }
            if (enemy.collide(player)) {
                if (!player.isInjure()) {
                    player.injure();
                    float enemyAttackForce = enemy.getAttackForce();
                    player.setLife(player.getLife() - enemyAttackForce);
                    statsLayer.decreaseLifeBar(enemyAttackForce);
                    if (player.getLife() <= 0) {
                        System.out.println("GAMEOVER");
                    }
                }
            }
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        map.render(container, g);
        statsLayer.render(container, g);
    }

    public static void main(String[] args) {
        try {
            new MMORPG();

        } catch (SlickException ex) {
            Logger.getLogger(MMORPG.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }
}
