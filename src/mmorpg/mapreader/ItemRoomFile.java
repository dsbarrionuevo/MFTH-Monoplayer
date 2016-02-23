package mmorpg.mapreader;

/**
 *
 * @author Barrionuevo Diego
 */
public class ItemRoomFile {

    private ItemType item;
    private PositionFile position;

    public ItemRoomFile(ItemType item, PositionFile position) {
        this.item = item;
        this.position = position;
    }

    public ItemType getItem() {
        return item;
    }

    public PositionFile getPosition() {
        return position;
    }

}
