package ru.rsreu.tyart.alienexplorer.model.object.logic.statemachines;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum PlayerStateType {
    STAND {
        @Override
        public List<Integer> getStates() {
            return new ArrayList<Integer>(Arrays.asList(0));
        }
    },
    WALK {
        @Override
        public List<Integer> getStates() {
            return new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        }
    },
    JUMP {
        @Override
        public List<Integer> getStates() {
            return new ArrayList<Integer>(Arrays.asList(10));
        }
    },
    DUCK {
        @Override
        public List<Integer> getStates() {
            return new ArrayList<Integer>(Arrays.asList(11));
        }
    },
    HURT {
        @Override
        public List<Integer> getStates() {
            return new ArrayList<Integer>(Arrays.asList(12));
        }
    };

    public abstract List<Integer> getStates();
}
