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

	public Client(Controller c, Socket X) {
		this.c = c;
		RECEIVE(X); // pinasa na yung socket :)

	}

	void RECEIVE(Socket S) {
		try {

			InputStream input = S.getInputStream();

			byte[] scannedbytes = new byte[655000];
			int bytesRead = input.read(scannedbytes, 0, scannedbytes.length);
			int current = bytesRead;

			do {
				bytesRead = input.read(scannedbytes, current, scannedbytes.length - current);
				if (bytesRead > -1)
					current += bytesRead;
				} while (bytesRead > 0);

		

			String InputCommand = new String(scannedbytes, "UTF-8");

			
			if (InputCommand.substring(0, 14).equals("\"TRANSACTION\" ")) {
				c.receiveTransaction(S.getInetAddress().toString().substring(1),
						Arrays.copyOfRange(scannedbytes, InputCommand.substring(0, 14).getBytes().length, current));
			} else if (InputCommand.substring(0, 20).equals("\"SENDTABLECONTENTS\" ")) {
				c.receiveTableContents(S.getInetAddress().toString().substring(1), Arrays.copyOfRange(scannedbytes, InputCommand.substring(0, 20).getBytes().length, current));
                        } else if (InputCommand.substring(0, 10).equals("\"OKWRITE\" ")) {
				c.receiveTransaction(S.getInetAddress().toString().substring(1),
						Arrays.copyOfRange(scannedbytes, InputCommand.substring(0, 10).getBytes().length, current));
			} else if (InputCommand.substring(0, 11).equals("\"OKCOMMIT\" ")) {
				c.commit(S.getInetAddress().toString().substring(1),
						Arrays.copyOfRange(scannedbytes, InputCommand.substring(0, 11).getBytes().length, current));
			}
		} catch (Exception x) {
			x.printStackTrace();

		}
	}
}
