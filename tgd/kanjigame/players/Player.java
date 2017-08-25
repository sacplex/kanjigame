package com.tgd.kanjigame.players;

import com.tgd.kanjigame.network.client.Client;

public class Player
{
    private String name;
    private Client client;

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setClient(Client client)
    {
        this.client = client;
    }

    public Client getClient()
    {
        return client;
    }
}
