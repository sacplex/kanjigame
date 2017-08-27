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
        cardNetworkObject.setColour(card.getColour());
        cardNetworkObject.setKanji(card.getKanji());
        cardNetworkObject.setKunyomi(card.getKunyomi());
        cardNetworkObject.setOnyomi(card.getOnyomi());
        cardNetworkObject.setEnglsih(card.getEnglish());
        cardNetworkObject.setCardIndex(card.getCardIndex());

        cards.add(cardNetworkObject);
    }

    public ArrayList<CardNetworkObject> getCards()
    {
        return cards;
    }
}
