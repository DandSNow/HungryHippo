package com.mygdx.game;

import static com.mygdx.game.MyHippoGame.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.TimeUtils;

public class Monkey {
    private ScreenGame game;
    private float x, y;
    private float width, height;
    private float rotation;
    public int state;
    private int phase, numPhases = 6;
    private long timeLastPhase, timeChangePhaseInterval = 80;
    private Vector2 impulse;

    public Monkey(ScreenGame game, float x, float y, float width, float height) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        state = MONKEY_WITH_ARBUZ;
    }

    void move() {
        if(state == MONKEY_THROW){
            if(TimeUtils.millis() > timeLastPhase+timeChangePhaseInterval) {
                if (++phase < 5) {
                    timeLastPhase = TimeUtils.millis();
                } else if (phase == 5) {
                    game.arbuz.getBody().setType(BodyDef.BodyType.DynamicBody);
                    //game.arbuz.getBody().setTransform(game.arbuz.getX(), game.arbuz.getY(), rotation-90);
                    game.arbuz.getBody().setAngularVelocity(-3f);
                    game.arbuz.setImpulse(impulse);
                } else if (phase == 6) {
                    state = MONKEY_WO_ARBUZ;
                    rotation = 0;
                }
            }
        }
        //rotation = (float) Math.toDegrees(Math.atan2(x-game.arbuz.getBody().getPosition().x, game.arbuz.getBody().getPosition().y-y));
        //float a = MathUtils.atan((x - game.arbuz.getBody().getPosition().x)/(y - game.arbuz.getBody().getPosition().y));
        //rotation = a*MathUtils.radiansToDegrees;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getX() {
        return x-width/2;
    }

    public float getY() {
        return y-height/2;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public void startThrowArbuz(Vector2 impulse){
        state = MONKEY_THROW;
        phase = 0;
        this.impulse = impulse;
    }

    public void start() {
        phase = 0;
        state = MONKEY_WITH_ARBUZ;
    }

    public void end() {
        phase = 6;
        state = MONKEY_WO_ARBUZ;
    }
}
