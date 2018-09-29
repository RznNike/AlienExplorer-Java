package ru.rsreu.tyart.alienexplorer.model.object.logic.statemachines;

import ru.rsreu.tyart.alienexplorer.model.object.GameObject;
import ru.rsreu.tyart.alienexplorer.model.object.logic.Space2D;
import ru.rsreu.tyart.alienexplorer.model.object.logic.Vector2D;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class SlimeStateMachine extends ObjectStateMachine<SlimeStateType> {
    private static final float SUBSTATE_PERIOD = 0.35f;

    public SlimeStateMachine() {
        Map<SlimeStateType, List<Integer>> objectStates = new Hashtable<SlimeStateType, List<Integer>>();

        for (SlimeStateType stateType : SlimeStateType.values()) {
            List<Integer> states = stateType.getStates();
            objectStates.put(stateType, states);
        }

        setObjectStates(objectStates);

        setMachineState(SlimeStateType.STAND);
        setTimeInState(0);
        setSubstatePeriod(SUBSTATE_PERIOD);
    }

    @Override
    public boolean changeState(GameObject enemy, Space2D freeSpace, Vector2D move, float deltaSeconds) {
        setTimeInState(getTimeInState() + deltaSeconds);
        SlimeStateType possibleState = findPossibleState(freeSpace, move);
        if (getMachineState() == possibleState) {
            return processInSameState(enemy);
        } else {
            processInNewState(enemy, possibleState);
            return true;
        }
    }

    private SlimeStateType findPossibleState(Space2D freeSpace, Vector2D move) {
        if (freeSpace.getBottom() > EPSILON) {
            return SlimeStateType.STAND;
        } else {
            if (Math.abs(move.getX()) > EPSILON) {
                return SlimeStateType.WALK;
            } else {
                return SlimeStateType.STAND;
            }
        }
    }

    private void processInNewState(GameObject enemy, SlimeStateType state) {
        setTimeInState(0);
        setMachineState(state);
        enemy.setState(getObjectStates().get(state).get(0));
    }
}
