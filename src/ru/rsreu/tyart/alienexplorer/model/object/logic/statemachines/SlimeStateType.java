package ru.rsreu.tyart.alienexplorer.model.object.logic.statemachines;

public enum  SlimeStateType {
    STAND {
        @Override
        public int[] getStates() {
            return new int[] {0};
        }
    },
    WALK {
        @Override
        public int[] getStates() {
            return new int[] {0, 1};
        }
    };

    public abstract int[] getStates();
}
