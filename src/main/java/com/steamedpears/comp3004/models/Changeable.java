package com.steamedpears.comp3004.models;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Changeable {

    private List<ChangeListener> changeListeners;

    public Changeable(){
        changeListeners = new ArrayList<ChangeListener>();
    }

    /**
     * Adds a change listener.
     * @param listener the change listener to be added
     */
    public void addChangeListener(ChangeListener listener) {
        this.changeListeners.add(listener);
    }

    /**
     * Announces a change to the added change listeners.
     * @param obj the object calling the change.
     */
    protected void announceChange(Object obj){
        ChangeEvent event = new ChangeEvent(obj);
        for(ChangeListener listener: changeListeners){
            listener.stateChanged(event);
        }
    }
}
