package client;

import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.codegen.CompilerConstants;

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
                    System.err.println(e.getMessage());
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
                    System.err.println(e.getMessage());
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
                    System.err.println(e.getMessage());
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
                    System.err.println(e.getMessage());
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
                    System.err.println(ex.getMessage());
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
            System.err.println("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            System.err.println("Error reading file '" + fileName + "'");                  
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
            System.err.println("Unable to open file '" + fileName + "'\n"+ex.getMessage());
        }
        catch(IOException ex) {
            System.err.println("Error reading file '" + fileName + "'\n"+ex.getMessage());                  
        }
        return 0;
    }
    
    /**
     * Lit l'objet envoyé et reçu par le socket et le renvoie
     * @return Objet reçu via le socket ou null en cas d'erreur
     */
    public Object lectureObjet()
    {
        Object o = null; //Initialise l'objet à null
        try {
            ObjectInputStream entree = new ObjectInputStream(this.socket.getInputStream()); //Permet de lire l'objet provenant du socket
            o = entree.readObject();    //Lit l'objet provenant du socket et le stocke dans o
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
            return o; //Renvoie un objet null en cas d'erreur
        }
        return o; //Renvoie l'objet reçu par le socket
    }

    /**
     * Le client se met à l'écoute du socket et se prépare a recevoir et copier un fichier
     * @param source le socket à écouter pour la reception du fichier
     * @param fichier le fichier de destination
     */
    public void receptionFichier(Socket source, File fichier)
    {

            InputStream entree = null;
            OutputStream sortie = null;

            try {
                entree = socket.getInputStream();
                sortie = new FileOutputStream(fichier);
                byte[] bytes = new byte[16*1024];
                int count;
                while ((count = entree.read(bytes)) > 0) 
                {
                    sortie.write(bytes, 0, count);
                }
                entree.close();
                sortie.close();
            } catch (FileNotFoundException ex) {
                System.err.println("Reception Fichier FileNotFoundException : " + ex.getMessage());
            } catch (IOException ex) {
                System.err.println("Reception Fichier IOEsception : " + ex.getMessage());
            }
    }
    
    /**
     * Méthode main du programme
     * @param args Arguments à ajouter pour le lancement du programme
     */
    public static void main(String[] args)
    {
        
            Client c = new Client("192.168.0.102", 5000);
            //Client c = new Client();
            if (c.envoiMessage("Bonjour, je suis un client."))
                System.out.println("Message du serveur : " + c.lectureMessage());
            else
                System.out.println("Erreur lors de l'envoie du message");
            //File f = (File)c.lectureObjet();
            File dest = new File("test.txt");
            //System.out.println(f.toString());
            if (dest.exists())
                dest.delete();
            try {
                dest.createNewFile();
            } catch (IOException e)
            {
                System.err.println(e.getMessage());
            }
            c.receptionFichier(c.socket, dest);
            System.out.println("Reception done");
            c.fermetureClient();
    }
}