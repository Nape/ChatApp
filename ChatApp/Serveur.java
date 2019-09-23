package com.nadirpelletier.ChatApp;

import com.nadirpelletier.messages.Fichier;
import com.nadirpelletier.messages.Message;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Nadir Pelletier
 * For : TP2-NadirPelletier
 * Date : 2019-06-20
 * Time : 14:24
 */
public class Serveur extends Thread
{
    private ServerSocket serverSocket;
    private InterfaceFX interfaceFX;
    private Socket socketReception;
    private int port;

    public Serveur(InterfaceFX interfaceFX, int port)
    {
        this.setDaemon(true);
        this.setName("Thread Écoute");
        this.port = port;
        this.interfaceFX = interfaceFX;

        try
        {
            this.serverSocket = new ServerSocket(port);
            this.socketReception = new Socket();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void run()
    {
        try
        {
            this.socketReception = serverSocket.accept();
            ObjectInputStream objectInputStream = new ObjectInputStream(this.socketReception.getInputStream());

            while (!socketReception.isClosed())
            {
                switch (objectInputStream.readChar())
                {
                    case 'm':
                        try
                        {
                            Message msg = (Message) objectInputStream.readObject();
                            interfaceFX.transfereMessage(msg);
                        }
                        catch (ClassNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                        break;

                    case 'f':
                        try
                        {
                            Fichier fichier = (Fichier) objectInputStream.readObject();
                            interfaceFX.transfereFichier(fichier);
                        }
                        catch (ClassNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                        break;
                }
            }

        }
        catch (IOException e)
        {
            Logger.getLogger(Serveur.class.getName()).log(Level.SEVERE, "Socket client ERREUR", e);
        }
    }

    public void arreter()
    {
        try
        {
            if (!this.socketReception.isClosed())
            {
                this.socketReception.close();
            }
            if (!this.serverSocket.isClosed())
            {
                this.serverSocket.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public int getPort()
    {
        return port;
    }
}