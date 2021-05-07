package com.app.guinote.TorneoApp;

import android.app.Application;

public class App1 extends Application {

    private int screenHeight;
    private static App1 applicationInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationInstance = this;

    }

    public static synchronized App1 getInstance() {
        return applicationInstance;
    }



    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }
}
