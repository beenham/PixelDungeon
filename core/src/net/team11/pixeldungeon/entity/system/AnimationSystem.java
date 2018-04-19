package net.team11.pixeldungeon.entity.system;

import com.badlogic.gdx.graphics.g2d.Animation;

import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;

import java.util.ArrayList;
import java.util.List;

public class AnimationSystem extends EntitySystem {

    private List<Entity> entities = new ArrayList<>();

    @Override
    public void init(EntityEngine entityEngine) {
        entities = entityEngine.getEntities(AnimationComponent.class);
    }

    @Override
    public void update(float delta) {
        for (Entity entity : entities) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            if (animationComponent.getCurrentAnimation().getPlayMode() == Animation.PlayMode.NORMAL) {
                if (animationComponent.getCurrentAnimation().isAnimationFinished(animationComponent.getStateTime())) {
                    animationComponent.setAnimation(animationComponent.getPreviousAnimation());
                }
            }

        }
    }
}
