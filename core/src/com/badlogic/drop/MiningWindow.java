package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MiningWindow extends Stage {
    private Skin skin;
    private Window window;
    private Image resourceImage;
    private Label resourceNameLabel;
    private Label resourceCountLabel;
    private long resourceCount = 0; // Initialize with the actual resource count
    private TextButton closeButton;
    private TextButton upSpeedButton;
    private Texture resourceDrawable;
    public boolean isWindowOpen;
    private int buildingType;

    public MiningWindow() {
        skin = new Skin(Gdx.files.internal("authPicture/uiskin.json"));
        initialize();
    }

    private void initialize() {
        window = new Window("Miner 1", skin);
        window.setSize(1500, 1000);
        window.setPosition(600, 125);

        resourceDrawable = new Texture(Gdx.files.internal("resources/copper.png"));
        resourceImage = new Image(resourceDrawable);

        // Увеличение шрифта надписей можно сделать через skin файл, здесь мы добавим больше отступа
        resourceNameLabel = new Label("Copper", skin);
        resourceNameLabel.setStyle(new Label.LabelStyle(new BitmapFont(), Color.WHITE)); // Пример изменения стиля надписи
        resourceCountLabel = new Label(String.valueOf(resourceCount), skin);
        resourceCountLabel.setFontScale(3f); // Увеличиваем масштаб шрифта в 1.5 раза

        closeButton = new TextButton("Close", skin);
        closeButton.getLabel().setFontScale(2f); // Увеличиваем шрифт текста на кнопке
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });

        upSpeedButton = new TextButton("Up Speed Mining", skin);
        upSpeedButton.getLabel().setFontScale(2f); // Аналогично, увеличиваем шрифт
        upSpeedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Implement the functionality to increase mining speed
            }
        });


        // Увеличиваем размер кнопок и добавляем отступы
        Table contentTable = new Table();
        contentTable.add(resourceImage).size(128, 128); // Увеличиваем размер изображения
        contentTable.add(resourceNameLabel).padLeft(20); // Увеличиваем отступ слева
        contentTable.add(resourceCountLabel).padLeft(20); // Увеличиваем отступ слева
        contentTable.row();
        contentTable.add(closeButton).size(300, 60).padTop(20); // Увеличиваем размер кнопки и добавляем отступ сверху
        contentTable.add(upSpeedButton).size(300, 60).padTop(20).padLeft(20); // Увеличиваем размер кнопки и добавляем отступы
        updateResourceImageAndLabel();
        window.add(contentTable).expand().fill();
        addActor(window);
    }

    public void show() {
        window.setVisible(true);
        isWindowOpen = true;
    }

    public void hide() {
        isWindowOpen = false;
        window.setVisible(false);
    }

    public void updateResourceCount(long newCount) {
        resourceCount = newCount;
        resourceCountLabel.setText(String.valueOf(resourceCount));
    }

    public void setBuildingType(int type) {
        buildingType = type;
        updateResourceImageAndLabel();
    }

    private void updateResourceImageAndLabel() {
        // Обновляем название и картинку ресурса в зависимости от типа постройки
        String resourceName = null;
        String resourceImagePath = null;
        switch (buildingType) {
            case 1:
                resourceName = "Copper";
                resourceImagePath = "resources/copper.png";
                break;
            case 2:
                resourceName = "Iron";
                resourceImagePath = "resources/iron.png";
                break;
            case 3:
                resourceName = "Gold";
                resourceImagePath = "resources/gold.png";
                break;
            default:
                resourceName = "Unknown";
                resourceImagePath = "resources/uknown.png";
                break;
        }

        if (resourceDrawable != null) {
            resourceDrawable.dispose(); // Освобождаем старый ресурс
        }
        resourceDrawable = new Texture(Gdx.files.internal(resourceImagePath));
        TextureRegionDrawable newDrawable = new TextureRegionDrawable(new TextureRegion(resourceDrawable));
        resourceImage.setDrawable(newDrawable);
        resourceNameLabel.setText(resourceName);
        resourceNameLabel.setFontScale(3f);

    }
}