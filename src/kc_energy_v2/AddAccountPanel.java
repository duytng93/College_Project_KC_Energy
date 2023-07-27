package kc_energy_v2;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


import java.sql.*;

public class AddAccountPanel extends JPanel{
	
	private JTextArea questions;
	private JTextField field1, field2, field2_1, field2_2, field2_3, field3,field4,field5,field6;
	private JPanel centerPanel, eastPanel, southPanel, westPanel, cityStatePanel;
	private JButton addAccountButton, modifyAccountButton, applyChangesButton, clearButton;
	private JComboBox<String> plansComboBox;
	private Accounts account;
	private Statement statement;
	private ResultSet resultSet;
	private JPanel messagePanel;
	private MainFrame mainFrame;
	AddAccountPanel( Statement statement, JPanel messagePanel,MainFrame mainFrame){
		this.messagePanel = messagePanel;
		this.statement = statement;
		this.mainFrame = mainFrame;
		/* Build Center Panel */
		
		centerPanel = new JPanel();
		centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10,8));
		
		questions = new JTextArea();
		questions.setBackground(null);
		questions.setFont(new Font("Times New Roman", Font.BOLD,15));
		questions.setEditable(false);;
		questions.setText("Name: \n\n"
				+ "Address: \n\n"
				+ "City, State, Zipcode: \n\n"
				+ "Phone#: \n\n"
				+ "Price first 600 kWh:\n\n"
				+ "Price next 400 kWh:\n\n"
				+ "Price over 1000 kWh:");
		
		centerPanel.add(questions);
		
		/* Build East Panel */
		
		eastPanel = new JPanel();
		eastPanel.setPreferredSize(new Dimension(220,60));
		eastPanel.setLayout(new FlowLayout(FlowLayout.LEFT,0,9));
		
		cityStatePanel = new JPanel();
		cityStatePanel.setPreferredSize(new Dimension(220,25));
		cityStatePanel.setLayout(new FlowLayout(FlowLayout.LEADING,1,0));
		cityStatePanel.setBackground(Color.WHITE);
		
		field1 = new JTextField(16);
		field2 = new JTextField(16);
		field2_1 = new JTextField(6);
		field2_2 = new JTextField(3);
		field2_3 = new JTextField(3);
		field3 = new JTextField(16);
		field4 = new JTextField(16);
		field4.setEditable(false);
		field5 = new JTextField(16);
		field5.setEditable(false);
		field6 = new JTextField(16);
		field6.setEditable(false);
		
		cityStatePanel.add(field2_1);
		cityStatePanel.add(field2_2);
		cityStatePanel.add(field2_3);
		
		
		eastPanel.add(field1);
		eastPanel.add(field2);
		eastPanel.add(cityStatePanel);
		eastPanel.add(field3);
		eastPanel.add(field4);
		eastPanel.add(field5);
		eastPanel.add(field6);
		
		/* Build West Panel */
		
		westPanel = new JPanel();
		westPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,112));
		westPanel.setPreferredSize(new Dimension(135,60));
		
		String[] plans = {"Choose a plan", "resident", "business"};
		plansComboBox = new JComboBox<String>(plans);
		plansComboBox.addActionListener(new actionListener());
		
		westPanel.add(plansComboBox);
		
		/* Build South Panel */
		
		southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout(FlowLayout.CENTER,15,14));
		southPanel.setPreferredSize(new Dimension(100,60));
		
		addAccountButton = new JButton("Add");
		addAccountButton.addActionListener(new actionListener());
		
		clearButton = new JButton("Clear");
		clearButton.addActionListener(new actionListener());
		
		modifyAccountButton = new JButton("Modify");
		modifyAccountButton.setEnabled(false);
		modifyAccountButton.addActionListener(new actionListener());
		
		applyChangesButton = new JButton("Apply");
		applyChangesButton.setVisible(false);
		applyChangesButton.addActionListener(new actionListener());
		
		southPanel.add(addAccountButton);
		southPanel.add(clearButton);
		southPanel.add(modifyAccountButton);
		southPanel.add(applyChangesButton);

		centerPanel.setOpaque(false);
		eastPanel.setOpaque(false);
		southPanel.setOpaque(false);
		westPanel.setOpaque(false);
		cityStatePanel.setOpaque(false);
		questions.setOpaque(false);
		
		/* Add all panels to frame */
		
		setLayout(new BorderLayout());  
		add(centerPanel, BorderLayout.CENTER);
		add(eastPanel, BorderLayout.EAST);
		add(southPanel, BorderLayout.SOUTH);
		add(westPanel,BorderLayout.WEST);
		this.setOpaque(false);
		setVisible(true);
		
	}
	
	
	/* Display an customer account information 
	 * @param {acc} an object hold account information
	 * */
	public void showAccountInfo(Accounts acc) {
		account = acc;
		String fullAddress = account.getAddress();
		addAccountButton.setEnabled(false);
		clearButton.setEnabled(false);
		modifyAccountButton.setEnabled(true);
		field1.setText(account.getName());
		field1.setEditable(false);
		field2.setText(fullAddress.substring(0,fullAddress.indexOf(',')));
		field2.setEditable(false);
		field2_1.setText(fullAddress.substring(fullAddress.indexOf(',')+2,fullAddress.lastIndexOf(',')));
		field2_1.setEditable(false);
		field2_2.setText(fullAddress.substring(fullAddress.lastIndexOf(',')+2,fullAddress.lastIndexOf(' ')));
		field2_2.setEditable(false);
		field2_3.setText(fullAddress.substring(fullAddress.lastIndexOf(' ')+1));
		field2_3.setEditable(false);
		field3.setText(account.getPhoneNumber());
		field3.setEditable(false);
		if(account.getPlan().equals("resident")) {
			plansComboBox.setSelectedItem("resident");
		}
		else if(account.getPlan().equals("business")) {
			plansComboBox.setSelectedItem("business");
		}
		plansComboBox.setEnabled(false);
		modifyAccountButton.setEnabled(true);
		applyChangesButton.setEnabled(true);
	}
	
	/* Reset the add account page to the original state */
	public void reset() {
		account = null;
		addAccountButton.setEnabled(true);
		clearButton.setEnabled(true);
		modifyAccountButton.setEnabled(false);
		field1.setText("");
		field1.setEditable(true);
		field2.setText("");
		field2.setEditable(true);
		field3.setText("");
		field3.setEditable(true);
		field2_1.setText("");
		field2_1.setEditable(true);
		field2_2.setText("");
		field2_2.setEditable(true);
		field2_3.setText("");
		field2_3.setEditable(true);
		plansComboBox.setSelectedItem("Choose a plan");
		plansComboBox.setEnabled(true);
		modifyAccountButton.setEnabled(false);
		modifyAccountButton.setVisible(true);
		applyChangesButton.setVisible(false);
		applyChangesButton.setEnabled(true);
	}
	
	private class actionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			/* add new account to the database */
			if(e.getSource() == addAccountButton) {
				try {
					if(!"".equals(field1.getText()) && !"".equals(field2.getText()) && !"".equals(field3.getText()) && plansComboBox.getSelectedIndex()!=0
							&& !"".equals(field2_1.getText())&& !"".equals(field2_2.getText())&& !"".equals(field2_3.getText())) {
						String fullAddress =  field2.getText()+", "+field2_1.getText()+", "+field2_2.getText()+" "+field2_3.getText();
						statement.executeUpdate("insert into accounts(name,address,phoneNumber,plan,"
								+"remainingDue, lastPayment, lastPaymentDate, currentUsage, lastUsage)" 
								+ "value('"+field1.getText()+"','"
								+fullAddress+"','" +field3.getText()+"','"
								+plansComboBox.getSelectedItem().toString()+"',"
								+ 0 +","+0+",\"1900-01-01\","+0+","+0+");");
						resultSet = statement.executeQuery("select accountNumber from accounts where address = '" + fullAddress+ "';");
						resultSet.next();
						(new Thread(new runnable("New account #" +resultSet.getInt("accountNumber")+ " added!",messagePanel))).start();
					}
					else {
						JOptionPane.showMessageDialog(null, "Please provide all needed information!", "Missing Info", JOptionPane.WARNING_MESSAGE);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
						
			}
			
			/* clear all the fields */
			else if(e.getSource() == clearButton) {
				field1.setText("");
				field2.setText("");
				field3.setText("");
				field1.setEditable(true);
				field2.setEditable(true);
				field3.setEditable(true);
				plansComboBox.setSelectedItem("Choose a plan");
				plansComboBox.setEnabled(true);
				addAccountButton.setEnabled(true);
				modifyAccountButton.setEnabled(false);
				(new Thread(new runnable("All information is gone!",messagePanel))).start();
			}
			
			/* choose a tariff plan */
			else if(e.getSource() == plansComboBox) {
				if(plansComboBox.getSelectedItem() == "resident")
				{
					field4.setText("0.25");
					field5.setText("0.31");
					field6.setText("0.35");
				}
				else if(plansComboBox.getSelectedItem() == "business"){
					field4.setText("0.35");
					field5.setText("0.41");
					field6.setText("0.45");
				}
				else if(plansComboBox.getSelectedItem() == "Choose a plan"){
					field4.setText("");
					field5.setText("");
					field6.setText("");
				}
			}
			
			/* modify existing account */
			else if(e.getSource() == modifyAccountButton) {
				field1.setEditable(true);
				field2.setEditable(true);
				field3.setEditable(true);
				field2_1.setEditable(true);
				field2_2.setEditable(true);
				field2_3.setEditable(true);
				(new Thread(new runnable("Fields are unlocked for modifying..!",messagePanel))).start();
				plansComboBox.setEnabled(true);
				modifyAccountButton.setVisible(false);
				applyChangesButton.setVisible(true);
			}
			
			/* apply changes to existing account */
			else if(e.getSource() == applyChangesButton) {
				String fullAddress =  field2.getText()+", "+field2_1.getText()+", "+field2_2.getText()+" "+field2_3.getText();
				if(!account.getName().equals(field1.getText())||!account.getAddress().equals(fullAddress)||!account.getPhoneNumber().equals(field3.getText())||!plansComboBox.getSelectedItem().toString().equals(account.getPlan())) {
					try {
						account.setName(field1.getText());
						account.setAddress(fullAddress);
						account.setPhoneNumber(field3.getText());
						account.setPlan(plansComboBox.getSelectedItem().toString());
						statement.execute("update accounts set name = '"+field1.getText()+"',"
										+ "address = '"+fullAddress+"',"
										+ "phoneNumber = '" + field3.getText()+"',"
										+ "plan = '" +plansComboBox.getSelectedItem().toString()+"'"
										+ "where accountNumber = " + account.getAccountNumber() + ";");
						(new Thread(new runnable("Account info updated!",messagePanel))).start();
						plansComboBox.setEnabled(false);
						modifyAccountButton.setVisible(true);
						applyChangesButton.setVisible(false);
						field1.setEditable(false);
						field2.setEditable(false);
						field2_1.setEditable(false);
						field2_2.setEditable(false);
						field2_3.setEditable(false);
						field3.setEditable(false);
						mainFrame.getDashBoardPanel().showAccountInfo(account);
						mainFrame.getPaymentPanel().showAccountInfo(account);
						mainFrame.getBillingPanel().setAccount(account);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				else {
					(new Thread(new runnable("No change detected!",messagePanel))).start();
					modifyAccountButton.setVisible(true);
					applyChangesButton.setVisible(false);
					plansComboBox.setEnabled(false);
					field1.setEditable(false);
					field2.setEditable(false);
					field3.setEditable(false);
				}
					
			}
			
			
		}
		
	}
	
}