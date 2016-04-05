package driver;

import controller.Controller;
import network.Server;
import view.MainGUI;

public class Driver
{
	final static int PORT = 1234;
	/*public static void main(String[] args)
	{
		MainGUI mainGUI = new MainGUI(new Controller());
	}*/
	
	public static void main(String[] args){
		
		// Starting the Server

		Controller con = new Controller("192.168.0.111", "Central");
		Server SER = new Server(con, PORT);
		Thread X = new Thread(SER);
		X.start();	// Runs the server process
		
		//con.add("192.168.1.115", "Central");
		MainGUI mainGUI = new MainGUI(con);
	}
}
