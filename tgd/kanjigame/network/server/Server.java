package com.tgd.kanjigame.network.server;

import com.tgd.kanjigame.network.object.NetworkObject;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server
{
    private static final int DEFAULT_PORT = 1564;

    private int numberOfClients;

    private HashMap<Object, ClientHandler> clients;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public Server()
    {
        int port = DEFAULT_PORT;

        clients = new HashMap<>();

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
        NetworkObject networkObject = null;

        if(clients.get(connection) == null)
        {
            try
            {
                objectOutputStream = new ObjectOutputStream((connection.getOutputStream()));
                objectInputStream = new ObjectInputStream(connection.getInputStream());

                networkObject = (NetworkObject)objectInputStream.readObject();

                System.out.println("ID: " + networkObject.getId());

                client = new ClientHandler(this, connection, connection.getInetAddress(), objectInputStream, objectOutputStream);

                clients.put(networkObject, client);

                clients.get(networkObject).startClient();
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

    public synchronized ClientHandler getClientHandler(NetworkObject networkObject)
    {
        return clients.get(networkObject);
    }

    public static void main(String [] args)
    {
        new Server();
    }
}




































