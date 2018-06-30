package net.team11.pixeldungeon.game.entity.component;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.team11.pixeldungeon.game.entitysystem.EntityComponent;

import java.util.HashMap;
import java.util.Map;

public class AnimationComponent implements EntityComponent {

    //  Char state variables
    private float stateTime;

    private Animation<TextureRegion> previousAnimation;
    private Animation<TextureRegion> currentAnimation;
    private Map<String, Animation<TextureRegion>> animationList = new HashMap<>();

    public AnimationComponent(float stateTime) {
        this.stateTime = stateTime;
    }

    public void addAnimation(String animationName, TextureAtlas textureAtlas, float frameDuration, Animation.PlayMode playMode) {
        animationList.put(animationName, new Animation<TextureRegion>(frameDuration, textureAtlas.findRegions(animationName),
                playMode));
    }

    public void setAnimation(String animationName) {
        setAnimation(animationList.get(animationName));
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        //Checks if they are the same or if its a ongoing animation
        if (this.getCurrentAnimation() != null) {
            if (this.currentAnimation == animation) {
                return;
            }

            if (this.getCurrentAnimation().getPlayMode() == Animation.PlayMode.NORMAL) {
                if (!this.getCurrentAnimation().isAnimationFinished(stateTime)) {
                    return;
                }
            }
        }

        //Set the animation
        this.previousAnimation = currentAnimation;
        this.currentAnimation = animation;
        setStateTime(0);
    }


    public void setAnimation(String animationName, float stateTime) {
        setAnimation(animationList.get(animationName));
        setStateTime(stateTime);
    }

    public void setAnimation(Animation<TextureRegion> animation, Animation<TextureRegion> nextAnimation) {
        //Checks if they are the same or if its a ongoing animation
        if (this.getCurrentAnimation() != null) {
            if (this.currentAnimation == animation) {
                return;
            }

            if (this.getCurrentAnimation().getPlayMode() == Animation.PlayMode.NORMAL) {
                if (!this.getCurrentAnimation().isAnimationFinished(stateTime)) {
                    this.currentAnimation = previousAnimation;
                }
            }
        }

        //Set the animation
        this.previousAnimation = nextAnimation;
        this.currentAnimation = animation;
        setStateTime(0);
    }

    //Getters & Setters
    public Animation<TextureRegion> getCurrentAnimation() {
        return currentAnimation;
    }

    public void setNextAnimation(String animation) {
        if (animationList.containsKey(animation)) {
            previousAnimation = animationList.get(animation);
        }
    }
    public Map<String, Animation<TextureRegion>> getAnimationList() {
        return animationList;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public Animation<TextureRegion> getPreviousAnimation() {
        return previousAnimation;
    }
}
