package com.tgd.kanjigame.network.object;

import com.tgd.kanjigame.card.Card;

import java.util.ArrayList;

public class CardHolderNetworkObject extends NetworkObject
{
    private ArrayList<CardNetworkObject> cards;

    public CardHolderNetworkObject()
    {
        cards = new ArrayList<>();
    }

    public void add(Card card)
    {
        CardNetworkObject cardNetworkObject = new CardNetworkObject();
        cardNetworkObject.setStrokeNumber(card.getStrokesValue());

        cards.add(cardNetworkObject);
    }

    public ArrayList<CardNetworkObject> getCards()
    {
        return cards;
    }
}
