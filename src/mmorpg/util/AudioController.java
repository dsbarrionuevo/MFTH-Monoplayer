package mmorpg.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *
 * @author Barrionuevo Diego
 */
public class AudioController {

    private static AudioController me;

    public AudioController() {

    }

    public static AudioController getInstance() {
        if (me == null) {
            me = new AudioController();
        }
        return me;
    }

    public void playSound(String fileName) {
        try {
            //deberia estar centralizado el acceso a recursos
            Sound sound = new Sound("res/sounds/" + fileName + ".wav");
            sound.play();
        } catch (SlickException ex) {
            Logger.getLogger(AudioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
