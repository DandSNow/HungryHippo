package com.mygdx.game;

import com.badlogic.gdx.utils.TimeUtils;

public class Hippopo {
    private ScreenGame game;
    public float x, y;
    private float width, height;
    private boolean isEat;
    private int phase, numPhases = 27;
    private long timeLastPhase, timeChangePhaseInterval = 50;

    public Hippopo(ScreenGame game, float x, float y, float width, float height) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    void move() {
        arbuzNearMouth();

        if(isEat){
            if(TimeUtils.millis() > timeLastPhase+timeChangePhaseInterval) {
                if (++phase < numPhases) {
                    timeLastPhase = TimeUtils.millis();
                } else {
                    isEat = false;
                    phase = 0;
                }
            }
        }
    }

    public boolean arbuzNearMouth() {
        if(phase == 0 &&
                Math.abs(game.arbuz.getBody().getPosition().x-x)<0.4 &&
                Math.abs(game.arbuz.getBody().getPosition().y-y)<0.4) {
            // арбуз поймали
            isEat = true;
            game.arbuz.getBody().setTransform(x, y, game.arbuz.getBody().getAngle());
            game.arbuz.getBody().setLinearVelocity(0, 0);
            return true;
        }
        return false;
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
}
