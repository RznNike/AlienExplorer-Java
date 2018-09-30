package ru.rsreu.tyart.alienexplorer.model.object.logic.statemachines;

import ru.rsreu.tyart.alienexplorer.model.object.GameObject;
import ru.rsreu.tyart.alienexplorer.model.object.logic.Space2D;
import ru.rsreu.tyart.alienexplorer.model.object.logic.Vector2D;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GhostStateMachine extends ObjectStateMachine<GhostStateType> {
    private static final float SUBSTATE_PERIOD = Float.MAX_VALUE; // нет анимации

    public GhostStateMachine() {
        Map<GhostStateType, List<Integer>> objectStates = new HashMap<GhostStateType, List<Integer>>();

        for (GhostStateType stateType : GhostStateType.values()) {
            List<Integer> states = stateType.getStates();
            objectStates.put(stateType, states);
        }

        setObjectStates(objectStates);

        setMachineState(GhostStateType.STAND);
        setTimeInState(0);
        setSubstatePeriod(SUBSTATE_PERIOD);
    }

    @Override
    public boolean changeState(GameObject enemy, Space2D freeSpace, Vector2D move, float deltaSeconds) {
        setTimeInState(getTimeInState() + deltaSeconds);
        GhostStateType possibleState = findPossibleState(move);
        if (getMachineState() == possibleState) {
            return processInSameState(enemy);
        } else {
            processInNewState(enemy, possibleState);
            return true;
        }
    }

    private GhostStateType findPossibleState(Vector2D move) {
        if (Math.abs(move.getX()) > EPSILON) {
            return GhostStateType.ATTACK;
        } else {
            return GhostStateType.STAND;
        }
    }

    private void processInNewState(GameObject enemy, GhostStateType state) {
        setTimeInState(0);
        setMachineState(state);
        enemy.setState(getObjectStates().get(state).get(0));
    }
}
