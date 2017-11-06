package com.mikroacse.blend.screens.objects.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mikroacse.blend.config.Config;
import com.mikroacse.blend.media.Assets;
import com.mikroacse.blend.screens.SceneManager;

/**
 * Created by MikroAcse on 10.07.2016.
 */
public class Cell extends Image {
    public static final String DEFAULT_COLOR = "default";
    public static final String PALE_COLOR = "pale";

    private String color;
    private String colorType;

    public Cell() {
        super(Assets.getTexture(SceneManager.LEVEL, "cell"));
        reset();
    }

    public void reset() {
        color = null;
        colorType = DEFAULT_COLOR;
        updateColor();
    }

    public void setColor(String colorName) {
        color = colorName;
        updateColor();
    }

    public String getColorName() {
        return color;
    }

    public void turnDefault() {
        colorType = DEFAULT_COLOR;
        updateColor();
    }

    public void turnPale() {
        colorType = PALE_COLOR;
        updateColor();
    }

    public boolean isPale() {
        return colorType.equals(PALE_COLOR);
    }

    public void updateColor() {
        if (color == null) {
            Color.rgb888ToColor(this.getColor(), 0);
            return;
        }

        int value;
        if (isPale()) {
            value = Config.getPaleColor(color, 0);
        } else {
            value = Config.getDefaultColor(color, 0);
        }
        Color.rgb888ToColor(this.getColor(), value);
    }

}
