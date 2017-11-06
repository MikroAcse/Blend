package com.mikroacse.engine.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.mikroacse.engine.media.ScenesAssets;

/**
 * Created by MikroAcse on 09.07.2016.
 */
public class Screen implements com.badlogic.gdx.Screen {
    protected Game game;
    protected Stage stage;
    protected Color backgroundColor;

    public Screen(Game game) {
        this.game = game;
        backgroundColor = new Color(0, 0, 0, 1);
        initStage();
    }

    protected void initStage() {
        // create stage
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        ScenesAssets.updateScale(width, height);
        stage.getViewport().update(width, height, true);
        stage.update();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public void showStage() {
        stage.show();
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
        stage.update();
    }

    @Override
    public void dispose() {
        game = null;
        stage.dispose();
    }

    public boolean isDisposable() {
        return false;
    }
}
