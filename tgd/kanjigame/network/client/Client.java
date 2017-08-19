package com.tgd.kanjigame.network.client;

import com.tgd.kanjigame.network.object.CardHolderNetworkObject;
import com.tgd.kanjigame.network.object.NetworkObject;

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

    private volatile boolean connected;

    public Client()
    {
        try
        {
            connect();
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

    private void connect() throws UnknownHostException, IOException
    {
        connection = new Socket("127.0.0.1", PORT);

        if(connection != null)
            connected = true;

        if(connected)
        {
            NetworkObject networkObject = new NetworkObject("KanjiGameClient");

            objectOutputStream = new ObjectOutputStream((connection.getOutputStream()));
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(connection.getInputStream());

            try
            {
                objectOutputStream.writeObject(networkObject);
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

    public void sendToServer(CardHolderNetworkObject cardHolderNetworkObject)
    {
        try
        {
            objectOutputStream.writeObject(cardHolderNetworkObject);
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
