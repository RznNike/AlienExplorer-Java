package ru.rsreu.tyart.alienexplorer.model.main.logic;

import ru.rsreu.tyart.alienexplorer.controller.ControllerCommand;
import ru.rsreu.tyart.alienexplorer.model.main.GameRoom;
import ru.rsreu.tyart.alienexplorer.model.main.logic.statemachines.MainMenuStateMachine;

public class MenuLogic extends BaseRoomLogic {
    public MenuLogic(GameRoom room) {
        room.getLogicBusySemaphore().drainPermits();
        setRoom(room);
        setStateMachine(new MainMenuStateMachine(room));
    }

    @Override
    protected void handleCommand(ControllerCommand command) {
        getStateMachine().changeState(command);
        switch (getStateMachine().getCurrentCommand()) {
            case LOAD_MENU:
            case LOAD_LEVEL:
            case EXIT:
                getRoom().getLogicBusySemaphore().release();
                break;
        }
    }
}
