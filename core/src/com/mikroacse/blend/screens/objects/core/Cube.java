package com.mikroacse.blend.screens.objects.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mikroacse.blend.media.Assets;
import com.mikroacse.blend.screens.SceneManager;
import com.mikroacse.engine.actors.Group;

/**
 * Created by MikroAcse on 11.07.2016.
 */
public class Cube extends Group {
    private Cell cell;
    private Image outline;

    public Cube() {
        super();
        init();
    }

    private void init() {
        cell = new Cell();
        outline = new Image(Assets.getTexture(SceneManager.LEVEL, "cube-outline"));

        outline.setX((int) (cell.getWidth() - (int) outline.getWidth()) / 2);
        outline.setY((int) (cell.getHeight() - outline.getHeight()) / 2);

        this.addActor(outline);
        this.addActor(cell);
    }

    public void dispose() {
        this.removeActor(cell);
        this.removeActor(outline);

        cell = null;
        outline = null;
    }

    @Override
    public Color getColor() {
        return cell.getColor();
    }

    @Override
    public void setColor(Color color) {
        cell.setColor(color);
    }
}
