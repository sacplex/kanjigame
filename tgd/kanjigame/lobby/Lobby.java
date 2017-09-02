package com.tgd.kanjigame.lobby;

import com.tgd.kanjigame.card.Card;
import com.tgd.kanjigame.database.LoadDatabase;
import com.tgd.kanjigame.network.object.InitialCardHolderNetworkObject;

import java.util.ArrayList;
import java.util.Random;

public class Lobby
{
    public static final int CAPABILITY = 4;

    private ArrayList<Session> sessions;
    private ArrayList<Card> cards;

    private Random random;

    public Lobby()
    {
        sessions = new ArrayList<>();
    }

    public Lobby(LoadDatabase database)
    {
        random = new Random();

        sessions = new ArrayList<>();
        cards = new ArrayList<>(Session.NUMBER_OF_STARTING_CARDS * CAPABILITY);

        for(int i=0; i < CAPABILITY; i++)
        {
            for(int j=0; j < Session.NUMBER_OF_STARTING_CARDS; j++)
            {
                cards.add(database.popSkewedUniqueCard());
            }
        }

        database.reset();
    }

    public void add(String player)
    {
        if(sessions.size() == 0) // Create the first session, if empty
        {
            Session session = new Session(CAPABILITY);
            session.addPlayer(player);
            InitialCardHolderNetworkObject initialCardHolderNetworkObject = new InitialCardHolderNetworkObject();

            for(int i=0; i<Session.NUMBER_OF_STARTING_CARDS; i++)
            {
                Card card = cards.remove(random.nextInt(cards.size()-1));
                initialCardHolderNetworkObject.add(card);
            }

            session.addCards(player, initialCardHolderNetworkObject);

            System.out.println("Number of Cards: " + cards.size());

            sessions.add(session);
        }
        else
        {
            if(sessions.get(sessions.size()-1).isFull())
            {
                InitialCardHolderNetworkObject initialCardHolderNetworkObject = new InitialCardHolderNetworkObject();

                Session session = new Session(CAPABILITY);
                session.addPlayer(player);

                for(int i=0; i<Session.NUMBER_OF_STARTING_CARDS; i++)
                {
                    Card card = cards.remove(random.nextInt(cards.size()-1));
                    initialCardHolderNetworkObject.add(card);
                }

                session.addCards(player, initialCardHolderNetworkObject);

                sessions.add(session);
            }
            else
            {
                InitialCardHolderNetworkObject initialCardHolderNetworkObject = new InitialCardHolderNetworkObject();

                sessions.get(sessions.size() - 1).addPlayer(player);

                for(int i=0; i<Session.NUMBER_OF_STARTING_CARDS; i++)
                {
                    Card card = cards.remove(random.nextInt(cards.size()-1));

                    initialCardHolderNetworkObject.add(card);
                }

                sessions.get(sessions.size() - 1).addCards(player, initialCardHolderNetworkObject);
            }
        }
    }

    public Session getSession(String player)
    {
        Session session = null;

        if(sessions.size() != 0)
        {
            for(int i=0; i < sessions.size(); i++)
            {
                if(sessions.get(i).isPlayerWithinSession(player))
                {
                    session = sessions.get(i);
                    break;
                }
            }
        }

        return session;
    }

    public void remove(String player)
    {
        if(sessions.size() != 0)
        {
            for(int i=0; i < sessions.size(); i++)
            {
                sessions.get(i).removePlayer(player);

                if(sessions.get(i).isEmpty())
                    sessions.remove(i);
            }
        }
    }

    public String toString()
    {
        String retSessions = "";

        for(int i=0; i < sessions.size(); i++)
            retSessions = retSessions + sessions.get(i) + "\n";

        return retSessions;
    }

    public static void main(String [] args)
    {
        Lobby lobby = new Lobby();

        lobby.add("sacplex");
        lobby.add("ayumi");
        lobby.add("tomoko");
        lobby.add("hojun");
        lobby.add("jessica");
        lobby.add("amber");

        System.out.println(lobby + "\n");

        lobby.remove("sacplex");

        System.out.println(lobby + "\n");

        lobby.remove("hojun");
        lobby.remove("ayumi");
        lobby.remove("tomoko");

        System.out.println(lobby + "\n");

        lobby.add("tom");
        lobby.add("dick");
        lobby.add("harry");

        System.out.println(lobby + "\n");

        lobby.add("rachael");

        System.out.println(lobby + "\n");

        lobby.add("ash");
        lobby.add("alice");
        lobby.add("john");

        System.out.println(lobby + "\n");

    }
}
