package kc_energy_v2;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.*;
import java.util.Random;

import javax.swing.*;
import javax.swing.text.BadLocationException;


public class MainFrame extends JFrame {
	private JPanel northPanel, westPanel, southPanel;
	private BackgroundPanel centerPanel;
	private Random rand = new Random();
	private JPanel blankPanel;
	private AddAccountPanel addAccountPanel;
	private DashBoardPanel dashBoardPanel;
	private BillingPanel billingPanel;
	private PaymentPanel paymentPanel;
	private JLabel accountLabel, backgroundLabel;
	private MovingLabel addAccountLabel, modifyAccountLabel, paymentLabel, dashBoardLabel, invoiceLabel;
	private JTextField getField;
	private JButton getAccountButton, searchAccountButton, resetButton, deleteAccountButton, helpButton;
	private Statement statement;
	private Accounts account;
	private boolean helpMode = false;
	private boolean labelsShowed = false;
	private Color blueColor = new Color(64, 189, 247);
	private Font fontBold = new Font("Arial", Font.BOLD, 12);
	private Font fontPlain = new Font("Arial", Font.PLAIN, 12);
	private CardLayout CL;

	MainFrame(Connection connection) throws SQLException{
		
		/* Retrieve a connection object from LoginFrame */
		statement = connection.createStatement(); 
		 
		/* window basic settings */
		setTitle("KC Energy");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(730,467);
		this.setIconImage(new ImageIcon("Icons/Icon.jpg").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
		
		/* Build North Panel */
		
		northPanel = new JPanel();
		northPanel.setPreferredSize(new Dimension(520,45));
		northPanel.setLayout(null);
		northPanel.setBackground(Color.white);
		
		accountLabel = new JLabel("               Account#:");
		//accountLabel.setBounds(5, 5, 105, 30);  desired location
		accountLabel.setBounds(505, 5, 105, 30);
		
		getField = new JTextField();
		//getField.setBounds(110 + 10,5, 70, 30); desired location // getFieldX = accountLabel(X+W) + 10 = 120
		getField.setBounds(500+110+10,5, 70, 30);
		
		getAccountButton = new JButton("Get");
		getAccountButton.addActionListener(new actionListener());
		getAccountButton.setBackground(blueColor);
		getAccountButton.setForeground(Color.white);
		getAccountButton.setFont(fontBold);
		//getAccountButton.setBounds(120+70+10,5,70,30); desired location // getAccountButtonX = getField(X+W) + 10 = 200
		getAccountButton.setBounds(500+120+70+10,5,70,30);
		
		//keybind for get button
		getAccountButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed ENTER"), "get");
		getAccountButton.getActionMap().put("get", new GetAccountAction());
		
		ImageIcon searchIcon = new ImageIcon(new ImageIcon("Icons/searchAccountIcon.png").getImage().getScaledInstance(17, 17, Image.SCALE_DEFAULT)); 
		searchAccountButton = new JButton(searchIcon);
		searchAccountButton.addActionListener(new actionListener());
		//searchAccountButton.setBounds(200+70+10,5,30,30); desired location // searchAccountButton = getAccountButton(X+W) + 10 = 280
		searchAccountButton.setBounds(500+200+70+10,5,30,30);
		
	    resetButton = new JButton("Reset");
	    resetButton.addActionListener(new actionListener());
	    resetButton.setEnabled(false);
	    //resetButton.setBounds(280+30+10, 5, 65, 30); desired location
	    resetButton.setBounds(500+280+30+10, 5, 65, 30);
	    
	    
		deleteAccountButton = new JButton("Delete");
		deleteAccountButton.setEnabled(false);
		deleteAccountButton.addActionListener(new actionListener());
		deleteAccountButton.setForeground(Color.red);
		//deleteAccountButton.setBounds(320+65+10,5,67,30); desired location
		deleteAccountButton.setBounds(500+320+65+10,5,67,30);
		
		ImageIcon helpIcon = new ImageIcon(new ImageIcon("Icons/help-icon.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)); 
		helpButton = new JButton(helpIcon);
		helpButton.setFocusable(false);
		helpButton.setPreferredSize(new Dimension(19,19));
		helpButton.addActionListener(new actionListener());
		//helpButton.setBounds(395+67+8, 5, 30, 30); desired location
		helpButton.setBounds(500+395+67+8, 5, 30, 30);
		
		northPanel.add(accountLabel);
		northPanel.add(getField);
		northPanel.add(getAccountButton);
		northPanel.add(searchAccountButton);
		northPanel.add(resetButton);
		northPanel.add(deleteAccountButton);
		northPanel.add(helpButton);
		
		/* Build West Panel */
		
		westPanel = new JPanel();
		westPanel.setPreferredSize(new Dimension(110,200));
		westPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,1));
		westPanel.setBackground(Color.white);
		
		addAccountLabel = new MovingLabel("Add new", westPanel);
		addAccountLabel.setFont(fontPlain);
		addAccountLabel.setPreferredSize(new Dimension(100,30));
		addAccountLabel.setOpaque(true);
		addAccountLabel.setHorizontalAlignment(JLabel.CENTER);
		addAccountLabel.setForeground(Color.black);
		addAccountLabel.addMouseListener(new mouseListener());
		addAccountLabel.setBorder(BorderFactory.createBevelBorder(1, Color.gray, blueColor));
		
		
		modifyAccountLabel = new MovingLabel("View/Modify",westPanel);
		modifyAccountLabel.setFont(fontPlain);
		modifyAccountLabel.setPreferredSize(new Dimension(100,30));
		modifyAccountLabel.setOpaque(true);
		modifyAccountLabel.setHorizontalAlignment(JLabel.CENTER);
		modifyAccountLabel.setForeground(Color.LIGHT_GRAY);
		modifyAccountLabel.addMouseListener(new mouseListener());
		
		dashBoardLabel = new MovingLabel("Dash Board",westPanel);
		dashBoardLabel.setFont(fontPlain);
		dashBoardLabel.setPreferredSize(new Dimension(100,30));
		dashBoardLabel.setOpaque(true);
		dashBoardLabel.setHorizontalAlignment(JLabel.CENTER);
		dashBoardLabel.setForeground(Color.LIGHT_GRAY);
		dashBoardLabel.addMouseListener(new mouseListener());
		
		invoiceLabel = new MovingLabel("Invoice",westPanel);
		invoiceLabel.setFont(fontPlain);
		invoiceLabel.setPreferredSize(new Dimension(100,30));
		invoiceLabel.setOpaque(true);
		invoiceLabel.setHorizontalAlignment(JLabel.CENTER);
		invoiceLabel.setForeground(Color.LIGHT_GRAY);
		invoiceLabel.addMouseListener(new mouseListener());
		
		paymentLabel = new MovingLabel("Payment",westPanel);
		paymentLabel.setFont(fontPlain);
		paymentLabel.setPreferredSize(new Dimension(100,30));
		paymentLabel.setOpaque(true);
		paymentLabel.setHorizontalAlignment(JLabel.CENTER);
		paymentLabel.setForeground(Color.LIGHT_GRAY);
		paymentLabel.addMouseListener(new mouseListener());
		
		westPanel.add(addAccountLabel);
		westPanel.add(modifyAccountLabel);
		westPanel.add(dashBoardLabel);
		westPanel.add(invoiceLabel);
		westPanel.add(paymentLabel);
		
		/* Build South Panel */
		
		southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout(FlowLayout.LEFT,0,10));
		southPanel.setPreferredSize(new Dimension(500,30));
		
		/* Build Center Panel */
		centerPanel = new BackgroundPanel();
		//centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		centerPanel.setLayout(new CardLayout());
		centerPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		
		blankPanel = new JPanel();
		blankPanel.setOpaque(false);
		
		addAccountPanel = new AddAccountPanel(statement,southPanel,this);
		//addAccountPanel.setVisible(false);
		
		dashBoardPanel = new DashBoardPanel();
		//dashBoardPanel.setVisible(false);
		
		try {
			billingPanel = new BillingPanel(statement,southPanel,this);
		//	billingPanel.setVisible(false);
		} catch (BadLocationException | SQLException e2) {
			e2.printStackTrace();
		}
		
		paymentPanel = new PaymentPanel(statement,this);
		//paymentPanel.setVisible(false);
		
		centerPanel.add(blankPanel,"blankPanel");
		centerPanel.add(addAccountPanel,"addAccountPanel");
		centerPanel.add(dashBoardPanel,"dashBoardPanel");
		centerPanel.add(billingPanel,"billingPanel");
		centerPanel.add(paymentPanel,"paymentPanel");
		
		CL = (CardLayout)(centerPanel.getLayout());
		
		setLayout(new BorderLayout());  
		add(northPanel, BorderLayout.NORTH);
		add(westPanel, BorderLayout.WEST);
		add(centerPanel,BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		
		setLocationRelativeTo(null);
		this.setVisible(true);
		
		/* Show animation of northPanel components moving in*/
		JComponent[] components = {accountLabel,getField, getAccountButton, searchAccountButton
								,resetButton, deleteAccountButton, helpButton};
		(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					addAccountLabel.show();
					for(JComponent i : components) {
						moveInComponents(i);
							Thread.sleep(150);
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		})).start();
		
		/* show message indicates a connection with the database is established */
		(new Thread(new runnable("Database connected!",southPanel))).start();
		
		/* close mysql connection when the window is closed */
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) {try {
				connection.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}}

