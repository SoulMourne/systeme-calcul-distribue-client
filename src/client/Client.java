package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] zero) {
        Socket socket;  //Création d'une variable socket
		BufferedReader in;  // Flux entrant
        PrintWriter out;    // Flux sortant
        try {
            socket = new Socket("192.168.0.94",2010);  //Envoi d'un socket à l'adresse LocalHost sur le port 2009
			in = new BufferedReader (new InputStreamReader (socket.getInputStream()));  // Déclaration du flux entrant
            out = new PrintWriter(socket.getOutputStream());    // Déclaration du flux sortant
            out.println("Message pour le serveur"); // Envoie d'un message au serveur
            out.flush();
            socket.close(); //Fermeture du socket
        }catch (UnknownHostException e) //Si le serveur n'est pas trouvé
        {
            e.printStackTrace();
        }catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}
