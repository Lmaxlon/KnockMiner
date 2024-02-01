package com.badlogic.drop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Task implements Runnable, ApplicationListener {

    ErrorScreen error;
    String login;
    String password;
    Game game;
    AuthScreen authScreen;
    String res;
    String context;

    Task(String login, String password, String context, Game game,AuthScreen authScreen) {
        this.login = login;
        this.password = password;
        this.context=context;
        this.game=game;
        this.authScreen=authScreen;
    }


    @Override
    public void run() {
        //new HttpClient().connectToServer(login, password);
        String response = null;
        try {
            response = new HttpClient().connectToServer(login, password,context);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        handleResponse(response);

    }

    public void handleResponse(String response) {
        res = response;
        Gdx.app.debug("tag", "Task" + " " + response + " 111");

        if (response.trim().isEmpty()) {
            // Handle empty response
            Gdx.app.debug("tag", "Empty response received");
            return;
        }

        try {
            Object o = new JSONParser().parse(response);
            JSONObject j = (JSONObject) o;

            if (((Long) j.get("request")) == -1) {
                game.setScreen(new ErrorScreen(game, "", authScreen));
            } else {
                FileHandle file= Gdx.files.local("jwtDir/jwtToken.text");
                file.writeString((String)j.get("token"),false);

                game.setScreen(new Map(j));
            }
        } catch (ParseException e) {
            // Handle parse exception
            Gdx.app.error("tag", "Error parsing JSON response", e);
        }
    }


    @Override
    public void create() {
        error = new ErrorScreen(game,res,authScreen);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
