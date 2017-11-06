package com.mikroacse.blend.screens.objects;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Expo;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.mikroacse.blend.Blend;
import com.mikroacse.blend.config.Lang;
import com.mikroacse.blend.listeners.interfaces.PauseMenuListener;
import com.mikroacse.blend.media.Assets;
import com.mikroacse.blend.screens.SceneManager;
import com.mikroacse.blend.ui.TextButton;
import com.mikroacse.blend.utils.Colors;
import com.mikroacse.engine.actors.Group;
import com.mikroacse.engine.listeners.core.ListenerSupport;
import com.mikroacse.engine.listeners.core.ListenerSupportFactory;
import com.mikroacse.engine.tween.ActorAccessor;
import com.mikroacse.engine.utils.Vector2D;

import static com.mikroacse.blend.listeners.PauseMenuListener.*;
import static com.mikroacse.engine.media.ScenesAssets.scale;

/**
 * Created by MikroAcse on 29.07.2016.
 */
public class PauseMenu extends Group {
    private TextButton continueButton;
    private TextButton backToLevels;
    private TextButton resetLevel;
    private TextButton backToMenu;
    private Image circle;
    private Vector2D offset;
    private PausedOverlay overlay;

    private boolean isAnimated;
    private PauseMenuListener listeners;

    public PauseMenu() {
        super();
        listeners = ListenerSupportFactory.create(PauseMenuListener.class);

        init();
    }

