package mmorpg.player;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mmorpg.common.AnimationHolder;
import mmorpg.common.Drawable;
import mmorpg.common.Movable;
import mmorpg.common.Placeable;
import mmorpg.util.Timer;
import mmorpg.util.TimerListener;
import mmorpg.enemies.Enemy;
import mmorpg.items.Catchable;
import mmorpg.items.Catcher;
import mmorpg.items.HealthPotion;
import mmorpg.items.SpeedPotion;
import mmorpg.items.Treasure;
import mmorpg.map.room.Room;
import mmorpg.util.AudioController;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Diego
 */
public class Player extends Movable implements Placeable, TimerListener, Catcher {

    public static final int STATE_WALKING = 0;
    public static final int STATE_STILL = 1;
    public static final int STATE_ATTACKING = 2;
    private Room room;
    private float life, maxLife;
    private float attackForce;
    private int state;
    private Timer timerAppearsSword, timerHealInjure, timerHitTheDoor, timerSpeedPotion;
    private AnimationHolder animation;
    private Sword sword;
    private float originalSpeed;
    private ArrayList<PlayerEventListener> listeners;

    public Player() {
        super(10f, new Vector2f(0, 0), new Rectangle(0, 0, 32, 32));
        this.state = STATE_STILL;
        this.maxLife = 100;
        this.life = maxLife;//full
        this.attackForce = 10;
        this.timerHitTheDoor = new Timer(3, 1000, true);
        this.timerHitTheDoor.addListener(this);
        this.timerHitTheDoor.start();
        this.timerHealInjure = new Timer(2, 2000);
        this.timerHealInjure.addListener(this);
        this.timerAppearsSword = new Timer(1, 140);
        this.timerAppearsSword.addListener(this);
        this.timerSpeedPotion = new Timer(4, 5000);
        this.timerSpeedPotion.addListener(this);
        this.originalSpeed = speed;
        this.listeners = new ArrayList<>();
        sword = new Sword();
        sword.setVisible(true);
        animation = new AnimationHolder();
        setupAnimations();
    }

    @Override
    public void update(GameContainer container, int delta) {
        move(container, delta);
        timerAppearsSword.update(delta);
        timerHealInjure.update(delta);
        timerHitTheDoor.update(delta);
        timerSpeedPotion.update(delta);
//        float swordMaxLength = 28;
//        float swordMinLength = 12;
        if (timerAppearsSword.isRunning()) {
            Vector2f swordPostion = new Vector2f(position.x + getWidth() / 2 - sword.getWidth() / 2, position.y + getHeight() / 2 - sword.getHeight() / 2);
            if (animation.getCurrentAnimationName() == "left") {
                sword.setOrientation(Room.DIRECTION_WEST);
                swordPostion.x -= (getWidth() / 2 + 5);
                //sword.setWidth(swordMaxLength);
                //sword.setHeight(swordMinLength);
            } else if (animation.getCurrentAnimationName() == "right") {
                sword.setOrientation(Room.DIRECTION_EAST);
                swordPostion.x += (getWidth() / 2 + 5);
//                sword.setWidth(swordMaxLength);
//                sword.setHeight(swordMinLength);
            } else if (animation.getCurrentAnimationName() == "back") {
                sword.setOrientation(Room.DIRECTION_NORTH);
                swordPostion.y -= (getHeight() / 2 + 5);
//                sword.setWidth(swordMinLength);
//                sword.setHeight(swordMaxLength);
            } else if (animation.getCurrentAnimationName() == "front") {
                sword.setOrientation(Room.DIRECTION_SOUTH);
                swordPostion.y += (getHeight() / 2 + 5);
//                sword.setWidth(swordMinLength);
//                sword.setHeight(swordMaxLength);
            }
            sword.setPosition(swordPostion);
            sword.setVisible(true);
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.blue);
        this.body.setX(position.x);
        this.body.setY(position.y);
        if (graphic != null) {
            if (this.timerHealInjure.isRunning()) {
                ((Animation) graphic).draw(body.getX(), body.getY(), new Color(1, 1, 1, 0.5f));

            } else {
                ((Animation) graphic).draw(body.getX(), body.getY());
            }
        } else {
            g.fill(body);
            if (this.timerHealInjure.isRunning()) {
                g.setColor(Color.red);
                g.fillRect(position.x + width / 2 - 10, position.y + height / 2 - 10, 20, 20);
            }
        }
        if (timerAppearsSword.isRunning()) {
            sword.render(gc, g);
        }
    }

