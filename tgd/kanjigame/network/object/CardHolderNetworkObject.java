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

    //public void add(Card card)
    public void add(int strokes, String colour, String kanji, String kunyomi, String onyomi, String english, int index)
    {
        CardNetworkObject cardNetworkObject = new CardNetworkObject();
        cardNetworkObject.setStrokeNumber(strokes);
        cardNetworkObject.setColour(colour);
        cardNetworkObject.setKanji(kanji);
        cardNetworkObject.setKunyomi(kunyomi);
        cardNetworkObject.setOnyomi(onyomi);
        cardNetworkObject.setEnglsih(english);
        cardNetworkObject.setCardIndex(index);

        cards.add(cardNetworkObject);
    }

    public int size()
    {
        return cards != null ? cards.size() : 0;
    }

    public ArrayList<CardNetworkObject> getCards()
    {
        return cards;
    }
}