    private void init() {
        circle = new Image(Assets.getTexture(SceneManager.LEVEL, "big-circle"));
        circle.setSize(getOriginalCircleSize(), getOriginalCircleSize());
        circle.setOrigin(Align.center);
        this.addActor(circle);

        continueButton = createButton(Lang.get(SceneManager.LEVEL, "pause.continueButton"));
        this.addActor(continueButton);

        backToLevels = createButton(Lang.get(SceneManager.LEVEL, "pause.backToLevels"));
        this.addActor(backToLevels);

        resetLevel = createButton(Lang.get(SceneManager.LEVEL, "pause.resetLevel"));
        this.addActor(resetLevel);

        backToMenu = createButton(Lang.get(SceneManager.LEVEL, "pause.backToMenu"));
        this.addActor(backToMenu);

        offset = new Vector2D(0, 0);
        update();

        continueButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isAnimated) return;
                hide(CONTINUE);
            }
        });
        backToLevels.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isAnimated) return;
                hide(BACK_TO_LEVELS);
            }
        });
        resetLevel.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isAnimated) return;
                hide(RESET_LEVEL);
            }
        });
        backToMenu.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isAnimated) return;
                hide(BACK_TO_MENU);
            }
        });
    }

    public void addListener(PauseMenuListener listener) {
        ((ListenerSupport<PauseMenuListener>) listeners).addListener(listener);
    }

    public void removeListener(PauseMenuListener listener) {
        ((ListenerSupport<PauseMenuListener>) listeners).removeListener(listener);
    }

    public void clearListeners() {
        ((ListenerSupport<PauseMenuListener>) listeners).clearListeners();
    }

    private TextButton createButton(String text) {
        TextButton button = new TextButton();
        button.setColor(Colors.DARK_GRAY);
        button.setText(text);
        return button;
    }

    private void update() {
        circle.setScale(scale);
        continueButton.setScale(scale);
        backToLevels.setScale(scale);
        resetLevel.setScale(scale);
        backToMenu.setScale(scale);

        continueButton.setX(0f);
        backToLevels.setX(0f);
        resetLevel.setX(0f);
        backToMenu.setX(0f);

        continueButton.setY((int) (-continueButton.getRealHeight()));
        resetLevel.setY((int) (continueButton.getY() - getButtonOffset() - resetLevel.getRealHeight()));
        backToLevels.setY((int) (resetLevel.getY() - getButtonOffset() - backToLevels.getRealHeight()));
        backToMenu.setY((int) (backToLevels.getY() - getButtonOffset() - backToMenu.getRealHeight()));

        circle.setX((int) (-circle.getWidth() / 2 + offset.x));
        circle.setY((int) (-circle.getHeight() / 2 - offset.y));
    }

    public void show() {
        update();
        if (overlay != null) {
            overlay.show();
            overlay.setVisible(true);
        }

        Blend.tweenManager.killTarget(circle);
        Blend.tweenManager.killTarget(continueButton);
        Blend.tweenManager.killTarget(backToLevels);
        Blend.tweenManager.killTarget(resetLevel);
        Blend.tweenManager.killTarget(backToMenu);

        Tween.to(circle, ActorAccessor.SCALE, 1.0f)
                .target(scale, scale)
                .ease(Expo.OUT)
                .start(Blend.tweenManager);
        circle.setScale(0f);

        Tween.to(continueButton, ActorAccessor.X, 0.49f)
                .target(continueButton.getX())
                .delay(0.15f)
                .ease(Expo.OUT)
                .start(Blend.tweenManager);
        Tween.to(resetLevel, ActorAccessor.X, 0.49f)
                .target(resetLevel.getX())
                .delay(0.18f)
                .ease(Expo.OUT)
                .start(Blend.tweenManager);
        Tween.to(backToLevels, ActorAccessor.X, 0.49f)
                .target(backToLevels.getX())
                .delay(0.21f)
                .ease(Expo.OUT)
                .start(Blend.tweenManager);
        Tween.to(backToMenu, ActorAccessor.X, 0.49f)
                .target(backToMenu.getX())
                .delay(0.24f)
                .ease(Expo.OUT)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        if (type == TweenCallback.COMPLETE) {
                            isAnimated = false;
                            listeners.onShown();
                        }
                    }
                })
                .start(Blend.tweenManager);

        continueButton.setX(-this.getX() - continueButton.getRealWidth());
        backToLevels.setX(-this.getX() - backToLevels.getRealWidth());
        resetLevel.setX(-this.getX() - resetLevel.getRealWidth());
        backToMenu.setX(-this.getX() - backToMenu.getRealWidth());

        isAnimated = true;
    }

    public void hide() {
        hide(NO_ACTION);
    }

    public void hide(final int action) {
        listeners.onHiding(action);
        if (overlay != null) {
            overlay.hide();
        }

        Blend.tweenManager.killTarget(circle);
        Blend.tweenManager.killTarget(continueButton);
        Blend.tweenManager.killTarget(backToLevels);
        Blend.tweenManager.killTarget(resetLevel);
        Blend.tweenManager.killTarget(backToMenu);

        Tween.to(backToMenu, ActorAccessor.X, 0.3f)
                .target(-this.getX() - backToMenu.getRealWidth())
                .ease(Expo.IN)
                .start(Blend.tweenManager);
        Tween.to(backToLevels, ActorAccessor.X, 0.3f)
                .target(-this.getX() - backToLevels.getRealWidth())
                .ease(Expo.IN)
                .delay(0.03f)
                .start(Blend.tweenManager);
        Tween.to(resetLevel, ActorAccessor.X, 0.3f)
                .target(-this.getX() - resetLevel.getRealWidth())
                .delay(0.06f)
                .ease(Expo.IN)
                .start(Blend.tweenManager);
        Tween.to(continueButton, ActorAccessor.X, 0.3f)
                .target(-this.getX() - continueButton.getRealWidth())
                .delay(0.09f)
                .ease(Expo.IN)
                .start(Blend.tweenManager);

        Tween.to(circle, ActorAccessor.SCALE, 0.4f)
                .target(0f, 0f)
                .ease(Expo.IN)
                .delay(0.12f)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        if (type == TweenCallback.COMPLETE) {
                            overlay.setVisible(false);
                            isAnimated = false;

                            listeners.onHidden(action);
                        }
                    }
                })
                .start(Blend.tweenManager);

        isAnimated = true;
    }

    public Vector2D getOffset() {
        return offset;
    }

    public void setOffset(Vector2D offset) {
        this.offset.set(offset);
        update();
    }

    public void setOffset(int x, int y) {
        offset.set(x, y);
        update();
    }

    public void setOverlay(PausedOverlay overlay) {
        this.overlay = overlay;
    }

    private float getButtonOffset() {
        return 35f * scale;
    }

    private float getOriginalCircleSize() {
        return 800f;
    }
}
