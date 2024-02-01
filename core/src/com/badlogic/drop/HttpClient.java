package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Timer;

import org.json.simple.JSONObject;

import java.util.concurrent.CountDownLatch;

public class HttpClient {

    public String connectToServer(String login, String password, String context) throws InterruptedException {
        String[] response = {""};
        CountDownLatch latch = new CountDownLatch(1);
        int[] attemptCounter = {0};

        HttpRequestBuilder builder = new HttpRequestBuilder();
        HelperForJsonBody body = new HelperForJsonBody(new JSONObject());
        String jsonStr = body.FormAuth(login, password).toJSONString();
        Net.HttpRequest httpRequest = builder.newRequest().method(Net.HttpMethods.POST)
                .url("https://3646-213-87-132-14.ngrok-free.app/"+context)
                .content(jsonStr)
                .header("Content-Type", "application/json")
                .build();

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                if (status.getStatusCode() == HttpStatus.SC_OK) {
                    response[0] = httpResponse.getResultAsString();
                    Gdx.app.debug("tag", "response server123" + response[0] + "03");
                } else {
                    response[0] = httpResponse.getResultAsString();
                    Gdx.app.debug("tag", "response server1" + status.getStatusCode());
                    attemptCounter[0]++;
                    if (attemptCounter[0] >= 3) {
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                Gdx.app.debug("tag", "Too many attempts, exiting game");
                                Timer.schedule(new Timer.Task() {
                                    @Override
                                    public void run() {
                                        Gdx.app.exit();
                                    }
                                }, 5);
                            }
                        });
                    }
                }
                latch.countDown();
            }

            @Override
            public void failed(Throwable t) {
                latch.countDown();
            }

            @Override
            public void cancelled() {
                latch.countDown();
            }
        });

        latch.await();
        httpRequest.reset();

        return response[0];
    }
    public String connectToServer(String token, String context) throws InterruptedException {
        String[] response = {""};
        CountDownLatch latch = new CountDownLatch(1);
        int[] attemptCounter = {0};

        HttpRequestBuilder builder = new HttpRequestBuilder();
        HelperForJsonBody body = new HelperForJsonBody(new JSONObject());
        String jsonStr = body.FormAuthToken(token).toJSONString();
        Net.HttpRequest httpRequest = builder.newRequest().method(Net.HttpMethods.POST)
                .url("https://3646-213-87-132-14.ngrok-free.app/"+context)
                .content(jsonStr)
                .header("Content-Type", "application/json")
                .build();

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                if (status.getStatusCode() == HttpStatus.SC_OK) {
                    response[0] = httpResponse.getResultAsString();
                    Gdx.app.debug("tag", "response server123" + response[0] + "03");
                } else {
                    response[0] = httpResponse.getResultAsString();
                    Gdx.app.debug("tag", "response server1" + status.getStatusCode());
                    attemptCounter[0]++;
                    if (attemptCounter[0] >= 3) {
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                Gdx.app.debug("tag", "Too many attempts, exiting game");
                                Timer.schedule(new Timer.Task() {
                                    @Override
                                    public void run() {
                                        Gdx.app.exit();
                                    }
                                }, 5); // Exit the game after 5 seconds
                            }
                        });
                    }
                }
                latch.countDown();
            }

            @Override
            public void failed(Throwable t) {
                latch.countDown();
            }

            @Override
            public void cancelled() {
                latch.countDown();
            }
        });

        latch.await();
        httpRequest.reset();

        return response[0];
    }
}
