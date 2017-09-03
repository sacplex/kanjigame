package com.tgd.kanjigame.network.object;

public class SetupNetworkObject extends NetworkObject
{
    public enum GAME_STATE {WAIT, PLAY}
    private GAME_STATE state;
    private InitialCardHolderNetworkObject initialCardHolderNetworkObject;
    private String position;

    public SetupNetworkObject(InitialCardHolderNetworkObject initialCardHolderNetworkObject, String position, GAME_STATE state)
    {
        this.initialCardHolderNetworkObject = initialCardHolderNetworkObject;
        this.position = position;
        this.state = state;
    }

    public InitialCardHolderNetworkObject getInitialCardHolderNetworkObject()
    {
        return initialCardHolderNetworkObject;
    }

    public String getPosition()
    {
        return position;
    }

    public GAME_STATE getPlayState()
    {
        return state;
    }
}
