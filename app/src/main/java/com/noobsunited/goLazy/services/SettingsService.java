package com.noobsunited.goLazy.services;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mark on 1-8-16.
 */
public class SettingsService {

    private final SharedPreferences settings;
    Context context;

    public SettingsService(Context applicationContext) {
        this.context=applicationContext;
        settings = context.getSharedPreferences("pokemonGoPreferences", 0);

    }

    public String get(String key){
        return get(key,"");
    }

    public String get(String key,String defaultValue){
        return settings.getString(key,defaultValue);
    }

    public void set(String key, String val){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key,val);
        editor.apply();
    }
}
