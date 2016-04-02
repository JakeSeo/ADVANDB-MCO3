package controller;

import java.awt.FlowLayout;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Transaction;

public class Controller
{
	// database manager?
	String type;
	static ArrayList<String> OnlineIPs = new ArrayList<String>();
	static ArrayList<String> OnlineNames = new ArrayList<String>();
	final static int Port = 1234;
	
	public Controller(String type)
	{
		this.type = type;
		// instantiate database manager
	}
	
	public String getType()
	{
		return type;
	}
	
	public void add(String ip, String name)
	{
		OnlineIPs.add(ip);
		OnlineNames.add(name);
	}
	
	// Send POST notification
	public static void READ(String message)
	{
		System.out.println("READ (start)");
		Socket SOCK;
		try {
			SOCK = new Socket(OnlineIPs.get(0), Port);					// Open socket
			PrintWriter OUT = new PrintWriter(SOCK.getOutputStream());
			OUT.println("\"READ\" " + message + "\0"); 					// Send message
			OUT.flush();		
			SOCK.close(); 												// Close socket	

		}
		catch (Exception e) {
			e.printStackTrace();
		} 
		System.out.println("READ (end)");
	 }

	// Receive POST notification
		public static void ReadingAction(String ip, byte [] bytes, String senderip)
		{
			System.out.println("ReadAction (start)");
			
			// Convert bytes to string
			String string = "";
			try {
				string = new String(bytes, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			// Get message (until NULL or EOF)
			String message = getUntilSpaceOrNull(string, 'e');
			
			
			// If character after message is null, display regular message
			System.out.println(ip + " " +  message);
			
			
			
			System.out.println("ReadAction (end)");
		}
		

		// Returns string cut off at either space, null or eof, depending on c
		public static String getUntilSpaceOrNull(String bytesinstring, char c){
			System.out.println("getUntilSpaceOrNull");
			
			int i = 0;											// Character index
			char [] bytesinchar = bytesinstring.toCharArray();	// String converted to char array
			 
			// Space or null
			if(c == 'b')
				while(bytesinchar[i] != ' ' && bytesinchar[i] != '\0')
					i++;
			  
			// Null
			else if(c == 'n')
				while(bytesinchar[i] != '\0')
					i++;
			  
			// Space
			else if(c == 's')
				while(bytesinchar[i] != ' ')
					i++;
			  
			// Null or EOF
			else if(c == 'e')
				while(bytesinchar[i] != '\0' && bytesinchar[i] != '\u001a')
					i++;
			  
			// Return cut up string
			return bytesinstring.substring(0, i);
		}
}
