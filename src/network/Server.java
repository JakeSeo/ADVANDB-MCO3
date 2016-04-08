package network;

import java.net.ServerSocket;
import java.net.Socket;

import controller.Controller;

public class Server implements Runnable {

	int port;
	static Client c;
	Controller controller;

	public Server(Controller controller, int port) {
		this.controller = controller;
		this.port = port;
	}

	// Thread process
	public void run() {
		try {

			ServerSocket SS = new ServerSocket(port); // Opens the server to
														// connection

			while (true) { // Waits for connections
				Socket X = SS.accept(); // Accepts connections
				c = new Client(controller, X);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
