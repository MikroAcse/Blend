package com.mikroacse.blend.screens.objects;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Expo;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mikroacse.blend.Blend;
import com.mikroacse.blend.config.Config;
import com.mikroacse.blend.listeners.interfaces.LevelPackListener;
import com.mikroacse.blend.media.Assets;
import com.mikroacse.blend.screens.SceneManager;
import com.mikroacse.blend.ui.TextActor;
import com.mikroacse.blend.utils.AnimationUtil;
import com.mikroacse.blend.utils.Colors;
import com.mikroacse.engine.actors.AnimationActor;
import com.mikroacse.engine.actors.Group;
import com.mikroacse.engine.listeners.AnimationListener;
import com.mikroacse.engine.listeners.core.ListenerSupport;
import com.mikroacse.engine.listeners.core.ListenerSupportFactory;
import com.mikroacse.engine.tween.ActorAccessor;
import com.mikroacse.engine.utils.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MikroAcse on 14.07.2016.
 */
public class LevelPack extends Group {
    private static final int LAYERS_COUNT = 3;
    private final LevelPackListener listeners;
    private TextActor title;
    private TextActor number;
    private Image icon;
    private Rectangle titleBox;
    private Rectangle numberBox;
    private Rectangle iconBox;
    private Vector2D showHidePosition;
    private AnimationActor showHideAnimation;
    private List<AnimationActor> layers;

    public LevelPack() {
        super();
        listeners = ListenerSupportFactory.create(LevelPackListener.class);

        init();
    }

    private void init() {
        showHidePosition = getShowHideAnimationPosition();
        titleBox = getTitleBoxRectangle();
        numberBox = getNumberBoxRectangle();
        // TODO: icon box

        showHideAnimation = new AnimationActor(AnimationUtil.create(SceneManager.CHOOSE_PACK, "showhide-levelpack"));
        this.addActor(showHideAnimation);
        showHideAnimation.setVisible(false);

        layers = new ArrayList<>();
        for (int i = 0; i < LAYERS_COUNT; i++) {
            AnimationActor layer = new AnimationActor(AnimationUtil.create(SceneManager.CHOOSE_PACK, "levelpack"));
            layer.setRepeatable(true);
            layers.add(layer);
            this.addActorAt(0, layer);
        }

        layers.get(1).setColor(Colors.MAGENTA);
        layers.get(2).setColor(Colors.CYAN);

        title = new TextActor(Assets.getGlobalFont(SceneManager.CHOOSE_PACK, "levelpack-title"));
        number = new TextActor(Assets.getGlobalFont(SceneManager.CHOOSE_PACK, "levelpack-number"));

        title.setColor(Colors.DARK_GRAY);
        number.setColor(Colors.DARK_GRAY);

        this.addActor(title);
        this.addActor(number);
        update();
    }

    private void update() {
        showHideAnimation.setX(showHidePosition.x);
        showHideAnimation.setY(showHidePosition.y);

        title.setX((int) (titleBox.x + titleBox.width / 2 - title.getRealWidth() / 2));
        title.setY((int) titleBox.y);

        number.setX((int) (numberBox.x + numberBox.width / 2 - number.getRealWidth() / 2));
        number.setY((int) numberBox.y);

        // TODO: update icon position
    }

    public void set(String title, int number, Texture icon) {
        this.title.setText(title);
        this.number.setText(String.valueOf(number));
        // TODO: set icon
        update();
    }

    public void setIcon(Texture texture) {
        // TODO: set icon
        update();
    }

    public void setTitle(String value) {
        title.setText(value);
        update();
    }

    public void setNumber(int value) {
        number.setText(String.valueOf(value));
        update();
    }

    public void addListener(LevelPackListener listener) {
        ((ListenerSupport<LevelPackListener>) listeners).addListener(listener);
    }

    public void removeListener(LevelPackListener listener) {
        ((ListenerSupport<LevelPackListener>) listeners).removeListener(listener);
    }

    public void clearListeners() {
        ((ListenerSupport<LevelPackListener>) listeners).clearListeners();
    }

