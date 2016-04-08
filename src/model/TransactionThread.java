package model;

import controller.Controller;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionThread implements Runnable {

	private Transaction t;
	private Controller c;
	private String ip;
	private CyclicBarrier cb;

	public TransactionThread(Transaction t, Controller c, String ip, CyclicBarrier cb) {
		this.t = t;
		this.c = c;
		this.ip = ip;
		this.cb = cb;
	}

	@Override
	public void run() {
		t.beginTransaction();
		String query = "";
		if (t.getQuery().startsWith("UPDATE")) {
			//t.lockTable("WRITE");
			query = t.updateData(t);
		    if(c.getType().equals("Central") && t.getTransType().equals("U"))
		    {
		        t.setTransType("G");
		        c.okWrite(t);
		    }
			else
		    {
				System.out.println("About to commit ");
				t.unlockTable();
				t.commit();
				c.okCommit(t);
		    }
		} else {
			//t.lockTable("READ");
			TableContents tc = t.getData(t);
			//t.unlockTable();
			c.sendTableContents(tc, t.getTransType(), ip);
		}

	}

}
