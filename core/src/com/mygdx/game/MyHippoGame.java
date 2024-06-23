package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Game;


public class MyHippoGame extends Game {
	public static final float SCR_WIDTH = 1600, SCR_HEIGHT = 900;
	public static final int TYPE_CIRCLE = 0, TYPE_BOX = 1, TYPE_POLY = 2, TYPE_WATER = 3;
	public static final int MONKEY_WITH_ARBUZ = 0, MONKEY_THROW = 1, MONKEY_WO_ARBUZ = 2;

	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 touch;

	BitmapFont fontSmall;
	BitmapFont fontMedium;

	ScreenMenu screenMenu;
	ScreenSettings screenSettings;
	ScreenAbout screenAbout;
	ScreenGame screenGame;

	boolean isSoundOn = false;
	String playerName = "Noname";
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
		touch = new Vector3();
		fontMedium = new BitmapFont(Gdx.files.internal("kotleopold80.fnt"));
		fontSmall = new BitmapFont(Gdx.files.internal("kotleopold60.fnt"));

		screenMenu = new ScreenMenu(this);
		screenSettings = new ScreenSettings(this);
		screenAbout = new ScreenAbout(this);
		screenGame = new ScreenGame(this);
		setScreen(screenMenu);

	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
