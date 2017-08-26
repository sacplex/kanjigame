package com.tgd.kanjigame.network.client;

import com.tgd.kanjigame.board.PlayArea;
import com.tgd.kanjigame.board.PlayerBoard;
import com.tgd.kanjigame.card.Card;
import com.tgd.kanjigame.network.object.CardHolderNetworkObject;
import com.tgd.kanjigame.network.object.NetworkObject;
import com.tgd.kanjigame.network.object.PlayerNetworkObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client implements Runnable
{
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

            startReaderThread();
        }
        else
        {
            System.out.println("Unable to connected to server");
        }
    }

    private void startReaderThread()
    {
        readerThread = new Thread(this);
        running = true;

        readerThread.start();
    }

    public void run()
    {
        receiveFromServer();
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

    private void receiveFromServer()
    {
        NetworkObject networkObject;

        while(running)
        {
            try
            {
                networkObject = (NetworkObject)objectInputStream.readObject();

                if(networkObject instanceof  CardHolderNetworkObject)
                {
                    playerBoard.addOtherPlayers(rebuildCards((CardHolderNetworkObject) networkObject));
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

    private ArrayList<Card> rebuildCards(CardHolderNetworkObject cardHolderNetworkObject)
    {
        ArrayList<Card> cards = new ArrayList<Card>(cardHolderNetworkObject.getCards().size());

        for(int i=0; i < cardHolderNetworkObject.getCards().size(); i++)
            cards.add(new Card(cardHolderNetworkObject.getCards().get(i)));

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
}
