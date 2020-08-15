package com.takilab.aroundhokkaido;

import androidx.appcompat.app.AppCompatActivity;

public class SingletonActivity {
    private static MainActivity _activity;

    public static void SetActivity(MainActivity activity) {
        _activity = activity;
    }

    public static MainActivity GetActivity(){
        return _activity;
    }
}
