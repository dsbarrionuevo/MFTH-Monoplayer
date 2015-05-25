package mmorpg.mapreader;

/**
 *
 * @author Barrionuevo Diego
 */
public class EnemyType {

    private int id;
    private String name;
    private double speed;

    public EnemyType(int id, String name, double speed) {
        this.id = id;
        this.name = name;
        this.speed = speed;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getSpeed() {
        return speed;
    }

}
