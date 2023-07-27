package kc_energy_v2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;

public class PaymentPanel extends JPanel{
	private JTextField remainingDueField, newPayAmountField;
	private JPanel centerPanel, northPanel, southPanel;
	private JLabel accountInfoLabel, lastPaymentLabel, remainingDueLabel,newPayLabel, newPayDateLabel, confirmLabel;
	private JButton processButton;
	private Statement statement;
	private Accounts account;
	private JDateChooser dateChooser;
	private MainFrame mainFrame;
	PaymentPanel(Statement stm, MainFrame mainFrame) throws SQLException{
		 statement = stm;
		 this.mainFrame = mainFrame;
		/* Build North panel */
		
	    accountInfoLabel = new JLabel();
	    accountInfoLabel.setFont(new Font("Times New Roman",Font.BOLD,15));
	    
	    northPanel = new JPanel();
		northPanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		northPanel.setPreferredSize(new Dimension(430,40));
		
		northPanel.add(accountInfoLabel);
		
		/* Build Center panel */
		
		lastPaymentLabel = new JLabel("Last payment: N/A");
		lastPaymentLabel.setFont(new Font("Times New Roman",Font.PLAIN,15));
		lastPaymentLabel.setPreferredSize(new Dimension(300,15));
		lastPaymentLabel.setHorizontalAlignment(JLabel.CENTER);
		
		
		remainingDueLabel = new JLabel("Remaining Balance: ");
		remainingDueLabel.setFont(new Font("Times New Roman",Font.PLAIN,15));
		
		remainingDueField = new JTextField(8);
		remainingDueField.setHorizontalAlignment(JTextField.CENTER);
		remainingDueField.setBackground(null);
		remainingDueField.setEditable(false);
		
		centerPanel= new JPanel();
		centerPanel.setPreferredSize(new Dimension(370,80));
		centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER,15,17));
		
		centerPanel.add(lastPaymentLabel);
		centerPanel.add(remainingDueLabel);
		centerPanel.add(remainingDueField);
		
		/* Build South panel */
		
		newPayLabel = new JLabel("New payment:");
		newPayLabel.setFont(new Font("Times New Roman",Font.BOLD,17));
		
		newPayAmountField = new JTextField(5);
		
		newPayDateLabel = new JLabel("Date:");
		newPayDateLabel.setFont(new Font("Times New Roman",Font.BOLD,17));
 
		Date date = new Date();
		Calendar maxDate = Calendar.getInstance();
		maxDate.add(Calendar.MONTH, 1);
		dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setDate(date);
		dateChooser.setMinSelectableDate(date);
		dateChooser.setMaxSelectableDate(maxDate.getTime());
		
		processButton = new JButton("Process");
		processButton.addActionListener(new actionListener());
		
		confirmLabel = new JLabel();
		confirmLabel.setFont(new Font("Times New Roman",Font.PLAIN,15));
		confirmLabel.setForeground(Color.blue);
		confirmLabel.setPreferredSize(new Dimension(400,20));
		confirmLabel.setHorizontalAlignment(JLabel.CENTER);
		
		
		southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		southPanel.setPreferredSize(new Dimension(370,110));
		
		southPanel.add(newPayLabel);
		southPanel.add(newPayAmountField);
		southPanel.add(newPayDateLabel);
		southPanel.add(dateChooser);
		southPanel.add(processButton);
		southPanel.add(confirmLabel);
		
		northPanel.setBackground(Color.white);
		centerPanel.setBackground(Color.white);
		southPanel.setBackground(Color.white);
		
		/* Add all panel to frames*/
		this.setLayout(new BorderLayout());
		this.add(northPanel,BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(southPanel, BorderLayout.SOUTH);
		
		northPanel.setOpaque(false);
		centerPanel.setOpaque(false);
		southPanel.setOpaque(false);
		this.setOpaque(false);
		
		
	}
	/* Display an account information 
	 * @param {acc} an object holds account information
	 * */
	public void showAccountInfo(Accounts acc) {
		confirmLabel.setText("");
		processButton.setEnabled(true);
		account = acc;
	    accountInfoLabel.setText(account.getName() + ", " + account.getAddress());
		lastPaymentLabel.setText("Last payment of $" + account.getLastPayment() + " received on " + account.getLastPaymentDate());
		remainingDueField.setText("$" + account.getRemainingDue());
		if (account.getRemainingDue() == 0) {
			processButton.setEnabled(false);
			confirmLabel.setText("Payment is not required at this time.");
		}
	}
	private class actionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			/* process new payment */
			if(e.getSource() == processButton) {
				if("".equals(newPayAmountField.getText())) {
					JOptionPane.showMessageDialog(null, "Please enter payment amount!","Missing info", JOptionPane.WARNING_MESSAGE);
				}
				else {
					try {
						double newPayAmt = Double.parseDouble(newPayAmountField.getText());
						if (newPayAmt > account.getRemainingDue())
							JOptionPane.showMessageDialog(null, "Payment amount can not be greater than amount due!","Check payment amount", JOptionPane.WARNING_MESSAGE);
						else {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String choseDate = sdf.format(dateChooser.getDate());
						account.setLastPayment(newPayAmt);
						account.setRemainingDue(MainFrame.round(account.getRemainingDue()-account.getLastPayment()));
						account.setLastPaymentDate(choseDate);
						try {
							statement.execute("update accounts set lastPayment = " + account.getLastPayment()+", remainingDue = " + account.getRemainingDue()
											+ ", lastPaymentDate = \"" + account.getLastPaymentDate()+"\" where accountNumber = " + account.getAccountNumber()+";");
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						confirmLabel.setText("Payment received! Remaining balance is: $" + MainFrame.round(account.getRemainingDue()));
						newPayAmountField.setText("");
						remainingDueField.setText(""+account.getRemainingDue());
						lastPaymentLabel.setText("Last payment of $" + account.getLastPayment() + " received on " + account.getLastPaymentDate());
						mainFrame.getDashBoardPanel().showAccountInfo(account);
						mainFrame.getBillingPanel().setAccount(account);
						}
					}
					catch(NumberFormatException e2){
						JOptionPane.showMessageDialog(null, "That is not a number!","Check payment amount", JOptionPane.WARNING_MESSAGE);
					}
				}
			}	
		}
	}
	
}