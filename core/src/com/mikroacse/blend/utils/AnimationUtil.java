package com.mikroacse.blend.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mikroacse.blend.config.Config;
import com.mikroacse.blend.media.Assets;

/**
 * Created by MikroAcse on 14.07.2016.
 */
public class AnimationUtil {
    public static Animation create(String scene, String name) {
        return create(Assets.getAtlas(scene, name));
    }

    public static Animation create(String scene, String name, boolean smoothing) {
        return create(Assets.getAtlas(scene, name), smoothing);
    }

    public static Animation create(TextureAtlas atlas) {
        return create(atlas, true);
    }

    public static Animation create(TextureAtlas atlas, boolean smoothing) {
        if (smoothing) {
            for (TextureRegion region : atlas.getRegions()) {
                region.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            }
        }
        return new Animation(Config.getAnimationFrameDuration(), atlas.getRegions());
    }
}
