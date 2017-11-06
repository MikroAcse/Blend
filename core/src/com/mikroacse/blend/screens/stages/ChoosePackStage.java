package com.mikroacse.blend.screens.stages;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Expo;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.mikroacse.blend.Blend;
import com.mikroacse.blend.config.Lang;
import com.mikroacse.blend.listeners.LevelPackListener;
import com.mikroacse.blend.media.Assets;
import com.mikroacse.blend.screens.SceneManager;
import com.mikroacse.blend.screens.objects.LevelPack;
import com.mikroacse.blend.ui.CornerTextButton;
import com.mikroacse.blend.ui.TextActor;
import com.mikroacse.engine.screens.Stage;
import com.mikroacse.engine.tween.ActorAccessor;

import static com.mikroacse.engine.media.ScenesAssets.scale;

/**
 * Created by MikroAcse on 14.07.2016.
 */
public class ChoosePackStage extends Stage {
    private Image background;
    private CornerTextButton backButton;
    private TextActor title;
    private LevelPack levelPack;

    public ChoosePackStage(Game game) {
        super(game);
    }

    @Override
    protected void init() {
        super.init();

        background = new Image(Assets.getTexture(SceneManager.CHOOSE_PACK, "background"));

        backButton = new CornerTextButton();
        backButton.setAlign(CornerTextButton.ALIGN_LEFT);
        backButton.setBigText(Lang.get("backButtonBig"));
        backButton.setSmallText(Lang.get("backButtonSmall"));

        title = new TextActor(Assets.getGlobalFont(SceneManager.CHOOSE_PACK, "title"));
        title.setText(Lang.get(SceneManager.CHOOSE_PACK, "choosePack"));

        levelPack = new LevelPack();

        this.addActor(background);
        this.addActor(backButton);
        this.addActor(title);
        this.addActor(levelPack);

        // TODO: organize listeners
        this.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (animated) return true;
                switch (keycode) {
                    case Input.Keys.LEFT:
                        setLevelPack("THE CONQUEROR", 1, null, ChangePackType.LEFT, true);
                        break;
                    case Input.Keys.RIGHT:
                        setLevelPack("MINDFUCK", 1, null, ChangePackType.RIGHT, true);
                        break;
                }
                return true;
            }
        });

        this.addListener(new ActorGestureListener() {
            @Override
            public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                if (animated) return;
                if (Math.abs(velocityX) > 1000) {
                    if (velocityX > 0) {
                        setLevelPack("THE CONQUEROR", 1, null, ChangePackType.LEFT, true);
                    } else {
                        setLevelPack("MINDFUCK", 1, null, ChangePackType.RIGHT, true);
                    }
                }
            }
        });

        backButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (animated) return;
                hide(SceneManager.MENU, HideType.BACK);
            }
        });

        levelPack.addListener(new ActorGestureListener() {
            boolean flinged = false;

            @Override
            public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                flinged = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (animated || flinged) return;
                hide(SceneManager.CHOOSE_LEVEL, HideType.PACK_SELECTED);
            }

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                flinged = false;
            }
        });
    }

    @Override
    public void update(boolean ignoreAnimated) {
        if (animated && !ignoreAnimated) return;
        super.update(ignoreAnimated);
        title.setScale(scale);
        backButton.setScale(scale);
        levelPack.setScale(scale);

        background.setPosition(0, 0);
        background.setSize((int) getWidth(), (int) getHeight());

        backButton.setX((int) getScreenOffset());
        backButton.setY((int) (getHeight() - backButton.getRealHeight() - getScreenOffset() - 50 * scale)); // TODO: magic number

        float minX = backButton.getX() + backButton.getRealWidth();
        float maxX = getWidth();

        if (maxX > minX * 3 + title.getRealWidth()) {
            title.setX((int) (getWidth() - title.getRealWidth()) / 2);
        } else {
            title.setX(Math.max(minX, (minX + maxX - title.getRealWidth()) / 2));
        }

        title.setY((int) (backButton.getY() + backButton.getRealHeight() / 2 - title.getRealHeight() / 2));

        levelPack.setX((int) (getWidth() - levelPack.getRealWidth()) / 2);
        levelPack.setY((int) (backButton.getY() - levelPack.getRealHeight()) / 2);
    }

    private void setLevelPack(final String title, final int number, final Texture icon,
                              final ChangePackType changePackType, final boolean killTweens) {
        float shiftX = getWidth() / 2 + levelPack.getRealWidth();
        if (changePackType == ChangePackType.RIGHT) {
            shiftX *= -1f;
        }
        final boolean move = changePackType == ChangePackType.RIGHT || changePackType == ChangePackType.LEFT;

        if (killTweens) {
            update(true);
            Blend.tweenManager.killTarget(levelPack);
        }

        if (move) {
            Tween.to(levelPack, ActorAccessor.X, 0.35f)
                    .targetRelative(shiftX)
                    .ease(Expo.IN)
                    .start(Blend.tweenManager);
        }

        LevelPackListener levelPackListener = new LevelPackListener() {
            @Override
            public void onHidden() {
                levelPack.removeListener(this);
                float shiftX = getWidth() / 2 + levelPack.getRealWidth();
                if (changePackType == ChangePackType.LEFT) {
                    shiftX *= -1f;
                }
                levelPack.setTitle(title);
                levelPack.setNumber(number);
                levelPack.setIcon(icon);
                levelPack.show();

                if (killTweens) {
                    update(true);
                    Blend.tweenManager.killTarget(levelPack);
                }

                if (move) {
                    Tween.from(levelPack, ActorAccessor.X, 0.5f)
                            .targetRelative(shiftX)
                            .ease(Expo.OUT)
                            .start(Blend.tweenManager);
                }


                levelPack.addListener(new LevelPackListener() {
                    @Override
                    public void onShown() {
                        levelPack.removeListener(this);
                        animated = false;
                    }
                });
            }
        };

        if (changePackType != ChangePackType.IMMEDIATE) {
            levelPack.hide();
            levelPack.addListener(levelPackListener);
        } else {
            levelPackListener.onHidden();
        }

        animated = true;
    }

    @Override
    public void show() {
        super.show();
        Blend.tweenManager.killTarget(background);
        Blend.tweenManager.killTarget(title);
        Blend.tweenManager.killTarget(backButton);
        Blend.tweenManager.killTarget(levelPack);

        Tween.to(background, ActorAccessor.Y, 0.6f)
                .ease(Expo.OUT)
                .target(background.getY())
                .start(Blend.tweenManager);
        Tween.to(backButton, ActorAccessor.Y, 0.7f)
                .ease(Expo.OUT)
                .target(backButton.getY())
                .delay(0.05f)
                .start(Blend.tweenManager);
        Tween.to(title, ActorAccessor.Y, 0.7f)
                .ease(Expo.OUT)
                .target(title.getY())
                .delay(0.05f)
                .start(Blend.tweenManager);
        Tween.to(levelPack, ActorAccessor.Y, 0.7f)
                .ease(Expo.OUT)
                .target(levelPack.getY())
                .delay(0.2f)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        if (type == TweenCallback.BEGIN) {
                            // TODO: levelpack
                            setLevelPack("TUTORIAL", 1, null, ChangePackType.IMMEDIATE, false);
                        }
                        if (type == TweenCallback.COMPLETE) {
                            animated = false;
                            update();
                        }
                    }
                })
                .setCallbackTriggers(TweenCallback.COMPLETE | TweenCallback.BEGIN)
                .start(Blend.tweenManager);

        background.setY(-background.getHeight() * 1.1f);
        title.setY(-title.getRealHeight() * 1.1f);
        backButton.setY(-backButton.getRealHeight() * 1.1f);
        levelPack.setY(-levelPack.getRealHeight() * 1.1f);
        animated = true;
    }

    @Override
    public void hide(final String nextScene) {
        hide(nextScene, HideType.BACK);
    }

    public void hide(final String nextScene, final HideType hideType) {
        super.hide(nextScene);

        Blend.tweenManager.killTarget(background);
        Blend.tweenManager.killTarget(title);
        Blend.tweenManager.killTarget(backButton);
        Blend.tweenManager.killTarget(levelPack);

        if (hideType == HideType.BACK) {
            levelPack.hide();
            Tween.to(levelPack, ActorAccessor.Y, 0.3f)
                    .ease(Expo.IN)
                    .target(-levelPack.getRealHeight() * 1.1f)
                    .start(Blend.tweenManager);
            Tween.to(title, ActorAccessor.Y, 0.4f)
                    .ease(Expo.IN)
                    .target(-title.getRealHeight() * 1.1f)
                    .delay(0.15f)
                    .start(Blend.tweenManager);
            Tween.to(backButton, ActorAccessor.Y, 0.4f)
                    .ease(Expo.IN)
                    .target(-backButton.getRealHeight() * 1.1f)
                    .delay(0.15f)
                    .start(Blend.tweenManager);
            Tween.to(background, ActorAccessor.Y, 0.4f)
                    .ease(Expo.IN)
                    .target(-background.getHeight() * 1.1f)
                    .delay(0.2f)
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
        } else if (hideType == HideType.PACK_SELECTED) {
            levelPack.hide(false);
            float newScale = 10f * scale; // TODO: magic number

            float newX = (int) (getWidth() - levelPack.getWidth() * newScale) / 2;
            float newY = (int) (backButton.getY() - levelPack.getHeight() * newScale) / 2;

            Tween.to(levelPack, ActorAccessor.SCALE, 0.4f)
                    .ease(Expo.IN)
                    .target(newScale, newScale)
                    .start(Blend.tweenManager);
            Tween.to(levelPack, ActorAccessor.POSITION, 0.4f)
                    .ease(Expo.IN)
                    .target(newX, newY)
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
        }

        animated = true;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            levelPack.move(-1, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            levelPack.move(1, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            levelPack.move(0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            levelPack.move(0, -1);
        }
    }

    @Override
    public void draw() {
        super.draw();
    }

    private float getScreenOffset() {
        return 30f * scale;
    }

    private enum HideType {
        BACK,
        PACK_SELECTED
    }

    private enum ChangePackType {
        RIGHT,
        LEFT,
        CENTER,
        IMMEDIATE
    }
}
