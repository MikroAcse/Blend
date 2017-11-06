package com.mikroacse.blend.screens.models;

import com.mikroacse.engine.screens.Model;

/**
 * Created by MikroAcse on 26.07.2016.
 */
public class ChooseLevelModel extends Model {
    private int selectedLevel;

    public ChooseLevelModel() {
        super();
    }

    @Override
    protected void init() {
        super.init();
        selectedLevel = 0;
    }

    public int getSelectedLevel() {
        return selectedLevel;
    }

    public void setSelectedLevel(int selectedLevel) {
        this.selectedLevel = selectedLevel;
    }
}
