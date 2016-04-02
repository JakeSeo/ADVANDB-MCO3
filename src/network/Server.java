package network;

import java.net.ServerSocket;
import java.net.Socket;

import controller.Controller;

public class Server implements Runnable {

	int port;
	static Client c;
	Controller controller;
	
	public Server(Controller controller, int port)
	{
		this.controller = controller;
		this.port = port;
	}
	
	// Thread process
	 public void run(){
		 	System.out.println("Server (start thread)");
			try{

				ServerSocket SS = new ServerSocket(port); //Opens the server to connection

				while(true){	// Waits for connections
					Socket X = SS.accept(); 	// Accepts connections
					c = new Client(controller, X); 			// Creates a new client for each connection
					System.out.println("Server (new client)");
				}
			}
			catch(Exception ex){
				System.out.println(ex);
			}
			System.out.println("Server (end thread)");
	 }
}
