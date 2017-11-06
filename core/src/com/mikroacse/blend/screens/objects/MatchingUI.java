package com.mikroacse.blend.screens.objects;

import com.badlogic.gdx.graphics.Color;
import com.mikroacse.blend.config.Lang;
import com.mikroacse.blend.screens.SceneManager;
import com.mikroacse.blend.screens.objects.core.MatchingBlock;
import com.mikroacse.engine.actors.Group;
import com.mikroacse.engine.utils.ColorUtil;

/**
 * Created by MikroAcse on 11.07.2016.
 */
public class MatchingUI extends Group {
    private MatchingBlock currentBlock;
    private MatchingBlock matchBlock;
    private MatchingBlock finalBlock;

    public MatchingUI() {
        super();
        init();
    }

    private void init() {
        currentBlock = new MatchingBlock();
        matchBlock = new MatchingBlock();
        finalBlock = new MatchingBlock();

        currentBlock.setText(Lang.get(SceneManager.LEVEL, "yourColor"));
        finalBlock.setText(Lang.get(SceneManager.LEVEL, "finalColor"));

        this.addActor(currentBlock);
        this.addActor(matchBlock);
        this.addActor(finalBlock);

        matchBlock.setX(currentBlock.getWidth());
        finalBlock.setX(matchBlock.getX() + matchBlock.getWidth());
    }

    private void update() {
        matchBlock.setColor(ColorUtil.getAverage(currentBlock.getColor(), finalBlock.getColor()), true);

        float difference = ColorUtil.getDifference(currentBlock.getColor(), finalBlock.getColor());
        int percent = 100 - (int) (100 * difference / 3);

        setMatch(percent);
    }

    public void setCurrentColor(Color color, boolean animated) {
        currentBlock.setColor(color, animated);
        update();
    }

    public void setFinalColor(Color color) {
        finalBlock.setColor(color);
        update();
    }

    public void setMatch(int percent) {
        matchBlock.setText(percent + "%");
    }

    @Override
    public float getRealWidth() {
        return (currentBlock.getWidth() + matchBlock.getWidth() + finalBlock.getWidth()) * getScaleX();
    }

    @Override
    public float getRealHeight() {
        return currentBlock.getHeight() * getScaleY();
    }
}
