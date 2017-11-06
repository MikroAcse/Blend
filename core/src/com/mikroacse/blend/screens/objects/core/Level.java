package com.mikroacse.blend.screens.objects.core;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Expo;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mikroacse.blend.Blend;
import com.mikroacse.blend.media.Assets;
import com.mikroacse.blend.screens.SceneManager;
import com.mikroacse.blend.ui.TextActor;
import com.mikroacse.blend.utils.Colors;
import com.mikroacse.engine.actors.Group;
import com.mikroacse.engine.tween.ActorAccessor;

/**
 * Created by MikroAcse on 24.07.2016.
 */
public class Level extends Group {
    private Image circle;
    private Image background;
    private TextActor text;

    public Level() {
        super();
        init();
    }

    private void init() {
        circle = new Image(Assets.getTexture(SceneManager.CHOOSE_LEVEL, "outlined-circle"));
        background = new Image(Assets.getTexture(SceneManager.CHOOSE_LEVEL, "circle"));
        this.addActor(background);
        this.addActor(circle);

        text = new TextActor(Assets.getGlobalFont(SceneManager.CHOOSE_LEVEL, "level-circle"));
        text.setColor(Colors.DARK_GRAY);
        this.addActor(text);
        update();
    }

    private void update() {
        circle.setY(getCircleOffset());

        text.setY((int) (circle.getY() + circle.getHeight() / 2 - text.getHeight() / 2));
        text.setX((int) (circle.getWidth() - text.getWidth()) / 2);
    }

    public void show(float delay) {
        update();
        Blend.tweenManager.killTarget(circle);
        Blend.tweenManager.killTarget(text);

        Tween.to(circle, ActorAccessor.Y, 1.0f)
                .target(circle.getY())
                .ease(Expo.OUT)
                .delay(delay)
                .start(Blend.tweenManager);
        Tween.to(text, ActorAccessor.Y, 1.0f)
                .target(text.getY())
                .ease(Expo.OUT)
                .delay(delay)
                .start(Blend.tweenManager);
        Tween.to(text, ActorAccessor.ALPHA, 1.0f)
                .target(1f)
                .ease(Expo.OUT)
                .delay(delay)
                .start(Blend.tweenManager);

        circle.setY(0);
        text.moveBy(0f, -getCircleOffset());
        text.getColor().a = 0f;
    }

    public void setBackgroundColor(Color color) {
        background.setColor(color);
    }

    public String getText() {
        return text.getText().toString();
    }

    public void setText(String value) {
        text.setText(value);
        update();
    }

    public boolean hit(float x, float y) {
        float minX = this.getX();
        float minY = this.getY();
        float maxX = minX + getRealWidth();
        float maxY = minY + getRealHeight();

        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }

    @Override
    public float getRealWidth() {
        return circle.getWidth() * getScaleX();
    }

    @Override
    public float getRealHeight() {
        return (circle.getHeight() + getCircleOffset()) * getScaleY();
    }

    private float getCircleOffset() {
        return 5f;
    }
}
