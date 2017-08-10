package com.tgd.kanjigame.card;

public class NCard
{
    private String kanjiCharacter;
    private String onyomiCharacter;
    private String kunyomiCharacter;
    private String nLevel;

    public NCard(String kanji, String onyomi, String kunyomi, String nLevel)
    {
        this.kanjiCharacter = kanji;
        this.onyomiCharacter = onyomi;
        this.kunyomiCharacter = kunyomi;
        this.nLevel = nLevel;
    }

    public String getKanjiCharacter()
    {
        return kanjiCharacter;
    }

    public String getOnyomi()
    {
        return onyomiCharacter;
    }

    public String getKunyomi()
    {
        return kunyomiCharacter;
    }

    public String getNLevel()
    {
        return nLevel;
    }

    public String toString()
    {
        return "Kanji Character: " + kanjiCharacter + ", Onyomi: " + onyomiCharacter + ", Kunyomi: " + kunyomiCharacter + ", nLevel: " + nLevel;
    }
}
