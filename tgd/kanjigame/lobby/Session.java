package com.tgd.kanjigame.lobby;

import com.tgd.kanjigame.card.Card;
import com.tgd.kanjigame.network.object.InitialCardHolderNetworkObject;
import com.tgd.kanjigame.network.object.InitialCardNetworkObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Session
{
    public static final int NUMBER_OF_STARTING_CARDS = 12;

    private InitialCardHolderNetworkObject initialCardHolderNetworkObject;

    private ArrayList<String> players;

    private int capability;

    private boolean isEmpty;
    private boolean isFull;

    private int turn;

    public Session(int capability)
    {
        initialCardHolderNetworkObject = new InitialCardHolderNetworkObject();

        isEmpty = true;
        isFull = false;

        turn = 1;

        this.capability = capability;

        players = new ArrayList<>();
    }

    public void addPlayer(String player)
    {
        if(players.size() != capability)
            players.add(player);

        if(players.size() == capability)
        {
            isFull = true;
        }

        if(players.size() == 1)
        {
            isEmpty = false;
        }
    }

    public void removePlayer(String player)
    {
        for(int i=0; i < players.size(); i++)
        {
            if(player.equals(players.get(i)))
            {
                players.remove(player);
                break;
            }
        }

        if(players.size() != capability)
        {
            isFull = false;
        }

        if(players.size() == 0)
        {
            isEmpty = true;
        }
    }

    public ArrayList<String> getPlayers(String sourcePlayer)
    {
        ArrayList<String> destPlayers = new ArrayList<>();

        for(int i=0; i < players.size(); i++)
        {
            if(!players.get(i).equals(sourcePlayer))
                destPlayers.add(players.get(i));
        }

        return destPlayers;
    }

    public boolean isPlayerWithinSession(String player)
    {
        boolean result = false;

        for(int i=0; i < players.size(); i++)
        {
            if(players.get(i).equals(player))
            {
                result = true;
                break;
            }
        }

        return result;
    }

    public void addCards(Card card)
    {
        initialCardHolderNetworkObject.add(card);
    }

    public InitialCardHolderNetworkObject getInitialCardHolderNetworkObject()
    {
        return initialCardHolderNetworkObject;
    }

    public void newTurn()
    {
        turn++;
    }

    public boolean isFull()
    {
        return isFull;
    }

    public boolean isEmpty()
    {
        return isEmpty;
    }

    public int getTurn()
    {
        return turn;
    }

    public String toString()
    {
        String retPlayers = "";

        for(int i=0; i < players.size(); i++)
        {
            retPlayers = retPlayers + players.get(i) + " ";
        }

        return "Player: " + retPlayers + "Empty: " + isEmpty + " Full: " + isFull + " Capability: " + capability;
    }
}
