package mmorpg.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.newdawn.slick.Animation;

/**
 *
 * @author Barrionuevo Diego
 */
public class AnimationHolder {

    private static final int INVALID_STILL_FRAME = -1;
    private Map<String, Animation> animationMap;
    private Animation currentAnimation;
    private String currentAnimationName;
    private int[] stillFrames;

    public AnimationHolder() {
        this.animationMap = new HashMap<String, Animation>();
        this.stillFrames = null;
        this.currentAnimation = null;
        this.currentAnimationName = null;
    }

    public void updateAnimation(int delta) {
        if (this.currentAnimation != null) {
            if (currentAnimation.isStopped()) {
                currentAnimation.start();
            }
            currentAnimation.update(delta);
        }
    }

    public void stop() {
        currentAnimation.stop();
    }

    public void start() {
        currentAnimation.start();
    }

    public boolean setup(List<String> movements, List<Animation> animations) {
        this.stillFrames = new int[movements.size()];
        for (int i = 0; i < stillFrames.length; i++) {
            stillFrames[i] = INVALID_STILL_FRAME;
        }
        return this.setup(movements, animations, stillFrames);
    }

    public boolean setup(List<String> movements, List<Animation> animations, int[] stillFrames) {
        if (movements.size() != animations.size()) {
            return false;
        }
        this.stillFrames = new int[movements.size()];
        for (int i = 0; i < movements.size(); i++) {
            animationMap.put(movements.get(i), animations.get(i));
            this.stillFrames[i] = stillFrames[i];
        }
        return true;
    }

    public boolean setStillFrameForAnimation(Animation animation, int stillFrame) {
        if (this.stillFrames.length != animationMap.size()) {
            this.stillFrames = new int[animationMap.size()];
            for (int i = 0; i < animationMap.size(); i++) {
                this.stillFrames[i] = INVALID_STILL_FRAME;
            }
        }
        int index = 0;
        for (Map.Entry<String, Animation> entry : animationMap.entrySet()) {
            if (entry.getValue().equals(animation)) {
                this.stillFrames[index] = stillFrame;
                return true;
            }
            index++;
        }
        return false;
    }

    public boolean still() {
        if (currentAnimation == null) {
            return false;
        }
        int index = 0;
        for (Map.Entry<String, Animation> entry : animationMap.entrySet()) {
            if (entry.getValue().equals(currentAnimation)) {
                if (index < 0 || index > stillFrames.length - 1 || stillFrames[index] == INVALID_STILL_FRAME) {
                    return false;
                }
                currentAnimation.stop();
                currentAnimation.setCurrentFrame(this.stillFrames[index]);
                return false;
            }
            index++;
        }
        return false;
    }

    public Animation changeAnimation(String name) {
        this.currentAnimation = animationMap.get(name);
        this.currentAnimationName = name;
        return this.currentAnimation;
    }

    public Map<String, Animation> getAnimationMap() {
        return animationMap;
    }

    public void setAnimationMap(Map<String, Animation> animationMap) {
        this.animationMap = animationMap;
    }

    public Animation getCurrentAnimation() {
        return currentAnimation;
    }
    
    public String  getCurrentAnimationName() {
        return this.currentAnimationName;
    }

    public void setCurrentAnimation(Animation currentAnimation) {
        this.currentAnimation = currentAnimation;
    }

}
