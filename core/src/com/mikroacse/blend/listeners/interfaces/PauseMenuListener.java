package com.mikroacse.blend.listeners.interfaces;

import com.mikroacse.engine.listeners.core.Listener;

/**
 * Created by MikroAcse on 30.07.2016.
 */
public interface PauseMenuListener extends Listener {
    void onShown();

    void onHidden(int action);

    void onHiding(int action);
}
