package com.mikroacse.blend.config;

import com.badlogic.gdx.Gdx;
import com.mikroacse.engine.config.Language;

/**
 * Created by MikroAcse on 08.07.2016.
 */
public class Lang extends Language {
    public static void load() {
        Gdx.app.log("LOADING", "loading language");
        load("data/languages/lang");
    }
}
