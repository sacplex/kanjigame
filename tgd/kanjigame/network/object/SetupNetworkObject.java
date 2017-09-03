package com.tgd.kanjigame.network.object;

public class SetupNetworkObject extends NetworkObject
{
    private InitialCardHolderNetworkObject initialCardHolderNetworkObject;
    private String position;

    public SetupNetworkObject(InitialCardHolderNetworkObject initialCardHolderNetworkObject, String position)
    {
        this.initialCardHolderNetworkObject = initialCardHolderNetworkObject;
        this.position = position;
    }

    public InitialCardHolderNetworkObject getInitialCardHolderNetworkObject()
    {
        return initialCardHolderNetworkObject;
    }

    public String getPosition()
    {
        return position;
    }
}
