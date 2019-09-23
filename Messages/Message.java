package com.nadirpelletier.messages;
import java.io.Serializable;

/**
 * Created by Nadir Pelletier
 * For : TP2-NadirPelletier
 * Date : 2019-06-20
 * Time : 15:38
 */
public class Message implements Serializable
{
    private static final long serialVersionUID = 1279404104437335091L;
    private String auteur;
    private String message;

    public Message(String auteur, String message)
    {
        this.auteur = auteur;
        this.message = message;
    }

    public String getAuteur()
    {
        return auteur;
    }

    public String getMessage()
    {
        return message;
    }

    @Override
    public String toString()
    {
        return this.auteur+" : "+this.message+"\n";
    }
}
