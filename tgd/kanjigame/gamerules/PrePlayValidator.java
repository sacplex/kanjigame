package com.tgd.kanjigame.gamerules;

import com.tgd.kanjigame.card.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PrePlayValidator
{
    public enum RULE_SET {SINGLE, DOUBLE, TRIPLE, QUADS, PENS, HEXS, SEPS, SEASON, STRAIGHT, ERROR}
    private RULE_SET ruleSet;
    private ArrayList<Card> cards;

    public PrePlayValidator()
    {
        cards = new ArrayList<>();
        ruleSet = RULE_SET.ERROR;
    }

    public void add(Card card)
    {
        cards.add(card);
    }

    public RULE_SET getRuleSet()
    {
        return ruleSet;
    }

    public void validate()
    {
        if(this.cards.size() == 1) // Single Card
            ruleSet = RULE_SET.SINGLE;
        else if(this.cards.size() == 2)
            validateDouble();
    }

    private void validateDouble()
    {
        double strokeAvg = cards.get(0).getStrokesValue();

        for(int i=1; i<cards.size(); i++)
            strokeAvg = strokeAvg + cards.get(i).getStrokesValue();

        strokeAvg = strokeAvg / cards.size();

        if(strokeAvg == cards.get(0).getStrokesValue())
            ruleSet = RULE_SET.DOUBLE;
    }


}
