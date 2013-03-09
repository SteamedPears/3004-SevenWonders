package com.steamedpears.comp3004.models;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Changeable {

    protected List<ChangeListener> changeListeners;

    public Changeable(){
        changeListeners = new ArrayList<ChangeListener>();
    }

    public void addChangeListener(ChangeListener listener) {
        this.changeListeners.add(listener);
    }

    protected void announceChange(Object obj){
        ChangeEvent event = new ChangeEvent(obj);
        for(ChangeListener listener: changeListeners){
            listener.stateChanged(event);
        }
    }
}
