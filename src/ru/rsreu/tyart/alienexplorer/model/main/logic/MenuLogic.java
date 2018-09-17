package ru.rsreu.tyart.alienexplorer.model.main.logic;


import ru.rsreu.tyart.alienexplorer.model.main.GameRoom;

public class MenuLogic extends BaseRoomLogic {
    public MenuLogic(GameRoom room) {
        setRoom(room);
    }

    @Override
    public void receiveCommand(ModelCommand command, boolean isThisACommandStart) {
        // TODO command handling
        handleCommand(command);
    }

    @Override
    protected void handleCommand(ModelCommand command) {

    }
}
