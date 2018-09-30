package ru.rsreu.tyart.alienexplorer.model.main.logic;

import ru.rsreu.tyart.alienexplorer.controller.ControllerCommand;
import ru.rsreu.tyart.alienexplorer.model.main.GameRoom;
import ru.rsreu.tyart.alienexplorer.model.main.logic.statemachines.ModelStateMachine;

public abstract class BaseRoomLogic {
    private GameRoom _room;
    private ModelStateMachine _stateMachine;

    public void receiveCommand(ControllerCommand command, boolean isThisACommandStart) {
        if (isThisACommandStart) {
            handleCommand(command);
        }
    }

    protected abstract void handleCommand(ControllerCommand command);

    public GameRoom getRoom() {
        return _room;
    }

    void setRoom(GameRoom value) {
        _room = value;
    }

    public ModelStateMachine getStateMachine() {
        return _stateMachine;
    }

    void setStateMachine(ModelStateMachine value) {
        _stateMachine = value;
    }

    public String getMenuHeader() {
        return _stateMachine.getMenuHeader();
    }
}
