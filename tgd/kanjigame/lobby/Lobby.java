package com.tgd.kanjigame.lobby;

import java.util.ArrayList;

public class Lobby
{
    private ArrayList<Session> sessions;

    public Lobby()
    {
        sessions = new ArrayList<>();
    }

    public void add(String player)
    {
        if(sessions.size() == 0) // Create the first session, if empty
        {
            Session session = new Session(4);
            session.addPlayer(player);

            sessions.add(session);
        }
        else
        {
            if(sessions.get(sessions.size()-1).isFull())
            {
                Session session = new Session(4);
                session.addPlayer(player);

                sessions.add(session);
            }
            else
                sessions.get(sessions.size()-1).addPlayer(player);
        }
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
