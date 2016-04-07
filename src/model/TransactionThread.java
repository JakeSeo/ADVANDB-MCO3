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
		try {
			System.out.println("Waiting ...");
			cb.await();
			System.out.println("G");
			String query = "";
			if (t.getQuery().startsWith("UPDATE")) {
				System.out.println("WENT IN UPDATE ");
				t.lockTable("WRITE");
				query = t.updateData(t);
                if(c.getType().equals("Central") && t.getTransType().equals("U"))
                {
                    System.out.println("CENTRAL AKO *******************");
                    t.setTransType("G");
                    c.okWrite(t);
                }
				else
                {
					//t.commit();
					t.endTransaction();
					t.setQuery(query);
					t.lockTable("READ");
					TableContents tc = t.getData(t);
					t.unlockTable();
					c.okCommit(t);
					/*String ipAdd = "";
					if(t.getDatabase().equals(c.getType())) {
						ipAdd = c.getIp();
					} else {
						if(c.getType().equals("Palawan")) {
							ipAdd = c.getMarin().getIpadd();
						} else if(c.getType().equals("Marinduque")) {
							ipAdd = c.getPalawan().getIpadd();
						}
					}
					c.sendTableContents(tc, t.getTransType(), ipAdd);*/
                }
			} else {
				System.out.println("GONNA READ NOW ");
				t.lockTable("READ");
				TableContents tc = t.getData(t);
				t.unlockTable();
				c.sendTableContents(tc, t.getTransType(), ip);
				System.out.println("START");
			}

		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} catch (BrokenBarrierException ex) {
			ex.printStackTrace();
		}

	}

}
