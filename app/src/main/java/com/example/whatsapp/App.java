package com.example.whatsapp;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("F4PklypWVhyPrk5Afbd2DxCjotjDcoidbX9sWKef")
                .clientKey("lMINuazkcaczOszb5nd6fJ4zCdOmL3UCeNbUS8GF")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
