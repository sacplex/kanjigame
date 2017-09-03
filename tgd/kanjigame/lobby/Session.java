package com.tgd.kanjigame.lobby;

import com.tgd.kanjigame.card.Card;
import com.tgd.kanjigame.network.object.InitialCardHolderNetworkObject;
import com.tgd.kanjigame.network.object.InitialCardNetworkObject;
import com.tgd.kanjigame.network.object.SetupNetworkObject;
import com.tgd.kanjigame.players.Content;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Session
{
    public static final int NUMBER_OF_STARTING_CARDS = 12;

    private HashMap<String, Content> playerContents;

    private ArrayList<String> players;
    private ArrayList<Integer> positions;

    private Random random;

    private int capability;

    private boolean isEmpty;
    private boolean isFull;

    private int turn;

    public Session(int capability)
    {
        playerContents = new HashMap<>();

        isEmpty = true;
        isFull = false;

        turn = 1;

        random = new Random();

        this.capability = capability;

        players = new ArrayList<>();

        initPositions();
    }

    private void initPositions()
    {
        positions = new ArrayList<>(capability);

        for(int i=0; i < capability; i++)
            positions.add(i);
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

    public void addCards(String player, InitialCardHolderNetworkObject initialCardHolderNetworkObject)
    {
        playerContents.put(player, new Content(initialCardHolderNetworkObject));
    }

    public void assignPosition(String player)
    {
        playerContents.get(player).setPosition(positions.remove(random.nextInt(positions.size())));
    }

    public InitialCardHolderNetworkObject getInitialCardHolderNetworkObject(String player)
    {
        return playerContents.get(player).getInitialCardHolderNetworkObject();
    }

    public SetupNetworkObject getSetupNetworkObject(String player)
    {
        SetupNetworkObject setupNetworkObject = new SetupNetworkObject(
                playerContents.get(player).getInitialCardHolderNetworkObject(),
                playerContents.get(player).getPLayerPosition()
        );

        return setupNetworkObject;
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
