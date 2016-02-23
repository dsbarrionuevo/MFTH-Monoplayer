package mmorpg.states;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Barrionuevo Diego
 */
public class End extends BasicGameState {

    private StateBasedGame stateController;
    private Image image;

    @Override
    public int getID() {
        return StateController.STATE_END;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.stateController = game;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.drawImage(image, 0, 0);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

    }

    public void setVictory(boolean victory) {
        try {
            if (victory) {
                image = new Image("res/images/scenes/victory.jpg");
            } else {
                image = new Image("res/images/scenes/gameover.jpg");
            }
        } catch (SlickException ex) {
            Logger.getLogger(End.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
