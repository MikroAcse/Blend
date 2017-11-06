package com.mikroacse.blend.screens.objects;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Expo;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mikroacse.blend.Blend;
import com.mikroacse.blend.config.Lang;
import com.mikroacse.blend.media.Assets;
import com.mikroacse.blend.screens.SceneManager;
import com.mikroacse.blend.ui.TextActor;
import com.mikroacse.engine.actors.Group;
import com.mikroacse.engine.tween.ActorAccessor;
import com.mikroacse.engine.utils.Vector2D;

import static com.mikroacse.engine.media.ScenesAssets.scale;

/**
 * Created by MikroAcse on 30.07.2016.
 */
public class PausedOverlay extends Group {
    private Image background;
    private TextActor pausedText;

    private Vector2D bounds;
    private Vector2D offset;

    public PausedOverlay() {
        super();
        init();
    }

    private void init() {
        Texture backgroundTexture = Assets.getTexture(SceneManager.LEVEL, "paused-background");
        backgroundTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        background = new Image(backgroundTexture);
        this.addActor(background);

        pausedText = new TextActor(Assets.getGlobalFont(SceneManager.LEVEL, "paused"));
        pausedText.setText(Lang.get(SceneManager.LEVEL, "pause.paused"));
        this.addActor(pausedText);

        bounds = new Vector2D();
        offset = new Vector2D();
    }

    private void update() {
        pausedText.setScale(scale);

        background.setSize(bounds.x, bounds.y);

        pausedText.setX((int) (bounds.x - pausedText.getRealWidth() - offset.x));
        pausedText.setY(offset.y);
    }

    public void show() {
        update();
        Blend.tweenManager.killTarget(background);
        Blend.tweenManager.killTarget(pausedText);

        Tween.to(background, ActorAccessor.ALPHA, 0.3f)
                .target(1f)
                .start(Blend.tweenManager);
        background.getColor().a = 0f;

        Tween.to(pausedText, ActorAccessor.Y, 0.5f)
                .target(pausedText.getY())
                .ease(Expo.OUT)
                .start(Blend.tweenManager);
        pausedText.setY(-pausedText.getHeight() * 1.1f);
        pausedText.getColor().a = 1f;
    }

    public void hide() {
        Blend.tweenManager.killTarget(background);
        Blend.tweenManager.killTarget(pausedText);

        Tween.to(background, ActorAccessor.ALPHA, 0.2f)
                .target(0f)
                .start(Blend.tweenManager);

        Tween.to(pausedText, ActorAccessor.ALPHA, 0.2f)
                .target(0f)
                .start(Blend.tweenManager);
    }

    public Vector2D getBounds() {
        return bounds;
    }

    public void setBounds(int width, int height) {
        bounds.set(width, height);
        update();
    }

    public Vector2D getOffset() {
        return offset;
    }

    public void setOffset(int x, int y) {
        offset.set(x, y);
        update();
    }
}
