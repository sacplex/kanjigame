package com.tgd.kanjigame.network.object;

public class InitialCardNetworkObject extends NetworkObject
{
    private String kanji_character;
    private String onyomi;
    private String kunyomi;
    //private String kana;
    private String colour;
    private int stroke;
    private String english;

    public InitialCardNetworkObject(int strokes, String colour, String kanji, String kunyomi, String onyomi, String english, int index)
    {
        this.kanji_character = kanji;
        this.onyomi = onyomi;
        this.kunyomi = kunyomi;
        //this.kana = card.;
        this.colour = colour;
        this.stroke = strokes;
        this.english = english;
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
