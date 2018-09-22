package ru.rsreu.tyart.alienexplorer.model.main.logic;

import ru.rsreu.tyart.alienexplorer.controller.ControllerCommand;
import ru.rsreu.tyart.alienexplorer.model.main.GameRoom;
import ru.rsreu.tyart.alienexplorer.model.main.logic.statemachines.ModelStateMachine;

public abstract class BaseRoomLogic {
    private GameRoom _room;
    private ModelStateMachine _stateMachine;
    private int _selectedMenuItem;

    public void receiveCommand(ControllerCommand command, boolean isThisACommandStart) {
        if (isThisACommandStart) {
            handleCommand(command);
        }
    }

    protected abstract void handleCommand(ControllerCommand command);

    public GameRoom getRoom() {
        return _room;
    }

    public void setRoom(GameRoom value) {
        _room = value;
    }

    public ModelStateMachine getStateMachine() {
        return _stateMachine;
    }

    public void setStateMachine(ModelStateMachine value) {
        _stateMachine = value;
    }

    public int getSelectedMenuItem() {
        return _selectedMenuItem;
    }

    public void setSelectedMenuItem(int value) {
        _selectedMenuItem = value;
    }

    public String getMenuHeader() {
        return _stateMachine.getMenuHeader();
    }
}
