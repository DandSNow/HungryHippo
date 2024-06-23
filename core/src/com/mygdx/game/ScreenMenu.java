package com.mygdx.game;

import static com.mygdx.game.MyHippoGame.*;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;


public class ScreenMenu implements Screen {
    MyHippoGame myHippoGame;
    SpriteBatch batch;
    OrthographicCamera camera;
    Vector3 touch;
    BitmapFont font;

    Texture imgBackGround;


    HippoButton btnPlay;
    HippoButton btnAbout;
    HippoButton btnSettings;
    HippoButton btnExit;

    public ScreenMenu(MyHippoGame myHippoGame){
        this.myHippoGame = myHippoGame;
        batch = myHippoGame.batch;
        camera = myHippoGame.camera;
        touch = myHippoGame.touch;
        font = myHippoGame.fontMedium;
        imgBackGround = new Texture("menuImg.png");

        btnPlay = new HippoButton("Play", font, 450);
        btnAbout = new HippoButton("About", font, 350);
        btnSettings = new HippoButton("Settings", font, 250);
        btnExit = new HippoButton("Exit", font, 150);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);
            if(btnPlay.hit(touch.x, touch.y)){
                myHippoGame.setScreen(myHippoGame.screenGame);
            }
            if(btnSettings.hit(touch.x, touch.y)){
                myHippoGame.setScreen(myHippoGame.screenSettings);
            }
            if(btnAbout.hit(touch.x, touch.y)){
                myHippoGame.setScreen(myHippoGame.screenAbout);
            }
            if(btnExit.hit(touch.x, touch.y)){
                Gdx.app.exit();
            }

        }
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        font.draw(batch, btnPlay.text, btnPlay.x, btnPlay.y);
        font.draw(batch, btnSettings.text, btnSettings.x, btnSettings.y);
        font.draw(batch, btnAbout.text, btnAbout.x, btnAbout.y);
        font.draw(batch, btnExit.text, btnExit.x, btnExit.y);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
