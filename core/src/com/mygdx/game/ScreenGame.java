package com.mygdx.game;

import static com.mygdx.game.MyHippoGame.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScreenGame implements Screen{
    public static final float WORLD_WIDTH = 16, WORLD_HEIGHT = 9;

    MyHippoGame myHippoGame;
    SpriteBatch batchGame, batchFont;
    OrthographicCamera cameraGame, cameraFont;
    Vector3 touch;
    World world;
    Box2DDebugRenderer debugRenderer;

    // ресурсы
    BitmapFont fontMedium;
    BitmapFont fontSmall;

    StaticBody coast0, coast1, bottom;
    StaticBody wall;
    Array<River> river = new Array<>();
    DynamicBody water;

    Texture imgBackGame, imgCoast, imgWalls, imgWm, imgWave, imgCrag;
    Texture imgBackGround;
    TextureRegion imgArbuz, imgCragRegion;
    Texture imgHippoAtlas;
    TextureRegion[] imgHippo = new TextureRegion[27];
    TextureRegion imgHippoEye;
    TextureRegion imgArrow;
    Texture imgMonkeyAtlas;
    TextureRegion imgMonkeyLegs;
    TextureRegion[] imgMonkey = new TextureRegion[7];

    Sound waterSnd;

    float [] vertices = new float[]{12,0,12,1,13,1,13,3,15,4,15,6,16,6,16,0};

    DynamicBody arbuz;
    Hippopo hippopo;
    HippoEye[] hippoEyes = new HippoEye[2];
    Monkey monkey;
    Arrow arrow;

    long timeArbuzOut, timeArbuzBeBack = 2000;
    long timeGameOverStart, timeGameOverEnd = 30000;
    long timeNextLevelStart, timeNextLevelEnd = 30000;

    int nArbuzes;
    int score;
    boolean isGameOver;
    boolean isNextLevel;
    boolean isGlobalRecords;

    HippoButton btnSwitchRecords;
    HippoButton btnBack;

    List<RecordFromDB> recordsFromDB = new ArrayList<>();
    Player[] players = new Player[7];

    public ScreenGame(MyHippoGame myHippoGame){
        this.myHippoGame = myHippoGame;
        batchFont = myHippoGame.batch;
        cameraFont = myHippoGame.camera;
        touch = myHippoGame.touch;
        fontMedium = myHippoGame.fontMedium;
        fontSmall = myHippoGame.fontSmall;
        batchGame = new SpriteBatch();
        cameraGame = new OrthographicCamera();
        cameraGame.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        world = new World(new Vector2(0f, -9.80665f), true);
        debugRenderer = new Box2DDebugRenderer();

        imgHippoAtlas = new Texture("hippoatlas.png");
        for (int i = 0; i < 9; i++) {
            imgHippo[i] = new TextureRegion(imgHippoAtlas, i*400, 0, 400, 400);
        }
        for (int i = 0; i < 18; i+=6) {
            imgHippo[i+9] = imgHippo[6];
            imgHippo[i+10] = imgHippo[6];
            imgHippo[i+11] = imgHippo[7];
            imgHippo[i+12] = imgHippo[7];
            imgHippo[i+13] = imgHippo[8];
            imgHippo[i+14] = imgHippo[8];
        }
        imgHippoEye = new TextureRegion(imgHippoAtlas, 3600, 0, 50, 50);
        imgArrow = new TextureRegion(imgHippoAtlas, 3600, 50, 50, 350);

        imgMonkeyAtlas = new Texture("monkeyatlas.png");
        for (int i = 0; i < imgMonkey.length; i++) {
            imgMonkey[i] = new TextureRegion(imgMonkeyAtlas, i*400, 0, 400, 400);
        }
        imgMonkeyLegs = new TextureRegion(imgMonkeyAtlas, imgMonkey.length*400, 0, 400, 400);

        imgBackGame = new Texture("gameImg.png");
        imgWm = new Texture("arbuz.png");
        imgBackGround = new Texture("background1.png");
        imgWave = new Texture("wave.png");
        imgArbuz = new TextureRegion(imgWm);

        waterSnd = Gdx.audio.newSound(Gdx.files.internal("waterSnd.wav"));
        boolean isPlaying = false;

        btnSwitchRecords = new HippoButton("Global/Local Records", fontSmall, 180);
        btnBack = new HippoButton("back to menu", fontSmall, 100);

        //wallLeft = new StaticBody(world, 0.5f, 5, 1.5f, 10);
        //wallRight = new StaticBody(world, 15.5f, 5, 1.5f, 10);
        //bottom = new StaticBody(world, 5f, 0.97f, 4f, 3);
        wall = new StaticBody(world, 16f, 5f, 1, 9);
        coast0 = new StaticBody(world, 1.5f, 0f, 3, 3);
        coast1 = new StaticBody(world, 12f, 0f, 10, 3);

        //water = new DynamicBody(world, 8f, 0.97f, 4.98f, 2f, 1f, 0.01f, 0.1f, 1f, "water");
        river.add(new River(world, 5f, 1.4f, 4f, 0.05f, 1.4f));
        river.add(new River(world, 5f, 0.4f, 4f, 0.05f, 1.4f));
        river.add(new River(world, 5f, -0.6f, 4f, 0.05f, 1.4f));
        arbuz = new DynamicBody(world, 0, 0, 0.3f, "ball0");
        monkey = new Monkey(this, 1.8f, 2.4f, 2, 2);

        hippopo = new Hippopo(this, 13, 2.3f, 2, 2);
        hippoEyes[0] = new HippoEye(this, 12.9f, 2.72f, 0.25f, 0.25f);
        hippoEyes[1] = new HippoEye(this, 13.18f, 2.72f, 0.25f, 0.25f);
        arrow = new Arrow();

        for (int i = 0; i < players.length; i++) {
            players[i] = new Player("Noname", 0);
        }

        arbuzToStartPosition();
        loadRecords();

        Gdx.input.setInputProcessor(new MyInputProcessor(this));
    }

    @Override
    public void show() {
        nArbuzes = 5; // количество арбузов
        if(isGameOver) {
            score = 0;
        }
        isGameOver = false;
        isNextLevel = false;
        monkey.start();
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            cameraFont.unproject(touch);

            if((isGameOver | isNextLevel) & btnSwitchRecords.hit(touch.x, touch.y)){
                isGlobalRecords = !isGlobalRecords;
            }
            if((isGameOver | isNextLevel) & btnBack.hit(touch.x, touch.y)){
                myHippoGame.setScreen(myHippoGame.screenMenu);
            }
        }

        // события
        for (River r: river){
            r.move();
        }
        hippopo.move();
        hippoEyes[0].move();
        hippoEyes[1].move();

        monkey.move();

        // арбуз вышел за пределы мира
        if(!arbuz.isOut) {
            if (arbuz.getX() < -arbuz.getWidth()
                    || arbuz.getX() > WORLD_WIDTH + arbuz.getWidth()
                    || arbuz.getY() < -arbuz.getHeight()) {
                arbuzOut();
            }
        }

        // арбуз остановился
        if(!arbuz.isOut & monkey.state == MONKEY_WO_ARBUZ) {
            if (!arbuz.getBody().isAwake()) {
                arbuzOut();
            }
        }

        // арбуз съели
        if (hippopo.getPhase() == 5) {
            score++;
            arbuzOut();
        }

        if(arbuz.isOut){
            if(TimeUtils.millis() > timeArbuzOut+timeArbuzBeBack){
                arbuzToStartPosition();
            }
        }

        if (arbuz.getY() < 2.2f && arbuz.getY() > 2.1f){
            if(myHippoGame.isSoundOn) waterSnd.play(0.02f);
        }

        // если завершение игры
        if(isGameOver) {
            if(timeGameOverStart+timeGameOverEnd<TimeUtils.millis()){
                myHippoGame.setScreen(myHippoGame.screenMenu);
            }
        }

        // если следующий уровень
        if(isNextLevel) {
            if(timeNextLevelStart+timeNextLevelEnd<TimeUtils.millis()){
                myHippoGame.setScreen(myHippoGame.screenMenu);
            }
        }

        // отрисовка
        //ScreenUtils.clear(0.8f, 0.4f, 0.6f, 0f);
        world.step(1/60f, 6, 2);
        //debugRenderer.render(world, cameraGame.combined);
        batchGame.setProjectionMatrix(cameraGame.combined);
        batchGame.begin();
        batchGame.draw(imgBackGround, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        batchGame.draw(imgHippo[hippopo.getPhase()], hippopo.getX(), hippopo.getY(), hippopo.getWidth(), hippopo.getHeight());
        for(HippoEye e: hippoEyes) {
            if(hippopo.getPhase()<1 | hippopo.getPhase()>5) {
                batchGame.draw(imgHippoEye, e.getX(), e.getY(), e.getWidth() / 2, e.getHeight() / 2,
                        e.getWidth(), e.getHeight(), 1, 1, e.getRotation());
            }
        }
        batchGame.draw(imgMonkeyLegs, monkey.getX(), monkey.getY(), monkey.getWidth(), monkey.getHeight());
        batchGame.draw(imgMonkey[monkey.getPhase()], monkey.getX(), monkey.getY(), monkey.getWidth()/2, monkey.getHeight()/2, monkey.getWidth(), monkey.getHeight(), 1, 1, monkey.getRotation());

        if(monkey.state == MONKEY_WO_ARBUZ & nArbuzes>-1) {
            batchGame.draw(imgArbuz, arbuz.getImgX(), arbuz.getImgY(), arbuz.getWidth() / 2, arbuz.getHeight() / 2, arbuz.getWidth(), arbuz.getHeight(), 1, 1, arbuz.getAngle());
        }

        for (River r: river){
            batchGame.draw(imgWave, r.kinematicBody.getX(), r.kinematicBody.getY(), r.kinematicBody.getWidth(), r.kinematicBody.getHeight()*4);
        }
        // рисуем арбузики
        for (int i = 0; i < nArbuzes; i++) {
            batchGame.draw(imgArbuz, 0.3f+i*0.5f, 0.5f, 0.4f, 0.4f);
        }
        batchGame.draw(imgArrow, arrow.getX(), arrow.getY(), arrow.getWidth()/2, 0, arrow.getWidth(), arrow.getHeight(), 1, 1, arrow.getAngle());

        /*batchGame.setColor(1f, 1f, 1f, 0.7f);
        batchGame.draw(imgWater, 5f, 0.3f, 5.5f, 3f);
        batchGame.setColor(1f, 1f, 1f, 1f);
        //batchGame.draw(imgWalls, wallLeft.getX(), wallLeft.getY(), wallLeft.getWidth(), wallLeft.getHeight());
        //batchGame.draw(imgWalls, wallRight.getX(), wallRight.getY(), wallRight.getWidth(), wallRight.getHeight());
        batchGame.draw(imgWalls, bottom.getX(), bottom.getY(), bottom.getWidth(), bottom.getHeight());
        batchGame.draw(imgCoast, coast.getX(), coast.getY(), coast.getWidth(), coast.getHeight());
        batchGame.draw(imgCoast, coast2.getX(), coast2.getY(), coast2.getWidth(), coast2.getHeight());*/

        batchGame.end();

        batchFont.setProjectionMatrix(cameraFont.combined);
        batchFont.begin();
        // рисуем очки
        fontSmall.draw(batchFont, "Score: "+score, 10, SCR_HEIGHT-10);

        if(isNextLevel){
            fontMedium.draw(batchFont, "Next Level (новые уровни скоро будут)", 0, 800, SCR_WIDTH, Align.center, true);
        }

        if(isGameOver) {
            fontMedium.draw(batchFont, "GAME OVER", 0, 800, SCR_WIDTH, Align.center, true);
        }

        if(isGameOver | isNextLevel) {
            if(isGlobalRecords) {
                fontSmall.draw(batchFont, "Global Records", 0, 700, SCR_WIDTH, Align.center, true);
                for (int i = 0; i < players.length - 1; i++) {
                    fontSmall.draw(batchFont, i + 1 + " " + recordsFromDB.get(i).name, 500, 600 - i * 60);
                    String nPoints = amountPoints(fontSmall, i + 1 + " " + recordsFromDB.get(i).name, "" + recordsFromDB.get(i).score, SCR_WIDTH - 1000);
                    fontSmall.draw(batchFont, nPoints + recordsFromDB.get(i).score, 500, 600 - i * 60, SCR_WIDTH - 1000, Align.right, true);
                }
            } else {
                fontSmall.draw(batchFont, "Local Records", 0, 700, SCR_WIDTH, Align.center, true);
                for (int i = 0; i < players.length - 1; i++) {
                    fontSmall.draw(batchFont, i + 1 + " " + players[i].name, 500, 600 - i * 60);
                    String nPoints = amountPoints(fontSmall, i + 1 + " " + players[i].name, "" + players[i].score, SCR_WIDTH - 1000);
                    fontSmall.draw(batchFont, nPoints + players[i].score, 500, 600 - i * 60, SCR_WIDTH - 1000, Align.right, true);
                }
            }
            btnSwitchRecords.font.draw(batchFont, btnSwitchRecords.text, btnSwitchRecords.x, btnSwitchRecords.y);
            btnBack.font.draw(batchFont, btnBack.text, btnBack.x, btnBack.y);
        }
        batchFont.end();
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

    }

    void arbuzOut(){
        arbuz.isOut = true;
        arbuz.getBody().setTransform(-1000, 0, 0);
        arbuz.getBody().setLinearVelocity(0,0);
        arbuz.getBody().setAngularVelocity(0);
        timeArbuzOut = TimeUtils.millis();

    }

    void arbuzToStartPosition(){
        arbuz.getBody().setType(BodyDef.BodyType.StaticBody);
        arbuz.getBody().setTransform(1.87f, 2.35f, 0);
        arbuz.isOut = false;
        monkey.start();
        nArbuzes--;
        if(nArbuzes == -1) {
            monkey.end();
            if(score == 0) {
                gameOver();
            } else {
                nextLevel();
            }
        }
    }

    private void gameOver(){
        isGameOver = true;
        timeGameOverStart = TimeUtils.millis();
        players[players.length-1].name = myHippoGame.playerName;
        players[players.length-1].score = score;
        sortRecords();
        saveRecords();
        saveRecordsToDB();
    }

    private void nextLevel(){
        isNextLevel = true;
        timeNextLevelStart = TimeUtils.millis();
        players[players.length-1].name = myHippoGame.playerName;
        players[players.length-1].score = score;
        sortRecords();
        saveRecords();
        saveRecordsToDB();
    }

    private String amountPoints(BitmapFont font, String text1, String text2, float width) {
        GlyphLayout layout1 = new GlyphLayout(font, text1);
        GlyphLayout layout2 = new GlyphLayout(font, text2);
        float pointsWidth = width-layout1.width-layout2.width;
        GlyphLayout layoutPoint = new GlyphLayout(font, ".");
        int amountPoints = (int) ((pointsWidth/layoutPoint.width)/1.65);
        String s = "";
        for (int i = 0; i < amountPoints; i++) s +=".";
        return s;
    }

    private void sortRecords(){
        boolean flag = true;
        while (flag){
            flag = false;
            for (int i = 0; i < players.length-1; i++) {
                if(players[i].score<players[i+1].score){
                    Player c = players[i];
                    players[i] = players[i+1];
                    players[i+1] = c;
                    flag = true;
                }
            }
        }
    }

    private void sortRecords2(){
        boolean flag = true;
        while (flag){
            flag = false;
            for (int i = 0; i < recordsFromDB.size()-1; i++) {
                if(recordsFromDB.get(i).score<recordsFromDB.get(i+1).score){
                    RecordFromDB c = recordsFromDB.get(i);
                    recordsFromDB.set(i, recordsFromDB.get(i+1));
                    recordsFromDB.set(i+1, c);
                    flag = true;
                }
            }
        }
    }

    private void saveRecords(){
        Preferences prefs = Gdx.app.getPreferences("SunArcadeRecords");
        for (int i = 0; i < players.length; i++) {
            prefs.putString("name"+i, players[i].name);
            prefs.putInteger("score"+i, players[i].score);
        }
        prefs.flush();
    }

    private void loadRecords(){
        Preferences prefs = Gdx.app.getPreferences("SunArcadeRecords");
        for (int i = 0; i < players.length; i++) {
            if(prefs.contains("name"+i)) players[i].name = prefs.getString("name"+i);
            if(prefs.contains("score"+i)) players[i].score = prefs.getInteger("score"+i);
        }
    }

    public void clearRecords(){
        for (int i = 0; i < players.length; i++) {
            players[i].name = "Noname";
            players[i].score = 0;
        }
    }

    void saveRecordsToDB(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://sun.sch120.ru")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi myApi = retrofit.create(MyApi.class);
        /*
        // синхронный запрос
        try {
            Response<List<RecordFromDB>> response = myApi.sendData(sunSpaceArcade.playerName, kills).execute();
            recordsFromDB = response.body();
        } catch (IOException e) {// если не получилось
        }*/
        // асинхронный запрос
        myApi.sendData(myHippoGame.playerName, score).enqueue(new Callback<List<RecordFromDB>>() {
            @Override
            public void onResponse(Call<List<RecordFromDB>> call, Response<List<RecordFromDB>> response) {
                recordsFromDB = response.body();
                sortRecords2();
            }

            @Override
            public void onFailure(Call<List<RecordFromDB>> call, Throwable t) {

            }
        });
    }
}
