package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client
{
	private Socket socket; // Création d'une variable socket
	private BufferedReader in; // Flux entrant
	private PrintWriter out;   // Flux sortant
	private String adresseServeur;  // Adresse IPv4 du serveur
	private int port;   // Port de connexion au serveur

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
			e.printStackTrace();
		}
	}

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
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String lectureMessage()
	{
		try
		{
			in = new BufferedReader(new InputStreamReader (this.socket.getInputStream())); //permet de lire les caractères provenant du socket
			return in.readLine();   //Renvoie le contenu de in
		}
		catch (IOException e)   //En cas d'erreur
		{
			e.printStackTrace();
			return null;
		}
	}

	public void fermetureClient()
	{
		try
		{
			socket.close();   //Ferme le socket
		}
		catch (IOException ex)  //En cas d'erreur
		{
			ex.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		Client c = new Client("192.168.0.94", 2010);
		if (c.envoiMessage("Bonjour, je suis un client."))
			System.out.println("Message du serveur : " + c.lectureMessage());
		else
			System.out.println("Erreur lors de l'envoie du message");
		c.fermetureClient();
	}
}
