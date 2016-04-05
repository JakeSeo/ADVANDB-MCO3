package model;

import controller.Controller;

public class TransactionThread implements Runnable{
	
	private DBModel db;
	private Transaction t;
	private Controller c;
	private String ip;
	
	public TransactionThread(Transaction t, Controller c, String ip) {
		this.t = t;
		this.c = c;
		this.ip = ip;
		db = DBModel.getInstance();
	}

	@Override
	public void run() {
		TableContents tc = db.getData(t.getName(), t.getQuery());
		
		c.sendTableContents(tc, ip);
		
	}

}
