package com.mygdx.game;

import static com.mygdx.game.MyHippoGame.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;


public class ScreenSettings  implements Screen {
    MyHippoGame myHippoGame;
    SpriteBatch batch;
    OrthographicCamera camera;
    Vector3 touch;
    BitmapFont fontSmall, fontMedium;

    Texture imgBackSet;

    HippoButton btnBack;
    HippoButton btnSound;
    HippoButton btnName;
    HippoButton btnClearRecords;

    InputKeyboard keyboard;
    boolean isKeyboardUse;

    public ScreenSettings(MyHippoGame myHippoGame){
        this.myHippoGame = myHippoGame;
        imgBackSet = new Texture("settingsImg.png");
        batch = myHippoGame.batch;
        camera = myHippoGame.camera;
        touch = myHippoGame.touch;
        fontSmall = myHippoGame.fontSmall;
        fontMedium = myHippoGame.fontMedium;
        loadSettings();

        btnName = new HippoButton("Name: "+ myHippoGame.playerName, fontMedium, SCR_WIDTH/3, 500);
        btnSound = new HippoButton("Sound OFF", fontMedium, SCR_WIDTH/3, 400);
        btnClearRecords = new HippoButton("Clear Records", fontMedium, SCR_WIDTH/3, 300);
        btnBack = new HippoButton("Back", fontMedium, SCR_WIDTH/3, 200);

        keyboard = new InputKeyboard(fontSmall, SCR_WIDTH, SCR_HEIGHT, 12);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(isKeyboardUse){
                if (keyboard.endOfEdit(touch.x, touch.y)) {
                    myHippoGame.playerName = keyboard.getText();
                    isKeyboardUse = false;
                    btnName.setText("Name: "+ myHippoGame.playerName);
                }
            } else {
                if (btnName.hit(touch.x, touch.y)) {
                    isKeyboardUse = true;
                }
                if (btnSound.hit(touch.x, touch.y)) {
                    myHippoGame.isSoundOn = !myHippoGame.isSoundOn;
                    btnSound.setText(myHippoGame.isSoundOn ? "Sound ON" : "Sound OFF");
                }
                if (btnClearRecords.hit(touch.x, touch.y)) {
                    myHippoGame.screenGame.clearRecords();
                    btnClearRecords.setText("Records Cleared");
                }
                if(btnBack.hit(touch.x, touch.y)){
                    myHippoGame.setScreen(myHippoGame.screenMenu);
                }
            }
        }

        batch.begin();
        batch.draw(imgBackSet, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnName.font.draw(batch, btnName.text, btnName.x, btnName.y);
        btnSound.font.draw(batch, btnSound.text, btnSound.x, btnSound.y);
        btnClearRecords.font.draw(batch, btnClearRecords.text, btnClearRecords.x, btnClearRecords.y);
        btnBack.font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        if(isKeyboardUse){
            keyboard.draw(batch);
        }
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
        btnClearRecords.setText("Clear Records");
        saveSettings();
    }

    @Override
    public void dispose() {
        batch.dispose();
        keyboard.dispose();
    }

    private void saveSettings(){
        Preferences prefs = Gdx.app.getPreferences("SunArcadeSettings");
        prefs.putBoolean("sound", myHippoGame.isSoundOn);
        prefs.putString("name", myHippoGame.playerName);
        prefs.flush();
    }

    private void loadSettings(){
        Preferences prefs = Gdx.app.getPreferences("SunArcadeSettings");
        if(prefs.contains("sound")) myHippoGame.isSoundOn = prefs.getBoolean("sound");
        if(prefs.contains("name")) myHippoGame.playerName = prefs.getString("name");
    }
}
