package com.tgd.kanjigame.gamerules;

import com.tgd.kanjigame.card.Card;
import com.tgd.kanjigame.lobby.Session;

public class Validator
{
    private PlayValidator playValidator;
    private PlayValidator.RULE_SET rule_set;

    private Session session;

    public Validator()
    {
        playValidator = new PlayValidator();
    }

    public void add(Card card)
    {
        playValidator.add(card);
    }

    public void add(Session session) { this.session = session; }

    public void validate()
    {
        if(session.getTurn() == 1)
            playValidator.validate(null);
        else
            playValidator.validate(rule_set);

        rule_set = playValidator.getRuleSet();

        session.newTurn();
    }

    public PlayValidator.RULE_SET getIntendedRuleSet()
    {
        return playValidator.getRuleSet();
    }
}
