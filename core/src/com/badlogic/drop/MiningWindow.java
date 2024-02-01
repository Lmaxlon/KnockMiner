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
    private long resourceCount = 0;
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


        resourceNameLabel = new Label("Copper", skin);
        resourceNameLabel.setStyle(new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        resourceCountLabel = new Label(String.valueOf(resourceCount), skin);
        resourceCountLabel.setFontScale(3f);

        closeButton = new TextButton("Close", skin);
        closeButton.getLabel().setFontScale(2f);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });

        upSpeedButton = new TextButton("Up Speed Mining", skin);
        upSpeedButton.getLabel().setFontScale(2f);
        upSpeedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });



        Table contentTable = new Table();
        contentTable.add(resourceImage).size(128, 128);
        contentTable.add(resourceNameLabel).padLeft(20);
        contentTable.add(resourceCountLabel).padLeft(20);
        contentTable.row();
        contentTable.add(closeButton).size(300, 60).padTop(20);
        contentTable.add(upSpeedButton).size(300, 60).padTop(20).padLeft(20);
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
            resourceDrawable.dispose();
        }
        resourceDrawable = new Texture(Gdx.files.internal(resourceImagePath));
        TextureRegionDrawable newDrawable = new TextureRegionDrawable(new TextureRegion(resourceDrawable));
        resourceImage.setDrawable(newDrawable);
        resourceNameLabel.setText(resourceName);
        resourceNameLabel.setFontScale(3f);

    }
}