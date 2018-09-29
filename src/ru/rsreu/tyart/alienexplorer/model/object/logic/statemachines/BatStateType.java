package ru.rsreu.tyart.alienexplorer.model.object.logic.statemachines;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum BatStateType {
    FLY {
        @Override
        public List<Integer> getStates() {
            return new ArrayList<Integer>(Arrays.asList(0, 1));
        }
    };

    public abstract List<Integer> getStates();
}
