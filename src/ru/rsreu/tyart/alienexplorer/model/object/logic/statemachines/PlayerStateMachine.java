package ru.rsreu.tyart.alienexplorer.model.object.logic.statemachines;

import ru.rsreu.tyart.alienexplorer.model.object.GameObject;
import ru.rsreu.tyart.alienexplorer.model.object.PlayerObject;
import ru.rsreu.tyart.alienexplorer.model.object.logic.Space2D;
import ru.rsreu.tyart.alienexplorer.model.object.logic.Vector2D;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class PlayerStateMachine extends ObjectStateMachine<PlayerStateType> {
    private static final float SUBSTATE_PERIOD = 0.05f;
    private static final int HURT_PERIOD_MULTIPLIER = 10;

    public PlayerStateMachine() {
        Map<PlayerStateType, List<Integer>> objectStates = new Hashtable<PlayerStateType, List<Integer>>();

        for (PlayerStateType stateType : PlayerStateType.values()) {
            List<Integer> states = stateType.getStates();
            objectStates.put(stateType, states);
        }

        setObjectStates(objectStates);

        setMachineState(PlayerStateType.STAND);
        setTimeInState(0);
    }

    @Override
    public void changeState(GameObject player, Space2D freeSpace, Vector2D move, float deltaSeconds) {
        setTimeInState(getTimeInState() + deltaSeconds);
        PlayerStateType possibleState = findPossibleState((PlayerObject)player, freeSpace, move);
        if (getMachineState() == possibleState) {
            processInSameState(player);
        } else {
            processInNewState(player, possibleState);
        }
    }

    private PlayerStateType findPossibleState(PlayerObject player, Space2D freeSpace, Vector2D move) {
        if (freeSpace.getBottom() > EPSILON) {
            return PlayerStateType.JUMP;
        } else {
            if (player.getCollider().getHeight() > player.getHeightSmall()) {
                if (Math.abs(move.getX()) > EPSILON) {
                    return PlayerStateType.WALK;
                } else {
                    return PlayerStateType.STAND;
                }
            } else {
                return PlayerStateType.DUCK;
            }
        }
    }

    private void processInSameState(GameObject player) {
        if (getTimeInState() >= SUBSTATE_PERIOD) {
            int multiplier = (int)Math.floor(getTimeInState() / SUBSTATE_PERIOD);
            setTimeInState(getTimeInState() - SUBSTATE_PERIOD * multiplier);

            int oldSubState = player.getState();
            int oldSubStatePosition = getObjectStates().get(getMachineState()).indexOf(oldSubState);
            int newSubStatePosition = 0;
            if ((oldSubStatePosition >= 0) && (oldSubStatePosition < getObjectStates().get(getMachineState()).size() - 1)) {
                newSubStatePosition = oldSubStatePosition + 1;
            }
            int newSubState = getObjectStates().get(getMachineState()).get(newSubStatePosition);
            player.setState(newSubState);
        }
    }

    private void processInNewState(GameObject player, PlayerStateType state) {
        if ((getMachineState() != PlayerStateType.HURT)
                || (getTimeInState() >= SUBSTATE_PERIOD * HURT_PERIOD_MULTIPLIER)) {
            setTimeInState(0);
            setMachineState(state);
            player.setState(getObjectStates().get(state).get(0));
        } else {
            player.setState(getObjectStates().get(PlayerStateType.HURT).get(0));
        }
    }
}
