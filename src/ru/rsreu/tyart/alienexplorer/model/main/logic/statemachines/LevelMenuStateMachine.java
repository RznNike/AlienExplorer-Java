package ru.rsreu.tyart.alienexplorer.model.main.logic.statemachines;

import ru.rsreu.tyart.alienexplorer.controller.ControllerCommand;
import ru.rsreu.tyart.alienexplorer.model.main.GameRoom;

public class LevelMenuStateMachine extends ModelStateMachine {
    public LevelMenuStateMachine(GameRoom gameRoom) {
        super(gameRoom);
    }

    @Override
    public void changeState(ControllerCommand command) {

    }

    @Override
    protected void acceptAction() {

    }

    @Override
    protected void cancelAction() {

    }
}
