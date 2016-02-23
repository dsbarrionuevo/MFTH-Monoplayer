package mmorpg.states;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Barrionuevo Diego
 */
public class StateController extends StateBasedGame {

    public static final int STATE_MENU = 0;
    public static final int STATE_GAME = 1;
    public static final int STATE_END = 2;

    public StateController() {
        super("Ordinary RPG Game");
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new Menu());
        addState(new Game());
        addState(new End());
    }

    public static void main(String[] args) {
        try {
            AppGameContainer container = new AppGameContainer(new StateController());
            container.setDisplayMode(800, 600, false);
            container.setShowFPS(false);
            container.start();
        } catch (SlickException ex) {
            Logger.getLogger(StateController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
