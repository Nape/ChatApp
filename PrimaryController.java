package com.nadirpelletier;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import com.nadirpelletier.ChatApp.*;
import com.nadirpelletier.messages.Fichier;
import com.nadirpelletier.messages.Message;
import com.nadirpelletier.messages.Messages;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PrimaryController implements InterfaceFX
{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField tfIPdistant;

    @FXML
    private TextField tfPortDistant;

    @FXML
    private TextField tfNomUtilisateur;

    @FXML
    private Button buttonConnection;

    @FXML
    private ListView<String> listViewMsg;

    @FXML
    private ListView<String> listViewInfo;

    @FXML
    private TextArea textAreaMsg;

    @FXML
    private Button buttonEnvoyerMsg;

    @FXML
    private Button buttonEnvoyerFichier;

    private ObservableList<String> messageObservableList;
    private ObservableList<String> infoObservableList;
    private Serveur serveur;
    private Client client;


    @FXML
    void initialize()
    {
        serveur = new Serveur(this, 55555);
        serveur.start();
        initButton();
        initListView();
        infoObservableList.add("Serveur en attente sur le port : " + serveur.getPort());
    }

    public void connection()
    {
        client = new Client(tfNomUtilisateur.getText(), tfIPdistant.getText(), Integer.parseInt(tfPortDistant.getText()));
        infoObservableList.add("Client connecté " + client.getNom() + " : " + client.getAddressIP());
        buttonEnvoyerFichier.setDisable(false);

        buttonConnection.disableProperty().unbind();
        buttonConnection.setDisable(true);

        buttonEnvoyerMsg.disableProperty().unbind();
        buttonEnvoyerMsg.disableProperty().bind(javafx.beans.binding.Bindings.isEmpty(textAreaMsg.textProperty()));
        Messages.ajouterClient(client);
    }

    public void envoyerMessage()
    {
        Message message = new Message(client.getNom(), textAreaMsg.getText());
        client.envoyerMessage(message);
        messageObservableList.add(message.toString());
        Messages.ajouterMessage(client,message);
        textAreaMsg.clear();
    }

    public void envoyerFichier()
    {
        Platform.runLater(() ->
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Fichier à envoyer");
            Fichier fichier = new Fichier(fileChooser.showOpenDialog(new Stage()));
            infoObservableList.add("Envoie du fichier : " + fichier.getFile().getAbsolutePath());
            client.evoyerFichier(fichier);
        });
    }

    /**
     * S'assure que les tf ne sont pas vides.
     */
    private void initButton()
    {
        buttonEnvoyerFichier.setText("Choisir un fichier à envoyer");
        buttonEnvoyerFichier.setDisable(true);
        buttonConnection.disableProperty().bind(javafx.beans.binding.Bindings.isEmpty(tfIPdistant.textProperty())
                                                                             .or(javafx.beans.binding.Bindings.isEmpty(tfNomUtilisateur.textProperty()))
                                                                             .or(javafx.beans.binding.Bindings.isEmpty(tfPortDistant.textProperty())));

        buttonEnvoyerMsg.disableProperty().bind(buttonConnection.disableProperty()
                                                                .or(javafx.beans.binding.Bindings.isEmpty(textAreaMsg.textProperty())));

    }

    /**
     * Listner sur les 2 ObservableList pour mettre à jour les listview OnChange.
     */
    private void initListView()
    {
        infoObservableList = FXCollections.observableArrayList();
        messageObservableList = FXCollections.observableArrayList();

        infoObservableList.addListener((ListChangeListener<? super String>) c -> listViewInfo.setItems(infoObservableList));
        messageObservableList.addListener((ListChangeListener<? super String>) c -> listViewMsg.setItems(messageObservableList));

        listViewInfo.setItems(infoObservableList);
        listViewMsg.setItems(messageObservableList);
    }

    /**
     * Interface utilisé par Serveur et This
     *
     * @param message
     */
    @Override
    public void transfereMessage(Message message)
    {
        Platform.runLater(() ->
        {
            System.out.println(message.toString());
            messageObservableList.add(message.toString());
            Messages.ajouterMessage(client,message);

        });
    }

    /**
     * Interface utilisé par Serveur et This
     *
     * @param fichierRecu
     */
    @Override
    public void transfereFichier(Fichier fichierRecu)
    {
        Platform.runLater(() ->
        {
            try
            {
                OutputStream out = new FileOutputStream(new File("FichierRecu/" + fichierRecu.getFile().getName()));
//                OutputStream out = new FileOutputStream(new File("/Users/Nadir/Desktop/FichierRecu/" + fichierRecu.getFile().getName()));
                out.write(fichierRecu.getContenu());
                out.close();
                infoObservableList.add("Fichier recu : " + fichierRecu.getFile().getName());
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        });
    }

    public void quitter()
    {
        client.arreter();
        serveur.arreter();
        Platform.exit();
    }
}