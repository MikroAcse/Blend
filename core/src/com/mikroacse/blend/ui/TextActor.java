package com.mikroacse.blend.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by MikroAcse on 07.08.2016.
 */
public class TextActor extends com.mikroacse.engine.actors.TextActor {
    private ShaderProgram shader;
    private float smoothing;

    public TextActor(BitmapFont font, String text) {
        super(font, text);
    }

    public TextActor(BitmapFont font) {
        super(font);
    }

    @Override
    protected void init() {
        super.init();
        // TODO: shader
        //shader = Assets.getShader(SceneManager.GLOBAL, "distance-field-font");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        ShaderProgram originalShader = batch.getShader();
        //batch.setShader(shader);
        super.draw(batch, parentAlpha);
        //batch.setShader(originalShader);
    }
}
