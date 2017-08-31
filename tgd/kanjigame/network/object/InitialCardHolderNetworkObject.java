package com.tgd.kanjigame.network.object;

import com.tgd.kanjigame.card.Card;

import java.util.ArrayList;

public class InitialCardHolderNetworkObject extends NetworkObject
{
    ArrayList<InitialCardNetworkObject> initialCards;

    public InitialCardHolderNetworkObject()
    {
        initialCards = new ArrayList<>();
    }

    public void add(Card card)
    {
        InitialCardNetworkObject initialCardNetworkObject = new InitialCardNetworkObject(card);

        initialCards.add(initialCardNetworkObject);
    }

    public InitialCardNetworkObject get()
    {
        return initialCards.remove(0);
    }
}
