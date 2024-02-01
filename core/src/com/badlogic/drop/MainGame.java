package com.badlogic.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGame extends Game {

    private SpriteBatch batch;
    private OrthographicCamera camera;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        setScreen(new WelcomeScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {

    }
}
