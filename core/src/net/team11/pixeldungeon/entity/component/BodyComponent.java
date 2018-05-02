package net.team11.pixeldungeon.entity.component;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import net.team11.pixeldungeon.entitysystem.EntityComponent;
import net.team11.pixeldungeon.screens.PlayScreen;
import net.team11.pixeldungeon.utils.CollisionCategory;

public class BodyComponent implements EntityComponent {
    private float width;
    private float height;
    private float x, y;
    private float density;
    private byte category, collision;
    private Body body;

    public BodyComponent(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public BodyComponent(float width, float height, float x, float y, float density, byte category, byte collision, BodyDef.BodyType bodyType) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.category = category;
        this.collision = collision;
        this.density = density;

        createBody(bodyType, density);
    }

    private void createBody(BodyDef.BodyType type, float density) {
        if (body != null) {
            removeBody();
        }
        BodyDef def = new BodyDef();
        switch (type) {
            case DynamicBody:
                def.type = BodyDef.BodyType.DynamicBody;
                break;
            case StaticBody:
                def.type = BodyDef.BodyType.StaticBody;
                break;
            default:
                def.type = BodyDef.BodyType.DynamicBody;

        }
        def.position.set(x, y);
        def.fixedRotation = true;
        def.allowSleep = false;
        body = PlayScreen.world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2, height/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.filter.categoryBits = category;
        fixtureDef.filter.maskBits = collision;

        body.createFixture(fixtureDef);
        body.setLinearDamping(2*density);
        body.setLinearVelocity(0,0);

        shape.dispose();
    }

    public void removeBody() {
        PlayScreen.world.destroyBody(body);
    }

    public void createBody(BodyDef.BodyType bodyType) {
        createBody(bodyType,density);
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void moveX(float x) {
        body.setLinearVelocity(x,body.getLinearVelocity().y);
    }

    public void moveY(float y) {
        body.setLinearVelocity(body.getLinearVelocity().x,y);
    }

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }

    public Body getBody() {
        return body;
    }
}
