package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.World;

public class River {
    int x, y;
    float width, height;
    float top;
    KinematicBody kinematicBody;
    public River(World world, float x, float y, float width, float height, float top) {
        kinematicBody = new KinematicBody(world, x, y, width, height);
        this.top = top;
        this.width = width;
        this.height = height;
        kinematicBody.getBody().setLinearVelocity(0, -0.5f);
    }

    void move(){
        if(kinematicBody.getBody().getPosition().y < -1.6f){
            kinematicBody.getBody().setTransform(kinematicBody.getBody().getPosition().x, top, 0);
        }
    }
}
