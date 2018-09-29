package ru.rsreu.tyart.alienexplorer.model.object.logic.statemachines;

import ru.rsreu.tyart.alienexplorer.model.object.GameObject;
import ru.rsreu.tyart.alienexplorer.model.object.logic.Space2D;
import ru.rsreu.tyart.alienexplorer.model.object.logic.Vector2D;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class BatStateMachine extends ObjectStateMachine<BatStateType> {
    private static final float SUBSTATE_PERIOD = 0.15f;

    public BatStateMachine() {
        Map<BatStateType, List<Integer>> objectStates = new Hashtable<BatStateType, List<Integer>>();

        for (BatStateType stateType : BatStateType.values()) {
            List<Integer> states = stateType.getStates();
            objectStates.put(stateType, states);
        }

        setObjectStates(objectStates);

        setMachineState(BatStateType.FLY);
        setTimeInState(0);
        setSubstatePeriod(SUBSTATE_PERIOD);
    }


    @Override
    public boolean changeState(GameObject enemy, Space2D freeSpace, Vector2D move, float deltaSeconds) {
        setTimeInState(getTimeInState() + deltaSeconds);
        return processInSameState(enemy);
    }
}
