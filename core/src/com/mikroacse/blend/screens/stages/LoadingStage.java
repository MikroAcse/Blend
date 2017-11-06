package ru.mikroacse.rolespell.screens.loading.view;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Expo;
import com.badlogic.gdx.scenes.scene2d.Stage;
import ru.mikroacse.engine.actors.AnimationActor;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.media.AssetManager;
import ru.mikroacse.util.AnimationUtil;

/**
 * Created by MikroAcse on 14.07.2016.
 */
public class LoadingStage extends Stage {
    private AnimationActor animation;
    
    public LoadingStage() {
        animation = new AnimationActor(
                AnimationUtil.create(
                        AssetManager.Bundle.LOADING,
                        "loading",
                        false
                )
        );
        
        animation.setFrameDuration(getAnimationFrameDuration());
        animation.setRepeatable(true);
    
        this.addActor(animation);
    }
    
    public void update() {
        update(false);
    }

    @Override
    public void update(boolean ignoreAnimated) {
        super.update(ignoreAnimated);
        animation.setScale(getAnimationScale());

        animation.setX((int) (getWidth() - animation.getRealWidth()) / 2);
        animation.setY((int) (getHeight() - animation.getRealHeight()) / 2);
    }

    @Override
    public void show() {
        super.show();
        Blend.tweenManager.killTarget(animation);

        Tween.to(animation, ActorAccessor.ALPHA, 0.2f)
                .ease(Expo.OUT)
                .target(1f)
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

        animation.getColor().a = 1;
        animated = true;
    }

    @Override
    public void hide(String nextScene) {
        super.hide(nextScene);
        Blend.tweenManager.killTarget(animation);

        Tween.to(animation, ActorAccessor.ALPHA, 0.2f)
                .ease(Expo.IN)
                .target(0f)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        if (type == TweenCallback.COMPLETE) {
                            SceneManager.setWaited();
                        }
                    }
                })
                .start(Blend.tweenManager);
        animated = true;
    }

    @Override
    public void act(float delta) {
        if (!animated && Assets.isLoaded()) {
            hide(SceneManager.getWaitScene());
        }

        super.act(delta);
    }

    @Override
    public void draw() {
        super.draw();
    }

    private float getAnimationFrameDuration() {
        return 1 / 20f;
    }

    private float getAnimationScale() {
        return 5f * RoleSpell.getAssetManager();
    }
}
