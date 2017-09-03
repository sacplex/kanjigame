package com.tgd.kanjigame.players;

import com.tgd.kanjigame.network.object.InitialCardHolderNetworkObject;

public class Content
{
    private final static String [] POSITIONS = {"First", "Second", "Third", "Fourth"};

    private InitialCardHolderNetworkObject initialCardHolderNetworkObject;
    private int position;

    public Content(InitialCardHolderNetworkObject initialCardHolderNetworkObject)
    {
        addInitialCards(initialCardHolderNetworkObject);
    }

    public void addInitialCards(InitialCardHolderNetworkObject initialCardHolderNetworkObject)
    {
        this.initialCardHolderNetworkObject = initialCardHolderNetworkObject;
    }

    public InitialCardHolderNetworkObject getInitialCardHolderNetworkObject()
    {
        return initialCardHolderNetworkObject;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    public String getNextPosition(int capability)
    {
        if(capability - 1 == position)
            return POSITIONS[0];
        else
            return POSITIONS[position + 1];
    }

    public int getPosition()
    {
        return position;
    }

    public String getPlayerPosition()
    {
        return POSITIONS[position];
    }
}
