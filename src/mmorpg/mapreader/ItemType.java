package mmorpg.mapreader;

/**
 *
 * @author Barrionuevo Diego
 */
public class ItemType {

    private int id;
    private String name;
    private String resourcePath;

    public ItemType(int id, String name, String resourcePath) {
        this.id = id;
        this.name = name;
        this.resourcePath = resourcePath;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getResourcePath() {
        return resourcePath;
    }

}
