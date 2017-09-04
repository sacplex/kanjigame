package com.tgd.kanjigame.network.client;

import com.tgd.kanjigame.board.PlayArea;
import com.tgd.kanjigame.board.PlayerBoard;
import com.tgd.kanjigame.card.Card;
import com.tgd.kanjigame.io.ImageIO;
import com.tgd.kanjigame.network.object.*;
import com.tgd.kanjigame.players.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client implements Runnable
{
    public final static String IP_LOCAL_HOST = "localhost";
    public final static String IP_TGD = "tgd.net.au";
    public final static String host = IP_LOCAL_HOST;
    private final static int PORT = 1564;

    private Thread readerThread;

    private Socket connection;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private PlayerBoard playerBoard;

    private String clientName;

    private volatile boolean connected;
    private volatile boolean running;

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
        connection = new Socket(host, PORT);

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

    public void startReaderThread()
    {
        readerThread = new Thread(this);
        running = true;

        readerThread.start();
    }

    public void run()
    {
        receiveFromServer();
    }

    public void sendPlayOrPassToServer(PlayOrPassNetworkObject playOrPassNetworkObject)
    {
        sendToServer(playOrPassNetworkObject);
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

    private void receiveFromServer()
    {
        NetworkObject networkObject;

        while(running)
        {
            try
            {
                networkObject = (NetworkObject)objectInputStream.readObject();

                if(networkObject instanceof PlayOrPassNetworkObject)
                {
                    System.out.println("From Server: " + ((PlayOrPassNetworkObject) networkObject).getPosition());
                    System.out.println("From Local: " + playerBoard.getPosition());

                    if(((PlayOrPassNetworkObject) networkObject).getPosition().equals(playerBoard.getPosition()))
                        PlayerBoard.playing = true;

                    playerBoard.setPlayState(null);

                    if(((PlayOrPassNetworkObject) networkObject).getPlayState() == PlayOrPassNetworkObject.PLAY_STATE.PLAY)
                        playerBoard.addOtherPlayersCards(rebuildCards((PlayOrPassNetworkObject) networkObject));
                    else if(((PlayOrPassNetworkObject) networkObject).getPlayState() == PlayOrPassNetworkObject.PLAY_STATE.PASS)
                        playerBoard.addOtherPlayersCards(null);
                }
                else if(networkObject instanceof SetupNetworkObject)
                {
                    System.out.println("New Setup Network Object has arrived");
                    System.out.println(((SetupNetworkObject)networkObject).getPosition());

                    playerBoard.setGameState((SetupNetworkObject)networkObject);
                    playerBoard.setFirstPlayerToPlaying();
                }
            }
            catch (ClassNotFoundException e)
            {
                System.out.println("<Client - ClassNotFoundException, can not convert network object>");
                e.printStackTrace();
            }
            catch(IOException e)
            {
                System.out.println("<Client - IOException, can not read network object>");
                e.printStackTrace();
            }
        }
    }

    public void receiveInitiallyFromServer()
    {
        NetworkObject networkObject;

        try
        {
            networkObject = (NetworkObject) objectInputStream.readObject();

            if(networkObject instanceof SetupNetworkObject)
            {
                System.out.println("Got Player's Cards");

                if(playerBoard == null)
                    playerBoard = new PlayerBoard((SetupNetworkObject)networkObject);

                System.out.println(((SetupNetworkObject)networkObject).getPosition());

                playerBoard.setGameState((SetupNetworkObject)networkObject);
                playerBoard.setFirstPlayerToPlaying();
            }
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("<Client - ClassNotFoundException, can not convert network object>");
            e.printStackTrace();
        }
        catch(IOException e)
        {
            System.out.println("<Client - IOException, can not read network object>");
            e.printStackTrace();
        }
    }

    private ArrayList<Card> rebuildCards(PlayOrPassNetworkObject playOrPassNetworkObject)
    {
        ArrayList<Card> cards = new ArrayList<Card>(playOrPassNetworkObject.getCardHolderNetworkObject().getCards().size());

        for(int i=0; i < playOrPassNetworkObject.getCardHolderNetworkObject().getCards().size(); i++)
            cards.add(new Card(playOrPassNetworkObject.getCardHolderNetworkObject().getCards().get(i)));

        System.out.println("Other players cards");

        for(int i=0; i < cards.size(); i++)
        {
            System.out.println("Stroke: " + cards.get(i).getStrokesValue());
            System.out.println("Colour: " + cards.get(i).getColour());
            System.out.println("Kanji: " + cards.get(i).getKanji());
            System.out.println("Kunyomi: " + cards.get(i).getKunyomi());
            System.out.println("Onyomi: " + cards.get(i).getOnyomi());
            System.out.println("English: " + cards.get(i).getEnglish() + "\n");
        }

        return cards;
    }

    public void addPlayerBoard(PlayerBoard playerBoard)
    {
        this.playerBoard = playerBoard;
    }

    public PlayerBoard getPlayerBoard() { return playerBoard; }
}
