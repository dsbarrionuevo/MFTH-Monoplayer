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
import mmorpg.items.Item;
import mmorpg.items.SpeedPotion;
import mmorpg.items.Treasure;
import mmorpg.map.MapEventListener;
import mmorpg.map.room.Room;
import mmorpg.player.PlayerEventListener;
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
public class MMORPG extends BasicGame implements PlayerEventListener, MapEventListener {

    private Map map;
    private Player player;
    private StatsLayer statsLayer;
    private Room currentRoom;
    private ArrayList<Enemy> enemiesInRoom;
    private ArrayList<Item> itemsInRoom;
    private boolean firstTimeRoomChanged;

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
        SpeedPotion t = new SpeedPotion();
        t.setCatcher(player);
        currentRoom.addObject(t, 5, 4);
        //
        currentRoom.focusObject(player);
        statsLayer = new StatsLayer();
//        enemiesInRoom = new ArrayList<>();
//        itemsInRoom = new ArrayList<>();
//        player = new Player();
//        player.addListener(this);
//        map = new Map(1);
//        map.addListener(this);
//        map.build();
//        currentRoom = map.getCurrentRoom();
//        currentRoom.addObject(player, 1, 1);
//        Treasure t = new Treasure();
//        t.setCatcher(player);
//        currentRoom.addObject(t, 5, 4);
//        System.out.println(player == null);
//        //currentRoom.focusObject(player);
//        statsLayer = new StatsLayer();
//        statsLayer.setMaxLife(player.getMaxLife());
//        //room is changed atuomatically, so the next setop is to init everything for the first time in roomChanged callback method below...
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
                    player.injure(enemy.getAttackForce());
                    //no haria falta la notificacion de gameover porque aqui la puedo checkear
                }
            }
        }
        ArrayList<Item> items = currentRoom.getItems();
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if(!item.isVisible()){
                currentRoom.removeObject(item);
                continue;
            }
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        map.render(container, g);
        statsLayer.render(container, g);

    }

    @Override
    public void playerDead() {
        System.out.println("CTRL GAMVEOVER!");
        currentRoom.removeObject(player);
    }

    @Override
    public void treasureFound() {
        System.out.println("CTRL WIN!");
    }

    @Override
    public void lifeChanged(float life) {
        statsLayer.updateLifeBar(life);
    }

    @Override
    public void roomChanged(Room currentRoom) {
        this.currentRoom = currentRoom;
        enemiesInRoom = this.currentRoom.getEnemies();
        System.out.println(enemiesInRoom.size());
        for (int i = 0; i < enemiesInRoom.size(); i++) {
            enemiesInRoom.get(i).setTarget(player);
        }
        itemsInRoom = this.currentRoom.getItems();
        for (int i = 0; i < itemsInRoom.size(); i++) {
            itemsInRoom.get(i).setCatcher(player);
        }
        if (player != null) {
            this.currentRoom.focusObject(player);
        }
    }

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

    public static void main(String[] args) {
        try {
            new MMORPG();

        } catch (SlickException ex) {
            Logger.getLogger(MMORPG.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

}
