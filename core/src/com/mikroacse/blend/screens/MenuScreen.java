package com.mikroacse.blend.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.mikroacse.blend.screens.stages.MenuStage;
import com.mikroacse.engine.screens.Screen;

/**
 * Created by MikroAcse on 09.07.2016.
 */
public class MenuScreen extends Screen implements com.badlogic.gdx.Screen {
    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    protected void initStage() {
        stage = new MenuStage(game);
        backgroundColor.set(Color.WHITE);
        super.initStage();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log("SCREEN", "Resize: " + stage + " -> " + (stage != null ? (stage.getViewport() != null) : false));
        super.resize(width, height);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        Gdx.app.log("SCREEN", "Screen " + this + " dispose");
        super.dispose();
    }
}
