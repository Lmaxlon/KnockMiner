package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class EmptyWindow extends Stage {
    private Skin skin;
    private Dialog buyDialog;
    boolean isWindowOpen;

    public EmptyWindow() {
        skin = new Skin(Gdx.files.internal("authPicture/uiskin.json"));
        initialize();
    }

    private void initialize() {
        buyDialog = new Dialog("", skin);
        buyDialog.setSize(1500, 1000);
        buyDialog.setPosition(600, 125);

        Label messageLabel = new Label("Are you really want to buy this building?", skin);
        messageLabel.setFontScale(2);

        buyDialog.text(messageLabel);

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.getLabel().setFontScale(3);
        closeButton.addListener(event -> {
            buyDialog.setVisible(false);
            isWindowOpen = false;
            return false;
        });

        Table buttonTable = new Table();
        buttonTable.add(closeButton).padTop(30).padRight(20);
        buyDialog.getButtonTable().add(buttonTable).expand().top().right();

        addActor(buyDialog);
    }

    public void show() {
        buyDialog.setVisible(true);
        isWindowOpen = true;
    }

    public void hide() {
        buyDialog.setVisible(false);
        isWindowOpen = false;
    }
}