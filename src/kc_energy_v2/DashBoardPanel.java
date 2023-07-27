package kc_energy_v2;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;


public class DashBoardPanel extends JPanel{
	private JPanel northPanel, centerPanel, innerCenterPanel, eastPanel;
	private JTextArea northText, eastText;
	private JLabel currentPeriodLabel, lastPaymentLabel, remainingDueLabel;
	private Accounts account;
	private double price1, price2, price3;
	DashBoardPanel() {
		
		this.setPreferredSize(new Dimension(600,350));
		
		/* Build North Panel */
		northPanel = new JPanel();
		northPanel.setPreferredSize(new Dimension(300,50));
		northPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
		
		northText = new JTextArea();
		northText.setBackground(null);
		northText.setFont(new Font("Times New Roman", Font.BOLD,15));
		northText.setEditable(false);
		
		
		northPanel.add(northText);
		
		/* Build Center Panel */
		
		centerPanel = new JPanel();
		
		currentPeriodLabel = new JLabel();
		currentPeriodLabel.setFont(new Font("Times New Roman", Font.PLAIN,15));
		
		remainingDueLabel = new JLabel();
		remainingDueLabel.setFont(new Font("Times New Roman", Font.BOLD,30));
		
		lastPaymentLabel = new JLabel();
		lastPaymentLabel.setFont(new Font("Times New Roman", Font.PLAIN,15));
		
		innerCenterPanel = new JPanel();
		innerCenterPanel.setPreferredSize(new Dimension(290,270));
		innerCenterPanel.setBorder(BorderFactory.createTitledBorder(""));
		innerCenterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 50));
		
		innerCenterPanel.add(currentPeriodLabel);
		innerCenterPanel.add(remainingDueLabel);
		innerCenterPanel.add(lastPaymentLabel);
		
		centerPanel.add(innerCenterPanel);
		
		/* Build East Panel */
		
		eastPanel = new JPanel();
		
		eastText = new JTextArea();
		eastText.setBackground(null);
		eastText.setFont(new Font("Times New Roman", Font.PLAIN,20));
		eastText.setBorder(BorderFactory.createTitledBorder(""));
		eastText.setPreferredSize(new Dimension(290,270));
		eastText.setEditable(false);
		
		eastPanel.add(eastText);
		
		/* Add all panel to frames*/
		this.setLayout(new BorderLayout());
		add(northPanel,BorderLayout.NORTH);
		add(eastPanel,BorderLayout.EAST);
		add(centerPanel,BorderLayout.CENTER);
		
//		centerPanel.setBackground(Color.white);
//		eastPanel.setBackground(Color.white);
//		northPanel.setBackground(Color.white);
//		innerCenterPanel.setBackground(Color.white);
		
		centerPanel.setOpaque(false);
		eastPanel.setOpaque(false);
		eastText.setOpaque(false);
		northPanel.setOpaque(false);
		innerCenterPanel.setOpaque(false);
		
		this.setOpaque(false);
		setVisible(true);
		
	}
	/* display an account information 
	 * @param {acc} an object hold account information
	 * */
	public void showAccountInfo(Accounts acc) {
		account = acc;
		northText.setText("Full Name: " + account.getName() + "     Address: " +account.getAddress());
		updateInnerCenterPanel();
		updateEastTextArea();
	}
	/* Update information in the left part when an account is retrieved 
	 * */
	private void updateInnerCenterPanel() {
		SimpleDateFormat dF = new SimpleDateFormat("MM/dd/yyyy");
		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date lastDate = c.getTime();
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date firstDate = c.getTime();
		currentPeriodLabel.setText("Current Period: "+ dF.format(firstDate) + " - " + dF.format(lastDate));
		remainingDueLabel.setText("Total Due: $" + MainFrame.round(account.getRemainingDue()));
		lastPaymentLabel.setText("Last payment made on " + account.getLastPaymentDate());
	}
	
	/* Update information in the right part when an account is retrieved */
	private void updateEastTextArea() {
		eastText.setText("\n   Current usage: " + MainFrame.round(account.getCurrentUsage()) + " kwh\n\n "+
		         "  Last month usage: " + MainFrame.round(account.getLastUsage()) + " kwh\n\n "+   // hold ALT and type 0162 for cent symbol
				 "  Price first 600 kwh: $" + price1 +"/kwh\n\n "+
		         "  Price next 400 kwh:  $" + price2 + "/kwh\n\n "+
				 "  Price over 1000 kwh: $" + price3 + "/kwh  \n");
	}
	
}