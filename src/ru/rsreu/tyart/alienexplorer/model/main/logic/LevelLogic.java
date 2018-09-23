package ru.rsreu.tyart.alienexplorer.model.main.logic;

import ru.rsreu.tyart.alienexplorer.controller.ControllerCommand;
import ru.rsreu.tyart.alienexplorer.model.main.GameRoom;
import ru.rsreu.tyart.alienexplorer.model.main.logic.statemachines.LevelMenuStateMachine;

public class LevelLogic extends BaseRoomLogic {
    public LevelLogic(GameRoom room) {
        room.getLogicBusySemaphore().drainPermits();
        setRoom(room);
        setStateMachine(new LevelMenuStateMachine(room));
    }

    @Override
    protected void handleCommand(ControllerCommand command) {
//        getStateMachine().changeState(command);
//        switch (getStateMachine().getCurrentCommand()) {
//            case LOAD_MENU:
//            case LOAD_SELECTED_LEVEL:
//            case LOAD_FIRST_LEVEL:
//            case EXIT:
//                getRoom().getLogicBusySemaphore().release();
//                break;
//        }
    }
}
