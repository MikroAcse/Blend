package com.mikroacse.blend.screens.stages;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Expo;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.mikroacse.blend.Blend;
import com.mikroacse.blend.config.Lang;
import com.mikroacse.blend.listeners.FieldListener;
import com.mikroacse.blend.listeners.PauseMenuListener;
import com.mikroacse.blend.media.Assets;
import com.mikroacse.blend.screens.SceneManager;
import com.mikroacse.blend.screens.objects.CellField;
import com.mikroacse.blend.screens.objects.MatchingUI;
import com.mikroacse.blend.screens.objects.PauseMenu;
import com.mikroacse.blend.screens.objects.PausedOverlay;
import com.mikroacse.blend.ui.CornerTextButton;
import com.mikroacse.blend.ui.TextActor;
import com.mikroacse.engine.graphics.GrayscaleShader;
import com.mikroacse.engine.screens.Stage;
import com.mikroacse.engine.tween.ActorAccessor;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.mikroacse.engine.media.ScenesAssets.scale;

/**
 * Created by MikroAcse on 09.07.2016.
 */
public class LevelStage extends Stage {
    private Image background;

    private CornerTextButton pauseButton;
    private CornerTextButton timeLeft;
    private TextActor movesLeft;
    private CellField field;
    private MatchingUI matchingUI;

    private PauseMenu pauseMenu;
    private PausedOverlay pausedOverlay;

    private boolean paused;
    private float currentTime;

    public LevelStage(Game game) {
        super(game);
    }

