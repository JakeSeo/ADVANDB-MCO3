package driver;

import controller.Controller;
import view.MainGUI;

public class Driver
{
	public static void main(String[] args)
	{
		MainGUI mainGUI = new MainGUI(new Controller());
	}
}
