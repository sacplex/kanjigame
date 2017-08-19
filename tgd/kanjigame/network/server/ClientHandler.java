package com.tgd.kanjigame.network.server;

import com.tgd.kanjigame.card.Card;
import com.tgd.kanjigame.gamerules.Validator;
import com.tgd.kanjigame.network.object.CardHolderNetworkObject;
import com.tgd.kanjigame.network.object.NetworkObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.ServerError;
import java.util.ArrayList;

public class ClientHandler implements Runnable
{
    private final Server server;
    private final Socket socket;
    private InetAddress address;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private String clientId;
    private boolean connected;

    public ClientHandler(final Server server, final Socket socket, InetAddress address, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream)
    {
        this.server = server;
        this.socket = socket;
        this.address = address;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        clientId = "Unknown Client";

        connected = true;
    }

    public void startClient()
    {
        Thread clientThread = new Thread(this);

        clientThread.start();
    }

    public void run()
    {
        try
        {
            readIncomingObject();
        }
        catch(UnknownHostException e)
        {
            System.out.println(e);
        }
        catch(IOException e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
        finally
        {
            server.removeClient(socket);
        }
    }

    private void readIncomingObject() throws IOException
    {
        try
        {
            NetworkObject networkObject = null;

            while(connected)
            {
                networkObject = (NetworkObject)objectInputStream.readObject();

                if(networkObject == null)
                    System.out.println("Network Object is null");

                if(networkObject != null)
                {
                    connected = !(networkObject).isTerminate();

                    if(connected)
                    {
                        clientId = networkObject.getId();

                        if(networkObject instanceof CardHolderNetworkObject)
                        {
                            rebuildCards((CardHolderNetworkObject)networkObject);
                        }
                    }
                }
            }
        }
        catch(ClassNotFoundException e)
        {
            System.out.println("Unknown Class incoming, Client<readIncomingObject>");
            System.out.println(e);
            e.printStackTrace();
        }
    }

    private void rebuildCards(CardHolderNetworkObject cardHolderNetworkObject)
    {
        ArrayList<Card> cards = new ArrayList<Card>(cardHolderNetworkObject.getCards().size());

        for(int i=0; i < cardHolderNetworkObject.getCards().size(); i++)
            cards.add(new Card(cardHolderNetworkObject.getCards().get(i)));

        validateCards(cards);
    }

    private void validateCards(ArrayList<Card> cards)
    {
        Validator validator = new Validator();

        for(int i=0; i<cards.size(); i++)
            validator.add(cards.get(i));

        System.out.println("Server validating");
        validator.validate();
        System.out.println(validator.getIntendedRuleSet());
    }

    public void close()
    {
        try
        {
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();
        }
        catch(IOException e)
        {
            System.out.println("Problem closing connecting to client: " + e);
        }
    }
}







































