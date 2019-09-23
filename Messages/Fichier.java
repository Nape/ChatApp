package com.nadirpelletier.messages;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

/**
 * Created by Nadir Pelletier
 * For : TP2-NadirPelletier
 * Date : 2019-06-26
 * Time : 12:35
 */
public class Fichier implements Serializable
{

    private static final long serialVersionUID = -314521914014983675L;
    private File file;
    private byte[] contenu;

    public Fichier(File file)
    {
        try
        {
            this.file = file;
            this.contenu = Files.readAllBytes(file.toPath());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public File getFile()
    {
        return file;
    }

    public byte[] getContenu()
    {
        return contenu;
    }

}
