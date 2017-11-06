package com.mikroacse.blend.screens.stages;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Expo;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.mikroacse.blend.Blend;
import com.mikroacse.blend.media.Assets;
import com.mikroacse.blend.screens.SceneManager;
import com.mikroacse.engine.actors.Group;
import com.mikroacse.engine.screens.Stage;
import com.mikroacse.engine.tween.ActorAccessor;

import static com.mikroacse.engine.media.ScenesAssets.scale;

/**
 * Created by MikroAcse on 09.07.2016.
 */
public class MenuStage extends Stage {
    private Group menu;
    private Image background;

    private Image logo;
    private Image playButton;
    private Image settingsButton;
    private Image infoButton;

    public MenuStage(Game game) {
        super(game);
    }

    @Override
    protected void init() {
        super.init();
        menu = new Group();

        background = new Image(Assets.getTexture(SceneManager.MENU, "background"));

        logo = new Image(Assets.getTexture(SceneManager.MENU, "blend-logo"));
        playButton = new Image(Assets.getTexture(SceneManager.MENU, "play-button"));
        settingsButton = new Image(Assets.getTexture(SceneManager.MENU, "settings-button"));
        infoButton = new Image(Assets.getTexture(SceneManager.MENU, "info-button"));

        menu.addActor(logo);
        menu.addActor(playButton);
        menu.addActor(settingsButton);
        menu.addActor(infoButton);

        this.addActor(background);
        this.addActor(menu);

        playButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (animated) return;
                Gdx.app.log("MENU", "Play button triggered");
                hide(SceneManager.CHOOSE_PACK);
            }
        });

        settingsButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (animated) return;
                Gdx.app.log("MENU", "Settings button triggered");
                hide(SceneManager.LEVEL);
            }
        });

        infoButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (animated) return;
                Gdx.app.log("MENU", "Info button triggered");
                hide(SceneManager.MENU);
            }
        });
    }

    @Override
    public void update() {
        if (animated) return;
        super.update();
        logo.setScale(scale);
        infoButton.setScale(scale);
        settingsButton.setScale(scale);
        playButton.setScale(scale);

        background.setPosition(0, 0);
        background.setSize(getWidth(), getHeight());

        infoButton.setX((int) -infoButton.getWidth() / 2 * scale);
        infoButton.setY(0);

        settingsButton.setX((int) -settingsButton.getWidth() / 2 * scale);
        settingsButton.setY(infoButton.getY() + (int) infoButton.getHeight() * scale + getButtonOffset());

        playButton.setX((int) -playButton.getWidth() / 2 * scale);
        playButton.setY(settingsButton.getY() + (int) settingsButton.getHeight() * scale + getButtonOffset());

        logo.setX((int) -logo.getWidth() / 2 * scale);
        logo.setY(playButton.getY() + (int) playButton.getHeight() * scale + getButtonOffset() * 2);

        menu.setX((int) getWidth() / 2);
        menu.setY((int) getHeight() / 2 - (int) menu.getRealHeight() / 2);
    }

    @Override
    public void show() {
        super.show();
        Blend.tweenManager.killTarget(logo);
        Blend.tweenManager.killTarget(playButton);
        Blend.tweenManager.killTarget(settingsButton);
        Blend.tweenManager.killTarget(infoButton);
        Blend.tweenManager.killTarget(background);

        Tween.to(background, ActorAccessor.Y, 0.7f)
                .ease(Expo.OUT)
                .target(background.getY())
                .start(Blend.tweenManager);
        Tween.to(infoButton, ActorAccessor.Y, 0.7f)
                .ease(Expo.OUT)
                .target(infoButton.getY())
                .delay(0.2f)
                .start(Blend.tweenManager);
        Tween.to(settingsButton, ActorAccessor.Y, 0.7f)
                .ease(Expo.OUT)
                .target(settingsButton.getY())
                .delay(0.3f)
                .start(Blend.tweenManager);
        Tween.to(playButton, ActorAccessor.Y, 0.7f)
                .ease(Expo.OUT)
                .target(playButton.getY())
                .delay(0.4f)
                .start(Blend.tweenManager);
        Tween.to(logo, ActorAccessor.Y, 0.7f)
                .ease(Expo.OUT)
                .target(logo.getY())
                .delay(0.5f)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        if (type == TweenCallback.COMPLETE) {
                            animated = false;
                            update();
                        }
                    }
                })
                .start(Blend.tweenManager);

        logo.setY(getHeight() * 1.1f);
        playButton.setY(getHeight() * 1.1f);
        settingsButton.setY(getHeight() * 1.1f);
        infoButton.setY(getHeight() * 1.1f);
        background.setY(getHeight() * 1.1f);
        animated = true;
    }

    @Override
    public void hide(final String nextScene) {
        super.hide(nextScene);
        Blend.tweenManager.killTarget(logo);
        Blend.tweenManager.killTarget(playButton);
        Blend.tweenManager.killTarget(settingsButton);
        Blend.tweenManager.killTarget(infoButton);
        Blend.tweenManager.killTarget(background);

        Tween.to(logo, ActorAccessor.Y, 0.4f)
                .ease(Expo.IN)
                .target(getHeight() * 1.1f)
                .start(Blend.tweenManager);
        Tween.to(playButton, ActorAccessor.Y, 0.4f)
                .ease(Expo.IN)
                .target(getHeight() * 1.1f - menu.getY())
                .delay(0.15f)
                .start(Blend.tweenManager);
        Tween.to(settingsButton, ActorAccessor.Y, 0.4f)
                .ease(Expo.IN)
                .target(getHeight() * 1.1f - menu.getY())
                .delay(0.3f)
                .start(Blend.tweenManager);
        Tween.to(infoButton, ActorAccessor.Y, 0.4f)
                .ease(Expo.IN)
                .target(getHeight() * 1.1f - menu.getY())
                .delay(0.45f)
                .start(Blend.tweenManager);
        Tween.to(background, ActorAccessor.Y, 0.5f)
                .ease(Expo.IN)
                .target(getHeight() * 1.1f)
                .delay(0.5f)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        if (type == TweenCallback.COMPLETE) {
                            animated = false;
                            SceneManager.setScene(nextScene);
                        }
                    }
                })
                .start(Blend.tweenManager);

        animated = true;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw() {
        super.draw();
    }

    private float getButtonOffset() {
        return 40 * scale;
    }
}