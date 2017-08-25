package com.tgd.kanjigame.network.client;

import com.tgd.kanjigame.network.object.CardHolderNetworkObject;
import com.tgd.kanjigame.network.object.NetworkObject;
import com.tgd.kanjigame.network.object.PlayerNetworkObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client
{
    private final static int PORT = 1564;

    private Socket connection;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private String clientName;

    private volatile boolean connected;

    public Client(String clientName)
    {
        this.clientName = clientName;

        try
        {
            connect(clientName);
        }
        catch (UnknownHostException e)
        {
            System.out.println(e);
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    private void connect(String clientName) throws UnknownHostException, IOException
    {
        connection = new Socket("127.0.0.1", PORT);

        if(connection != null)
            connected = true;

        if(connected)
        {
            PlayerNetworkObject playerNetworkObject = new PlayerNetworkObject(clientName);

            objectOutputStream = new ObjectOutputStream((connection.getOutputStream()));
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(connection.getInputStream());

            try
            {
                objectOutputStream.writeObject(playerNetworkObject);
                objectOutputStream.flush();
            }
            catch(IOException e)
            {
                System.out.println("IOException: " + e);
            }
        }
        else
        {
            System.out.println("Unable to connected to server");
        }
    }

    public void sendCardsToServer(CardHolderNetworkObject cardHolderNetworkObject)
    {
        sendToServer(cardHolderNetworkObject);
    }

    public void sendPlayerToServer(PlayerNetworkObject playerNetworkObject)
    {
        sendToServer(playerNetworkObject);
    }

    private void sendToServer(NetworkObject networkObject)
    {
        try
        {
            objectOutputStream.writeObject(networkObject);
            objectOutputStream.flush();
        }
        catch(UnknownHostException e)
        {
            System.out.println(e);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }
}
