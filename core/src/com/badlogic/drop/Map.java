package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import org.json.simple.JSONObject;

public class Map implements Screen {
    Texture img;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture back;
    private Texture welcomeBack;
    private Texture citadel;
    private Texture not_opened_miner;
    private Texture not_opened_miner1;
    private Texture not_opened_miner2;
    private Texture opened_miner;
    private Texture opened_miner1;
    private Texture opened_miner2;
    private Texture stock;
    private Texture arrowDown;
    private Texture arrowDown1;
    private Texture arrowDown2;
    private BitmapFont font;
    private Texture copper;
    private Texture dollars;
    private Texture gold;
    private Texture iron;
    private boolean showWelcomeMessage;
    private long startTime;
    private float mapX = 0;
    private float mapY = 0;
    private Texture cellTexture;
    private Texture rocksTexture;
    private int mapWidth = 40; // Ширина острова в клетках
    private int mapHeight = 80; // Высота острова в клетках
    private int[][] islandMap; // Карта острова, где каждое значение представляет тип клетки (например, вода, земля и т. д.)
    private float cellSize = 150; // Размер клетки
    private Vector2 touch1 = new Vector2();
    private Vector2 touch2 = new Vector2();
    private float initialDistance;
    private EmptyWindow emptyWindow;
    private MiningWindow mining_window;
    private WarehouseWindow warehouse;
    private CitadelWindow citadel_window;
    private boolean flag;
    private boolean flag2;
    private boolean flag3;
    private boolean flag4;
    private MapInputProcessor inputProcessor;
    private int numberBuilding;
    private boolean build_flag1;
    private boolean build_flag2;
    private boolean build_flag3;
    private long balance ;
    private long copper_bal;
    private long gold_bal ;
    private long iron_bal ;
    private int rating ;




    private float timeSinceLastCopperUpdate = 0;
    private float timeSinceLastIronUpdate = 0;
    private float timeSinceLastGoldUpdate = 0;

    private final float copperUpdateInterval = 1f / (120f / 60f);
    private final float ironUpdateInterval = 1f / (60f / 60f);
    private final float goldUpdateInterval = 1f / (40f / 60f);
    JSONObject object;



    public Map(JSONObject object){


        this.object = object;
        rating = Integer.parseInt(object.get("rating").toString());
        build_flag1 = Integer.parseInt(object.get("building_flag1").toString())!=0;
        build_flag2 = Integer.parseInt(object.get("building_flag2").toString()) != 0;
        build_flag3 = Integer.parseInt(object.get("building_flag3").toString()) != 0;
        copper_bal = Long.parseLong(object.get("copper").toString());
        iron_bal = Long.parseLong(object.get("iron").toString());
        gold_bal = Long.parseLong(object.get("gold").toString());
        balance = Long.parseLong(object.get("money").toString());






    }

    private void init(){
        inputProcessor = new MapInputProcessor(mapX, mapY);
        Gdx.input.setInputProcessor(inputProcessor);

        batch = new SpriteBatch();
        // Создание камеры с параметрами экрана
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);
        camera.update(); // Обновление камеры
        cellTexture = new Texture(Gdx.files.internal("cell.png"));
        cellTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        rocksTexture = new Texture(Gdx.files.internal("rocks.png"));
        rocksTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        welcomeBack = new Texture(Gdx.files.internal("welcome_back.png"));
        citadel = new Texture(Gdx.files.internal("base.png"));
        not_opened_miner = new Texture(Gdx.files.internal("not_opened_miner.png"));
        not_opened_miner1 = new Texture(Gdx.files.internal("not_opened_miner.png"));
        not_opened_miner2 = new Texture(Gdx.files.internal("not_opened_miner.png"));
        opened_miner = new Texture(Gdx.files.internal("opened_miner.png"));
        opened_miner1 = new Texture(Gdx.files.internal("opened_miner.png"));
        opened_miner2 = new Texture(Gdx.files.internal("opened_miner.png"));
        stock = new Texture(Gdx.files.internal("stock.png"));
        arrowDown = new Texture(Gdx.files.internal("arrowDown.png"));
        arrowDown1 = new Texture(Gdx.files.internal("arrowDown.png"));
        arrowDown2 = new Texture(Gdx.files.internal("arrowDown.png"));
        copper = new Texture(Gdx.files.internal("resources/copper.png"));
        dollars = new Texture(Gdx.files.internal("resources/dollars.png"));
        gold = new Texture(Gdx.files.internal("resources/gold.png"));
        iron = new Texture(Gdx.files.internal("resources/iron.png"));
        font = new BitmapFont();
        font.getData().setScale(3f);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        showWelcomeMessage = true;
        startTime = System.currentTimeMillis();
        emptyWindow = new EmptyWindow();
        mining_window = new MiningWindow();
        warehouse = new WarehouseWindow(copper_bal, gold_bal, iron_bal);
        citadel_window = new CitadelWindow(copper_bal, gold_bal, iron_bal, balance, build_flag1, build_flag2, build_flag3);

