package mmorpg.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Barrionuevo Diego
 */
public class Menu extends BasicGameState {

    private StateBasedGame stateController;
    private Image image;

    @Override
    public int getID() {
        return StateController.STATE_MENU;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.stateController = game;
        stateController.enterState(StateController.STATE_GAME);//solo para pruebas
        image = new Image("res/images/scenes/welcome.jpg");
        (new Sound("res/sounds/ambient.wav")).loop();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.drawImage(image, 0, 0);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        stateController.enterState(StateController.STATE_GAME);
    }

}
