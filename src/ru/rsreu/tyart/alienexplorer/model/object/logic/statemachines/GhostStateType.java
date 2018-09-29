package ru.rsreu.tyart.alienexplorer.model.object.logic.statemachines;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum GhostStateType {
    STAND {
        @Override
        public List<Integer> getStates() {
            return new ArrayList<Integer>(Arrays.asList(0));
        }
    },
    ATTACK {
        @Override
        public List<Integer> getStates() {
            return new ArrayList<Integer>(Arrays.asList(1));
        }
    };

    public abstract List<Integer> getStates();
}
