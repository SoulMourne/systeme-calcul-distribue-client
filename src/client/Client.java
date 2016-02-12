package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] zero) {
        Socket socket;  //Création d'une variable socket
        try {
            socket = new Socket(InetAddress.getLocalHost(),2009);  //Envoi d'un socket à l'adresse LocalHost sur le port 2009
            socket.close(); //Fermeture du socket
        }catch (UnknownHostException e) //Si le serveur n'est pas trouvé
        {
            System.out.println(e.getMessage());
        }catch (IOException e) 
        {
            System.out.println(e.getMessage());
        }
    }
}