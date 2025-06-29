package com.example.prm392_v1;

import android.app.Application;

import com.example.prm392_v1.data.BrainBoxDatabase;

public class BrainBoxApp extends Application {
    public static BrainBoxDatabase db;

    @Override
    public void onCreate(){
        super.onCreate();
        db = BrainBoxDatabase.getInstance(this);
    }
    public static BrainBoxDatabase getDatabase(){
        return db;
    }
}
