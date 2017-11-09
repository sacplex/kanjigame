package com.tgd.kanjigame.network.object;

/**
 * Created by sacplex on 7/11/2017.
 */

import java.util.ArrayList;

public class InitialCardHolderNetworkObject extends NetworkObject
{
    ArrayList<InitialCardNetworkObject> initialCards;

    public InitialCardHolderNetworkObject()
    {
        initialCards = new ArrayList<>();
    }

    public void add(int strokes, String colour, String kanji, String kunyomi, String onyomi, String english, int index)
    {
        InitialCardNetworkObject initialCardNetworkObject = new InitialCardNetworkObject(strokes,
                colour, kanji, kunyomi, onyomi, english, index);

        initialCards.add(initialCardNetworkObject);
    }

    public InitialCardNetworkObject get()
    {
        return initialCards.remove(0);
    }
}