    public void attack(ArrayList<Placeable> objects) {
        boolean hurtEnemy = false;
        for (int i = 0; i < objects.size(); i++) {
            Placeable object = objects.get(i);
            if (object instanceof Enemy) {
                Enemy enemy = (Enemy) object;
                if (sword.collide(enemy)) {
                    enemy.injure(getAttackForce(), getPosition());
                    hurtEnemy = true;
                }
            }
        }
        if (hurtEnemy) {
            AudioController.getInstance().playSound("attack");
        } else {
            AudioController.getInstance().playSound("missattack");
        }
        this.timerAppearsSword.start();
    }

    private void move(GameContainer container, int delta) {
        Input input = container.getInput();
        float moveFactor = speed * (delta / 100f);
        if (input.isKeyDown(Input.KEY_LEFT) && room.canMoveTo(this, Room.DIRECTION_WEST) && room.movingInsideCamera(this, moveFactor, Room.DIRECTION_WEST)) {
            position.x -= moveFactor;
            updateAnimation(delta);
            setOrientation(Room.DIRECTION_WEST);
            this.state = STATE_WALKING;
        }
        if (input.isKeyDown(Input.KEY_RIGHT) && room.canMoveTo(this, Room.DIRECTION_EAST) && room.movingInsideCamera(this, moveFactor, Room.DIRECTION_EAST)) {
            position.x += moveFactor;
            updateAnimation(delta);
            setOrientation(Room.DIRECTION_EAST);
            this.state = STATE_WALKING;
        }
        if (input.isKeyDown(Input.KEY_UP) && room.canMoveTo(this, Room.DIRECTION_NORTH) && room.movingInsideCamera(this, moveFactor, Room.DIRECTION_NORTH)) {
            position.y -= moveFactor;
            updateAnimation(delta);
            setOrientation(Room.DIRECTION_NORTH);
            this.state = STATE_WALKING;
        }
        if (input.isKeyDown(Input.KEY_DOWN) && room.canMoveTo(this, Room.DIRECTION_SOUTH) && room.movingInsideCamera(this, moveFactor, Room.DIRECTION_SOUTH)) {
            position.y += moveFactor;
            updateAnimation(delta);
            setOrientation(Room.DIRECTION_SOUTH);
            this.state = STATE_WALKING;
        }
        if (input.isKeyDown(Input.KEY_LEFT)) {
            graphic = animation.changeAnimation("left");
        } else if (input.isKeyDown(Input.KEY_RIGHT)) {
            graphic = animation.changeAnimation("right");
        } else if (input.isKeyDown(Input.KEY_UP)) {
            graphic = animation.changeAnimation("back");
        } else if (input.isKeyDown(Input.KEY_DOWN)) {
            graphic = animation.changeAnimation("front");
        }
        if (!input.isKeyDown(Input.KEY_LEFT) && !input.isKeyDown(Input.KEY_RIGHT) && !input.isKeyDown(Input.KEY_UP) && !input.isKeyDown(Input.KEY_DOWN)) {
            animation.still();
        }
        //}
    }

    private void updateAnimation(int delta) {
        animation.updateAnimation(delta);
    }

