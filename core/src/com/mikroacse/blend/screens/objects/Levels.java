package com.mikroacse.blend.screens.objects;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Expo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.mikroacse.blend.Blend;
import com.mikroacse.blend.listeners.interfaces.LevelsListener;
import com.mikroacse.blend.media.Assets;
import com.mikroacse.blend.screens.SceneManager;
import com.mikroacse.blend.screens.objects.core.Level;
import com.mikroacse.blend.utils.Colors;
import com.mikroacse.engine.actors.Group;
import com.mikroacse.engine.listeners.core.ListenerSupport;
import com.mikroacse.engine.listeners.core.ListenerSupportFactory;
import com.mikroacse.engine.tween.ActorAccessor;

import java.util.ArrayList;
import java.util.List;

import static com.mikroacse.engine.media.ScenesAssets.scale;

/**
 * Created by MikroAcse on 24.07.2016.
 */
public class Levels extends Group {
    private static final int LARGE_LINE = 4;
    private static final int SMALL_LINE = 3;
    private final LevelsListener listeners;
    private List<Level> levels;
    private int levelsCount;
    private Image circle;
    private boolean isAnimated;

    public Levels() {
        super();
        listeners = ListenerSupportFactory.create(LevelsListener.class);

        init();
    }

    private void init() {
        circle = new Image(Assets.getTexture(SceneManager.CHOOSE_LEVEL, "big-circle"));
        this.addActor(circle);
        circle.setVisible(false);
        circle.setOrigin(Align.center);

        levels = new ArrayList<>();

        levelsCount = 12;

        for (int i = 0; i < levelsCount; i++) {
            Level level = new Level();
            level.setText(String.valueOf(i + 1));

            this.addActor(level);
            levels.add(level);
        }

        update();

        this.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                if (isAnimated) return;
                for (int i = 0; i < levelsCount; i++) {
                    Level level = levels.get(i);
                    if (level.hit(x, y)) {
                        listeners.onLevelSelected(i);
                        break;
                    }
                }
            }
        });
    }

    public void addListener(LevelsListener listener) {
        ((ListenerSupport<LevelsListener>) listeners).addListener(listener);
    }

    public void removeListener(LevelsListener listener) {
        ((ListenerSupport<LevelsListener>) listeners).removeListener(listener);
    }

    public void clearListeners() {
        ((ListenerSupport<LevelsListener>) listeners).clearListeners();
    }

    public void update() {
        boolean largeLine = true;
        int maxLevelsOnLine = LARGE_LINE;
        int linesCount = getLinesCount(levelsCount);
        int levelsOnLine = 0;
        int currentLine = 0;
        for (int i = 0; i < levelsCount; i++) {
            Level level = levels.get(i);

            level.setScale(scale);
            level.setX(levelsOnLine * (level.getRealWidth() + getLevelOffsetX()));

            if (!largeLine) {
                level.moveBy((int) (level.getRealWidth() + getLevelOffsetX()) * (LARGE_LINE - SMALL_LINE) / 2, 0f);
            }

            level.setY((linesCount - currentLine - 1) * (level.getRealHeight() + getLevelOffsetY()));

            if (i % 2 != 0) {
                level.setBackgroundColor(Colors.MAGENTA);
            } else {
                level.setBackgroundColor(Colors.CYAN);
            }

            levelsOnLine++;
            if (levelsOnLine >= maxLevelsOnLine) {
                currentLine++;
                levelsOnLine = 0;
                largeLine = !largeLine;
                maxLevelsOnLine = largeLine ? LARGE_LINE : SMALL_LINE;
            }
        }
        circle.setVisible(false);
    }

    public void show() {
        update();

        Tween lastTween = null;
        for (int i = 0; i < levelsCount; i++) {
            Level level = levels.get(i);
            level.show(i * 0.05f + 0.25f);

            Blend.tweenManager.killTarget(level);
            Tween.to(level, ActorAccessor.SCALE, 0.6f)
                    .target(scale, scale)
                    .delay(i * 0.05f)
                    .ease(Expo.OUT)
                    .start(Blend.tweenManager);

            lastTween =
                    Tween.to(level, ActorAccessor.POSITION, 0.6f)
                            .targetRelative(-level.getRealWidth() / 2, -level.getRealHeight() / 2)
                            .delay(i * 0.05f)
                            .ease(Expo.OUT)
                            .start(Blend.tweenManager);

            level.moveBy(level.getRealWidth() / 2, level.getRealHeight() / 2);
            level.setScale(0f);
        }

        if (lastTween != null) {
            lastTween.setCallback(new TweenCallback() {
                @Override
                public void onEvent(int type, BaseTween<?> source) {
                    if (type == TweenCallback.COMPLETE) {
                        isAnimated = false;
                        listeners.onShown();
                    }
                }
            });
        } else {
            Gdx.app.log("LEVELS", "Something went wrong while showing levels.");
        }

        isAnimated = true;
    }

    public void hide(float stageHeight, float delay) {
        circle.setVisible(false);

        Tween lastTween = null;
        for (int i = 0; i < levelsCount; i++) {
            Level level = levels.get(i);

            Blend.tweenManager.killTarget(level);

            lastTween =
                    Tween.to(level, ActorAccessor.Y, 0.4f)
                            .target(stageHeight - this.getY() - level.getHeight())
                            .delay(i * 0.04f + delay)
                            .ease(Expo.IN)
                            .start(Blend.tweenManager);
        }

        if (lastTween != null) {
            lastTween.setCallback(new TweenCallback() {
                @Override
                public void onEvent(int type, BaseTween<?> source) {
                    if (type == TweenCallback.COMPLETE) {
                        isAnimated = false;
                        listeners.onHidden();
                    }
                }
            });
        } else {
            Gdx.app.log("LEVELS", "Something went wrong while showing levels.");
        }

        isAnimated = true;
    }

    public void hide(int levelNumber) {
        Level level = levels.get(levelNumber);
        this.addActor(circle);
        this.addActor(level);

        circle.setVisible(true);

        circle.setScale(0f);
        circle.setX((int) (level.getX() + level.getRealWidth() / 2 - circle.getWidth() / 2));
        circle.setY((int) (level.getY() + level.getRealHeight() / 2 - circle.getHeight() / 2));

        float y = this.getY();
        Timeline.createSequence()
                .push(Tween.to(circle, ActorAccessor.SCALE, 0.6f)
                        .target(3f, 3f)) // TODO: magic number
                .push(Tween.to(level, ActorAccessor.Y, 0.3f)
                        .target(-y - level.getY() - level.getRealHeight())
                        .ease(Expo.IN))
                .setCallbackTriggers(TweenCallback.COMPLETE)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        if (type == TweenCallback.COMPLETE) {
                            isAnimated = false;
                            listeners.onHidden();
                        }
                    }
                })
                .start(Blend.tweenManager);

        isAnimated = true;
    }

    private int getLinesCount(int count) {
        int lines = (count / (LARGE_LINE + SMALL_LINE)) * 2;
        int rest = count % (LARGE_LINE + SMALL_LINE);

        if (rest == 0) {
            return lines;
        }

        if (lines % 2 != 0) {
            rest -= SMALL_LINE;
        } else {
            rest -= LARGE_LINE;
        }

        return rest <= 0 ? lines + 1 : lines + 2;
    }

    private boolean isLineLarge(int count) {
        return getLinesCount(count) % 2 != 0;
    }

    private float getLevelOffsetX() {
        return 25 * scale;
    }

    private float getLevelOffsetY() {
        return 15 * scale;
    }
}
