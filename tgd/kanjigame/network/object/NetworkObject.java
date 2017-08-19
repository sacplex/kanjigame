package com.tgd.kanjigame.network.object;

import java.io.Serializable;

public class NetworkObject implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;

    private boolean terminate;

    public NetworkObject()
    {
        id = null;
    }

    public NetworkObject(String id)
    {
        this.id = id;
    }

    public NetworkObject(NetworkObject networkObject)
    {
        this.id = networkObject.getId();
    }

    public String getId() { return id; }

    public void terminateConnection() { terminate  = true; }

    public boolean isTerminate() { return terminate;  }
}
