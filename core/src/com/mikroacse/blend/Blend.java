package com.mikroacse.blend;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mikroacse.blend.config.Config;
import com.mikroacse.blend.config.Lang;
import com.mikroacse.blend.media.Assets;
import com.mikroacse.blend.screens.SceneManager;
import com.mikroacse.engine.tween.ActorAccessor;
import com.mikroacse.engine.tween.TweenManager;

public class Blend extends Game {
    public static TweenManager tweenManager;

    @Override
    public void create() {
        Tween.registerAccessor(Actor.class, new ActorAccessor());
        Tween.setCombinedAttributesLimit(4);
        tweenManager = new TweenManager();

        Config.load();
        Lang.load();
        Assets.init(560, 940); // TODO: magic numbers (original screensize)
        SceneManager.init(this);

        // TODO: faster loading
        Assets.loadScene(SceneManager.LOADING, true);
        Assets.loadScene(SceneManager.GLOBAL);
        Assets.loadScene(SceneManager.MENU);
        Assets.loadScene(SceneManager.CHOOSE_PACK);
        Assets.loadScene(SceneManager.CHOOSE_LEVEL);
        Assets.loadScene(SceneManager.LEVEL);
        Assets.loadScene(SceneManager.MENU);
        //Assets.finishLoading();

        SceneManager.setWaitScene(SceneManager.MENU);
        SceneManager.setScene(SceneManager.LOADING);
    }

    @Override
    public void dispose() {
        super.dispose();

        SceneManager.disposeAll();
        Assets.disposeScene(SceneManager.GLOBAL);
    }

    @Override
    public void render() {
        super.render();
        tweenManager.update(Gdx.graphics.getDeltaTime());
    }
}
