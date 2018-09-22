package ru.rsreu.tyart.alienexplorer.model.main.logic;

import ru.rsreu.tyart.alienexplorer.controller.ControllerCommand;
import ru.rsreu.tyart.alienexplorer.model.main.GameRoom;
import ru.rsreu.tyart.alienexplorer.model.main.logic.statemachines.MainMenuStateMachine;

public class MenuLogic extends BaseRoomLogic {
    public MenuLogic(GameRoom room) {
        setRoom(room);
        setStateMachine(new MainMenuStateMachine(room));
    }

    @Override
    protected void handleCommand(ControllerCommand command) {
        getStateMachine().changeState(command);
//        switch (getStateMachine().getCurrentCommand())
//        {
//            case LOAD_MENU:
//                LoadAnotherModel?.Invoke(GameModelType.Menu);
//                break;
//            case LOAD_SELECTED_LEVEL:
//                LoadAnotherModel?.Invoke(GameModelType.Level, _stateMachine.SelectedMenuItem);
//                break;
//            case LOAD_FIRST_LEVEL:
//                int firstLevelID = LevelLoader.CheckAvailableLevels().Min();
//                LoadAnotherModel?.Invoke(GameModelType.Level, firstLevelID);
//                break;
//            case EXIT:
//                CloseApplication?.Invoke();
//                break;
//        }
    }
}
