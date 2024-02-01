package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class EmptyWindow extends Stage {
    private Skin skin;
    private Window window;
    public boolean isWindowOpen;
    public int great_push;
    public int cost;
    private Label costLabel;
    public boolean buildStation = false;
    private Runnable purchaseCallback;
    public long balance;

    public EmptyWindow() {
        skin = new Skin(Gdx.files.internal("authPicture/uiskin.json"));
        initialize();
    }
    public void setPurchaseCallback(Runnable callback) {
        this.purchaseCallback = callback;
    }

    private void initialize() {
        window = new Window("Buy Building", skin);
        window.setSize(1500, 1000);
        window.setPosition(600, 125);

        Label messageLabel = new Label("Are you really want to buy this building? This building has a cost", skin);
        messageLabel.setFontScale(3);

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.getLabel().setFontScale(3);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buildStation = false;
                hide();
            }
        });

        costLabel = new Label("", skin);
        costLabel.setFontScale(2);

        TextButton purchaseButton = new TextButton("Purchase", skin);
        purchaseButton.getLabel().setFontScale(3);
        purchaseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (balance >= cost){
                    buildStation = true;
                    balance = balance - cost;
                    hide();
                } else {
                    Label messageLabel = new Label("You don't have enough money to buy this!", skin);
                    messageLabel.setFontScale(3);
                }
                if (purchaseCallback != null) {
                    purchaseCallback.run();
                }
            }
        });

        Table contentTable = new Table();
        contentTable.add(messageLabel).pad(10).row();
        contentTable.add(costLabel).pad(10).row();
        contentTable.add(purchaseButton).pad(10).row();
        contentTable.add(closeButton).pad(10);

        window.add(contentTable).expand().fill();
        addActor(window);
    }

    private void updateCostLabel() {
        costLabel.setText("Cost: " + cost + "M$");
        costLabel.setFontScale(3);
    }


    public void set_cost(int num) {
        if (num == 1){
            cost = 10000000;
        }
        if (num == 2){
            cost = 20000000;
        }
        if (num == 3){
            cost = 30000000;
        }
        updateCostLabel();
    }

    public void show(){
        window.setVisible(true);
        isWindowOpen = true;
    }

    public void hide() {
        isWindowOpen = false;
        window.setVisible(false);
    }

    public void setBalance(long bal) {
        balance = bal;
    }
}
