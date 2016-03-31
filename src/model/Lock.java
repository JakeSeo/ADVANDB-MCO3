package model;

import java.util.ArrayList;

public class Lock {

	private int read;
	private int write;
	private ArrayList<Transaction> transactions;
	
	public Lock()
	{
		read = 0;
		write = 0;
		transactions = new ArrayList<Transaction>();
	}
	
	public boolean unlock(Transaction t)
	{
		boolean status = false;
		transactions.remove(t);
		if(write == 1)
		{
			write = 0;
		}else
		{
			read--;
		}
		
		notifyAll();
		
		return status;
	}
	
	public synchronized boolean writelock(Transaction t)
	{
		boolean status = false;
		
		while(write == 1 || read > 0)
		{
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		write = 1;
		transactions.add(t);
		
		return status;
	}
	
	public synchronized boolean readlock(Transaction t)
	{
		boolean status = false;
		
		while(write == 1)
		{
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		read++;
		transactions.add(t);
		
		return status;
	}
}