        islandMap = new int[mapWidth][mapHeight];
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                islandMap[x][y] = (Math.random() < 0.030) ? 0 : 1;
            }
        }
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                if (islandMap[x][y] == 1) {
                    boolean hasNeighbourRock = false;
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            if (dx != 0 || dy != 0) {
                                int nx = x + dx;
                                int ny = y + dy;
                                if (nx >= 0 && nx < mapWidth && ny >= 0 && ny < mapHeight && islandMap[nx][ny] == 1) {
                                    hasNeighbourRock = true;
                                    break;
                                }
                            }
                        }
                        if (hasNeighbourRock) {
                            break;
                        }
                    }
                    if (!hasNeighbourRock) {
                        islandMap[x][y] = 0;
                    }
                }
            }
        }
        camera.position.set(mapWidth * cellSize / 2, mapHeight * cellSize / 2, 0);
        camera.update();
    }

    @Override
    public void show() {
    }

    private float arrowOffsetY = 0;
    private float arrowSpeed = 50;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (showWelcomeMessage && System.currentTimeMillis() - startTime < 5000) {
            batch.begin();
            batch.draw(welcomeBack, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            font.draw(batch, "Welcome to the Game!", Gdx.graphics.getWidth() / 2 - 200, Gdx.graphics.getHeight() / 2 - 200);
            batch.end();
        } else {
            showWelcomeMessage = false;
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            mapX = inputProcessor.getMapX();
            mapY = inputProcessor.getMapY();
            for (int x = 0; x < mapWidth; x++) {
                for (int y = 0; y < mapHeight; y++) {
                    float offsetX = (y % 2 == 0) ? 0 : cellSize / 2; // Смещение для каждой второй строки
                    float offsetY = ((y * cellSize * 0.5f) * 3) / 4;
                    if (islandMap[x][y] == 1) {
                        batch.draw(cellTexture, x * cellSize /*'* 0.75f*/ + offsetX + mapX, offsetY + mapY, cellSize, (cellSize * 3) / 4 );
                    }
                    if (islandMap[x][y] == 0) {
                        batch.draw(cellTexture, x * cellSize /*'* 0.75f*/ + offsetX + mapX, offsetY + mapY, cellSize, (cellSize * 3) / 4);
                        batch.draw(rocksTexture, x * cellSize /*Как'* 0.75f*/ + offsetX + mapX, offsetY + mapY, cellSize,(cellSize * 3 ) / 4);
                    }
                }
            }
            float centerX = 5 * cellSize * 0.75f + ((5 % 2 == 0) ? 0 : cellSize) + mapX;
            float centerY = cellSize * 0.5f + mapY;;
            batch.draw(citadel, centerX + cellSize * 8, centerY + cellSize * 7, citadel.getWidth(), citadel.getHeight());


            batch.draw(stock, centerX + cellSize * 15, centerY + cellSize * 9, citadel.getWidth() * 1.25f, citadel.getHeight());
            batch.end();
            batch.begin();
            arrowOffsetY += arrowSpeed * delta;
            if (arrowOffsetY > 10 || arrowOffsetY < -10) {
                arrowSpeed *= -1;
            }
            if (Gdx.input.justTouched() && !emptyWindow.isWindowOpen) {
                Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);
                float touchX = touch.x;
                float touchY = touch.y;

                float buildingX = centerX - cellSize * 3 ;
                float buildingY = centerY + cellSize * 1 ;
                float buildingWidth = citadel.getWidth();
                float buildingHeight = citadel.getHeight();
                if (touchX >= buildingX && touchX <= buildingX + buildingWidth &&
                        touchY >= buildingY && touchY <= buildingY + buildingHeight) {
                    numberBuilding = 1;
                    if (!build_flag1){
                        emptyWindow.set_cost(numberBuilding);
                        emptyWindow.setBalance(balance);
                        emptyWindow.show();
                        emptyWindow.setPurchaseCallback(() -> {
                            build_flag1 = emptyWindow.buildStation;
                            balance = emptyWindow.balance;
                        });
                    }
                    if (build_flag1){
                        mining_window.updateResourceCount(copper_bal);
                        mining_window.setBuildingType(numberBuilding);
                        mining_window.show();
                    }
                }
                buildingX = centerX + cellSize * 12 ;
                buildingY = centerY + cellSize * 15 ;
                if (touchX >= buildingX && touchX <= buildingX + buildingWidth &&
                        touchY >= buildingY && touchY <= buildingY + buildingHeight) {
                    numberBuilding = 2;
                    if (!build_flag2){
                        emptyWindow.set_cost(numberBuilding);
                        emptyWindow.setBalance(balance);
                        emptyWindow.show();
                        emptyWindow.setPurchaseCallback(() -> {
                            build_flag2 = emptyWindow.buildStation;
                            balance = emptyWindow.balance;
                        });
                    }
                    if (build_flag2){
                        mining_window.updateResourceCount(iron_bal);
                        mining_window.setBuildingType(numberBuilding);
                        mining_window.show();
                    }
                }
                buildingX = centerX + cellSize * 1 ;
                buildingY = centerY + cellSize * 12 ;
                if (touchX >= buildingX && touchX <= buildingX + buildingWidth &&
                        touchY >= buildingY && touchY <= buildingY + buildingHeight) {
                    numberBuilding = 3;
                    if(!build_flag3){
                        emptyWindow.set_cost(numberBuilding);
                        emptyWindow.setBalance(balance);
                        emptyWindow.show();
                        emptyWindow.setPurchaseCallback(() -> {
                            build_flag3 = emptyWindow.buildStation;
                            balance = emptyWindow.balance;
                        });
                    }
                    if (build_flag3){
                        mining_window.updateResourceCount(gold_bal);
                        mining_window.setBuildingType(numberBuilding);
                        mining_window.show();
                    }
                }
                buildingX = centerX + cellSize * 15 ;//
                buildingY = centerY + cellSize * 9 ;//
                if (touchX >= buildingX && touchX <= buildingX + buildingWidth &&
                        touchY >= buildingY && touchY <= buildingY + buildingHeight) {
                    numberBuilding = 4;
                    warehouse.updateResources(copper_bal, iron_bal, gold_bal);
                    warehouse.show();
                }
                buildingX = centerX + cellSize * 8 ;//
                buildingY = centerY + cellSize * 7 ;//
                if (touchX >= buildingX && touchX <= buildingX + buildingWidth &&
                        touchY >= buildingY && touchY <= buildingY + buildingHeight) {
                    numberBuilding = 5;
                    citadel_window.update(copper_bal, iron_bal, gold_bal, balance, build_flag1, build_flag2, build_flag3);
                    citadel_window.show();
                    rating = citadel_window.rating;
                }
            }







            if (emptyWindow.isWindowOpen) {
                Gdx.input.setInputProcessor(emptyWindow);
                emptyWindow.act(delta);
                emptyWindow.draw();
                flag = true;
                // System.out.println("1");
                // emptyWindow.isWindowOpen = false;
            }
            if (!emptyWindow.isWindowOpen && flag){
                Gdx.input.setInputProcessor(inputProcessor);
                flag = false;
            }


            if (mining_window.isWindowOpen) {
                Gdx.input.setInputProcessor(mining_window);
                mining_window.act(delta);
                mining_window.draw();
                flag2 = true;
                // System.out.println("1");
                // emptyWindow.isWindowOpen = false;
            }
            if (!mining_window.isWindowOpen && flag2){
                Gdx.input.setInputProcessor(inputProcessor);
                flag2 = false;
            }


            if (warehouse.isWindowOpen) {
                Gdx.input.setInputProcessor(warehouse);
                warehouse.act(delta);
                warehouse.draw();
                flag3 = true;
                // System.out.println("1");
                // emptyWindow.isWindowOpen = false;
            }
            if (!warehouse.isWindowOpen && flag3){
                Gdx.input.setInputProcessor(inputProcessor);
                flag3 = false;
            }

            if (citadel_window.isWindowOpen) {
                Gdx.input.setInputProcessor(citadel_window);
                citadel_window.act(delta);
                citadel_window.draw();
                flag4 = true;
                // System.out.println("1");
                // emptyWindow.isWindowOpen = false;
            }
            if (!citadel_window.isWindowOpen && flag4){
                Gdx.input.setInputProcessor(inputProcessor);
                flag4 = false;
            }



            if (build_flag1) {
                timeSinceLastCopperUpdate += delta;
                if (timeSinceLastCopperUpdate >= copperUpdateInterval) {
                    copper_bal += 1;
                    timeSinceLastCopperUpdate -= copperUpdateInterval;
                }
            }

            // Обновление ресурса железа (iron)
            if (build_flag2) {
                timeSinceLastIronUpdate += delta;
                if (timeSinceLastIronUpdate >= ironUpdateInterval) {
                    iron_bal += 1;
                    timeSinceLastIronUpdate -= ironUpdateInterval;
                }
            }

            // Обновление ресурса золота (gold)
            if (build_flag3) {
                timeSinceLastGoldUpdate += delta;
                if (timeSinceLastGoldUpdate >= goldUpdateInterval) {
                    gold_bal += 1;
                    timeSinceLastGoldUpdate -= goldUpdateInterval;
                }
            }





            if (!build_flag1){
                batch.draw(not_opened_miner, centerX - cellSize * 3, centerY + cellSize * 1, citadel.getWidth(), citadel.getHeight());
                batch.draw(arrowDown, centerX - cellSize * 0, centerY + cellSize * 7 + arrowOffsetY, citadel.getWidth() / 6, citadel.getHeight() / 6);
                // batch.draw(arrowDown, centerX - cellSize * 0, centerY + cellSize * 7, citadel.getWidth()/6, citadel.getHeight()/6);
            }
            if(!build_flag2){
                batch.draw(not_opened_miner1, centerX + cellSize * 12, centerY + cellSize * 15, citadel.getWidth(), citadel.getHeight());
                batch.draw(arrowDown1, centerX + cellSize * 15, centerY + cellSize * 21 + arrowOffsetY, citadel.getWidth() / 6, citadel.getHeight() / 6);
                // batch.draw(arrowDown1, centerX + cellSize * 15, centerY + cellSize * 21, citadel.getWidth()/6, citadel.getHeight()/6);
            }
            if(!build_flag3){
                batch.draw(not_opened_miner2, centerX + cellSize * 1, centerY + cellSize * 12, citadel.getWidth(), citadel.getHeight());
                batch.draw(arrowDown2, centerX + cellSize * 4, centerY + cellSize * 18 + arrowOffsetY, citadel.getWidth() / 6, citadel.getHeight() / 6);
                // batch.draw(arrowDown2, centerX + cellSize * 4, centerY + cellSize * 18, citadel.getWidth()/6, citadel.getHeight()/6);
            }

            //  if (emptyWindow.great_push == 1){
            if (build_flag1){
                batch.draw(opened_miner, centerX - cellSize * 3, centerY + cellSize * 1, citadel.getWidth(), (citadel.getWidth() * 5)/4);
            }
            if(build_flag2){
                batch.draw(opened_miner1, centerX + cellSize * 12, centerY + cellSize * 15, citadel.getWidth(), (citadel.getHeight() * 5)/4);
            }
            if(build_flag3){
                batch.draw(opened_miner2, centerX + cellSize * 1, centerY + cellSize * 12, citadel.getWidth(), (citadel.getHeight() * 5)/4);
            }
            //  }
            batch.end();
            batch.begin();

            batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())); // Сброс камеры
            font.draw(batch, "Pre-alpha version", Gdx.graphics.getWidth() - 400, Gdx.graphics.getHeight() - 50);
            batch.draw(dollars, 0, 1200, (Gdx.graphics.getWidth() - 2000)/5, (Gdx.graphics.getHeight() - 1700)/5);
            font.draw(batch, balance + "$", 150, Gdx.graphics.getHeight() - 50);

            if (Gdx.input.isTouched(0) && Gdx.input.isTouched(1)) {
                // Обработка мультитача для масштабирования
                touch1.set(Gdx.input.getX(0), Gdx.input.getY(0));
                touch2.set(Gdx.input.getX(1), Gdx.input.getY(1));

                float distance = touch1.dst(touch2);

                if (!Gdx.input.justTouched()) {
                    if (initialDistance != 0) {
                        float scale = initialDistance / distance;
                        camera.zoom *= scale;
                        camera.update();
                    }

                    initialDistance = distance;
                } else {
                    initialDistance = distance;
                }
            } else {
                initialDistance = 0;
            }
            batch.end();
        }
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
        cellTexture.dispose();
        welcomeBack.dispose();
        citadel.dispose();
        not_opened_miner.dispose();
        not_opened_miner1.dispose();
        not_opened_miner2.dispose();
        opened_miner.dispose();
        opened_miner1.dispose();
        opened_miner2.dispose();
        arrowDown.dispose();
        arrowDown1.dispose();
        arrowDown2.dispose();
        // copper.dispose();
        dollars.dispose();
        gold.dispose();
        iron.dispose();
        stock.dispose();
        font.dispose();
    }
}