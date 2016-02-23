package mmorpg.util;

import java.util.ArrayList;

/**
 *
 * @author Barrionuevo Diego
 */
public class Timer {

    private long time, duration;
    private int id;
    private ArrayList<TimerListener> listeners;
    private boolean isStarted, isLoop;

    public Timer(int id, long duration, boolean loop) {
        this.id = id;
        this.time = 0;
        this.duration = duration;
        this.isLoop = loop;
        this.isStarted = false;
        this.listeners = new ArrayList<>();
    }

    public Timer(int id, long duration) {
        this(id, duration, false);
    }

    public boolean addListener(TimerListener listener) {
        return this.listeners.add(listener);
    }

    public boolean removeListener(TimerListener listener) {
        return this.listeners.remove(listener);
    }

    public void update(int delta) {
        if (isStarted) {
            if (this.time > this.duration) {
                this.time = 0;
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).finished(this.id);
                }
                //loop isnt the default behavior
                isStarted = false;
            }
            this.time += delta;
            if (!isStarted) {
                this.time = 0;
            }
            if (isLoop) {
                this.start();
            }
        }
    }

    public void start() {
        this.isStarted = true;
    }

    public void stop() {
        this.isStarted = false;
        this.time = 0;
    }

    public boolean isFinished() {
        return !(this.time < this.duration && this.time != 0);
    }

    public boolean isRunning() {
        return isStarted;
    }

    public boolean isLoop() {
        return isLoop;
    }

}
