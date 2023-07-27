package kc_energy_v2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class SearchPanel extends JPanel {
	private JComboBox<String> searchByComboBox;
	private JTextField searchField;
	private JButton searchButton;
	private JPanel centerPanel, northPanel;
	private JLabel resultLabel, searchByLabel, messageLabel;
	private String[] searchByItems;
	private Statement statement;
	private int accountNumber=0;
	private JScrollPane scrollPane;
	private Vector<String> columnNames;
	private Vector<Vector> rowData;
	private JTable table;
	SearchPanel(Statement stm) throws SQLException{

		statement = stm;
		this.setPreferredSize(new Dimension(600,200));

		/* Build North Panel */

		searchByLabel = new JLabel("Search by:");

		searchByItems = new String[3];
		searchByItems[0] = "Name";
		searchByItems[1] = "Address";
		searchByItems[2] = "PhoneNumber";

		searchByComboBox = new JComboBox<String>(searchByItems);

		searchField = new JTextField(10);

		searchButton = new JButton("Search");
		searchButton.addActionListener(new actionListener());

		northPanel = new JPanel();
		northPanel.add(searchByLabel);
		northPanel.add(searchByComboBox);
		northPanel.add(searchField);
		northPanel.add(searchButton);

		/* Build Center Panel */

		centerPanel = new JPanel();

		resultLabel = new JLabel();

		columnNames = new Vector<String>();
		columnNames.addElement("Acc#");
		columnNames.addElement("Name");
		columnNames.addElement("Adress");
		columnNames.addElement("Phone#");
		rowData = new Vector<Vector>();
		table = new JTable();
		ListSelectionModel cellSelectionModel = table.getSelectionModel();
		cellSelectionModel.addListSelectionListener(new listSelectionListener()); 

		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(500,120));



		messageLabel = new JLabel();
		messageLabel.setForeground(Color.blue);


		centerPanel.add(resultLabel);
		centerPanel.add(scrollPane);
		centerPanel.setVisible(false);

		/* add all panels to main panel */
		add(northPanel,BorderLayout.NORTH);
		add(centerPanel,BorderLayout.CENTER);
		add(messageLabel,BorderLayout.SOUTH );

	}

	public int getAccountNumber() {
		return accountNumber;
	}

	/* retrieve the selected account */
	private class listSelectionListener implements ListSelectionListener{

		@Override
		public void valueChanged(ListSelectionEvent e) {
			int row = table.getSelectedRow();
			String choseAccount = (String) table.getValueAt(row, 0);
			accountNumber = Integer.parseInt(choseAccount);
			messageLabel.setText("Account# " + accountNumber + " is selected! Please click OK to continue.");
		}

	}

	private class actionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			/* search for an account */
			if(e.getSource() == searchButton) {
				rowData.clear();
				if("".equals(searchField.getText())) {
					JOptionPane.showMessageDialog(null, "Please enter something to search!","Missing info", JOptionPane.WARNING_MESSAGE);
				}
				else {
					centerPanel.setVisible(true);
					int resultsNumber = 0;
					try {
						ResultSet resultSet = statement.executeQuery("select accountNumber, name, address, phoneNumber from accounts where "
								+ searchByComboBox.getSelectedItem().toString() + " like '%" + searchField.getText()+"%';");
						if(!resultSet.next()) {
							resultLabel.setText(resultsNumber +" result found!");
						}
						else {
							resultsNumber++;

							Vector<String> row = new Vector<String>();
							row.addElement(resultSet.getString("accountNumber"));
							row.addElement(resultSet.getString("name"));
							row.addElement(resultSet.getString("address"));
							row.addElement(resultSet.getString("phoneNumber"));
							rowData.addElement(row);

							while(resultSet.next()) {
								resultsNumber++;
								Vector<String> nextRow = new Vector<String>();
								nextRow.addElement(resultSet.getString("accountNumber"));
								nextRow.addElement(resultSet.getString("name"));
								nextRow.addElement(resultSet.getString("address"));
								nextRow.addElement(resultSet.getString("phoneNumber"));
								rowData.addElement(nextRow);
							}

							resultLabel.setText(resultsNumber +" result(s) found!");
							DefaultTableModel model = (DefaultTableModel) table.getModel();
							model.setDataVector(rowData, columnNames);
							table.getColumnModel().getColumn(0).setPreferredWidth(50);
							table.getColumnModel().getColumn(1).setPreferredWidth(120);
							table.getColumnModel().getColumn(2).setPreferredWidth(300);
							table.getColumnModel().getColumn(3).setPreferredWidth(100);
							messageLabel.setText("Please select one of the account(s) to retrieve");
						}
						resultSet.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
}