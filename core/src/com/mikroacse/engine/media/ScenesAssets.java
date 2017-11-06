package com.mikroacse.engine.media;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.mikroacse.engine.utils.Vector2D;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MikroAcse on 12.07.2016.
 */
public class ScenesAssets {
    public static float scale;
    public static Vector2D originalSize;
    protected static Map<String, Assets> assets;

    public static void updateScale(int width, int height) {
        scale = Math.min((float) width / originalSize.x, (float) height / originalSize.y);
    }

    public static void init(int originalWidth, int originalHeight) {
        assets = new HashMap<>();
        originalSize = new Vector2D(originalWidth, originalHeight);
        scale = 1.0f;
    }

    public static void initScene(String scene) {
        if (!assets.containsKey(scene)) {
            assets.put(scene, new Assets());
        }
    }

    public static void disposeScene(String scene) {
        unloadMusic(scene);
        unloadSounds(scene);
        unloadTextures(scene);
        unloadAtlases(scene);
        unloadFonts(scene);
        unloadShaders(scene);
        assets.remove(scene);
    }

    public static boolean isLoaded() {
        for (Assets sceneAssets : assets.values()) {
            if (!sceneAssets.isLoaded()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isLoaded(String scene) {
        if (assets.containsKey(scene)) {
            return getAssets(scene).isLoaded();
        }
        return false;
    }

    public static void finishLoading(String scene) {
        if (assets.containsKey(scene)) {
            getAssets(scene).finishLoading();
        }
    }

    public static void finishLoading() {
        for (Assets sceneAssets : assets.values()) {
            sceneAssets.finishLoading();
        }
    }

    public static Assets getAssets(String scene) {
        return assets.get(scene);
    }

    public static Texture getTexture(String scene, String name) {
        return getAssets(scene).getTexture(name);
    }

    public static Sound getSound(String scene, String name) {
        return getAssets(scene).getSound(name);
    }

    public static Music getMusic(String scene, String name) {
        return getAssets(scene).getMusic(name);
    }

    public static TextureAtlas getAtlas(String scene, String name) {
        return getAssets(scene).getAtlas(name);
    }

    public static BitmapFont getFont(String scene, String name) {
        return getAssets(scene).getFont(name);
    }

    public static ShaderProgram getShader(String scene, String name) {
        return getAssets(scene).getShader(name);
    }

    public static void unloadSounds(String scene) {
        getAssets(scene).unloadSounds();
    }

    public static void unloadMusic(String scene) {
        getAssets(scene).unloadMusic();
    }

    public static void unloadTextures(String scene) {
        getAssets(scene).unloadTextures();
    }

    public static void unloadAtlases(String scene) {
        getAssets(scene).unloadAtlases();
    }

    public static void unloadFonts(String scene) {
        getAssets(scene).unloadFonts();
    }

    public static void unloadShaders(String scene) {
        getAssets(scene).unloadShaders();
    }

    protected static void addFont(String scene, String name, String path) {
        getAssets(scene).addFont(name, path);
    }

    protected static void addSound(String scene, String name, String path) {
        getAssets(scene).addSound(name, path);
    }

    protected static void addMusic(String scene, String name, String path) {
        getAssets(scene).addMusic(name, path);
    }

    protected static void addTexture(String scene, String name, String path) {
        getAssets(scene).addTexture(name, path);
    }

    protected static void addAtlas(String scene, String name, String path) {
        getAssets(scene).addAtlas(name, path);
    }

    protected static void addShader(String scene, String name, String path) {
        getAssets(scene).addShader(name, path);
    }
}
