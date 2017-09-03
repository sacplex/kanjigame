package com.tgd.kanjigame.network.server;

import com.tgd.kanjigame.card.Card;
import com.tgd.kanjigame.gamerules.PlayValidator;
import com.tgd.kanjigame.gamerules.Validator;
import com.tgd.kanjigame.lobby.Session;
import com.tgd.kanjigame.network.object.CardHolderNetworkObject;
import com.tgd.kanjigame.network.object.NetworkObject;
import com.tgd.kanjigame.network.object.PlayOrPassNetworkObject;
import com.tgd.kanjigame.network.object.PlayerNetworkObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ClientHandler implements Runnable
{
    private final Server server;
    private final Socket socket;
    private InetAddress address;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private Session session;

    private String playerName;
    private boolean connected;

    public ClientHandler(final Server server, final Socket socket, InetAddress address, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, String playerName)
    {
        this.server = server;
        this.socket = socket;
        this.address = address;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.playerName = playerName;

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
                        if(networkObject instanceof PlayOrPassNetworkObject)
                        {
                            playOrPass(((PlayOrPassNetworkObject)networkObject));
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

    private void playOrPass(PlayOrPassNetworkObject playOrPassNetworkObject)
    {
        rebuildCards(playOrPassNetworkObject);
    }

    private void rebuildCards(PlayOrPassNetworkObject playOrPassNetworkObject)
    {
        if(playOrPassNetworkObject.getPlayState() == PlayOrPassNetworkObject.PLAY_STATE.PLAY)
            validateCards(playOrPassNetworkObject);
        else if(playOrPassNetworkObject.getPlayState() == PlayOrPassNetworkObject.PLAY_STATE.PASS)
            pass(playOrPassNetworkObject);
    }

    private void validateCards(PlayOrPassNetworkObject playOrPassNetworkObject)
    {
        System.out.println(session);

        Validator validator = new Validator();
        validator.add(session);

        ArrayList<Card> cards = new ArrayList<Card>(playOrPassNetworkObject.getCardHolderNetworkObject().getCards().size());

        for (int i = 0; i < playOrPassNetworkObject.getCardHolderNetworkObject().getCards().size(); i++)
            cards.add(new Card(playOrPassNetworkObject.getCardHolderNetworkObject().getCards().get(i)));

        for (int i = 0; i < cards.size(); i++)
            validator.add(cards.get(i));

        System.out.println("Server validating");
        validator.validate();

        playOrPassNetworkObject.setPosition(session.getPlayerContents(playerName).getNextPosition(session.getCapability()));

        if (validator.getIntendedRuleSet() != PlayValidator.RULE_SET.ERROR) {
            for (String name : session.getPlayers(playerName))
                writeToClients(playOrPassNetworkObject, name);


        System.out.println(validator.getIntendedRuleSet());
        }


    }

    private void writeToClients(PlayOrPassNetworkObject playOrPassNetworkObject, String otherPlayer)
    {
        try
        {
            server.getClientHandler(otherPlayer).getObjectOutputStream().writeObject(playOrPassNetworkObject);
            server.getClientHandler(otherPlayer).getObjectOutputStream().flush();
        }
        catch(IOException e)
        {
            System.out.println("Problem sending out going network object, Client<writeToClients>");
            System.out.println(e);
            e.printStackTrace();
        }
    }

    private void pass(PlayOrPassNetworkObject playOrPassNetworkObject)
    {
        System.out.println(playOrPassNetworkObject.getPosition());
        playOrPassNetworkObject.setPosition(session.getPlayerContents(playerName).getNextPosition(session.getCapability()));
        System.out.println(playOrPassNetworkObject.getPosition());

        for (String name : session.getPlayers(playerName))
            writeToClients(playOrPassNetworkObject, name);
    }

    public void addSession(Session session)
    {
        this.session = session;
    }

    /*private void validatePlayerName(PlayerNetworkObject playerNetworkObject)
    {
        if(!playersNames.contains(playerNetworkObject.getName()))
            playersNames.add(playerNetworkObject.getName());
        else
        {
            // TODO: MESSAGE BACK TO PLAYER
        }
    }*/

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
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







































