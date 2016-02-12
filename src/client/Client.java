package client;

import java.io.BufferedReader;
import java.io.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Classe faisant l'abstraction  d'un client gérant la connexion avec le serveur
 */
public class Client
{
    /**
     * Socket permettant la connexion avec le serveur
     */
    private Socket socket;
    /**
     * Flux entrant
     */
    private BufferedReader in;
    /**
     * Flux sortant
     */
    private PrintWriter out;
    /**
     * // Adresse IPv4 du serveur
     */
    private String adresseServeur;
    /**
     * Port de connexion au serveur
     */
    private int port;

    /**
     * Constructeur par défaut
     */
    public Client()
    {
            this.adresseServeur = getIPServeur();
            this.port = getPortServeur();
            try
            {
                    socket = new Socket(this.adresseServeur, this.port);   // Ouverture de la socket
            }
            catch (IOException e) //Si le serveur n'est pas trouvé
            {
                    System.out.println(e.getMessage());
            }
    }

    /**
     * Constructeur avec spécification de l'adresse et du port
     * @param adresseServeur L'adresse IP du serveur
     * @param port Le numéro de port sur lequel le serveur écoute.
     */
    public Client(String adresseServeur, int port)
    {
            this.adresseServeur = adresseServeur;
            this.port = port;
            try
            {
                    socket = new Socket(adresseServeur, port);   // Ouverture de la socket
            }
            catch (IOException e) //Si le serveur n'est pas trouvé
            {
                    System.out.println(e.getMessage());
            }
    }

    /**
     * Envoie une chaîne de caractère au serveur
     * @param message Message pour le serveur
     * @return booléen si le message a été ou non envoyé
     */
    public boolean envoiMessage(String message)
    {
            try
            {
                    out = new PrintWriter(this.socket.getOutputStream());    // Déclaration du flux sortant
                    out.println(message); // Envoie d'un message au serveur
                    out.flush();
            }
            catch (IOException e)   //En cas d'erreur
            {
                    System.out.println(e.getMessage());
                    return false;
            }
            return true;
    }

    /**
     * Lecture d'une chaîne de caractère envoyé par le serveur
     * @return chaîne de caractère du message reçu
     */
    public String lectureMessage()
    {
            try
            {
                    in = new BufferedReader(new InputStreamReader (this.socket.getInputStream())); //permet de lire les caractères provenant du socket
                    return in.readLine();   //Renvoie le contenu de in
            }
            catch (IOException e)   //En cas d'erreur
            {
                    System.out.println(e.getMessage());
                    return null;
            }
    }

    /**
     * Ferme le socket ouvert avec le serveur
     */
    public void fermetureClient()
    {
            try
            {
                    socket.close();   //Ferme le socket
            }
            catch (IOException ex)  //En cas d'erreur
            {
                    System.out.println(ex.getMessage());
            }
    }

    /**
     * Retourne l'adresse IP du serveur du fichier de configuration
     * @return adresse IP du serveur
     */
    public String getIPServeur()
    {
            String fileName = "conf.txt";
            String line = null;

            try {
                    FileReader fileReader = new FileReader(fileName);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);

                    while((line = bufferedReader.readLine()) != null) {
                            return line;
                    }   
                    bufferedReader.close();         
            }
            catch(FileNotFoundException ex) {
                System.out.println("Unable to open file '" + fileName + "'");
            }
            catch(IOException ex) {
                System.out.println("Error reading file '" + fileName + "'");                  
            }
            return "";
    }

    /**
     * Retourne le port socket du serveur du fichier de configuration
     * @return port socket du serveur
     */
    public int getPortServeur()
    {
            String fileName = "conf.txt";
            String line = null;

            try {
                    FileReader fileReader = new FileReader(fileName);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);

                    int i = 0;
                    while((line = bufferedReader.readLine()) != null) {
                            if (i == 1)
                                    return Integer.parseInt(line);
                            i++;
                    }   
                    bufferedReader.close();         
            }
            catch(FileNotFoundException ex) {
                System.out.println("Unable to open file '" + fileName + "'\n"+ex.getMessage());
            }
            catch(IOException ex) {
                System.out.println("Error reading file '" + fileName + "'\n"+ex.getMessage());                  
            }
            return 0;
    }

    /**
     * Méthode main du programme
     * @param args Arguments à ajouter pour le lancement du programme
     */
    public static void main(String[] args)
    {
            Client c = new Client("127.0.0.1", 2009);
            //Client c = new Client();
            if (c.envoiMessage("Bonjour, je suis un client."))
                    System.out.println("Message du serveur : " + c.lectureMessage());
            else
                    System.out.println("Erreur lors de l'envoie du message");
            c.fermetureClient();
    }
}
