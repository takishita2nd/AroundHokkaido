package com.takilab.aroundhokkaido;

import androidx.appcompat.app.AppCompatActivity;

public class SingletonActivity {
    private static AppCompatActivity _activity;

    public static void SetActivity(AppCompatActivity activity) {
        _activity = activity;
    }

    public static AppCompatActivity GetActivity(){
        return _activity;
    }
}