    private void setupAnimations() {
        try {
            animation = new AnimationHolder();
            ArrayList<String> animationNames = new ArrayList<>();
            animationNames.add("front");
            animationNames.add("back");
            animationNames.add("left");
            animationNames.add("right");
            ArrayList<Animation> animations = new ArrayList<>();
            String model = "model3";
            int duration = 340;
            //deberia buscar el directorio de imagenes de un archivo de configuracion
            animations.add(new Animation(new Image[]{
                new Image("res/images/players/" + model + "/front0.png"),
                new Image("res/images/players/" + model + "/front1.png"),
                new Image("res/images/players/" + model + "/front2.png")
            }, duration, true));
            animations.add(new Animation(new Image[]{
                new Image("res/images/players/" + model + "/back0.png"),
                new Image("res/images/players/" + model + "/back1.png"),
                new Image("res/images/players/" + model + "/back2.png")
            }, duration, true));
            animations.add(new Animation(new Image[]{
                new Image("res/images/players/" + model + "/left0.png"),
                new Image("res/images/players/" + model + "/left1.png"),
                new Image("res/images/players/" + model + "/left2.png")
            }, duration, true));
            animations.add(new Animation(new Image[]{
                new Image("res/images/players/" + model + "/right0.png"),
                new Image("res/images/players/" + model + "/right1.png"),
                new Image("res/images/players/" + model + "/right2.png")
            }, duration, true));
            animation.setup(animationNames, animations, new int[]{0, 0, 0, 0});
            setOrientation(Room.DIRECTION_SOUTH);

        } catch (SlickException ex) {
            Logger.getLogger(Player.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setPosition(Vector2f position) {
        this.position = position;
        this.body.setX(position.x);
        this.body.setY(position.y);
    }

    @Override
    public Vector2f getPosition() {
        return position;
    }

    @Override
    public Room getRoom() {
        return this.room;
    }

    public float getLife() {
        return life;
    }

    public void setLife(float life) {
        this.maxLife = life;
        this.life = life;
    }

    public float getAttackForce() {
        return attackForce;
    }

    public void setAttackForce(float attackForce) {
        this.attackForce = attackForce;
    }

    public void injure(float enemyAttackForce) {
        life -= enemyAttackForce;
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).lifeChanged(life);
        }
        if (life <= 0) {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).playerDead();
            }
        }
        timerHealInjure.start();
    }

    public boolean isInjure() {
        return this.timerHealInjure.isRunning();
    }

    @Override
    public void setOrientation(int orientation) {
        switch (orientation) {
            case (Room.DIRECTION_WEST):
                graphic = animation.changeAnimation("left");
                break;
            case (Room.DIRECTION_EAST):
                graphic = animation.changeAnimation("right");
                break;
            case (Room.DIRECTION_NORTH):
                graphic = animation.changeAnimation("back");
                break;
            case (Room.DIRECTION_SOUTH):
                graphic = animation.changeAnimation("front");
                break;
        }
    }

    @Override
    public void finished(int timerId) {
        switch (timerId) {
            case (1)://appears sword
                sword.setVisible(false);
                break;
            case (2)://heal injure
                break;
            case (3)://pass throught door
                room.hitTheDoor(this);
                break;
            case (4)://speed potion
                speed = originalSpeed;
                break;
        }
    }

    @Override
    public void catches(Catchable catchable) {
        if (catchable instanceof Treasure) {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).treasureFound();
            }
        } else if (catchable instanceof HealthPotion) {
            float healPower = 50;
            if ((maxLife - life) > healPower) {
                this.life += healPower;
            } else {
                this.life = maxLife;
            }
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).lifeChanged(life);
            }
            AudioController.getInstance().playSound("healthpotion");
        } else if (catchable instanceof SpeedPotion) {
            timerSpeedPotion.start();
            float speedPower = originalSpeed * 2;//no es acumulativo
            speed = speedPower;
            AudioController.getInstance().playSound("speedpotion");
        }
    }

    public float getMaxLife() {
        return maxLife;
    }

    @Override
    public Drawable getDrawable() {
        return this;
    }

    public boolean addListener(PlayerEventListener listener) {
        return this.listeners.add(listener);
    }

    public boolean removeListener(PlayerEventListener listener) {
        return this.listeners.remove(listener);
    }
}
