package mmorpg.mapreader;

/**
 *
 * @author Barrionuevo Diego
 */
public class AnimationFile {

    private String path;
    private String[] sprites;
    private String still;

    public AnimationFile(String path, String[] sprites, String still) {
        this.path = path;
        this.sprites = sprites;
        this.still = still;
    }

    public String getPath() {
        return path;
    }

    public String[] getSprites() {
        return sprites;
    }

    public String getStill() {
        return still;
    }

}
