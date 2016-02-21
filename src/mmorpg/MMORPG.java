package mmorpg;

import java.util.ArrayList;
import mmorpg.map.Map;
import mmorpg.player.Player;
import java.util.logging.Level;
import java.util.logging.Logger;
import mmorpg.camera.Camera;
import mmorpg.camera.StatsLayer;
import mmorpg.enemies.Enemy;
import mmorpg.map.room.Room;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
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
            //esto lo deberia hacer cada enemigo en su update, pero el enemigo no sabe cual es su target y este puede cambiar... (puede ser null cuando el jugador deja la sala)
            enemy.updateFollowingStrategy(player, delta);
            if (enemy.collide(player)) {
                if (player.isAttacking()) {
                    //kill enemy
                    currentRoom.removeObject(enemy);
                } else {
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
