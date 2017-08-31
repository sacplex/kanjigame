package com.tgd.kanjigame.network.server;

import com.tgd.kanjigame.database.LoadDatabase;
import com.tgd.kanjigame.lobby.Lobby;
import com.tgd.kanjigame.network.object.InitialCardHolderNetworkObject;
import com.tgd.kanjigame.network.object.NetworkObject;
import com.tgd.kanjigame.network.object.PlayerNetworkObject;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server
{
    private static final int DEFAULT_PORT = 1564;

    private int numberOfClients;

    private HashMap<String, ClientHandler> clients;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private Lobby lobby;

    public Server()
    {
        int port = DEFAULT_PORT;

        clients = new HashMap<>();

        lobby = new Lobby(LoadDatabase.getInstance());

        setUp(port);
    }

    private void setUp(int port)
    {
        ServerSocket server = null;

        try
        {
            server = ServerSocketFactory.getDefault().createServerSocket(port);

            System.out.println("server address: " + server.getInetAddress());
            System.out.println("port number: " + port);

            while(true)
            {
                System.out.println("Waiting for clients");

                addClient(server.accept());

                numberOfClients++;
            }
        }
        catch(IOException e)
        {
            System.out.println("IOException, Server<setUp>");
            System.out.println("Problem with the server set up");
            System.out.println(e);
        }
        finally
        {
            try
            {
                if(server != null)
                {
                    objectInputStream.close();
                    objectOutputStream.close();
                    server.close();
                }
            }
            catch (IOException e)
            {
                System.out.println("Problem with the server closing");
            }
        }
    }

    private synchronized void addClient(Socket connection)
    {
        System.out.println("Adding new client");

        ClientHandler client = null;
        PlayerNetworkObject playerNetworkObject = null;

        try
        {
            objectOutputStream = new ObjectOutputStream((connection.getOutputStream()));
            objectInputStream = new ObjectInputStream(connection.getInputStream());

            playerNetworkObject = (PlayerNetworkObject)objectInputStream.readObject();

            if(validatePlayerName(playerNetworkObject))
            {
                System.out.println("ID: " + playerNetworkObject.getId());
                System.out.println("Game Player's Name: " + playerNetworkObject.getName());

                lobby.add(playerNetworkObject.getName());

                System.out.println(lobby);

                client = new ClientHandler(this, connection, connection.getInetAddress(), objectInputStream, objectOutputStream, playerNetworkObject.getName());
                client.addSession(lobby.getSession(playerNetworkObject.getName()));

                writeCardsToClient(lobby.getSession(playerNetworkObject.getName()).getInitialCardHolderNetworkObject());

                clients.put(playerNetworkObject.getName(), client);

                clients.get(playerNetworkObject.getName()).startClient();
            }
            else
            {
                // TODO - REMOVE INVALID PLAYER NAME BACK TO CLIENT
            }
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(e);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }

    public synchronized void removeClient(Socket connection)
    {
        System.out.println("Removing old client");

        if(clients.get(connection) != null)
        {
            clients.get(connection).close();
            clients.remove(connection);
        }
    }

    public synchronized ClientHandler getClientHandler(String name)
    {
        return clients.get(name);
    }

    private boolean validatePlayerName(PlayerNetworkObject playerNetworkObject)
    {
        boolean validPlayerName = false;

        if(clients.get(playerNetworkObject.getName()) == null)
            validPlayerName = true;

        return validPlayerName;
    }

    private void writeCardsToClient(InitialCardHolderNetworkObject initialCardHolderNetworkObject)
    {
        System.out.println("Writing Player's Card to Client");

        try
        {
            objectOutputStream.writeObject(initialCardHolderNetworkObject);
            objectOutputStream.flush();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public static void main(String [] args)
    {
        new Server();
    }
}




































