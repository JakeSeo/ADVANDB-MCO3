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
		Server SER = new Server(PORT);
		Thread X = new Thread(SER);
		X.start();	// Runs the server process
		
		Controller con = new Controller("Central");
		con.add("192.168.1.102", "Palawan");
		MainGUI mainGUI = new MainGUI(con);
	}
}
