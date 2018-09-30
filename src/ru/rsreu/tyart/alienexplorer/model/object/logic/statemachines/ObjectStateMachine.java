package ru.rsreu.tyart.alienexplorer.model.object.logic.statemachines;

import ru.rsreu.tyart.alienexplorer.model.object.GameObject;
import ru.rsreu.tyart.alienexplorer.model.object.logic.Space2D;
import ru.rsreu.tyart.alienexplorer.model.object.logic.Vector2D;

import java.util.List;
import java.util.Map;

public abstract class ObjectStateMachine<MachineStateEnum> {
    private MachineStateEnum _machineState;
    private Map<MachineStateEnum, List<Integer>> _objectStates;
    private float _timeInState;
    private float _substatePeriod;

    public abstract boolean changeState(GameObject gameObject, Space2D freeSpace, Vector2D move, float deltaSeconds);

    boolean processInSameState(GameObject object) {
        if (_timeInState >= _substatePeriod) {
            int multiplier = (int)(_timeInState / _substatePeriod);
            _timeInState -= _substatePeriod * multiplier;

            int oldSubState = object.getState();
            int oldSubStatePosition = _objectStates.get(_machineState).indexOf(oldSubState);
            int newSubStatePosition = 0;
            if ((oldSubStatePosition >= 0) && (oldSubStatePosition < _objectStates.get(_machineState).size() - 1)) {
                newSubStatePosition = oldSubStatePosition + 1;
            }
            int newSubState = _objectStates.get(_machineState).get(newSubStatePosition);
            object.setState(newSubState);
            return oldSubState != newSubState;
        }
        return false;
    }

    MachineStateEnum getMachineState() {
        return _machineState;
    }

    public void setMachineState(MachineStateEnum value) {
        _machineState = value;
    }

    Map<MachineStateEnum, List<Integer>> getObjectStates() {
        return _objectStates;
    }

    void setObjectStates(Map<MachineStateEnum, List<Integer>> value) {
        _objectStates = value;
    }

    float getTimeInState() {
        return _timeInState;
    }

    void setTimeInState(float value) {
        _timeInState = value;
    }

    void setSubstatePeriod(float value) {
        _substatePeriod = value;
    }
}
