package com.mikroacse.blend.screens.stages;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Expo;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.mikroacse.blend.Blend;
import com.mikroacse.blend.config.Lang;
import com.mikroacse.blend.listeners.LevelsListener;
import com.mikroacse.blend.media.Assets;
import com.mikroacse.blend.screens.SceneManager;
import com.mikroacse.blend.screens.models.ChooseLevelModel;
import com.mikroacse.blend.screens.objects.Levels;
import com.mikroacse.blend.ui.CornerTextButton;
import com.mikroacse.blend.ui.TextActor;
import com.mikroacse.blend.utils.Colors;
import com.mikroacse.engine.screens.Stage;
import com.mikroacse.engine.tween.ActorAccessor;

import static com.mikroacse.engine.media.ScenesAssets.scale;

/**
 * Created by MikroAcse on 14.07.2016.
 */
public class ChooseLevelStage extends Stage {
    private CornerTextButton backButton;
    private TextActor title;
    private Levels levels;
    private ChooseLevelModel model;

    public ChooseLevelStage(Game game) {
        super(game);
    }

    @Override
    protected void init() {
        super.init();

        model = (ChooseLevelModel) SceneManager.getModel(SceneManager.CHOOSE_LEVEL);

        backButton = new CornerTextButton();
        backButton.setColor(Colors.DARK_GRAY);
        backButton.setBigText(Lang.get("backButtonBig"));
        backButton.setSmallText(Lang.get("backButtonSmall"));
        this.addActor(backButton);

        title = new TextActor(Assets.getGlobalFont(SceneManager.CHOOSE_LEVEL, "title"));
        title.setColor(Colors.DARK_GRAY);
        title.setText("THE CONQUEROR");
        this.addActor(title);

        levels = new Levels();
        this.addActor(levels);

        levels.addListener(new LevelsListener() {
            @Override
            public void onLevelSelected(int level) {
                model.setSelectedLevel(level);
                hide(SceneManager.LEVEL, HideType.LEVEL_SELECTED);
            }
        });

        backButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                hide(SceneManager.CHOOSE_PACK, HideType.BACK);
            }
        });
    }

    @Override
    public void update(boolean ignoreAnimated) {
        if (animated && !ignoreAnimated) return;
        super.update(ignoreAnimated);

        backButton.setScale(scale);
        title.setScale(scale);

        levels.update();

        backButton.setX((int) getScreenOffset());
        backButton.setY((int) (getHeight() - backButton.getRealHeight() - getScreenOffset()));

        float minX = backButton.getX() + backButton.getRealWidth();
        float maxX = getWidth();

        if (maxX > minX * 3 + title.getRealWidth()) {
            title.setX((int) (getWidth() - title.getRealWidth()) / 2);
        } else {
            title.setX(Math.max(minX, (int) (minX + maxX - title.getRealWidth()) / 2));
        }

        title.setY((int) (backButton.getY() + backButton.getRealHeight() / 2 - title.getRealHeight() / 2));

        levels.setX((int) (getWidth() - levels.getRealWidth()) / 2);
        levels.setY((int) (backButton.getY() - levels.getRealHeight()) / 2);
    }

    @Override
    public void show() {
        super.show();
        update(true);

        Blend.tweenManager.killTarget(backButton);
        Blend.tweenManager.killTarget(title);

        Tween.to(backButton, ActorAccessor.Y, 0.5f)
                .target(backButton.getY())
                .ease(Expo.OUT)
                .start(Blend.tweenManager);
        Tween.to(title, ActorAccessor.Y, 0.5f)
                .target(title.getY())
                .ease(Expo.OUT)
                .delay(0.05f)
                .start(Blend.tweenManager);

        backButton.setY(getHeight() * 1.1f);
        title.setY(getHeight() * 1.1f);

        levels.show();
        levels.addListener(new LevelsListener() {
            @Override
            public void onShown() {
                levels.removeListener(this);
                animated = false;
                update();
            }
        });
        animated = true;
    }

    public void hide(final String nextScene, final HideType hideType) {
        super.hide(nextScene);

        Blend.tweenManager.killTarget(backButton);
        Blend.tweenManager.killTarget(title);

        switch (hideType) {
            case BACK:
                Tween.to(title, ActorAccessor.Y, 0.3f)
                        .target(getHeight() * 1.1f)
                        .ease(Expo.IN)
                        .start(Blend.tweenManager);
                Tween.to(backButton, ActorAccessor.Y, 0.3f)
                        .target(getHeight() * 1.1f)
                        .ease(Expo.IN)
                        .delay(0.05f)
                        .start(Blend.tweenManager);

                levels.hide(getHeight() * 1.1f, 0.1f);
                break;
            case LEVEL_SELECTED:
                levels.hide(model.getSelectedLevel());
                break;
        }

        levels.addListener(new LevelsListener() {
            @Override
            public void onHidden() {
                levels.removeListener(this);
                animated = false;
                SceneManager.setScene(nextScene);
            }
        });

        animated = true;
    }

    @Override
    public void hide(String nextScene) {
        hide(nextScene, HideType.BACK);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw() {
        super.draw();
    }

    private float getScreenOffset() {
        return 30f * scale;
    }

    enum HideType {
        BACK,
        LEVEL_SELECTED
    }
}