    public void show() {
        Blend.tweenManager.killTarget(title);
        Blend.tweenManager.killTarget(number);
        title.getColor().a = 0f;
        number.getColor().a = 0f;

        Tween.to(title, ActorAccessor.ALPHA, 0.3f)
                .target(1f)
                .delay(0.2f)
                .start(Blend.tweenManager);
        Tween.to(number, ActorAccessor.ALPHA, 0.3f)
                .target(1f)
                .delay(0.2f)
                .start(Blend.tweenManager);

        for (int i = 0; i < LAYERS_COUNT; i++) {
            AnimationActor layer = layers.get(i);
            layer.setFrame((layer.getFramesCount() / LAYERS_COUNT) * i);
            layer.pause();

            Blend.tweenManager.killTarget(layer);
            if (i > 0) {
                layer.setVisible(true);
                layer.setPosition(layer.getWidth() / 2, layer.getHeight() / 2);
                layer.setScale(0f);

                Tween.to(layer, ActorAccessor.SCALE, 0.3f)
                        .target(1f, 1f)
                        .ease(Expo.OUT)
                        .delay(0.15f + (i - 1) * 0.15f)
                        .start(Blend.tweenManager);
                Tween.to(layer, ActorAccessor.POSITION, 0.3f)
                        .target(0f, 0f)
                        .ease(Expo.OUT)
                        .delay(0.15f + (i - 1) * 0.15f)
                        .start(Blend.tweenManager);
            } else {
                layer.setPosition(0f, 0f);
                layer.setScale(1f);
                layer.setVisible(false);
            }
        }

        showHideAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        showHideAnimation.setFrameDuration(Config.getAnimationFrameDuration());
        showHideAnimation.setVisible(true);
        showHideAnimation.setFrame(0);

        showHideAnimation.addListener(new AnimationListener() {
            @Override
            public void onComplete() {
                showHideAnimation.removeListener(this);
                showHideAnimation.setVisible(false);

                for (int i = 0; i < LAYERS_COUNT; i++) {
                    AnimationActor layer = layers.get(i);
                    layer.resume();

                    if (i == 0) {
                        layer.setVisible(true);
                    }
                }

                listeners.onShown();
            }
        });
    }

    public void hide() {
        hide(true);
    }

    public void hide(final boolean hideAnimation) {
        Blend.tweenManager.killTarget(title);
        Blend.tweenManager.killTarget(number);
        Tween.to(title, ActorAccessor.ALPHA, 0.2f)
                .target(0f)
                .start(Blend.tweenManager);
        Tween.to(number, ActorAccessor.ALPHA, 0.2f)
                .target(0f)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        if (hideAnimation) {
                            return;
                        }
                        if (type == TweenCallback.COMPLETE) {
                            listeners.onHidden();
                        }
                    }
                })
                .start(Blend.tweenManager);

        for (int i = 0; i < LAYERS_COUNT; i++) {
            Blend.tweenManager.killTarget(layers.get(i));
        }

        if (!hideAnimation) {
            return;
        }

        for (int i = 0; i < LAYERS_COUNT; i++) {
            AnimationActor layer = layers.get(i);
            layer.setVisible(true);

            float duration = i > 0 ? 0.25f : 0.1f;
            Tween.to(layer, ActorAccessor.SCALE, duration)
                    .target(0f, 0f)
                    .ease(Expo.IN)
                    .start(Blend.tweenManager);
            Tween.to(layer, ActorAccessor.POSITION, duration)
                    .target(layer.getWidth() / 2, layer.getHeight() / 2)
                    .ease(Expo.IN)
                    .start(Blend.tweenManager);
        }

        showHideAnimation.setFrameDuration(Config.getAnimationFrameDuration() / getHideAnimationSpeed());
        showHideAnimation.setPlayMode(Animation.PlayMode.REVERSED);
        showHideAnimation.setVisible(true);
        showHideAnimation.setFrame(0);

        showHideAnimation.addListener(new AnimationListener() {
            @Override
            public void onComplete() {
                showHideAnimation.removeListener(this);
                showHideAnimation.setVisible(false);

                for (int i = 0; i < LAYERS_COUNT; i++) {
                    AnimationActor layer = layers.get(i);
                    layer.setVisible(false);
                    layer.pause();
                }
                listeners.onHidden();
            }
        });
    }

    @Override
    public float getWidth() {
        return layers.get(0).getWidth();
    }

    @Override
    public float getHeight() {
        return layers.get(0).getHeight();
    }

    @Override
    public float getRealWidth() {
        return getWidth() * getScaleX();
    }

    @Override
    public float getRealHeight() {
        return getHeight() * getScaleY();
    }

    // TODO: remove
    public void move(int x, int y) {
        /*title.moveBy(x, y);
        number.moveBy(x, y);
        Gdx.app.log("Vector2D", "Title: " + title.getX() + "," + title.getY() + " | Number: "  + number.getX() + "," + number.getY()
                + " | Title max: " + (title.getX() + title.getWidth()) + "," + (title.getY() + title.getHeight()) + " | Number max: "  + (number.getX() + number.getWidth()) + "," + (number.getY() + number.getHeight()));
    */
    }

    private Vector2D getShowHideAnimationPosition() {
        return new Vector2D(10, 4);
    }

    private Rectangle getTitleBoxRectangle() {
        return new Rectangle(47, 248, 363, 26);
    }

    private Rectangle getNumberBoxRectangle() {
        return new Rectangle(250, 103, 197, 51);
    }

    private Rectangle getIconBoxRectangle() {
        return null;
    }

    private float getHideAnimationSpeed() {
        return 1.5f;
    }
}
