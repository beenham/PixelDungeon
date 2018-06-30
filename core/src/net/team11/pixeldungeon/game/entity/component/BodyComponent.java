package net.team11.pixeldungeon.game.entity.component;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import net.team11.pixeldungeon.game.entitysystem.EntityComponent;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.CollisionUtil;

import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody;
import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.StaticBody;

public class BodyComponent implements EntityComponent {
    private enum ShapeType {
        POLYGON, RECTANGLE
    }

    private float width;
    private float height;
    private float x, y;
    private float density;
    private byte category, collision;
    private Body body;

    private ShapeType shapeType;
    private Polygon bodyShape;

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

    public BodyComponent(ChainShape bodyShape, float density, byte category, byte collision, BodyDef.BodyType bodyType) {
        this.category = category;
        this.collision = collision;
        this.density = density;
        createBody(bodyShape, bodyType);
        x = body.getPosition().x;
        y = body.getPosition().y;
    }

    private void createBody(BodyDef.BodyType type, float density) {
        shapeType = ShapeType.RECTANGLE;
        Array<Body> bodies = new Array<>();
        PlayScreen.world.getBodies(bodies);
        if (body != null) {
            for (Body body : bodies) {
                if (body.equals(this.body)) {
                    removeBody();
                }
            }
        }

        BodyDef def = new BodyDef();
        switch (type) {
            case DynamicBody:
                def.type = DynamicBody;
                break;
            case StaticBody:
                def.type = StaticBody;
                break;
            default:
                def.type = DynamicBody;

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

    private void createBody(ChainShape shape, BodyDef.BodyType type) {
        shapeType = ShapeType.POLYGON;
        Array<Body> bodies = new Array<>();
        PlayScreen.world.getBodies(bodies);
        if (body != null) {
            for (Body body : bodies) {
                if (body.equals(this.body)) {
                    removeBody();
                }
            }
        }

        BodyDef def = new BodyDef();
        switch (type) {
            case DynamicBody:
                def.type = DynamicBody;
                break;
            case StaticBody:
                def.type = StaticBody;
                break;
            default:
                def.type = DynamicBody;

        }
        def.fixedRotation = true;
        def.allowSleep = false;
        body = PlayScreen.world.createBody(def);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.filter.categoryBits = category;
        fixtureDef.filter.maskBits = collision;
        body.createFixture(fixtureDef);
        body.setLinearDamping(2*density);
        body.setLinearVelocity(0,0);

        bodyShape = CollisionUtil.createPolygon(shape);
        shape.dispose();
    }

    public void removeBody() {
        PlayScreen.world.destroyBody(body);
        this.body = null;
    }

    public void recreateBody(BodyDef.BodyType bodyType) {
        createBody(bodyType,density);
    }

    public Polygon getPolygon() {
        if (body != null) {
            switch (shapeType) {
                case POLYGON:
                    return bodyShape;
                case RECTANGLE:
                    return CollisionUtil.createRectangle(body.getPosition().x, body.getPosition().y, width, height);
                default:
                    return new Polygon();
            }
        } else {
            return CollisionUtil.createRectangle(x,y,width,height);
        }
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

    public void setCoords(float x, float y) {
        body.setTransform(this.x = x,this.y = y,body.getAngle());
    }

    public float getX() {
        if (body == null) {
            return x;
        } else {
            return x = body.getPosition().x;
        }
    }

    public float getY() {
        if (body == null) {
            return y;
        } else {
            return y = body.getPosition().y;
        }
    }

    public boolean isPushing() {
        for (Contact contact : PlayScreen.world.getContactList()) {
            if (contact.getFixtureA().getBody().equals(body)
                    && contact.getFixtureB().getDensity() > 0
                || contact.getFixtureB().getBody().equals(body)
                    && contact.getFixtureA().getDensity() > 0) {
                return true;
            }
        }
        return false;
    }

    public Body getBody() {
        return body;
    }
}
