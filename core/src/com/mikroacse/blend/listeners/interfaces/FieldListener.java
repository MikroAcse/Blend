package com.mikroacse.blend.listeners.interfaces;

import com.mikroacse.engine.listeners.core.Listener;

/**
 * Created by MikroAcse on 12.07.2016.
 */
public interface FieldListener extends Listener {
    void onFinalCell();

    void onMove();
}
