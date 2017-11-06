package com.mikroacse.blend.config;

import com.badlogic.gdx.Gdx;
import com.mikroacse.blend.screens.SceneManager;
import com.mikroacse.engine.config.Configuration;

/**
 * Created by MikroAcse on 08.07.2016.
 */
public class Config extends Configuration {
    public static void load() {
        Gdx.app.log("LOADING", "loading config");
        load("data/config.json");
    }

    public static int getBlockerColor(int defaultValue) {
        String key = String.format("scenes.%s.field.blocker.color", SceneManager.LEVEL);
        return getInt(key, defaultValue);
    }

    public static int getBlockerColor() {
        return getBlockerColor(0);
    }

    public static int getDefaultColor(String colorname, int defaultValue) {
        String key = String.format("scenes.%s.field.colors.%s.default", SceneManager.LEVEL, colorname);
        return getColor(key, defaultValue);
    }

    public static int getDefaultColor(String colorname) {
        return getDefaultColor(colorname, 0);
    }

    public static int getPaleColor(String colorname, int defaultValue) {
        String key = String.format("scenes.%s.field.colors.%s.pale", SceneManager.LEVEL, colorname);
        return getColor(key, defaultValue);
    }

    public static int getPaleColor(String colorname) {
        return getPaleColor(colorname, 0);
    }

    public static String getFont(String scene, String key) {
        key = String.format("scenes.%s.fonts.%s", scene, key);
        return getString(key);
    }

    public static int getAnimationFps() {
        return getInt("animationFps");
    }

    public static float getAnimationFrameDuration() {
        return 1f / getAnimationFps();
    }
}
