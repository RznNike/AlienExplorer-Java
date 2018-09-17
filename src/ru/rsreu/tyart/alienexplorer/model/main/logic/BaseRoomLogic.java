package ru.rsreu.tyart.alienexplorer.model.main.logic;

import ru.rsreu.tyart.alienexplorer.model.main.GameRoom;
import ru.rsreu.tyart.alienexplorer.model.main.logic.statemachines.ModelStateMachine;

public abstract class BaseRoomLogic {
    private GameRoom _room;
    private ModelStateMachine _stateMachine;
    private int _selectedMenuItem;
    private String _menuHeader;

    public abstract void receiveCommand(ModelCommand command, boolean isThisACommandStart);

    protected abstract void handleCommand(ModelCommand command);

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
        return _menuHeader;
    }

    public void setMenuHeader(String value) {
        _menuHeader = value;
    }
}
