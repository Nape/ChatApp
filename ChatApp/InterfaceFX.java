package com.nadirpelletier.ChatApp;

import com.nadirpelletier.messages.Fichier;
import com.nadirpelletier.messages.Message;

/**
 * Created by Nadir Pelletier
 * For : TP2-NadirPelletier
 * Date : 2019-06-24
 * Time : 14:59
 */
public interface InterfaceFX
{
    void transfereMessage(Message message);

    void transfereFichier(Fichier fichier);
}
