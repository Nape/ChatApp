package com.nadirpelletier.ChatApp;

import com.nadirpelletier.messages.Fichier;
import com.nadirpelletier.messages.Message;
import java.io.*;
import java.net.Socket;

/**
 * Created by Nadir Pelletier
 * For : TP2-NadirPelletier
 * Date : 2019-06-24
 * Time : 15:29
 */
public class Client
{
    private String nom;
    private String addressIP;
    private int port;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;


    public Client(String nom, String addressIP, int port)
    {
            this.nom = nom;
            this.addressIP = addressIP;
            this.port = port;

            try
            {
                this.socket = new Socket(addressIP, port);
                this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e)
            {
                e.printStackTrace();
            }
    }
    public void envoyerMessage(Message message)
    {
        new Thread(() ->
        {
            Thread.currentThread().setName("Thread-EnvoieMessage");
            try
            {
                this.getObjectOutputStream().writeChar('m');
                this.getObjectOutputStream().flush();
                this.getObjectOutputStream().writeObject(message);
                this.getObjectOutputStream().flush();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }).start();
    }
    public void evoyerFichier(Fichier fichier)
    {
        new Thread(() ->
        {   Thread.currentThread().setName("Thread-EnvoieFichier");
            try
            {
                this.getObjectOutputStream().writeChar('f');
                this.getObjectOutputStream().flush();
                this.getObjectOutputStream().writeObject(fichier);
                this.getObjectOutputStream().flush();

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }).start();
    }

    public void arreter()
    {
        try
        {
            this.socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private ObjectOutputStream getObjectOutputStream()
    {
        return objectOutputStream;
    }

    public String getNom()
    {
        return this.nom;
    }

    public String getAddressIP()
    {
        return this.addressIP;
    }
    public int getPort()
    {
        return this.port;
    }
}
