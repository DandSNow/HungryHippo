package com.mygdx.game;

import static com.mygdx.game.MyHippoGame.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class ScreenAbout implements Screen {

    MyHippoGame myHippoGame;
    SpriteBatch batch;
    OrthographicCamera camera;
    Vector3 touch;
    BitmapFont font;

    Texture imgBackAbt;

    HippoButton btnBack;
    String textAbout = "Это игра.\n" +
            "В неё можно играть.\n"+
            "А можно не играть. \n"+
            "Тут ты должен\n " +
            "накормить бегемота.";

    public ScreenAbout(MyHippoGame myHippoGame){
        this.myHippoGame = myHippoGame;
        imgBackAbt = new Texture("aboutImg.png");
        batch = myHippoGame.batch;
        camera = myHippoGame.camera;
        touch = myHippoGame.touch;
        font = myHippoGame.fontMedium;

        btnBack = new HippoButton("Back", font, 150);
    }
    public void render(){

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnBack.hit(touch.x, touch.y)){
                myHippoGame.setScreen(myHippoGame.screenMenu);
            }

        }

        batch.begin();
        batch.draw(imgBackAbt,0 ,0, SCR_WIDTH, SCR_HEIGHT);
        font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        font.draw(batch, textAbout, 500, 750);
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
