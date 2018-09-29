package ru.rsreu.tyart.alienexplorer.model.object.logic.statemachines;

import ru.rsreu.tyart.alienexplorer.model.object.GameObject;
import ru.rsreu.tyart.alienexplorer.model.object.logic.Space2D;
import ru.rsreu.tyart.alienexplorer.model.object.logic.Vector2D;

import java.util.List;
import java.util.Map;

public abstract class ObjectStateMachine<MachineStateEnum> {
    protected static final float EPSILON = 0.01f;

    private MachineStateEnum _machineState;
    private Map<MachineStateEnum, List<Integer>> _objectStates;
    private float _timeInState;

    public abstract void changeState(GameObject gameObject, Space2D freeSpace, Vector2D move, float deltaSeconds);


    protected MachineStateEnum getMachineState() {
        return _machineState;
    }

    public void setMachineState(MachineStateEnum value) {
        _machineState = value;
    }

    protected Map<MachineStateEnum, List<Integer>> getObjectStates() {
        return _objectStates;
    }

    protected void setObjectStates(Map<MachineStateEnum, List<Integer>> value) {
        _objectStates = value;
    }

    protected float getTimeInState() {
        return _timeInState;
    }

    protected void setTimeInState(float value) {
        _timeInState = value;
    }
}
