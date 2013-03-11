package com.steamedpears.comp3004.views;

import com.steamedpears.comp3004.models.Wonder;

public abstract class WonderSelectionListener {

    /**
     * A method to handle the selection of a wonder.
     * @param wonder The wonder that was selected.
     */
    public abstract void handleSelection(Wonder wonder);
}
