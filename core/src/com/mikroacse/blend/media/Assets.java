package com.mikroacse.blend.media;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.JsonValue;
import com.mikroacse.blend.config.Config;
import com.mikroacse.blend.screens.SceneManager;
import com.mikroacse.engine.media.ScenesAssets;
import com.mikroacse.engine.utils.JSONLoader;
import com.mikroacse.engine.utils.StringUtil;

/**
 * Created by MikroAcse on 08.07.2016.
 */
public class Assets extends ScenesAssets {
    private static final String ASSETS_DIRECTORY = "data/";
    private static final String LEVELS_DIRECTORY = ASSETS_DIRECTORY + "levels/";
    private static final String SCENE_ASSETS = ASSETS_DIRECTORY + "resources/%s/";

    public static void loadScene(String scene) {
        loadScene(scene, false);
    }

    public static void loadScene(String scene, boolean sync) {
        Gdx.app.log("LOADING", "loading scene: " + scene);

        initScene(scene);

        JsonValue config = JSONLoader.load(getSceneConfigPath(scene));
        JsonValue files = config.get("files");

        String[] textures = files.get("textures").asStringArray();
        for (String texture : textures) {
            Gdx.app.log("LOADING", "loading texture: " + texture);
            addTexture(scene, StringUtil.getFilename(texture), getSceneTexturePath(scene, texture));
        }

        String[] sounds = files.get("sounds").asStringArray();
        for (String sound : sounds) {
            Gdx.app.log("LOADING", "loading sound: " + sound);
            addSound(scene, StringUtil.getFilename(sound), getSceneSoundPath(scene, sound));
        }

        String[] musics = files.get("music").asStringArray();
        for (String music : musics) {
            Gdx.app.log("LOADING", "loading music: " + music);
            addMusic(scene, StringUtil.getFilename(music), getSceneMusicPath(scene, music));
        }

        String[] atlases = files.get("atlases").asStringArray();
        for (String atlas : atlases) {
            Gdx.app.log("LOADING", "loading atlas: " + atlas);
            addAtlas(scene, StringUtil.getFilename(atlas), getSceneAtlasPath(scene, atlas));
        }

        String[] fonts = files.get("fonts").asStringArray();
        for (String font : fonts) {
            Gdx.app.log("LOADING", "loading font: " + font);
            addFont(scene, StringUtil.getFilename(font), getSceneFontPath(scene, font));
        }

        String[] shaders = files.get("shaders").asStringArray();
        for (String shader : shaders) {
            Gdx.app.log("LOADING", "loading shader: " + shader);
            addFont(scene, StringUtil.getFilename(shader), getSceneShaderPath(scene, shader));
        }

        if (sync) {
            finishLoading(scene);
        }
    }

    public static BitmapFont getGlobalFont(String scene, String key) {
        return getFont(SceneManager.GLOBAL, Config.getFont(scene, key));
    }

    public static int getCellWidth() {
        return getTexture(SceneManager.LEVEL, "cell").getWidth();
    }

    public static int getCellHeight() {
        return getTexture(SceneManager.LEVEL, "cell").getHeight();
    }

    private static String getSceneAssetsPath(String scene) {
        return String.format(SCENE_ASSETS, scene);
    }

    private static String getSceneTexturePath(String scene, String asset, int scale) {
        String path = getSceneAssetsPath(scene) + "textures/x%d/%s";
        return String.format(path, scale, asset);
    }

    private static String getSceneTexturePath(String scene, String asset) {
        return getSceneTexturePath(scene, asset, 1);
    }

    private static String getSceneSoundPath(String scene, String asset) {
        String path = getSceneAssetsPath(scene) + "sounds/%s";
        return String.format(path, asset);
    }

    private static String getSceneMusicPath(String scene, String asset) {
        String path = getSceneAssetsPath(scene) + "music/%s";
        return String.format(path, asset);
    }

    private static String getSceneFontPath(String scene, String asset) {
        String path = getSceneAssetsPath(scene) + "fonts/%s";
        return String.format(path, asset);
    }

    private static String getSceneAtlasPath(String scene, String asset) {
        String path = getSceneAssetsPath(scene) + "atlases/%s";
        return String.format(path, asset);
    }

    private static String getSceneShaderPath(String scene, String asset) {
        String path = getSceneAssetsPath(scene) + "shaders/%s";
        return String.format(path, asset);
    }

    private static String getSceneConfigPath(String scene) {
        return String.format(getSceneAssetsPath(scene) + "config.json", scene);
    }
}
