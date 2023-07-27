package kc_energy_v2;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class LoginFrame extends JFrame {
	private JLabel loginLabel, messageLabel;
	private JPanel credentialPanel;
	private JTextField userNameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private Connection connection;
	private Border blackLineBorder = new LineBorder(Color.black,1,true);
	private Border redLineBorder = new LineBorder(Color.red,1,true);
	private TitledBorder userNameBorder, passwordBorder;
	LoginFrame(){
		setTitle("KC Energy");
		setSize(new Dimension(240,330));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.white);
		Font font = new Font("Times New Roman", Font.BOLD, 15);
		
		ImageIcon imageIcon1 = new ImageIcon(new ImageIcon("Icons/Icon.jpg").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)); 
		loginLabel = new JLabel(imageIcon1);
		
		credentialPanel = new JPanel();
		credentialPanel.setLayout(null);
		credentialPanel.setPreferredSize(new Dimension(220,110));
		credentialPanel.setOpaque(false);
		
		userNameField = new JTextField();
		userNameField.setOpaque(false);
		//userNameField.setBounds(30,0,160, 50); //desired position
		userNameField.setBounds(-170,0,160, 50); // hiding positon
		userNameField.setHorizontalAlignment(JTextField.CENTER);
		userNameField.setFont(font);
		userNameField.setForeground(Color.blue);
		userNameBorder = BorderFactory.createTitledBorder(blackLineBorder,"User name");
		userNameBorder.setTitleJustification(TitledBorder.CENTER);
		userNameBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
		userNameBorder.setTitleFont(font);
		userNameField.setBorder(userNameBorder);
		userNameField.setBackground(null);
		userNameField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				userNameBorder.setBorder(blackLineBorder);
			}
			@Override
			public void focusLost(FocusEvent e) {}
		});
		
		passwordField = new JPasswordField();
		passwordField.setOpaque(false);
		//passwordField.setBounds(30,60,160, 50); //desired position
		passwordField.setBounds(230,60,160, 50); // hiding position
		passwordField.setHorizontalAlignment(JTextField.CENTER);
		passwordField.setFont(font);
		passwordField.setForeground(Color.blue);
		passwordBorder = BorderFactory.createTitledBorder(blackLineBorder,"Password");
		passwordBorder.setTitleJustification(TitledBorder.CENTER);
		passwordBorder.setTitlePosition(TitledBorder.LEFT);
		passwordBorder.setTitleFont(font);
		passwordField.setBorder(passwordBorder);
		passwordField.setBackground(null);
		passwordField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				passwordBorder.setBorder(blackLineBorder);
			}
			@Override
			public void focusLost(FocusEvent e) {}
		});
		
		
		loginButton = new JButton("Login");
		loginButton.setForeground(Color.white);
		loginButton.setHorizontalAlignment(JButton.CENTER);
		loginButton.addActionListener(new ButtonListener());
		loginButton.setBackground(new Color(64, 189, 247));
		loginButton.setFont(font);
		
		// keybinding
		loginButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed ENTER"), "Login");
		loginButton.getActionMap().put("Login", new LoginAction());
		
		messageLabel = new JLabel();
		messageLabel.setPreferredSize(new Dimension(220,20));
		messageLabel.setVerticalAlignment(JLabel.TOP);
		messageLabel.setHorizontalAlignment(JLabel.CENTER);
		messageLabel.setForeground(Color.red);
		
		credentialPanel.add(userNameField);
		credentialPanel.add(passwordField);
		
		setLayout(new FlowLayout(FlowLayout.CENTER,30,20));
		add(loginLabel);
		add(credentialPanel);
		add(loginButton);
		add(messageLabel);
		setVisible(true);
		setLocationRelativeTo(null);
		
		moveInAnimation(userNameField);
		moveInAnimation(passwordField);

	}
	
	private class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == loginButton) {
				messageLabel.setText("");
				String password = new String(passwordField.getPassword());	
				if("".equals(userNameField.getText())||"".equals(password)) {
					JOptionPane.showMessageDialog(null, "Who are you, bro?", "No credential input found",JOptionPane.ERROR_MESSAGE);
				}else {
					/* show connecting animation*/
					Thread th1 = new Thread(new Runnable() {
						@Override
						public void run() {
							loginButton.setText("Connecting");
							for(int i = 1; i<4; i++) {
								loginButton.setText(loginButton.getText()+".");
								try {
									Thread.sleep(300);
								} catch (InterruptedException e2) {
									e2.printStackTrace();
								}
							}
							/* Try to connect to the database */
							try {
								connection = DriverManager.getConnection("jdbc:mysql:///kc_energy", userNameField.getText(), password);
								setVisible(false);
								new MainFrame(connection);
							} catch (SQLException e1) {
								if(e1.getMessage().contains("Access denied")) {
									userNameField.setText("");
									passwordField.setText("");
									userNameBorder.setBorder(redLineBorder);
									passwordBorder.setBorder(redLineBorder);
									wrongCredentialAnimation(userNameField);
									wrongCredentialAnimation(passwordField);
									messageLabel.setText("<Invalid credentials!>");
								}
								else {
									JOptionPane.showMessageDialog(null, "Please make sure kc_energy database is available!","Connecion Failed", JOptionPane.ERROR_MESSAGE);
								}
								loginButton.setText("Login");
								
							}
						}
					});
					th1.start();
				}
			}
		}
	}	
	
	private class LoginAction extends AbstractAction {

		@Override
		public void actionPerformed(ActionEvent e) {
			loginButton.doClick();
		}
		
	}
	
	private void moveInAnimation(JComponent cp) {
		Thread th1 = new Thread(new Runnable() {

			@Override
			public void run() {
				int originX = cp.getX();
				int originY = cp.getY();
				if (originX < 0) {
					for(int i = 1 ; i<=100; i++) {
						cp.setBounds(originX+i*2,originY,cp.getWidth(),cp.getHeight());
						credentialPanel.repaint();
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				else {
					for(int i = 1 ; i<=100; i++) {
						cp.setBounds(originX-i*2,originY,cp.getWidth(),cp.getHeight());
						credentialPanel.repaint();
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				
			}
			
		});
		th1.start();
	}
	
	private void wrongCredentialAnimation(JComponent cp) {
		Thread th1 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					int originX = cp.getX();
					int originY = cp.getY();
					int width = cp.getWidth();
					int height = cp.getHeight();
					for(int i = 5; i>=0; i--) {
						cp.setBounds(originX-i, originY, width, height);
						credentialPanel.repaint();
						Thread.sleep(50);
						cp.setBounds(originX+i, originY, width, height);
						credentialPanel.repaint();
						Thread.sleep(50);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
				
		});
		th1.start();
	}
	
	public static void main(String[] args) throws SQLException {
		/* set look and feel */
		try
  	  	{
			UIManager.setLookAndFeel("com.formdev.flatlaf.themes.FlatMacLightLaf");
  	  	}
		catch (Exception e)
  	  	{
			JOptionPane.showMessageDialog(null,
					"Error setting the look and feel.");
			System.exit(0);
  	  	}
		new LoginFrame();
	}
	
}