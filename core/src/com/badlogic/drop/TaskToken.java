
package com.badlogic.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TaskToken implements Runnable{

    private String token;
    private String context;
    private String res;
    private Game game;

    TaskToken(String token, String context, Game game){
        this.token=token;
        this.context=context;
        this.game=game;
    }
    @Override
    public void run() {
        String response = null;
        try {
            response = new HttpClient().connectToServer(token,context);
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
                game.setScreen( new AuthScreen(game));
            } else {
                game.setScreen(new Map());
            }
        } catch (ParseException e) {
            // Handle parse exception
            Gdx.app.error("tag", "Error parsing JSON response", e);
        }
    }


}
