package kc_energy_v2;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.text.*;
import java.util.Date;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

public class BillingPanel extends JPanel {
	private JPanel northPanel;
	private JButton getInvoiceButton, sendEmailButton, viewInvoiceButton, zoomInButton, zoomOutButton;
	private JLabel invoiceLabel;
	private JScrollPane scrollPane;
	private Accounts account;
	private double currentBill, totalDue;
	private double price1, price2, price3;
	private JPanel messagePanel;
	private String invoiceDate;
	private Statement statement;
	private MainFrame mainFrame;
	BillingPanel(Statement stm,JPanel messagePanel, MainFrame mainFrame) throws BadLocationException, SQLException {
		this.statement = stm;
		this.messagePanel = messagePanel;
		this.mainFrame = mainFrame;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		invoiceDate = sdf.format(new Date());

		this.setPreferredSize(new Dimension(595, 345));

		/* Build North panel */

		getInvoiceButton = new JButton("Generate");
		getInvoiceButton.addActionListener(new actionListener());
		
		viewInvoiceButton = new JButton("View");
		viewInvoiceButton.addActionListener(new actionListener());
		viewInvoiceButton.setFocusable(false);
		
		sendEmailButton = new JButton("Send Email");
		sendEmailButton.addActionListener(new actionListener());
		sendEmailButton.setFocusable(false);
		sendEmailButton.setEnabled(false);
		
		ImageIcon zoomInIcon = new ImageIcon(new ImageIcon("Icons/zoomIn.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)); 
		zoomInButton = new JButton(zoomInIcon);
		zoomInButton.addActionListener(new actionListener());
		zoomInButton.setEnabled(false);
		
		ImageIcon zoomOutIcon = new ImageIcon(new ImageIcon("Icons/zoomOut.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)); 
		zoomOutButton = new JButton(zoomOutIcon);
		zoomOutButton.addActionListener(new actionListener());
		zoomOutButton.setEnabled(false);
		
		northPanel = new JPanel();
		northPanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		
		northPanel.add(getInvoiceButton);
		northPanel.add(viewInvoiceButton);
		northPanel.add(sendEmailButton);
		northPanel.add(zoomInButton);
		northPanel.add(zoomOutButton);
		
		/* Build Center Region content */
		
		invoiceLabel = new JLabel();
		invoiceLabel.setHorizontalAlignment(JLabel.CENTER);
		scrollPane = new JScrollPane(invoiceLabel);
		
		/* Add all components to the Panel */
		setLayout(new BorderLayout());
		add(northPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		
		northPanel.setOpaque(false);
		scrollPane.setOpaque(false);   // to make scrollPane transparent 
		scrollPane.getViewport().setOpaque(false); // to make scrollPane transparent 
		
		this.setOpaque(false);
		setVisible(true);

	}

	/* Calculate number of kwh depends on usage range
	 * @param {type} a string represent first, second, or third range
	 * @param {totalUsage} total kwh of usage
	 */
	private double getkwh(String type, double totalUsage) {

		if (totalUsage >= 1000) {
			if (type == "first")
				return 600;
			else if (type == "second")
				return 400;
			else
				return totalUsage - 1000;
		} else if (totalUsage >= 600) {
			if (type == "first")
				return 600;
			else if (type == "second")
				return totalUsage - 600;
			else
				return 0;
		} else {
			if (type == "first")
				return totalUsage;
			else if (type == "second")
				return 0;
			else
				return 0;
		}
	}

	/*
	 * Create a invoice as pdf file 
	 * @param {cost1} cost of first 600 kwh
	 * @param {cost2} cost of next 400 kwh
	 * @param {cost3} cost of over 1000 kwh
	 */
	private void buildPDF(double cost1, double cost2, double cost3) throws IOException {

		CreateInvoicePdf invoice = new CreateInvoicePdf(account, currentBill, totalDue, cost1, cost2, cost3);
		Path accountInvoiceFolder = Paths.get("Invoices/"+account.getAccountNumber());
		try {
			Files.createDirectory(accountInvoiceFolder);
		}catch(FileAlreadyExistsException e){
			//do nothing
		}
		invoice.createPdf("Invoices/"+account.getAccountNumber()+"/Invoice_"+invoiceDate+".pdf", invoiceDate);
	}

	/* Create an image file from the invoice pdf file to display the
	 * invoice on the Frame
	 */
	private void generateImageFromPDF(String invoicePath) throws IOException {
		File filename;
		if(invoicePath == null)
			filename = new File("Invoices/"+account.getAccountNumber()+"/Invoice_"+invoiceDate+".pdf");
		else
			filename = new File(invoicePath);
		PDDocument document = Loader.loadPDF(filename);
		PDFRenderer pdfRenderer = new PDFRenderer(document);
		BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB); //300 dpi produce 2480x3507 pixels image
		ImageIOUtil.writeImage(bim, "tempInvoiceImage.png", 300);
		document.close();
	}
	
	/* Retrieve an account object from the MainFrame*/
	public void setAccount(Accounts acc) {
		account = acc;
	}

	private class actionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			/* generate invoice from the account input by user or the account selected from the search button*/
			if(e.getSource() == getInvoiceButton) {
				Path invoicePath = Paths.get("Invoices/"+account.getAccountNumber()+"/Invoice_"+invoiceDate+".pdf");
				if(!Files.exists(invoicePath))
				{
					int ans = JOptionPane.showConfirmDialog(null, "Generate invoice will reset current usage and update remaining due in the database! Proceed?", "Message",JOptionPane.INFORMATION_MESSAGE);
					if(ans == 0) {
						if(account.getPlan().equals("resident")) {
							price1 = 0.25;
							price2 = 0.31;
							price3 = 0.35;
						}
						else if (account.getPlan().equals("business")) {
							price1 = 0.35;
							price2 = 0.41;
							price3 = 0.45;
						}
						currentBill = MainFrame.round(price1*getkwh("first",account.getCurrentUsage())+price2*getkwh("second",account.getCurrentUsage())+price3*getkwh("third",account.getCurrentUsage())); 
						totalDue = MainFrame.round(account.getRemainingDue() + currentBill);
						double cost1 = MainFrame.round(price1*getkwh("first",account.getCurrentUsage()));
						double cost2 = MainFrame.round(price2*getkwh("second",account.getCurrentUsage()));
						double cost3 = MainFrame.round(price3*getkwh("third",account.getCurrentUsage()));
						try {
							buildPDF(cost1, cost2, cost3);
							generateImageFromPDF(null);
						} catch (IOException e1) {
							e1.printStackTrace();
						}																							// the image original dimension is 2480 x 3507
						ImageIcon invoiceImage = new ImageIcon(new ImageIcon("tempInvoiceImage.png").getImage().getScaledInstance(589, 833, Image.SCALE_SMOOTH));
						invoiceLabel.setIcon(invoiceImage);
						getInvoiceButton.setEnabled(false);
						(new Thread(new runnable("New invoice generated.",messagePanel))).start();
						sendEmailButton.setEnabled(true);
						zoomInButton.setEnabled(true);
						zoomOutButton.setEnabled(true);
//						try {
//							account.setLastUsage(account.getCurrentUsage());
//							account.setCurrentUsage(0);
//							account.setRemainingDue(totalDue);
//							statement.execute("update accounts set lastUsage = currentUsage, currentUsage = 0, remainingDue = "
//										+ totalDue+" where accountNumber = " + account.getAccountNumber()+";");
//						} catch (SQLException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
						mainFrame.getAddAccountPanel().showAccountInfo(account);
						mainFrame.getDashBoardPanel().showAccountInfo(account);
						mainFrame.getPaymentPanel().showAccountInfo(account);
					}	
				}
				else {
					JOptionPane.showMessageDialog(null, "This month's invoice has already been generated. Please use View to review the invoice.","Couldn't generate new invoice", JOptionPane.WARNING_MESSAGE);
					getInvoiceButton.setEnabled(false);
				}
				
			}
			
			else if(e.getSource() == viewInvoiceButton) {
				Path accountInvoiceFolder = Paths.get("Invoices/"+account.getAccountNumber());
				try {
					Files.createDirectory(accountInvoiceFolder);
				}catch(FileAlreadyExistsException e1){
					//do nothing
				} catch (IOException e1) {
					e1.printStackTrace();
				} 
				JFileChooser fileChooser = new JFileChooser(accountInvoiceFolder.toString());
				int file = fileChooser.showOpenDialog(null);
				if(file == JFileChooser.APPROVE_OPTION) {
					String invoicePath = fileChooser.getSelectedFile().getAbsolutePath();
					if(!invoicePath.contains(String.valueOf(account.getAccountNumber()))) {
						JOptionPane.showMessageDialog(null, "Selected invoice is not belong to this account");
					}
					else {
						try {
							generateImageFromPDF(invoicePath);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
																											// the image original dimension is 2480 x 3507
						ImageIcon invoiceImage = new ImageIcon(new ImageIcon("tempInvoiceImage.png").getImage().getScaledInstance(589, 833, Image.SCALE_SMOOTH));
						invoiceLabel.setIcon(invoiceImage);
						zoomInButton.setEnabled(true);
						zoomOutButton.setEnabled(true);
					}
				}
			}
			
			else if (e.getSource() == sendEmailButton) {
				String emailAddress = JOptionPane.showInputDialog("Enter customer email address: ");
				if (emailAddress != null && !"".equals(emailAddress) ) {
					SendEmail emailInvoice = new SendEmail(emailAddress, "Invoices/"+account.getAccountNumber()+"/Invoice_"+invoiceDate+".pdf");
					emailInvoice.send();
					if(emailInvoice.getSendStatus()) {
						(new Thread(new runnable("Email contains the invoice has been sent.",messagePanel))).start();
					}
					else
						JOptionPane.showMessageDialog(null, "Couldn't send the email. It looks like our email server is down!", "Bad news",JOptionPane.ERROR_MESSAGE);

				}
			}
			
			else if(e.getSource() == zoomInButton) {
				ImageIcon invoiceIcon = new ImageIcon("tempInvoiceImage.png");
				Image invoiceImg = invoiceIcon.getImage();
		        Image newInvoiceImg = invoiceImg.getScaledInstance(invoiceLabel.getWidth() +40, invoiceLabel.getHeight() +40,Image.SCALE_SMOOTH);
		        invoiceLabel.setIcon(new ImageIcon(newInvoiceImg));
			}
			
			else if(e.getSource() == zoomOutButton) {
				if(invoiceLabel.getWidth()>589+40 && invoiceLabel.getHeight()>289+40) {
					ImageIcon invoiceIcon = new ImageIcon("tempInvoiceImage.png");
					Image invoiceImg = invoiceIcon.getImage();
					Image newInvoiceImg = invoiceImg.getScaledInstance(invoiceLabel.getWidth() -40, invoiceLabel.getHeight() -40,Image.SCALE_SMOOTH);
					invoiceLabel.setIcon(new ImageIcon(newInvoiceImg));
				}
			}
		}
	}
	
	/* Reset Invoice Panel */
	public void reset() {
		getInvoiceButton.setEnabled(true);
		invoiceLabel.setIcon(null);
		sendEmailButton.setEnabled(false);
		zoomInButton.setEnabled(false);
		zoomOutButton.setEnabled(false);
		File invoiceImg = new File("tempInvoiceImage.png");
		invoiceImg.delete();
	}
}