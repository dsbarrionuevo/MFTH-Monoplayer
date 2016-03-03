package mmorpg.mapreader;

/**
 *
 * @author Barrionuevo Diego
 */
public class PlayerFile {

    private double life;
    private double attackForce;
    private double speed;
    private int roomId;
    private PositionFile position;

    public PlayerFile(double life, double attackForce, double speed, int roomId, PositionFile position) {
        this.life = life;
        this.attackForce = attackForce;
        this.speed = speed;
        this.roomId = roomId;
        this.position = position;
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

    public int getRoomId() {
        return roomId;
    }

    public PositionFile getPosition() {
        return position;
    }

}
