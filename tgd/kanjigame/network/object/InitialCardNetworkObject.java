package com.tgd.kanjigame.network.object;

import com.tgd.kanjigame.card.Card;

public class InitialCardNetworkObject extends NetworkObject
{
    private String kanji_character;
    private String onyomi;
    private String kunyomi;
    //private String kana;
    private String colour;
    private int stroke;
    private String english;

    public InitialCardNetworkObject(Card card)
    {
        this.kanji_character = card.getKanji();
        this.onyomi = card.getOnyomi();
        this.kunyomi = card.getKunyomi();
        //this.kana = card.;
        this.colour = card.getColour();
        this.stroke = card.getStrokesValue();
        this.english = card.getEnglish();
    }

    public String getKanji_character()
    {
        return kanji_character;
    }

    public String getOnyomi()
    {
        return kunyomi;
    }

    public String getKunyomi()
    {
        return kunyomi;
    }

    /*public String getKana()
    {
        return kana;
    }*/

    public String getColour()
    {
        return colour;
    }

    public int getStroke()
    {
        return stroke;
    }

    public String getEnglish()
    {
        return english;
    }
}
