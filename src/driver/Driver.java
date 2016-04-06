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

		//String str = "SELECT * FROM db WHERE id = 1;";
		//System.out.println("Hi " + str.substring(str.indexOf("WHERE"), str.length()));
		
		/*String str = "UPDATE table hpq_crop SET id = 353181, croptype = \"Magic\" FROM db WHERE id = 1;";
		System.out.println("Hi " + str.substring(str.indexOf("SET")+4, str.indexOf("FROM"))+"x");
		System.out.println("Hi " + str.substring(str.indexOf("WHERE")+6, str.length()-1)+"x");
		System.out.println("Hi" + str.substring(str.indexOf("FROM")+4, str.indexOf("WHERE")+6));
		
		System.out.println("SELECT * "
				+ str.substring(str.indexOf("FROM"), str.indexOf("WHERE")+6)
				+ str.substring(str.indexOf("SET")+4, str.indexOf("FROM"))
				);*/
		
		//Controller con = new Controller("192.168.1.138", "Marinduque");
		Controller con = new Controller("10.2.181.98", "Marinduque");
		Server SER = new Server(con, PORT);
		Thread X = new Thread(SER);
		X.start();	// Runs the server process
		
		con.add("192.168.1.148", "Palawan");
		con.add("5", "Central");
		MainGUI mainGUI = new MainGUI(con);
		
		/*Controller con = new Controller("192.168.1.138", "Central");
		con.add("192.111.1.1", "Palawan");
		System.out.println("Alive ba -> " + con.isAlive("192.168.1.138"));*/
		
	}
}
