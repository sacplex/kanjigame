package com.tgd.kanjigame.gamerules;

import com.tgd.kanjigame.card.Card;

public class Validator
{
    private PrePlayValidator prePlayValidator;

    public Validator()
    {
        prePlayValidator = new PrePlayValidator();
    }

    public void add(Card card)
    {
        prePlayValidator.add(card);
    }

    public void validate()
    {
        prePlayValidator.validate();
    }

    public PrePlayValidator.RULE_SET getIntendedRuleSet()
    {
        return prePlayValidator.getRuleSet();
    }
}
