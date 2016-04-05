package model;

import controller.Controller;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionThread implements Runnable{
	
	private DBModel db;
	private Transaction t;
	private Controller c;
	private String ip;
        private CyclicBarrier cb;
	
	public TransactionThread(Transaction t, Controller c, String ip, CyclicBarrier cb) {
		this.t = t;
		this.c = c;
		this.ip = ip;
		db = DBModel.getInstance();
                this.cb = cb;
	}

	@Override
	public void run() {
            try {
                System.out.println("Waiting ...");
                cb.await();
                System.out.println("G");
                TableContents tc = db.getData(t.getName(), t.getQuery());
                c.sendTableContents(tc, ip);
                
                System.out.println("START");
                
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (BrokenBarrierException ex) {
                ex.printStackTrace();
            }
		
	}

}
