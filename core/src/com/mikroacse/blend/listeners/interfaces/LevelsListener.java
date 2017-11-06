package com.mikroacse.blend.listeners.interfaces;

import com.mikroacse.engine.listeners.core.Listener;

/**
 * Created by MikroAcse on 24.07.2016.
 */
public interface LevelsListener extends Listener {
    void onHidden();

    void onShown();

    void onLevelSelected(int level);
}
