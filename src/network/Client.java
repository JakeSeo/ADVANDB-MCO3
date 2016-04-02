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
		  	RECEIVE(X); // pinasa na yung socket :)
		  	this.c = c;
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
				
			   
				if(InputCommand.substring(0, 7).equals("\"READ\" "))
				{
					System.out.println("Client receive (enter READ)");
					c.ReadingAction(S.getInetAddress().toString().substring(1), Arrays.copyOfRange(scannedbytes, InputCommand.substring(0,7).getBytes().length, current), "localhost");
				}
			}
			catch(Exception x){
				System.out.println(x);
			}
	 }	 
}
