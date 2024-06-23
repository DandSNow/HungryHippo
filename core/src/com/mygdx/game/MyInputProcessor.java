package com.mygdx.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MyInputProcessor implements InputProcessor {
    ScreenGame main;
    OrthographicCamera camera;
    Vector3 touch;

    final Vector2 firstTouch = new Vector2();
    DynamicBody bodyTouched;

    boolean arbuzTouched;

    public MyInputProcessor(ScreenGame main) {
        this.main = main;
        camera = main.cameraGame;
        touch = main.touch;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, screenY, 0);
        camera.unproject(touch);

        if(main.arbuz.hit(touch.x, touch.y)){
            bodyTouched = main.arbuz;
            firstTouch.set(touch.x, touch.y);
            if(main.arbuz.isNotMoving()) {
                main.arrow.setXY(main.arbuz.getX(), main.arbuz.getY());
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(bodyTouched != null) {
            touch.set(screenX, screenY, 0);
            camera.unproject(touch);
            //Vector2 impulse = new Vector2((-touch.x + firstTouch.x)/2, (-touch.y + firstTouch.y)/2);
            Vector2 impulse = new Vector2(touch.x - firstTouch.x, touch.y - firstTouch.y);
            //bodyTouched.setImpulse(impulse);
            main.monkey.startThrowArbuz(impulse);
            bodyTouched = null;
            main.arrow.set(new Vector2(0, 0));
        }
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(bodyTouched != null) {
            touch.set(screenX, screenY, 0);
            camera.unproject(touch);
            if (main.arbuz.isNotMoving()) {
                main.arrow.set(new Vector2(firstTouch.x - touch.x, touch.y - firstTouch.y));
                main.monkey.setRotation(main.arrow.getAngle() + 90);
            }
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
