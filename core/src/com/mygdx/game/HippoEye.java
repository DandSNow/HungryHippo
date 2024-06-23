package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;

public class HippoEye {
    private ScreenGame game;
    private float x, y;
    private float width, height;
    private boolean isOut;
    private float rotation;

    public HippoEye(ScreenGame game, float x, float y, float width, float height) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    void move() {
        rotation = (float) Math.toDegrees(Math.atan2(x-game.arbuz.getBody().getPosition().x, game.arbuz.getBody().getPosition().y-y));

        //float a = MathUtils.atan((x - game.arbuz.getBody().getPosition().x)/(y - game.arbuz.getBody().getPosition().y));
        //rotation = a*MathUtils.radiansToDegrees;
    }

    public float getRotation() {
        return rotation;
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
}
