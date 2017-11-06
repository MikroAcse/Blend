package com.mikroacse.blend.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Timer;
import com.mikroacse.blend.media.Assets;
import com.mikroacse.blend.screens.models.ChooseLevelModel;
import com.mikroacse.engine.screens.Model;
import com.mikroacse.engine.screens.Screen;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MikroAcse on 09.07.2016.
 */
// TODO: move it to the engine
public class SceneManager {
    public static final String MENU = "menu";
    public static final String LEVEL = "level";
    public static final String CHOOSE_PACK = "choosePack";
    public static final String CHOOSE_LEVEL = "chooseLevel";
    public static final String LOADING = "loading";
    public static final String GLOBAL = "global";

    private static Game game;
    private static Map<String, Screen> scenes;
    private static Map<String, Model> models;
    private static String currentScene;
    private static String waitScene;

    private static Timer.Task task;

    public static void init(Game game) {
        SceneManager.game = game;

        scenes = new HashMap<>();
        models = new HashMap<>();
    }

    public static void disposeAll() {
        for (String name : scenes.keySet()) {
            Screen scene = scenes.get(name);
            scene.dispose();
            Assets.disposeScene(name);
        }
        scenes.clear();
        for (Model model : models.values()) {
            model.dispose();
        }
        models.clear();
        scenes = null;
        game = null;
        currentScene = null;
    }

    public static void disposeScene(String name, boolean checkDisposable) {
        if (name != null) {
            Screen scene = scenes.get(name);
            if (scene.isDisposable() || !checkDisposable) {
                scene.dispose();
                Assets.disposeScene(name);
                scenes.remove(name);

                Model model = models.get(name);
                model.dispose();
                models.remove(name);
            }
        }
    }

    public static void setScene(final String name, boolean wait) {
        if (!Assets.isLoaded(name)) {
            setWaitScene(name);

            game.setScreen(getScreen(LOADING));
            getScreen(LOADING).showStage();

            Assets.loadScene(name);
            disposeScene(currentScene, true);
            return;
        }

        if (!models.containsKey(name)) {
            Model model = createModel(name);
            if (model != null) {
                models.put(name, model);
            }
        }

        if (!scenes.containsKey(name)) {
            scenes.put(name, createScene(name));
        }

        if (task != null) task.cancel();
        task = Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                setCurrentScene(name);
            }
        }, 0.3f);
    }

    private static void setCurrentScene(String name) {
        Screen scene = scenes.get(name);
        game.setScreen(scene);
        scene.showStage();

        disposeScene(currentScene, true);
        currentScene = name;
    }

    public static String getWaitScene() {
        return waitScene;
    }

    public static void setWaitScene(String value) {
        waitScene = value;
    }

    public static void setWaited() {
        if (waitScene == null) return;
        setScene(waitScene);
    }

    public static Model getModel(String name) {
        return models.get(name);
    }

    public static Screen getScreen() {
        return getScreen(currentScene);
    }

    public static Screen getScreen(String scene) {
        return scenes.get(scene);
    }

    public static String getScene() {
        return currentScene;
    }

    public static void setScene(String name) {
        setScene(name, true);
    }

    private static Screen createScene(String name) {
        switch (name) {
            case MENU:
                return new MenuScreen(game);
            case LEVEL:
                return new LevelScreen(game);
            case CHOOSE_PACK:
                return new ChoosePackScreen(game);
            case CHOOSE_LEVEL:
                return new ChooseLevelScreen(game);
            case LOADING:
                return new LoadingScreen(game);
        }
        return null;
    }

    private static Model createModel(String name) {
        switch (name) {
            case CHOOSE_LEVEL:
                return new ChooseLevelModel();
        }
        return null;
    }
}
