package com.tgd.kanjigame.network.object;

public class PlayerNetworkObject extends NetworkObject
{
    private String name;

    public PlayerNetworkObject(String name)
    {
        this.name = name;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
