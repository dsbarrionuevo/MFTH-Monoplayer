package mmorpg.mapreader;

/**
 *
 * @author Barrionuevo Diego
 */
public class Animation {

    private String side;
    private AnimationFile animationFile;

    public Animation(String side, AnimationFile animationFile) {
        this.side = side;
        this.animationFile = animationFile;
    }

    public String getSide() {
        return side;
    }

    public AnimationFile getAnimationFile() {
        return animationFile;
    }

}
