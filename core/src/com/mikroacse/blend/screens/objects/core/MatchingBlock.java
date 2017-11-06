package com.mikroacse.blend.screens.objects.core;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mikroacse.blend.Blend;
import com.mikroacse.blend.media.Assets;
import com.mikroacse.blend.screens.SceneManager;
import com.mikroacse.blend.ui.TextActor;
import com.mikroacse.blend.utils.Colors;
import com.mikroacse.engine.actors.Group;
import com.mikroacse.engine.tween.ActorAccessor;
import com.mikroacse.engine.utils.ColorUtil;

/**
 * Created by MikroAcse on 11.07.2016.
 */
public class MatchingBlock extends Group {
    private Image background;
    private TextActor text;
    private BitmapFont font;
    private Color color;

    public MatchingBlock() {
        super();
        init();
    }

    private void init() {
        background = new Image(Assets.getTexture(SceneManager.LEVEL, "matching-block"));
        this.addActor(background);

        font = Assets.getGlobalFont(SceneManager.LEVEL, "matching-block");
        text = new TextActor(font);
        this.addActor(text);

        color = new Color(1, 1, 1, 1f);
    }

    private void update() {
        text.setX((int) (background.getWidth() - text.getWidth()) / 2);
        text.setY((int) (background.getHeight() - text.getHeight()) / 2);
    }

    public void setColor(Color value, boolean animated) {
        color = value;
        Color textColor = ColorUtil.isContrast(value) ? Colors.DARK_GRAY : Colors.WHITE;

        if (animated) {
            Blend.tweenManager.killTarget(background);

            Tween.to(background, ActorAccessor.COLOR_RGB, 0.3f)
                    .target(value.r, value.g, value.b)
                    .start(Blend.tweenManager);

            if (!ColorUtil.equals(textColor, text.getColor())) {
                Blend.tweenManager.killTarget(text, true);

                Tween.to(text, ActorAccessor.COLOR_RGB, 0.3f)
                        .target(textColor.r, textColor.g, textColor.b)
                        .start(Blend.tweenManager);
            }
        } else {
            if (!ColorUtil.equals(textColor, text.getColor())) {
                text.setColor(textColor);
            }

            background.setColor(value);
        }
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color value) {
        color = value;
        setColor(value, false);
    }

    public String getText() {
        return text.getText().toString();
    }

    public void setText(String value) {
        text.setText(value);
        update();
    }

    @Override
    public float getWidth() {
        return background.getWidth();
    }

    @Override
    public float getHeight() {
        return background.getHeight();
    }
}
