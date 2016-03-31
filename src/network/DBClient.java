package network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class DBClient implements Runnable {
	Socket SOCKET;
	Scanner INPUT;
	Scanner SEND = new Scanner(System.in);
	PrintWriter OUT;
	
	public DBClient(Socket S)
	{
		this.SOCKET = S;
	}
	
	public void run()
	{
		try
		{
			try
			{
				INPUT = new Scanner(SOCKET.getInputStream());
				OUT = new PrintWriter(SOCKET.getOutputStream());
				OUT.flush();
				CheckStream();
			}
			finally
			{
				SOCKET.close();
			}
		}
		catch(Exception X)
		{
			System.out.print(X);
		}
		
	}
	
	public void DISCONNECT() throws IOException
	{
		//OUT.println(DBClientGUI.UserName + " has disconnected");
		//OUT.flush();
		SOCKET.close();
		JOptionPane.showMessageDialog(null, "You disconnected!");
		System.exit(0);
	}
	
	public void CheckStream()
	{
		while(true)
		{
			RECEIVE();
		}
	}
	
	public void RECEIVE()
	{
		if(INPUT.hasNext())
		{
			String MESSAGE = INPUT.nextLine();
			
			if(MESSAGE.contains("#?!"))
			{
				String TEMP1 = MESSAGE.substring(3);
				TEMP1 = TEMP1.replace("[", "");
				TEMP1 = TEMP1.replace("]", "");
				String[] CurrentUsers = TEMP1.split(", ");
				
				//DBClientGUI.JL_ONLINE.setListData(CurrentUsers);
			}
			else
			{
				//DBClientGUI.TA_CONVERSATION.append(MESSAGE + "\n");
			}
		}
	}
	
	public void SEND(String X)
	{
		OUT.println(DBClientGUI.UserName + ": " + X);
		OUT.flush();
		//DBClientGUI.TF_Message.setText("");
	}

}
