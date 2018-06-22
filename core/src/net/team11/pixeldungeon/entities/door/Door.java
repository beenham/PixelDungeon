package net.team11.pixeldungeon.entities.door;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.DoorComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.CollisionUtil;

public class Door extends Entity {
    public enum Type {
        BUTTON, LOCKED, MECHANIC;

        @Override
        public final String toString() {
            switch (this) {
                case BUTTON:
                    return "BUTTON";
                case LOCKED:
                    return "LOCKED";
                case MECHANIC:
                    return "MECHANIC";
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    protected Type type;
    protected boolean open;

    protected Door(String name, Rectangle bounds, Type type, boolean open) {
        super(name);
        this.type = type;
        this.open = open;

        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()/2;

        AnimationComponent animationComponent;
        this.addComponent(new DoorComponent(this));
        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(), posX, posY, 0f,
                (CollisionUtil.ENTITY),
                (byte)(CollisionUtil.ENTITY | CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));
        this.addComponent(animationComponent = new AnimationComponent(0));
        setupAnimations(animationComponent);
    }

    private void setupAnimations(AnimationComponent animationComponent) {
        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.BLOCKS);
        animationComponent.addAnimation(AssetName.DOOR_OPENED, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.BUTTONDOOR_CLOSED, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.LOCKEDDOOR_CLOSED, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.MECHANICDOOR_CLOSED, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.BUTTONDOOR_OPENING, textureAtlas, 1f, Animation.PlayMode.NORMAL);
        animationComponent.addAnimation(AssetName.LOCKEDDOOR_OPENING, textureAtlas, 1f, Animation.PlayMode.NORMAL);
        animationComponent.addAnimation(AssetName.MECHANICDOOR_OPENING, textureAtlas, 1f, Animation.PlayMode.NORMAL);
        animationComponent.addAnimation(AssetName.MECHANICDOOR_CLOSING, textureAtlas, .75f, Animation.PlayMode.NORMAL);
        switch (type) {
            case BUTTON:
                if (open) {
                    animationComponent.setAnimation(AssetName.DOOR_OPENED);
                } else {
                    animationComponent.setAnimation(AssetName.BUTTONDOOR_CLOSED);
                }
                break;
            case LOCKED:
                if (open) {
                    animationComponent.setAnimation(AssetName.DOOR_OPENED);
                } else {
                    animationComponent.setAnimation(AssetName.LOCKEDDOOR_CLOSED);
                }
                break;
            case MECHANIC:
                if (open) {
                    animationComponent.setAnimation(AssetName.DOOR_OPENED);
                } else {
                    animationComponent.setAnimation(AssetName.MECHANICDOOR_CLOSED);
                }
                break;
        }
        if (open) {
            getComponent(BodyComponent.class).removeBody();
        }
    }

    protected void setOpened(boolean opened) {
        this.open = opened;
        if (!open) {
            switch (type) {
                case BUTTON:
                    getComponent(AnimationComponent.class).setAnimation(AssetName.BUTTONDOOR_CLOSED);
                    break;
                case LOCKED:
                    getComponent(AnimationComponent.class).setAnimation(AssetName.LOCKEDDOOR_CLOSED);
                    break;
                case MECHANIC:
                    getComponent(AnimationComponent.class).setAnimation(AssetName.MECHANICDOOR_CLOSING);
                    getComponent(AnimationComponent.class).setNextAnimation(AssetName.MECHANICDOOR_CLOSED);
                    break;
            }
            getComponent(BodyComponent.class).createBody(BodyDef.BodyType.StaticBody);
        } else {
            switch (type) {
                case BUTTON:
                    getComponent(AnimationComponent.class).setAnimation(AssetName.BUTTONDOOR_OPENING);
                    break;
                case LOCKED:
                    getComponent(AnimationComponent.class).setAnimation(AssetName.LOCKEDDOOR_OPENING);
                    break;
                case MECHANIC:
                    getComponent(AnimationComponent.class).setAnimation(AssetName.MECHANICDOOR_OPENING);
                    break;
            }
            getComponent(AnimationComponent.class).setNextAnimation(AssetName.DOOR_OPENED);
            getComponent(BodyComponent.class).removeBody();
        }
    }

    public boolean isOpen() {
        return open;
    }
}
