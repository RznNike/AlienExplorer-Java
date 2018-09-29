package ru.rsreu.tyart.alienexplorer.model.object.logic.statemachines;

public enum BatStateType {
    FLY {
        @Override
        public int[] getStates() {
            return new int[] {0, 1};
        }
    };

    public abstract int[] getStates();
}
