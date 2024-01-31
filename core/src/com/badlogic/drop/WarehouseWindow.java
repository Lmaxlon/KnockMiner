package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class WarehouseWindow extends Stage {
    private Skin skin;
    private Window window;
    private Label copperLabel;
    private Label ironLabel;
    private Label goldLabel;
    private Label textLabel;
    private long copper_bal;
    private long iron_bal;
    private long gold_bal;
    private TextButton closeButton;
    public boolean isWindowOpen;

    public WarehouseWindow(long copper_bal, long iron_bal, long gold_bal) {
        this.copper_bal = copper_bal;
        this.iron_bal = iron_bal;
        this.gold_bal = gold_bal;

        skin = new Skin(Gdx.files.internal("authPicture/uiskin.json")); // Убедитесь, что путь к skin корректен
        initialize();
    }

    private void initialize() {
        window = new Window("Your resources", skin);
        window.setSize(1500, 1000);
        window.setPosition(600, 125);

        copperLabel = new Label("Copper: " + copper_bal, skin);
        copperLabel.setStyle(new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        copperLabel.setFontScale(3f);
        ironLabel = new Label("Iron: " + iron_bal, skin);
        ironLabel.setStyle(new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        ironLabel.setFontScale(3f);
        goldLabel = new Label("Gold: " + gold_bal, skin);
        goldLabel.setStyle(new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        goldLabel.setFontScale(3f);
        textLabel = new Label("Your resources:", skin);
        textLabel.setStyle(new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        textLabel.setFontScale(3f);
        closeButton = new TextButton("Close", skin);
        closeButton.getLabel().setFontScale(2f);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide(); // Скрываем окно при нажатии
            }
        });

        Table contentTable = new Table();
        contentTable.add(textLabel).padBottom(20).row();
        contentTable.add(copperLabel).padTop(20).row();
        contentTable.add(ironLabel).padTop(20).row();
        contentTable.add(goldLabel).padTop(20).row();
        contentTable.row(); // Добавляем строку в таблицу для кнопки закрытия
        contentTable.add(closeButton).padTop(50).colspan(2).center(); // Размещаем кнопку закрытия под метками ресурсов

        window.add(contentTable).expand().fill();
        addActor(window);
    }

    public void updateResources(long copper_bal, long iron_bal, long gold_bal) {
        this.copper_bal = copper_bal;
        this.iron_bal = iron_bal;
        this.gold_bal = gold_bal;
        copperLabel.setText("Copper: " + copper_bal);
        ironLabel.setText("Iron: " + iron_bal);
        goldLabel.setText("Gold: " + gold_bal);
    }

    public void show() {
        window.setVisible(true);
        isWindowOpen = true;
    }

    public void hide() {
        isWindowOpen = false;
        window.setVisible(false);
    }
}
