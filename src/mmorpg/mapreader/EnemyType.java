package mmorpg.mapreader;

/**
 *
 * @author Barrionuevo Diego
 */
public class EnemyType {

    private int id;
    private String name;
    private double life;
    private double attackForce;
    private double speed;

    public EnemyType(int id, String name, double life, double attackForce, double speed) {
        this.id = id;
        this.name = name;
        this.life = life;
        this.attackForce = attackForce;
        this.speed = speed;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLife() {
        return life;
    }

    public double getAttackForce() {
        return attackForce;
    }

    public double getSpeed() {
        return speed;
    }

}
