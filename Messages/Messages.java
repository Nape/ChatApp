package com.nadirpelletier.messages;

import com.nadirpelletier.ChatApp.Client;

import java.util.*;

/**
 * Created by Nadir Pelletier
 * For : TP2-NadirPelletier
 * Date : 2019-06-20
 * Time : 15:42
 */

/**
 * Permet d'enregistrer l'historique des conversations.
 * J'aurais aimé avoir le temps d'implémenter une sauvegarde des messages mais je n'ai pas eu le temp :(
 */
public class Messages
{
    private static Map<Client,LinkedList<Message>> clientMessageMap = new HashMap<>();


    public static void ajouterClient(Client client)
    {
        clientMessageMap.put(client,new LinkedList<Message>());
    }

    public static void ajouterMessage(Client client, Message message)
    {
        clientMessageMap.get(client).add(message);
    }

}
