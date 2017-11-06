package com.mikroacse.blend.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mikroacse.blend.media.Assets;
import com.mikroacse.blend.screens.SceneManager;
import com.mikroacse.engine.actors.Group;
import com.mikroacse.engine.utils.TextureUtil;

/**
 * Created by MikroAcse on 29.07.2016.
 */
public class TextButton extends Group {
    private Image underline;
    private TextActor text;
    private Image hitbox;

    public TextButton() {
        super();
        init();
    }

    private void init() {
        underline = new Image(Assets.getTexture(SceneManager.GLOBAL, "button-underline"));
        this.addActor(underline);

        text = new TextActor(Assets.getGlobalFont(SceneManager.GLOBAL, "text-button"));
        this.addActor(text);

        hitbox = new Image(TextureUtil.create(10, 10)); // just empty resizable texture
        hitbox.getColor().a = 0f;
        this.addActor(hitbox);

        update();
    }

    private void update() {
        text.setY(underline.getHeight() + getUnderlineOffset());

        underline.setWidth(text.getWidth());

        // TODO: magic numbers
        hitbox.setWidth(text.getWidth() + 10);
        hitbox.setHeight(text.getY() + text.getHeight() + 10);
        hitbox.setPosition(-5, -5);
    }

    public String getText() {
        return text.getText().toString();
    }

    public void setText(String value) {
        text.setText(value);
        update();
    }

    public Color getColor() {
        return text.getColor();
    }

    public void setColor(Color color) {
        text.setColor(color);
        underline.setColor(color);
    }

    @Override
    public float getRealWidth() {
        return text.getWidth() * getScaleX();
    }

    @Override
    public float getRealHeight() {
        return (text.getY() + text.getHeight()) * getScaleY();
    }

    private float getUnderlineOffset() {
        return 5f;
    }
}