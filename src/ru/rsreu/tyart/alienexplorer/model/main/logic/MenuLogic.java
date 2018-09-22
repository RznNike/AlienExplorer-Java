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
        switch (getStateMachine().getCurrentCommand())
        {
            case LOAD_MENU:
                getRoom().getLogicBusySemaphore().release();
//                LoadAnotherModel?.Invoke(GameModelType.Menu);
                break;
            case LOAD_SELECTED_LEVEL:
                getRoom().getLogicBusySemaphore().release();
//                LoadAnotherModel?.Invoke(GameModelType.Level, _stateMachine.SelectedMenuItem);
                break;
            case LOAD_FIRST_LEVEL:
                getRoom().getLogicBusySemaphore().release();
//                int firstLevelID = LevelLoader.CheckAvailableLevels().Min();
//                LoadAnotherModel?.Invoke(GameModelType.Level, firstLevelID);
                break;
            case EXIT:
                getRoom().getLogicBusySemaphore().release();
//                CloseApplication?.Invoke();
                break;
        }
    }
}
