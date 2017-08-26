package com.tgd.kanjigame.network.object;

public class CardNetworkObject extends NetworkObject
{
    private int strokeNumber;
    private String colour;
    private String kanji;
    private String kunyomi;
    private String onyomi;
    private String englsih;

    public void setStrokeNumber(int strokeNumber) { this.strokeNumber = strokeNumber; }

    public int getStrokeNumber() { return strokeNumber; }

    public void setColour(String colour) { this.colour = colour; }

    public String getColour() { return colour; }

    public void setKanji(String kanji) { this.kanji = kanji; }

    public String getKanji() { return kanji; }

    public void setKunyomi(String kunyomi) { this.kunyomi = kunyomi; }

    public String getKunyomi() { return kunyomi; }

    public void setOnyomi(String onyomi) { this.onyomi = onyomi; }

    public String getOnyomi() { return onyomi; }

    public void setEnglsih(String englsih) { this.englsih = englsih; }

    public String getEnglsih() { return englsih; }
}
