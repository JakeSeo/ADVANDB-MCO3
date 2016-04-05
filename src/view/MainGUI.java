package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import controller.Controller;
import model.TableContents;

import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.EtchedBorder;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class MainGUI extends JFrame implements ActionListener, KeyListener, ListSelectionListener
{
	private Controller controller;
	
	private DefaultListModel<String> defaultListModel;
	
	private JButton buttonAdd, buttonRemove, buttonExecute;
	private JList<String> listTransaction;
	private JPanel jpanel;
	private JScrollPane scrollpanePanel, scrollpaneTransaction;
	private JTabbedPane tabbedpaneTransactionPanel;
	private JTextField txtfieldTransactionName;

	private ArrayList<String> transactionNameList;
	private JLabel labelWarning;
	private JLabel labelInfo;
	
	private ArrayList<Integer> selectedTransactions;
	private ArrayList<String> queryInputs;
	
	public MainGUI( Controller controller )
	{
		try
		{
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
			{
				if ("Nimbus".equals(info.getName()))
				{
					UIManager.setLookAndFeel(info.getClassName());
					LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
					UIDefaults defaults = lookAndFeel.getDefaults();
					defaults.put("ScrollBar.minimumThumbSize", new Dimension(30, 30));
					break;
				}
			}
		}
		catch (Exception e)
		{
			// If Nimbus is not available, you can set the GUI to another
			// look and feel.
		}
		
		this.controller = controller;
		this.controller.setView(this);
		
		transactionNameList = new ArrayList<>(0);
		selectedTransactions = new ArrayList<>(0);
		queryInputs = new ArrayList<>(0);
		
		jpanel =  new JPanel();
		jpanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		jpanel.setSize(1300, 700);
		jpanel.setLayout(null);
		
		TransactionPanel t1 = new TransactionPanel(controller);
		t1.setName("Transaction 1");
		t1.setTransactionName("Transaction 1");
		transactionNameList.add("Transaction 1");
		
		tabbedpaneTransactionPanel = new JTabbedPane(JTabbedPane.TOP);
		tabbedpaneTransactionPanel.addTab("Transaction 1", t1);
		scrollpanePanel = new JScrollPane(tabbedpaneTransactionPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpanePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		scrollpanePanel.setBounds(10, 11, 1274, 510);
		jpanel.add(scrollpanePanel);
		
		labelInfo = new JLabel("<html>To select two or more transactions: Hold control while selecting<br>"
				+ "To deselect transactions: Hold control while selecting</html>");
		labelInfo.setBounds(11, 523, 384, 38);
		jpanel.add(labelInfo);
		
		defaultListModel = new DefaultListModel<>();
		listTransaction = new JList<String>(defaultListModel);
		listTransaction.setFont(new Font("Courier New", Font.PLAIN, 13));
		listTransaction.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listTransaction.setLayoutOrientation(JList.VERTICAL);
		listTransaction.addListSelectionListener(this);
		scrollpaneTransaction = new JScrollPane(listTransaction, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpaneTransaction.setBounds(10, 564, 349, 98);
		jpanel.add(scrollpaneTransaction);
		
		txtfieldTransactionName = new JTextField("Transaction Name");
		txtfieldTransactionName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtfieldTransactionName.setBounds(371, 533, 140, 28);
		txtfieldTransactionName.setColumns(10);
		txtfieldTransactionName.addKeyListener(this);
		jpanel.add(txtfieldTransactionName);
		
		labelWarning = new JLabel("<- Transaction name here");
		labelWarning.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		labelWarning.setBounds(516, 535, 335, 23);
		jpanel.add(labelWarning);
		
		buttonAdd = new JButton("Add");
		buttonAdd.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		buttonAdd.setBounds(371, 570, 140, 23);
		buttonAdd.addActionListener(this);
		jpanel.add(buttonAdd);
		
		buttonRemove = new JButton("Remove");
		buttonRemove.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		buttonRemove.setBounds(371, 604, 140, 23);
		buttonRemove.setEnabled(false);
		buttonRemove.addActionListener(this);
		jpanel.add(buttonRemove);
		
		buttonExecute = new JButton("Execute Transactions");
		buttonExecute.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		buttonExecute.setBounds(371, 639, 140, 23);
		buttonExecute.setEnabled(false);
		buttonExecute.addActionListener(this);
		jpanel.add(buttonExecute);
		
		this.setTitle("ADVANDB MCO3");
		this.setSize(1300, 700);
		this.setContentPane(jpanel);				
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		
		updateTransactionList();
	}
	
	private void updateTransactionList()
	{
		defaultListModel = new DefaultListModel<>();
		
		for( int i = 0; i < transactionNameList.size(); i++ )
		{			
			defaultListModel.addElement(transactionNameList.get(i));
		}
		
		listTransaction.setModel(defaultListModel);
		
		this.repaint();
		this.revalidate();
	}
	
	private void getSelectedIndexes()
	{
		List<String> selectedValues = listTransaction.getSelectedValuesList();
		selectedTransactions = new ArrayList<Integer>();
		for( int i = 0; i < selectedValues.size(); i++ )
		{
			selectedTransactions.add(transactionNameList.indexOf(selectedValues.get(i)));
		}
	}
	
	public void populateTable( String transactionName, String[] columns, Object[][] rows )
	{
		((TransactionPanel) tabbedpaneTransactionPanel.getComponent(transactionNameList.indexOf(transactionName))).populateTable(columns, rows);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if( e.getSource() == buttonAdd )
		{
			String name = txtfieldTransactionName.getText().toString();
			if( !name.isEmpty() )
			{
				TransactionPanel t1 = new TransactionPanel(controller);
				t1.setName(name);
				t1.setTransactionName(name);
				transactionNameList.add(name);
				
				tabbedpaneTransactionPanel.addTab(name, t1);
				
				updateTransactionList();
				
				txtfieldTransactionName.setText("");
				buttonAdd.setEnabled(false);
			}
			else
			{
				labelWarning.setText("Transaction names must not be empty and must be unique!");
				labelWarning.setForeground(Color.RED);
			}
		}
		else if( e.getSource() == buttonRemove )
		{
			tabbedpaneTransactionPanel.remove(listTransaction.getSelectedIndex());
			
			transactionNameList.remove(listTransaction.getSelectedValue().toString());
			
			updateTransactionList();
		}
		else if( e.getSource() == buttonExecute )
		{
			getSelectedIndexes();
			for( int i = 0; i < selectedTransactions.size(); i++ )
			{
				// to get query:
				// ((TransactionPanel) tabbedpaneTransactionPanel.getComponent(i)).getQuery();
				
				System.out.println(selectedTransactions.size() + "asdfasdfasdfasd");
				String name = ((TransactionPanel) tabbedpaneTransactionPanel.getComponent(i)).getTransactionName();
				
				String query = "select * from hpq_crop WHERE hpq_hh_id = 11333;";/*((TransactionPanel) tabbedpaneTransactionPanel.getComponent(i)).getQuery();*/
				if(i == 1)
				{
					query = "select id,crop_line from hpq_crop WHERE hpq_hh_id = 11333;";
				} 
				else if(i==2)
				{
					query = "select id, croptype, croptype_o from hpq_crop WHERE hpq_hh_id = 11333;";
				}
				
				controller.sendTransaction(name, query, 1, 1);
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		if( e.getValueIsAdjusting() == false )
		{
			if( listTransaction.getSelectedIndex() != -1 )
			{
				buttonRemove.setEnabled(true);
				buttonExecute.setEnabled(true);
			}
			else
			{
				buttonRemove.setEnabled(false);
				buttonExecute.setEnabled(false);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0)
	{
		checkForDuplicateName();
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		checkForDuplicateName();
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
		checkForDuplicateName();
	}
	
	private void checkForDuplicateName()
	{
		if( !txtfieldTransactionName.getText().isEmpty() && transactionNameList.contains(txtfieldTransactionName.getText().toString()) ) 
		{
			buttonAdd.setEnabled(false);
			labelWarning.setText("Transaction names must not be empty and must be unique!");
			labelWarning.setForeground(Color.RED);
		}
		else
		{
			buttonAdd.setEnabled(true);
			labelWarning.setText("<- Transaction name here");
			labelWarning.setForeground(Color.BLACK);
		}
	}
}