    @Override
    protected void init() {
        super.init();

        background = new Image(Assets.getTexture(SceneManager.LEVEL, "background"));
        this.addActor(background);

        field = new CellField(10);
        Random rand = new Random();
        List<String> colors = Arrays.asList("magenta", "yellow", "blue", "cyan", "green", "orange");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                field.setColor(i, j, colors.get(rand.nextInt(colors.size())));
            }
        }
        this.addActor(field);

        matchingUI = new MatchingUI();
        this.addActor(matchingUI);

        BitmapFont font = Assets.getGlobalFont(SceneManager.LEVEL, "moves-left");
        movesLeft = new TextActor(font);
        this.addActor(movesLeft);

        pauseButton = new CornerTextButton();
        pauseButton.setBigText("II");
        pauseButton.setSmallText(Lang.get(SceneManager.LEVEL, "pauseButton"));
        pauseButton.setAlign(CornerTextButton.ALIGN_LEFT);
        this.addActor(pauseButton);

        timeLeft = new CornerTextButton();
        timeLeft.setSmallText(Lang.get(SceneManager.LEVEL, "timeLeft"));
        timeLeft.setAlign(CornerTextButton.ALIGN_RIGHT);
        this.addActor(timeLeft);

        pausedOverlay = new PausedOverlay();
        this.addActor(pausedOverlay);
        pausedOverlay.setVisible(false);

        pauseMenu = new PauseMenu();
        this.addActor(pauseMenu);
        pauseMenu.setVisible(false);
        pauseMenu.setOverlay(pausedOverlay);

        field.setStartPoint(1, 2, Color.GREEN);
        field.setFinalPoint(8, 6, Color.CYAN);
        matchingUI.setCurrentColor(Color.GREEN, false);
        matchingUI.setFinalColor(Color.CYAN);

        this.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (animated || paused) return true;
                switch (keycode) {
                    case Input.Keys.UP:
                        field.moveCube(0, 1);
                        break;
                    case Input.Keys.DOWN:
                        field.moveCube(0, -1);
                        break;
                    case Input.Keys.LEFT:
                        field.moveCube(-1, 0);
                        break;
                    case Input.Keys.RIGHT:
                        field.moveCube(1, 0);
                        break;
                }
                return true;
            }
        });

        this.addListener(new ActorGestureListener() {
            @Override
            public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                if (animated || paused) return;
                if (Math.abs(velocityX) > 1000) {
                    if (velocityX > 0) {
                        field.moveCube(1, 0);
                    } else {
                        field.moveCube(-1, 0);
                    }
                } else if (Math.abs(velocityY) > 1000) {
                    if (velocityY > 0) {
                        field.moveCube(0, 1);
                    } else {
                        field.moveCube(0, -1);
                    }
                }
            }
        });

        field.addListener(new FieldListener() {
            @Override
            public void onFinalCell() {
                Gdx.app.log("LEVEL_STAGE", "Final point!");
                updateMoves(true);
                update();
            }

            @Override
            public void onMove() {
                matchingUI.setCurrentColor(field.getCubeColor(), true);
                updateMoves(true);
                update();
            }
        });

        pauseButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (animated || paused) return;
                pauseMenu.setVisible(true);
                pauseMenu.show();

                getBatch().setShader(GrayscaleShader.instance);
                paused = true;
            }
        });

        pauseMenu.addListener(new PauseMenuListener() {
            @Override
            public void onHiding(int action) {
                getBatch().setShader(null);
            }

            @Override
            public void onHidden(int action) {
                pauseMenu.setVisible(false);
                paused = false;

                switch (action) {
                    case BACK_TO_LEVELS:
                        hide(SceneManager.CHOOSE_LEVEL);
                        break;
                    case RESET_LEVEL:
                        // TODO: reset level
                        field.reset();
                        Random rand = new Random();
                        List<String> colors = Arrays.asList("magenta", "yellow", "blue", "cyan", "green", "orange");
                        for (int i = 0; i < 10; i++) {
                            for (int j = 0; j < 10; j++) {
                                field.setColor(i, j, colors.get(rand.nextInt(colors.size())));
                            }
                        }

                        field.setStartPoint(1, 2, Color.GREEN);
                        field.setFinalPoint(8, 6, Color.CYAN);
                        matchingUI.setCurrentColor(Color.GREEN, false);
                        matchingUI.setFinalColor(Color.CYAN);

                        currentTime = 0;
                        updateMoves(false);
                        update();
                        break;
                    case BACK_TO_MENU:
                        hide(SceneManager.MENU);
                        break;
                }
            }
        });

        pausedOverlay.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                pauseMenu.hide();
            }
        });
    }

    private void gameStarted() {

    }

    @Override
    public void act(float delta) {
        if (animated) return;
        super.act(delta);

        if (paused) return;
        currentTime += delta;
        updateTime();
    }

    @Override
    public void update() {
        if (animated) return;
        super.update();
        field.setScale(scale);
        matchingUI.setScale(scale);
        pauseButton.setScale(scale);
        timeLeft.setScale(scale);
        movesLeft.setScale(scale);

        background.setPosition(0, 0);
        background.setSize(getWidth(), getHeight());

        field.setX((int) (getWidth() - field.getRealWidth()) / 2);
        field.setY((int) (getHeight() - field.getRealHeight()) / 2);

        matchingUI.setX((int) (getWidth() - matchingUI.getRealWidth()) / 2);
        matchingUI.setY((int) (field.getY() - matchingUI.getRealHeight() - getFieldOffset()));

        updateMoves(false);

        movesLeft.setX((int) (getWidth() - movesLeft.getRealWidth()) / 2);
        movesLeft.setY((int) (field.getY() + field.getRealHeight() + getFieldOffset()));

        pauseButton.setX((int) getScreenOffset());
        pauseButton.setY((int) (getHeight() - pauseButton.getRealHeight() - getScreenOffset()));

        updateTimePosition();

        timeLeft.setY((int) (getHeight() - timeLeft.getRealHeight() - getScreenOffset()));

        pauseMenu.setX(pauseButton.getX());
        pauseMenu.setY((int) (pauseButton.getY() + pauseButton.getRealHeight()));
        pauseMenu.setOffset((int) pauseButton.getRealWidth() / 2, (int) pauseButton.getRealHeight() / 2);

        pausedOverlay.setBounds((int) getWidth(), (int) getHeight());
        pausedOverlay.setOffset((int) getScreenOffset(), (int) getScreenOffset());
    }

    private void updateTime() {
        int seconds = (int) currentTime;
        int minutes = seconds / 60;

        if (timeLeft.setBigText(String.format("%d:%02d", minutes, seconds % 60))) {
            updateTimePosition();
        }
    }

    private void updateTimePosition() {
        timeLeft.setX(getWidth() - (int) timeLeft.getRealWidth() - (int) getScreenOffset());
    }

    private void updateMoves(boolean animate) {
        int moves = Math.max(0, 30 - field.getPathLength());
        movesLeft.setText(Lang.get(SceneManager.LEVEL, "movesLeft", moves));

        if (animate) {
            Blend.tweenManager.killTarget(movesLeft);
            Timeline.createSequence()
                    .push(Tween.to(movesLeft, ActorAccessor.ALPHA, 0.1f)
                            .target(0.8f))
                    .push(Tween.to(movesLeft, ActorAccessor.ALPHA, 0.1f)
                            .target(1f))
                    .start(Blend.tweenManager);
        }
    }

    @Override
    public void show() {
        super.show();
        currentTime = 0;
        updateTime();

        Blend.tweenManager.killTarget(matchingUI);
        Blend.tweenManager.killTarget(field);
        Blend.tweenManager.killTarget(movesLeft);
        Blend.tweenManager.killTarget(pauseButton);
        Blend.tweenManager.killTarget(timeLeft);
        Blend.tweenManager.killTarget(background);

        Tween.to(background, ActorAccessor.Y, 0.7f)
                .ease(Expo.OUT)
                .target(background.getY())
                .start(Blend.tweenManager);
        Tween.to(matchingUI, ActorAccessor.Y, 0.7f)
                .ease(Expo.OUT)
                .target(matchingUI.getY())
                .delay(0.1f)
                .start(Blend.tweenManager);
        Tween.to(field, ActorAccessor.Y, 0.7f)
                .ease(Expo.OUT)
                .target(field.getY())
                .delay(0.2f)
                .start(Blend.tweenManager);
        Tween.to(movesLeft, ActorAccessor.Y, 0.7f)
                .ease(Expo.OUT)
                .target(movesLeft.getY())
                .delay(0.35f)
                .start(Blend.tweenManager);
        Tween.to(pauseButton, ActorAccessor.Y, 0.7f)
                .ease(Expo.OUT)
                .target(pauseButton.getY())
                .delay(0.45f)
                .start(Blend.tweenManager);
        Tween.to(timeLeft, ActorAccessor.Y, 0.7f)
                .ease(Expo.OUT)
                .target(timeLeft.getY())
                .delay(0.45f)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        if (type == TweenCallback.COMPLETE) {
                            animated = false;
                            update();
                            gameStarted();
                        }
                    }
                })
                .start(Blend.tweenManager);

        field.setY(getHeight() * 1.1f);
        matchingUI.setY(getHeight() * 1.1f);
        movesLeft.setY(getHeight() * 1.1f);
        pauseButton.setY(getHeight() * 1.1f);
        timeLeft.setY(getHeight() * 1.1f);
        background.setY(getHeight() * 1.1f);
        animated = true;
    }

    @Override
    public void hide(final String nextScene) {
        super.hide(nextScene);
        Blend.tweenManager.killTarget(matchingUI);
        Blend.tweenManager.killTarget(field);
        Blend.tweenManager.killTarget(movesLeft);
        Blend.tweenManager.killTarget(pauseButton);
        Blend.tweenManager.killTarget(timeLeft);
        Blend.tweenManager.killTarget(background);

        Tween.to(pauseButton, ActorAccessor.Y, 0.3f)
                .ease(Expo.IN)
                .target(getHeight() * 1.1f)
                .start(Blend.tweenManager);
        Tween.to(timeLeft, ActorAccessor.Y, 0.3f)
                .ease(Expo.IN)
                .target(getHeight() * 1.1f)
                .start(Blend.tweenManager);
        Tween.to(movesLeft, ActorAccessor.Y, 0.3f)
                .ease(Expo.IN)
                .target(getHeight() * 1.1f)
                .delay(0.02f)
                .start(Blend.tweenManager);
        Tween.to(field, ActorAccessor.Y, 0.4f)
                .ease(Expo.IN)
                .target(getHeight() * 1.1f)
                .delay(0.1f)
                .start(Blend.tweenManager);
        Tween.to(matchingUI, ActorAccessor.Y, 0.4f)
                .ease(Expo.IN)
                .target(getHeight() * 1.1f)
                .delay(0.15f)
                .start(Blend.tweenManager);
        Tween.to(background, ActorAccessor.Y, 0.5f)
                .ease(Expo.IN)
                .target(getHeight() * 1.1f)
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

        animated = true;
    }

    @Override
    public void draw() {
        super.draw();
    }

    private float getScreenOffset() {
        return 30 * scale;
    }

    private float getFieldOffset() {
        return 40 * scale;
    }
}