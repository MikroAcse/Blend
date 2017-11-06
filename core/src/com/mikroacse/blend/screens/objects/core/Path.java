package com.mikroacse.blend.screens.objects.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.mikroacse.blend.media.Assets;
import com.mikroacse.blend.screens.SceneManager;
import com.mikroacse.engine.actors.Group;

/**
 * Created by MikroAcse on 11.07.2016.
 */
public class Path extends Group {
    public static final String START = "path-start";
    public static final String LINE = "path-line";
    public static final String CORNER = "path-corner";

    private String type;
    private Image image;

    public Path() {
        super();
        init();
    }

    private void init() {
        Texture texture = Assets.getTexture(SceneManager.LEVEL, START);
        image = new Image(texture);
        this.addActor(image);
    }

    public void setType(String type) {
        this.type = type;
        updateType();
    }

    public void setRotation(int rotation) {
        image.setRotation(rotation * 90f);

        switch (rotation) {
            case 0:
                image.setX(0);
                image.setY(0);
                break;
            case 1:
                image.setX(Assets.getCellWidth());
                image.setY(0);
                break;
            case 2:
                image.setX(Assets.getCellWidth());
                image.setY(Assets.getCellHeight());
                break;
            case 3:
                image.setX(0);
                image.setY(Assets.getCellHeight());
                break;
        }
    }

    public void dispose() {
        this.removeActor(image);
        image = null;
        type = null;
    }

    private void updateType() {
        Texture texture = Assets.getTexture(SceneManager.LEVEL, type);

        if (type == START) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        } else {
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }
        image.setDrawable(new SpriteDrawable(new Sprite(texture)));
    }
}