			@Override
			public void windowClosed(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowActivated(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {}});
	}

	/* change the position of components in northPanel to make them 
	 * looks like moving in to the northPanel
	 * @param {cp} components in the northPanel*/
	private void moveInComponents(JComponent cp) {
		Thread th1 = new Thread(new Runnable() {

			@Override
			public void run() {
				int originX = cp.getX();
				int originY = cp.getY();
				for(int i = 1 ; i<=100; i++) {
					cp.setBounds(originX-i*5,originY,cp.getWidth(),cp.getHeight());
					northPanel.repaint();
					try {
						Thread.sleep(2-(i/75));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		th1.start();
	}
	
	private class actionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			/* get existing account with account number input by user
			 * or account number selected from the search window
			 * */
			if(e.getSource() == getAccountButton){
				if(getField.getText().equals(""))
					JOptionPane.showMessageDialog(null, "Please type in an account# first!", "Missing Info", JOptionPane.WARNING_MESSAGE);
				else {
					try {
						int accountNum = Integer.parseInt(getField.getText());
						ResultSet resultSet = statement.executeQuery("select * from accounts where accountNumber = " + accountNum + ";");
						if(!resultSet.next()) { 
							JOptionPane.showMessageDialog(null, "Please check account number or add new acount!", "Account not found", JOptionPane.ERROR_MESSAGE);

						}
						else {
							/* Hiding all main function panels
							 * Reset them to original state (no account object)*/
							//addAccountPanel.setVisible(false);
							addAccountPanel.reset();
							//dashBoardPanel.setVisible(false);
							//billingPanel.setVisible(false);
							billingPanel.reset();
							//paymentPanel.setVisible(false);
							CL.show(centerPanel, "blankPanel");
							
							// enable reset button to use for reset the main page to original state
							resetButton.setEnabled(true);
							
							// enable delete Button to use for remove an account in the database
							deleteAccountButton.setEnabled(true);
							
							// show labels move in animation (the labels are here the whole time)
							showLabels();
							
							// reset labels appearance with appropriate foreground color
							resetLabel(addAccountLabel,Color.lightGray);
							addAccountLabel.setBorder(BorderFactory.createBevelBorder(1, Color.LIGHT_GRAY, Color.LIGHT_GRAY));
							resetLabel(modifyAccountLabel,Color.black);
							resetLabel(invoiceLabel,Color.black);
							resetLabel(paymentLabel,Color.black);
							resetLabel(dashBoardLabel,Color.black);
							
							// create account object to hold data from the database
							account = new Accounts(resultSet.getInt("accountNumber"),resultSet.getString("name"),
									resultSet.getString("address"),resultSet.getString("phoneNumber"),resultSet.getString("plan")
									,resultSet.getDouble("remainingDue"), resultSet.getDouble("lastPayment"), resultSet.getString("lastPaymentDate"),
									resultSet.getDouble("currentUsage"), resultSet.getDouble("lastUsage"));
							
							// display message in southPanel
							(new Thread(new runnable("Account# " + account.getAccountNumber() + " retrieved successfully!",southPanel))).start();
							
							// pass the account object to main function panels to show account information
							addAccountPanel.showAccountInfo(account);
							dashBoardPanel.showAccountInfo(account);
							billingPanel.setAccount(account);
							paymentPanel.showAccountInfo(account);
							
							// check if helpMode is enable then show toolTip for labels that has just showed up after click GET
							if(helpMode)
								ExplainTextEnable(true);
						}
						resultSet.close();
					} catch (SQLException | NumberFormatException e2) {
						JOptionPane.showMessageDialog(null, "That is not a number!", "Unacceptable input", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			
			/* Reset the program to beginning state */
			else if (e.getSource() == resetButton) {
				// reset textField and main function labels to their original state with appropriate foreground color
				getField.setText("");
				resetLabel(addAccountLabel,Color.black);
				resetLabel(modifyAccountLabel,Color.lightGray);
				resetLabel(invoiceLabel,Color.lightGray);
				resetLabel(paymentLabel,Color.lightGray);
				resetLabel(dashBoardLabel,Color.lightGray);
				
				//reset main function panels
				addAccountPanel.reset();
				billingPanel.reset();
				
				// set invisible for all main function panels
				//showPanel(null);
				CL.show(centerPanel, "blankPanel");
				centerPanel.setBackground(null);
				
				//show labels move out animation (the labels are still here but not show)
				hideLabels();
				
				// disable delete and reset buttons
				deleteAccountButton.setEnabled(false);
				resetButton.setEnabled(false);
				
				// only show tool Tips for the visible labels and buttons (not the hidden main function labels)
				ExplainTextEnable(false);
				if(helpMode)
					ExplainTextEnable(true); 
				
				// display message in southPanel
				(new Thread(new runnable("Main page reset completed!",southPanel))).start();
				
			}
			
			/* Search for existing an account */
			else if(e.getSource() == searchAccountButton) {
				try {
					// create search panel object and pass to JOptionPane message dialog
					SearchPanel searchPanel = new SearchPanel(statement);
					JOptionPane.showMessageDialog(null, searchPanel,"Searching account...",JOptionPane.QUESTION_MESSAGE);
					
					// get selected account number and doClick GET to retrieve account
					if(searchPanel.getAccountNumber()!=0) {
						getField.setText(String.valueOf(searchPanel.getAccountNumber()));
						getAccountButton.doClick();
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			
			/* delete existing account */
			else if(e.getSource() == deleteAccountButton) {
				// show confirm message
				int ans = JOptionPane.showConfirmDialog(null, "Are you sure to delete account "+ account.getAccountNumber()+"?", "Action can't be undone", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if(ans == 0) // ok = 0, cancel =2, X = -1
				{
					try {
						// delete account in the database
						statement.execute("delete from accounts where accountNumber = "+account.getAccountNumber()+";");
						statement.execute("alter table accounts AUTO_INCREMENT = 1;"); // reset auto_increment
						
						// reset entire page to original state
						resetButton.doClick();
						
						// display message in southPanel
						(new Thread(new runnable("Account# " + account.getAccountNumber() + " deleted!",southPanel))).start();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
			
			/* Enable helping mode */
			else if(e.getSource() == helpButton) {
				// check if helpMode is disable then enable it
				if (!helpMode) {
					int ans = JOptionPane.showConfirmDialog(null, "Help mode will show extra information and images when you hover the mouse on items of the top and left panels."
							+ " \nDo you want to enable help mode? (Fact: click the help icon again to disable it.)", "Help Mode", JOptionPane.YES_NO_OPTION); // 0: yes, 1: no
					if(ans == 0) {
						helpMode = true;
						
						// display message in southPanel
						(new Thread(new runnable("Help mode is enabled",southPanel))).start();
						
						// set showing time to 30s and set delay show time to 0s
						javax.swing.ToolTipManager.sharedInstance().setDismissDelay(30000);
						javax.swing.ToolTipManager.sharedInstance().setInitialDelay(0);
						
						// set toolTips
						ExplainTextEnable(true);
					}
				}
				// if helpMode is enable then disable it
				else {
					// display message in southPanel
					(new Thread(new runnable("Help mode is disabled",southPanel))).start();
					helpMode = false;
					
					// remove toolTips
					ExplainTextEnable(false);
				}
			}
		}

	}

	public AddAccountPanel getAddAccountPanel() {
		return addAccountPanel;
	}

	public DashBoardPanel getDashBoardPanel() {
		return dashBoardPanel;
	}

	public BillingPanel getBillingPanel() {
		return billingPanel;
	}

	public PaymentPanel getPaymentPanel() {
		return paymentPanel;
	}

	private class mouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getSource() == addAccountLabel) {
				if(addAccountLabel.getForeground() == Color.black) {
					// display message in southPanel
					(new Thread(new runnable("Ready to add new Account",southPanel))).start();
					
					// change appearance of the label to show that it is clicked
					addAccountLabel.setBackground(blueColor);
					addAccountLabel.setFont(fontBold);
					addAccountLabel.setForeground(Color.white);
					addAccountLabel.setBorder(BorderFactory.createBevelBorder(1));
					
					// display this main function panel
					//showPanel(addAccountPanel);
					CL.show(centerPanel, "addAccountPanel");
					centerPanel.setBackground(Color.white);
				}else {
					(new Thread(new runnable("Please reset Main Page to add new Account",southPanel))).start();
				}
			}
			else if (e.getSource() == modifyAccountLabel) {
				// if foreground is black, main function is enable
				if(modifyAccountLabel.getForeground() == Color.black) {
					// change appearance of the label to show that it is clicked
					paintClickedLabel(modifyAccountLabel);
					
					// display this main function panel
					//showPanel(addAccountPanel);
					CL.show(centerPanel, "addAccountPanel");
					centerPanel.setBackground(Color.white);
				}
			}
			else if (e.getSource() == dashBoardLabel) {
				// if foreground is black, main function is enable
				if(dashBoardLabel.getForeground() == Color.black) {
					// change appearance of the label to show that it is clicked
					paintClickedLabel(dashBoardLabel);
					
					// display this main function panel
					//showPanel(dashBoardPanel);
					CL.show(centerPanel, "dashBoardPanel");
					centerPanel.setBackground(Color.white);
				}
			}
			else if (e.getSource() == invoiceLabel) {
				// if foreground is black, main function is enable
				if(invoiceLabel.getForeground() == Color.black) {
					// change appearance of the label to show that it is clicked
					paintClickedLabel(invoiceLabel);

					// display this main function panel
					//showPanel(billingPanel);
					CL.show(centerPanel, "billingPanel");
					centerPanel.setBackground(Color.white);
				}
			}
			else if(e.getSource() == paymentLabel) {
				// if foreground is black, main function is enable
				if(paymentLabel.getForeground() == Color.black) {
					// change appearance of the label to show that it is clicked
					paintClickedLabel(paymentLabel);

					// display this main function panel
					//showPanel(paymentPanel);
					CL.show(centerPanel, "paymentPanel");
					centerPanel.setBackground(Color.white);
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}
		
	}
	
	private class GetAccountAction extends AbstractAction{

		@Override
		public void actionPerformed(ActionEvent e) {
			getAccountButton.doClick();
		}
		
	}
	
	// show labels move in animation (the labels are there the entire time)
	private void showLabels() {
		modifyAccountLabel.show();
		paymentLabel.show();
		invoiceLabel.show();
		dashBoardLabel.show();
		labelsShowed = true;
	}
	
	// show labels move out animation (the labels are still there just not being seen)
	private void hideLabels() {
		modifyAccountLabel.hide();
		paymentLabel.hide();
		invoiceLabel.hide();
		dashBoardLabel.hide();
		labelsShowed = false;
	}
	
	/*
	 * Set no background and specific foreground for the main
	 * function labels.
	 * Set the border to be raised bevel border to show that the label is not clicked. 
	 * Set font to be plain Arial
	 * @param {label} the label that will be reset
	 * @param {color} the desire foreground color of the label
	 */
	private void resetLabel(JLabel label, Color color) {
		label.setBackground(null);
		label.setForeground(color);
		label.setFont(fontPlain);
		label.setBorder(BorderFactory.createBevelBorder(1, Color.gray, blueColor));
	}
	
	/*
	 * Set blue background to the given label and set no
	 * background for the rest of main functions' labels
	 * @param {label} the label that will be painted
	 */
	private void paintClickedLabel(JLabel label){
		resetLabel(modifyAccountLabel,Color.black);
		resetLabel(invoiceLabel,Color.black);
		resetLabel(paymentLabel,Color.black);
		resetLabel(dashBoardLabel,Color.black);
		label.setBackground(blueColor);
		label.setFont(fontBold);
		label.setForeground(Color.white);
		label.setBorder(BorderFactory.createBevelBorder(1));	
	}
	
	/* Display the given panel and hide others panels 
	 * @param {panel} panel that will be shown
	 * */
//	private void showPanel(JPanel panel) {
//		centerPanel.setBackground(Color.white);
//		addAccountPanel.setVisible(false);
//		dashBoardPanel.setVisible(false);
//		billingPanel.setVisible(false);
//		paymentPanel.setVisible(false);
//		if(panel != null)
//			panel.setVisible(true);
//		else
//			centerPanel.setBackground(null);
//		
//		
//	}
	
	/*
	 * Set ToolTip for components if enable is true. Otherwise, it
	 * sets all ToolTips to null
	 * @param {enable} either true or false
	 */
	private void ExplainTextEnable(boolean enable) {
		if(enable) {
			setExplainText(getField, "Put in an account# and press Enter or click GET to retrieve.<br>"
					+ " <img src=\"file:ToolTips/getFieldTip.gif\">", 250);
			setExplainText(getAccountButton, "Retrieve an account with account#.", 200);
			setExplainText(searchAccountButton,"Existing account can be retrieved without account# using SEARCH.<br> After click the search icon:"
					+ " Step1: Choose a category to search.<br><img src=\"file:ToolTips/searchTip_1.gif\"><br>"
					+ " Step2: type in a few letters and click search.<br><img src=\"file:ToolTips/searchTip_2.gif\"><br>"
					+ " Step3: Choose one of the result(s) if there is any.<br><img src=\"file:ToolTips/searchTip_3.gif\"><br>"
					+ " Step4: Click OK to continue.<br><img src=\"file:ToolTips/searchTip_4.gif\">", 370);
			setExplainText(resetButton, "Reset to beginning page where user can choose add new accounts or retrieve an account.", 200);
			setExplainText(deleteAccountButton, "Completely remove current account from the database. Only available after an account is retrieved.", 200);
			setExplainText(addAccountLabel, "Add new account the database.<br> After click the ADD NEW button:<br>"
					+ " Step1: Input customer's name, address, and phone#.<br><img src=\"file:ToolTips/addAccountTip_1.gif\"><br>"
					+ " Step2: Choose an appropriate tariff plan.<br><img src=\"file:ToolTips/addAccountTip_2.gif\"><br>"
					+ " Step3: Click Add to add new customer.<br><img src=\"file:ToolTips/addAccountTip_3.gif\"><br>"
					+ " Fact1: Click Clear to remove all input and tariff choice.<br><img src=\"file:ToolTips/addAccountTip_4.gif\"><br>"
					+ " Fact2: Modify button only available in View/Modify after retrieving the account using GET or SEARCH.<br><img src=\"file:ToolTips/addAccountTip_5.gif\"><br>", 350);
			
			// if main function labels are showed then display their toolTips
			if(labelsShowed) {
				setExplainText(modifyAccountLabel, "Modify information of existing account include: name, address, phone# and tariff.<br>"
						+ "Available only when an account is retrieved.<br>"
						+ "After an account is retrieved, click VIEW/MODIFY:<br>"
						+ " Step1: Click Modfiy to unlock all fields and tariff plan.<br><img src=\"file:ToolTips/viewModifyTip_1.gif\"><br>"
						+ " Step2: Make any neccessary changes.<br><img src=\"file:ToolTips/viewModifyTip_2_1.gif\"><img src=\"file:ToolTips/viewModifyTip_2_2.gif\"><br>"
						+ " Step3: Click Apply to save changes.<br><img src=\"file:ToolTips/viewModifyTip_3.gif\"><br>", 300);
				setExplainText(dashBoardLabel, "View basic account owner information, tariff information and total due.<br>"
						+ "Available only when an account is retrieved.", 200);
				setExplainText(invoiceLabel,"Generate, view and send latest invoice via email.<br>"
						+ "Available only when an account is retrieved.<br>"
						+ " After click Invoice:<br>"
						+ " Step1: Click Generate to create an invoice.<br><img src=\"file:ToolTips/invoiceTip_1.gif\"><br>"
						+ " Step2: Read the message and choose to proceed or not.<br><img src=\"file:ToolTips/invoiceTip_2.gif\"><br>"
						+ " Step3: Click Send Email if needed, fill in the customer email address then click OK to send.<br><img src=\"file:ToolTips/invoiceTip_3.gif\"><br>"
						+ " Fact1: Old invoice could be review by clicking View to open old invoice file.<br><img src=\"file:ToolTips/invoiceTip_4.gif\"><br><br><img src=\"file:ToolTips/invoiceTip_5.gif\"><br>"
						+ " Fact2: Click magnifying glass icon to zoom in or zoom out.<br><img src=\"file:ToolTips/invoiceTip_6.gif\">", 380);
				setExplainText(paymentLabel,"Process new payment. Available only when an account is retrieved.<br>"
						+ " After click Payment:<br>"
						+ " Step1: Input an amount for this payment.<br><img src=\"file:ToolTips/paymentTip_1.gif\"><br>"
						+ " Step2: Choose a pay date for this payment. By default: the date will be today.<br><img src=\"file:ToolTips/paymentTip_2.gif\"><br>"
						+ " Step3: Click Process to record new payment.<br><img src=\"file:ToolTips/paymentTip_3.gif\"><br>", 400);
			}
		}
		else {
			// remove all toolTips
			getField.setToolTipText(null);
			getAccountButton.setToolTipText(null);
			searchAccountButton.setToolTipText(null);
			resetButton.setToolTipText(null);
			deleteAccountButton.setToolTipText(null);
			addAccountLabel.setToolTipText(null);
			modifyAccountLabel.setToolTipText(null);
			dashBoardLabel.setToolTipText(null);
			invoiceLabel.setToolTipText(null);
			paymentLabel.setToolTipText(null);
		}
	}
	
	
	/* Round double number to 2 decimals places 
	 * @param {num} number to be round.
	 * */
	public static double round(double num) {
		return Math.round(num*100.0)/100.0;
	}
	
	/* Set the toolTips text (as paragraph with given width) for JComponents 
	 * @param {cp} a JComponent that will be set ToolTips
	 * @param {text} the body of the ToolTips
	 * @param {width} the width of this ToolTips box
	 * */
	public static void setExplainText(JComponent cp, String text, int width) {
		cp.setToolTipText("<html><p width=\""+ width + "\">" +text+"</p></html>");
	}
	
	private class BackgroundPanel extends JPanel{
		
		private Image bgImg;
		private ImageIcon imgIcon;
		private int x,y;  // x,y position of image
		private int size;  //size of image
		
		public BackgroundPanel() {
			x=125;
			y=0;
			size = 350;
			imgIcon = new ImageIcon((new ImageIcon("Icons/IconGray.png")).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
			bgImg = imgIcon.getImage();//1100,1100  decrease 10 every 15 ms 67 times
			runAnimation();
		}
		

			
		public void runAnimation() {
			Thread th = new Thread(new Runnable() {

				@Override
				public void run() {

					try {
						for(int i = 0 ; i<45; i++) {
							x-=5;
							y-=5;
							ImageIcon imgIcon1 = new ImageIcon((new ImageIcon("Icons/IconGray.png")).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
							bgImg = imgIcon1.getImage();//1100,1100  decrease 10 every 15 ms 67 times
							size+=10;
							repaint();
							Thread.sleep(1);
						}
						for(int i = 0 ; i<45; i++) {
							x+=5;
							y+=5;
							ImageIcon imgIcon2 = new ImageIcon((new ImageIcon("Icons/IconGray.png")).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
							bgImg = imgIcon2.getImage();//1100,1100  decrease 10 every 15 ms 67 times
							size-=10;
							repaint();
							Thread.sleep(8);
						}
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			});
			th.start();
			
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(bgImg, x, y, null);
		}
		
	}

}
