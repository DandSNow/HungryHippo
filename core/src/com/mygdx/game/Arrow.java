package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Arrow {
    private float x, y;
    private float width, height;
    private float rotation;

    public Arrow() {
        width = 0.1f;
    }

    void set(Vector2 v) {
        rotation = (float) Math.toDegrees(Math.atan2(v.x, v.y));
        height = (float) Math.sqrt(v.x*v.x+v.y*v.y);
    }

    void setXY(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float getAngle() {
        return rotation;
    }

    public float getX() {
        return x-width/2;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
