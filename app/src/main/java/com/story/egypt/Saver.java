package com.story.egypt;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Saver {

    SharedPreferences sharedPreferences;

    public Saver(Context context){
        sharedPreferences = context.getSharedPreferences("Conf", MODE_PRIVATE);
    }

    public String getUrlReference() {
        return sharedPreferences.getString("url", "");
    }

    public void setUrlReference(String url) {
        sharedPreferences.edit().putString("url", url).apply();
    }

    public boolean getFirst() {
        return sharedPreferences.getBoolean("first", true);
    }

    public void setFirst(boolean firstReference) {
        sharedPreferences.edit().putBoolean("first", firstReference).apply();
    }

    public boolean getFirstPlay() {
        return sharedPreferences.getBoolean("firstPlay", true);
    }

    public void setFirstPlay(boolean firstPlay) {
        sharedPreferences.edit().putBoolean("firstPlay", firstPlay).apply();
    }

    public boolean getFirstFlyerRecived() {
        return sharedPreferences.getBoolean("flyer", true);
    }

    public void setFirstFlyerRecived(boolean firstReference) {
        sharedPreferences.edit().putBoolean("flyer", firstReference).apply();
    }
}
