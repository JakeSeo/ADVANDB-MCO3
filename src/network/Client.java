package network;

import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JOptionPane;

import controller.Controller;

public class Client {
	private Controller c;
	 public Client(Controller c, Socket X)
	 {
		  	this.c = c;
		  	RECEIVE(X); // pinasa na yung socket :)

	 }


	 void RECEIVE(Socket S){
			try{
				System.out.println("Client receive (start)");
				
				InputStream input = S.getInputStream();
							
				System.out.println("Client receive (read bytes)");
				
				byte [] scannedbytes = new byte [65500];
			    int bytesRead = input.read(scannedbytes,0,scannedbytes.length);
			    int current = bytesRead;
			    
			    System.out.println("Client receive (continue to read bytes)");
			    
			    do{
			    	System.out.println("Client receive (in do while)");
			    	bytesRead = input.read(scannedbytes,current,scannedbytes.length - current);
			    	if(bytesRead > -1)
			    		current += bytesRead;
			    	System.out.println("Client receive (read " + bytesRead + "/" + current + " bytes)");
			    } while(bytesRead > 0);
			    
			    System.out.println("Client receive (bytes read: " + current + ")");
			    
			    String InputCommand = new String(scannedbytes, "UTF-8");
			    
			    System.out.println("Client receive (input string start: " + InputCommand.substring(0,5) + ")");
				
			   
				if(InputCommand.substring(0, 14).equals("\"TRANSACTION\" "))
				{
					System.out.println("Client receive (enter READ)");
					c.receiveTransaction(S.getInetAddress().toString().substring(1), Arrays.copyOfRange(scannedbytes, InputCommand.substring(0,14).getBytes().length, current), "localhost");	
				} else if(InputCommand.substring(0, 20).equals("\"SENDTABLECONTENTS\" "))
				{
					c.receiveTableContents(S.getInetAddress().toString().substring(1), InputCommand.substring(20, 22), Arrays.copyOfRange(scannedbytes, InputCommand.substring(0,22).getBytes().length, current));
				}
//				if(InputCommand.substring(0, 16).equals("\"SENDRESULTSET\" "))
//				{
//					System.out.println("Client receive (enter SENDRESULTSET)");
//				 	c.FileAction(S.getInetAddress().toString().substring(1), Arrays.copyOfRange(scannedbytes, InputCommand.substring(0,16).getBytes().length, current));
//				}
			}
			catch(Exception x){
				x.printStackTrace();
				
			}
	 }	 
}
