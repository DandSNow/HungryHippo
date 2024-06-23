package com.mygdx.game;

import static com.mygdx.game.MyHippoGame.*;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class DynamicBody {
    private float x, y;
    private float r;
    private float width, height;
    private Body body;
    public int type;
    boolean isOut = false;

    DynamicBody(World world, float x, float y, float r, String name){
        type = TYPE_CIRCLE;
        this.x = x;
        this.y = y;
        this.r = r;
        width = height = r*2;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 0.2f; // затухание скорости
        bodyDef.angularDamping = 0.2f;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);
        body.setUserData(name);

        CircleShape shape = new CircleShape();
        shape.setRadius(r);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.6f; // плотность
        fixtureDef.friction = 0.4f; // трение
        fixtureDef.restitution = 0.3f; // упругость

        body.createFixture(fixtureDef);

        shape.dispose();
    }

    DynamicBody(World world, float x, float y, float width, float height, String name){
        type = TYPE_BOX;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 0.1f; // затухание скорости
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);
        body.setUserData(name);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2, height/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.8f; // плотность
        fixtureDef.friction = 0.4f; // трение
        fixtureDef.restitution = 0.2f; // упругость

        body.createFixture(fixtureDef);

        shape.dispose();
    }

    DynamicBody(World world, float x, float y, Polygon p){
        type = TYPE_POLY;
        this.x = x;
        this.y = y;
        this.width = p.getBoundingRectangle().getWidth();
        this.height = p.getBoundingRectangle().getHeight();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 0.1f; // затухание скорости
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.set(p.getVertices());

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f; // плотность
        fixtureDef.friction = 0.4f; // трение
        fixtureDef.restitution = 0.5f; // упругость

        body.createFixture(fixtureDef);

        shape.dispose();
    }

    DynamicBody(World world, float x, float y, Polygon p0, Polygon p1){
        type = TYPE_POLY;
        this.x = x;
        this.y = y;
        this.width = p0.getBoundingRectangle().getWidth();
        this.height = p0.getBoundingRectangle().getHeight()*2;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 0.1f; // затухание скорости
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        PolygonShape shape0 = new PolygonShape();
        shape0.set(p0.getVertices());
        FixtureDef fixtureDef0 = new FixtureDef();
        fixtureDef0.shape = shape0;
        fixtureDef0.density = 0.5f; // плотность
        fixtureDef0.friction = 0.4f; // трение
        fixtureDef0.restitution = 0.5f; // упругость
        body.createFixture(fixtureDef0);
        shape0.dispose();

        PolygonShape shape1 = new PolygonShape();
        shape1.set(p1.getVertices());
        FixtureDef fixtureDef1 = new FixtureDef();
        fixtureDef1.shape = shape1;
        fixtureDef1.density = 0.5f; // плотность
        fixtureDef1.friction = 0.4f; // трение
        fixtureDef1.restitution = 0.5f; // упругость
        body.createFixture(fixtureDef1);
        shape1.dispose();
    }

    DynamicBody(World world, float x, float y, float width, float height, float density, float friction, float restitution, float damping, String name){
        type = TYPE_WATER;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        BodyDef waterBodyDef = new BodyDef();
        waterBodyDef.type = BodyDef.BodyType.DynamicBody;
        waterBodyDef.linearDamping = damping; // затухание скорости
        waterBodyDef.position.set(x, y);

        body = world.createBody(waterBodyDef);
        body.setUserData(name);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2, height/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density; // плотность
        fixtureDef.friction = friction; // трение
        fixtureDef.restitution = restitution; // упругость

        body.createFixture(fixtureDef);

        shape.dispose();
    }

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }

    public float getImgX() {
        return body.getPosition().x-width/2;
    }

    public float getImgY() {
        return body.getPosition().y-height/2;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getAngle() {
        return body.getAngle() * MathUtils.radiansToDegrees;
    }

    public Body getBody() {
        return body;
    }

    public boolean hit(float tx, float ty) {
        for(Fixture f: body.getFixtureList()) {
            if(f.testPoint(tx, ty)) return true;
        }
        return false;
    }

    public void setImpulse(Vector2 p){
        body.applyLinearImpulse(p, body.getWorldCenter(), true);
    }
    public void overlap(World world){
        world.destroyBody(body);
    }

    public boolean isNotMoving(){
        return body.getLinearVelocity().x == 0 && body.getLinearVelocity().y == 0;
    }
}

