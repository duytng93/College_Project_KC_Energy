package kc_energy_v2;

import java.awt.Color;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.*;

/* this class is used to create and then remove message when new message comes in */
public class runnable implements Runnable {
	private static Lock lock = new ReentrantLock();
	private static Condition waitToDispose = lock.newCondition();
	private String newMessage;
	private JPanel panel;
	runnable(String newM, JPanel pa){
		newMessage = " <"+newM+"> ";
		panel = pa;
	}
	
	@Override
	public void run() {
		lock.lock();
		String message ="";
		message+=newMessage;
		JLabel newLabel = new JLabel(message);
		newLabel.setForeground(Color.blue);
		panel.add(newLabel);
		panel.revalidate();
		waitToDispose.signal();
	
		try {
			waitToDispose.await(); // (***) this message will wait until the next message come in because the next message will execute signal() method
		} catch (InterruptedException e1) { // Then it automatically acquire the lock again and continue executing
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally {
			lock.unlock();   // we need to release the lock here because of the reason (***). otherwise, the lock will locked forever
		}
		
		newLabel.setForeground(Color.black);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		for(int i = 0; i<newMessage.length();i++) {
			try {
				message = message.replaceFirst(String.valueOf(newMessage.charAt(i)), "");
				newLabel.setText(message);
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		panel.remove(newLabel);
	}

}
