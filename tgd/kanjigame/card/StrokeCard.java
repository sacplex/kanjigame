package com.tgd.kanjigame.card;

public class StrokeCard
{
    private String kanjiCharacter;
    private String onyomiCharacter;
    private String kunyomiCharacter;
    private String kana;

    public StrokeCard(String kanji, String onyomi, String kunyomi, String kana)
    {
        this.kanjiCharacter = kanji;
        this.onyomiCharacter = onyomi;
        this.kunyomiCharacter = kunyomi;
        this.kana = kana;
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

    public String getExtraKana()
    {
        return kana;
    }

    public String toString()
    {
        return "Kanji Character: " + kanjiCharacter + ", Onyomi: " + onyomiCharacter + ", Kunyomi: " + kunyomiCharacter + ", Kana: " + kana;
    }
}
