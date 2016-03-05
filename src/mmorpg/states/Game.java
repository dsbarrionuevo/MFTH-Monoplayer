package mmorpg.states;

import java.util.ArrayList;
import mmorpg.camera.Camera;
import mmorpg.camera.StatsLayer;
import mmorpg.common.Placeable;
import mmorpg.enemies.Enemy;
import mmorpg.items.Item;
import mmorpg.map.Map;
import mmorpg.map.MapEventListener;
import mmorpg.map.room.Room;
import mmorpg.player.Player;
import mmorpg.player.PlayerEventListener;
import mmorpg.util.AudioController;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Barrionuevo Diego
 */
public class Game extends BasicGameState implements PlayerEventListener, MapEventListener {

    private StateBasedGame stateController;
    private Map map;
    private Player player;
    private StatsLayer statsLayer;
    private Room currentRoom;
    private ArrayList<Enemy> enemiesInRoom;
    private ArrayList<Item> itemsInRoom;

    @Override
    public int getID() {
        return StateController.STATE_GAME;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.stateController = game;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        Camera.createCamera(new Vector2f(0f, 0f), container.getWidth(), container.getHeight());
        Camera.getInstance().setPadding(4);
        map = new Map(3);
        map.addListener(this);
        map.build();
        currentRoom = map.getCurrentRoom();
        //currentRoom.addObject(player, 1, 1);
        ArrayList<Placeable> objects = currentRoom.getObjects();
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) instanceof Player) {
                player = (Player) objects.get(i);
            }
        }
        if (player == null) {
            System.err.println("Player not set in any room");
        }
        player.addListener(this);
        enemiesInRoom = currentRoom.getEnemies();
        for (int i = 0; i < enemiesInRoom.size(); i++) {
            enemiesInRoom.get(i).setTarget(player);
        }
        itemsInRoom = currentRoom.getItems();
        for (int i = 0; i < itemsInRoom.size(); i++) {
            itemsInRoom.get(i).setCatcher(player);
        }
        currentRoom.focusObject(player);
        statsLayer = new StatsLayer();
        statsLayer.setMaxLife(player.getMaxLife());
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        currentRoom.update(container, delta);
        //todo el control se hace aquí
        ArrayList<Enemy> enemies = enemiesInRoom;
        for (Enemy enemy : enemies) {
            if (enemy.isDead()) {
                currentRoom.removeObject(enemy);
                //enemiesInRoom.remove(enemy);
                continue;
            }
            if (enemy.collide(player)) {
                if (!player.isInjure()) {
                    player.injure(enemy.getAttackForce());
                }
            }
        }
        ArrayList<Item> items = itemsInRoom;
        for (Item item : items) {
            if (!item.isVisible()) {
                currentRoom.removeObject(item);
                //itemsInRoom.remove(item);
                continue;
            }
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        map.render(container, g);
        statsLayer.render(container, g);
    }

    @Override
    public void playerDead() {
        currentRoom.removeObject(player);
        ((End) stateController.getState(StateController.STATE_END)).setVictory(false);
        AudioController.getInstance().playSound("gameover");
        stateController.enterState(StateController.STATE_END);
    }

    @Override
    public void treasureFound() {
        ((End) stateController.getState(StateController.STATE_END)).setVictory(true);
        AudioController.getInstance().playSound("victory");
        stateController.enterState(StateController.STATE_END);
    }

    @Override
    public void lifeChanged(float life) {
        statsLayer.updateLifeBar(life);
    }

    @Override
    public void roomChanged(Room newRoom) {
        currentRoom = newRoom;
        if (player != null) {
            enemiesInRoom = currentRoom.getEnemies();
            for (int i = 0; i < enemiesInRoom.size(); i++) {
                enemiesInRoom.get(i).setTarget(player);
            }
            itemsInRoom = currentRoom.getItems();
            for (int i = 0; i < itemsInRoom.size(); i++) {
                itemsInRoom.get(i).setCatcher(player);
            }
            currentRoom.focusObject(player);
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        super.keyReleased(key, c);
        //debo comprobar que el juego haya iniciado
        if (key == Input.KEY_SPACE && player != null) {
            ArrayList<Placeable> objects = currentRoom.getObjectsNearby(player);
            //podría agregar una caracteristica a cada placeable para ver si es afecatdo por la esapda, por ejemplo un cofre rompible
            player.attack(objects);
        }
    }

}
