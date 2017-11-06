package com.mikroacse.engine.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by MikroAcse on 09.07.2016.
 */
public class Stage extends com.badlogic.gdx.scenes.scene2d.Stage {
    protected Game game;
    protected boolean animated;

    public Stage(Game game) {
        super(new ScreenViewport());
        this.game = game;
        init();
    }

    protected void init() {

    }

    public void show() {

    }

    public void hide(final String nextScene) {

    }

    public void update(boolean ignoreAnimated) {

    }

    public void update() {
        update(false);
    }
}
