package com.mikroacse.blend.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mikroacse.blend.media.Assets;
import com.mikroacse.blend.screens.SceneManager;
import com.mikroacse.engine.actors.Group;
import com.mikroacse.engine.utils.TextureUtil;

/**
 * Created by MikroAcse on 12.07.2016.
 */
public class CornerTextButton extends Group {
    public static final String ALIGN_LEFT = "alignLeft";
    public static final String ALIGN_RIGHT = "alignRight";

    private TextActor bigText;
    private TextActor smallText;
    private Image hitbox;
    private String align;

    public CornerTextButton() {
        super();
        init();
    }

    private void init() {
        bigText = new TextActor(Assets.getGlobalFont(SceneManager.GLOBAL, "corner-button-big"));
        smallText = new TextActor(Assets.getGlobalFont(SceneManager.GLOBAL, "corner-button-small"));
        this.addActor(bigText);
        this.addActor(smallText);

        hitbox = new Image(TextureUtil.create(10, 10)); // just random numbers >0
        hitbox.getColor().a = 0f;
        this.addActor(hitbox);

        align = ALIGN_LEFT;
    }

    private void update() {
        smallText.setY(0);
        bigText.setY(smallText.getY() + smallText.getHeight() + getTextOffset());

        switch (align) {
            case ALIGN_LEFT:
                if (bigText.getWidth() > smallText.getWidth()) {
                    bigText.setX(0);
                    smallText.setX(bigText.getX());
                } else {
                    smallText.setX(0);
                    bigText.setX(smallText.getX());
                }
                break;
            case ALIGN_RIGHT:
                if (bigText.getWidth() > smallText.getWidth()) {
                    bigText.setX(0);
                    smallText.setX(bigText.getX() + bigText.getWidth() - smallText.getWidth());
                } else {
                    smallText.setX(0);
                    bigText.setX(smallText.getX() + smallText.getWidth() - bigText.getWidth());
                }
                break;
        }

        // TODO: magic numbers
        hitbox.setWidth(Math.max(bigText.getWidth(), smallText.getWidth()) + 10);
        hitbox.setHeight(bigText.getY() + bigText.getHeight() + 10);
        hitbox.setPosition(-5, -5);
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String value) {
        align = value;
        update();
    }

    public boolean setBigText(String value, boolean update) {
        if (bigText.getText().equals(value)) {
            return false;
        }
        bigText.setText(value);
        if (update) this.update();
        return true;
    }

    public boolean setBigText(String value) {
        return setBigText(value, true);
    }

    public String getBigText() {
        return bigText.getText().toString();
    }

    public void setColor(Color color) {
        bigText.setColor(color);
        smallText.setColor(color);
    }

    public Color getBigTextColor() {
        return bigText.getColor();
    }

    public void setBigTextColor(Color color) {
        bigText.setColor(color);
    }

    public Color getSmallTextColor() {
        return smallText.getColor();
    }

    public void setSmallTextColor(Color color) {
        smallText.setColor(color);
    }

    public boolean setSmallText(String value, boolean update) {
        if (smallText.getText().equals(value)) {
            return false;
        }
        smallText.setText(value);
        if (update) this.update();
        return true;
    }

    public boolean setSmallText(String value) {
        return setSmallText(value, true);
    }

    public String getSmallText() {
        return smallText.getText().toString();
    }

    private float getTextOffset() {
        return 15f;
    }
}
