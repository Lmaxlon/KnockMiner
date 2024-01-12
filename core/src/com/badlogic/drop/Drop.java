package com.badlogic.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Drop extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture back;
	private Texture welcomeBack;
	private Texture citadel;
	private BitmapFont font;
	private boolean showWelcomeMessage;
	private long startTime;

	private float mapX = 0;
	private float mapY = 0;

	private Texture cellTexture;
	private Texture rocksTexture;
	private int mapWidth = 100; // Ширина острова в клетках
	private int mapHeight = 100; // Высота острова в клетках
	private int[][] islandMap; // Карта острова, где каждое значение представляет тип клетки (например, вода, земля и т. д.)
	private float cellSize = 200; // Размер клетки

	@Override
	public void create() {
		batch = new SpriteBatch();
	//	back = new Texture(Gdx.files.internal("back.png"));
		cellTexture = new Texture(Gdx.files.internal("cell.png")); // Загрузка текстуры клетки
		rocksTexture = new Texture(Gdx.files.internal("rocks.png"));
		welcomeBack = new Texture(Gdx.files.internal("welcome_back.png"));
		citadel = new Texture(Gdx.files.internal("citadel.png"));
		font = new BitmapFont();
		font.getData().setScale(3f);
		font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		showWelcomeMessage = true;
		startTime = System.currentTimeMillis();

		// Инициализация карты острова (здесь просто заполняем всю карту землей)
		islandMap = new int[mapWidth][mapHeight];
		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				islandMap[x][y] = (Math.random() < 0.175) ? 0 : 1; // Предполагаем, что 1 - это тип "rocks", 0 - это тип "grass"
			}
		}
		// Проход по карте для группировки клеток типа "rocks"
		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				if (islandMap[x][y] == 1) {
					// Проверка соседних клеток
					boolean hasNeighbourRock = false;
					for (int dx = -1; dx <= 1; dx++) {
						for (int dy = -1; dy <= 1; dy++) {
							if (dx != 0 || dy != 0) { // Пропускаем текущую клетку
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
					// Если у клетки типа "rocks" есть соседи типа "rocks", она остается типом "rocks", иначе становится типом "grass"
					if (!hasNeighbourRock) {
						islandMap[x][y] = 0;
					}
				}
			}
		}

		// Добавляем обработчик ввода для обработки свайпов
		Gdx.input.setInputProcessor(new InputAdapter() {
			private float lastX;
			private float lastY;

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				lastX = screenX;
				lastY = screenY;
				return true;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				float deltaX = screenX - lastX;
				float deltaY = screenY - lastY;
				mapX += deltaX;
				mapY -= deltaY; // Инвертируем направление движения по Y, так как Y растет вниз
				lastX = screenX;
				lastY = screenY;
				return true;
			}
		});
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (showWelcomeMessage && System.currentTimeMillis() - startTime < 5000) {
			batch.begin();
			batch.draw(welcomeBack, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			font.draw(batch, "Welcome to the Game!", Gdx.graphics.getWidth() / 2 - 200, Gdx.graphics.getHeight() / 2 - 200);
			batch.end();
		} else {
			showWelcomeMessage = false;
			batch.begin();
			// Отрисовка острова из клеток
			for (int x = 0; x < mapWidth; x++) {
				for (int y = 0; y < mapHeight; y++) {
					float offsetX = (y % 2 == 0) ? 0 : cellSize / 2; // Смещение для каждой второй строки
					if (islandMap[x][y] == 1) {
						batch.draw(cellTexture, x * cellSize * 0.75f + offsetX + mapX, y * cellSize * 0.5f + mapY, cellSize, cellSize);
					}
					if (islandMap[x][y] == 0) {
						batch.draw(rocksTexture, x * cellSize * 0.75f + offsetX + mapX, y * cellSize * 0.5f + mapY, cellSize, cellSize);
					}
				}
			}
			float centerX = 5 * cellSize * 0.75f + ((5 % 2 == 0) ? 0 : cellSize / 2) + mapX;
			float centerY = cellSize * 0.5f + mapY;;
			batch.draw(citadel, centerX, centerY, citadel.getWidth() / 3, citadel.getHeight() / 3);
			font.draw(batch, "Pre-alpha version", Gdx.graphics.getWidth() - 400, Gdx.graphics.getHeight() - 50);
			batch.end();
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		cellTexture.dispose();
		welcomeBack.dispose();
		citadel.dispose();
		font.dispose();
	}
}
