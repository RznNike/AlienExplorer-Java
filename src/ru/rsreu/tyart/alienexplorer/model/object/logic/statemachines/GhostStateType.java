package ru.rsreu.tyart.alienexplorer.model.object.logic.statemachines;

public enum  GhostStateType {
    STAND {
        @Override
        public int[] getStates() {
            return new int[] {0};
        }
    },
    ATTACK {
        @Override
        public int[] getStates() {
            return new int[] {1};
        }
    };

    public abstract int[] getStates();
}
