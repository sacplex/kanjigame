package com.tgd.kanjigame.gamerules;

import com.tgd.kanjigame.card.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PlayValidator
{
    public enum RULE_SET {SINGLE, DOUBLE, TRIPLE, QUADS, PENS, HEXS, SEPS, SEASON, STRAIGHT_3_CARDS,
        STRAIGHT_4_CARDS, STRAIGHT_5_CARDS, STRAIGHT_6_CARDS, STRAIGHT_7_CARDS, STRAIGHT_8_CARDS,
        STRAIGHT_9_CARDS, STRAIGHT_10_CARDS, STRAIGHT_11_CARDS, STRAIGHT_12_CARDS, ERROR}

    private RULE_SET ruleSet;
    private ArrayList<Card> cards;

    public PlayValidator()
    {
        cards = new ArrayList<>();
        ruleSet = RULE_SET.ERROR;
    }

    public void add(Card card)
    {
        cards.add(card);
    }

    public void validate(RULE_SET ruleSet)
    {
        if(this.cards.size() == 1) // Single Card
            this.ruleSet = RULE_SET.SINGLE;
        else if(validateMultiCard() && this.cards.size() == 2) // Double Cards
            this.ruleSet = RULE_SET.DOUBLE;
        else if(validateMultiCard() && this.cards.size() == 3) // TRIPLE Cards
            this.ruleSet = RULE_SET.TRIPLE;
        else if(validateMultiCard() && this.cards.size() == 4) // QUADS Cards
            this.ruleSet = RULE_SET.QUADS;
        else if(validateMultiCard() && this.cards.size() == 5) // PENS Cards
            this.ruleSet = RULE_SET.PENS;
        else if(validateMultiCard() && this.cards.size() == 6) // HEXS Cards
            this.ruleSet = RULE_SET.HEXS;
        else if(validateMultiCard() && this.cards.size() == 7) // SEPS Cards
            this.ruleSet = RULE_SET.SEPS;
        else if(validateStraight() && this.cards.size() == 3)
            this.ruleSet = RULE_SET.STRAIGHT_3_CARDS;
        else if(validateStraight() && this.cards.size() == 4)
            this.ruleSet = RULE_SET.STRAIGHT_4_CARDS;
        else if(validateStraight() && this.cards.size() == 5)
            this.ruleSet = RULE_SET.STRAIGHT_5_CARDS;
        else if(validateStraight() && this.cards.size() == 6)
            this.ruleSet = RULE_SET.STRAIGHT_6_CARDS;
        else if(validateStraight() && this.cards.size() == 7)
            this.ruleSet = RULE_SET.STRAIGHT_7_CARDS;
        else if(validateStraight() && this.cards.size() == 8)
            this.ruleSet = RULE_SET.STRAIGHT_8_CARDS;
        else if(validateStraight() && this.cards.size() == 9)
            this.ruleSet = RULE_SET.STRAIGHT_9_CARDS;
        else if(validateStraight() && this.cards.size() == 10)
            this.ruleSet = RULE_SET.STRAIGHT_10_CARDS;
        else if(validateStraight() && this.cards.size() == 11)
            this.ruleSet = RULE_SET.STRAIGHT_11_CARDS;
        else if(validateStraight() && this.cards.size() == 12)
            this.ruleSet = RULE_SET.STRAIGHT_12_CARDS;

        if(ruleSet != null && ruleSet != RULE_SET.ERROR && this.ruleSet != RULE_SET.ERROR)
        {
            if(this.ruleSet == ruleSet)
            {
                System.out.println("Both Players have the same rule set: " + ruleSet);
            }
            else
            {
                System.out.println("Error, players have different rule sets");
                System.out.println(ruleSet);
                System.out.println(this.ruleSet);
            }
        }
    }

    private boolean validateMultiCard()
    {
        boolean retValidMutliCard = false;
        double strokeAvg = cards.get(0).getStrokesValue();

        for(int i=1; i<cards.size(); i++)
            strokeAvg = strokeAvg + cards.get(i).getStrokesValue();

        strokeAvg = strokeAvg / cards.size();

        if(strokeAvg == cards.get(0).getStrokesValue())
            retValidMutliCard = true;

        return retValidMutliCard;
    }

    private boolean validateStraight()
    {
        boolean validStraight = true;

        for(int i=1; i<cards.size(); i++)
        {
            if(cards.get(i-1).getStrokesValue() != cards.get(i).getStrokesValue()-1)
            {
                validStraight = false;
                break;
            }
        }

        return validStraight;
    }

    public RULE_SET getRuleSet()
    {
        return ruleSet;
    }
}
