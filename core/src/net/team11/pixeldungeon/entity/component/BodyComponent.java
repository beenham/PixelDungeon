package net.team11.pixeldungeon.entity.component;

import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import net.team11.pixeldungeon.entitysystem.EntityComponent;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.CollisionUtil;

import java.util.ArrayList;

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
        for (Body body : bodies) {
            if (body.equals(this.body)) {
                removeBody();
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
        for (Body body : bodies) {
            if (body.equals(this.body)) {
                removeBody();
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

        ArrayList<Float> vertices = new ArrayList<>();
        for (int i = 0 ; i < shape.getVertexCount() ; i++) {
            Vector2 vector2 = new Vector2();
            shape.getVertex(i, vector2);
            vertices.add(vector2.x);
            vertices.add(vector2.y);
        }
        float[] vertices2 = new float[vertices.size()];
        for (int i = 0 ; i < vertices.size() ; i++) {
            vertices2[i] = vertices.get(i);
        }
        bodyShape = new Polygon(vertices2);
        shape.dispose();
    }

    public void removeBody() {
        PlayScreen.world.destroyBody(body);
    }

    public void createBody(BodyDef.BodyType bodyType) {
        createBody(bodyType,density);
    }

    public Polygon getPolygon() {
        switch (shapeType) {
            case POLYGON:
                return bodyShape;
            case RECTANGLE:
                return CollisionUtil.createRectangle(body.getPosition().x,body.getPosition().y,width,height);
            default:
                return new Polygon();
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
        body.setTransform(x,y,body.getAngle());
    }

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
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
