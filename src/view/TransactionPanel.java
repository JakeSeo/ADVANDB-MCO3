package view;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;
import javax.swing.table.DefaultTableModel;

import controller.Controller;

import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;

public class TransactionPanel extends JPanel implements ActionListener, KeyListener, MouseListener
{
	private Controller controller;
	
	private DefaultComboBoxModel<String> dcbmIsolation, dcbmAction;
	private DefaultListModel<String> defaultListModel;
	private DefaultTableModel defaultTableModel;
	private JCheckBox ckboxCentral, ckboxMarinduque, ckboxPalawan;
	private JComboBox<String> cmboxIsolation, cmboxQueryAction;
	private JLabel labelResults, labelRowsReturned, labelQueryRuntime, labelDatabase, labelIsolationLevel, labelQueryAction;
	private JList<String> listDefaultQuery;
	private JRadioButton rdbtnCustomQuery, rdbtnDefaultQueries;
	private JScrollPane scrollpaneTable, scrollpaneCustomQuery, scrollpaneDefaultQuery;
	private JTabbedPane tabbedpaneCustomQuery;
	private JTable table;
	private JTextArea textareaQuery;
	
	private final String[] defaultQueries = { "Query 1", "Query 2", "Query 3 Query 3 Query 3 Query 3 Query 3", "Query 4", "Query 5", "Query 6"
			, "Query 7"};
	
	public TransactionPanel( Controller controller )
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

		this.setLayout(null);
		this.setSize(1274, 480);
		this.setVisible(true);
		
		labelResults = new JLabel("Results");
		labelResults.setFont(new Font("Segoe UI", Font.BOLD, 15));
		labelResults.setBounds(10, 11, 103, 26);
		this.add(labelResults);
		
		defaultTableModel = new DefaultTableModel();
		table = new JTable(defaultTableModel);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		scrollpaneTable = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpaneTable.setBounds(10, 38, 1254, 186);
		this.add(scrollpaneTable);
		
		labelRowsReturned = new JLabel("Rows Returned: ");
		labelRowsReturned.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		labelRowsReturned.setBounds(11, 224, 276, 26);
		this.add(labelRowsReturned);
		
		labelQueryRuntime = new JLabel("Query Runtime: ");
		labelQueryRuntime.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		labelQueryRuntime.setBounds(644, 224, 377, 26);
		this.add(labelQueryRuntime);
		
		rdbtnCustomQuery = new JRadioButton("Custom Query");
		rdbtnCustomQuery.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		rdbtnCustomQuery.setBounds(10, 263, 153, 23);
		rdbtnCustomQuery.addActionListener(this);
		this.add(rdbtnCustomQuery);
		
		rdbtnDefaultQueries = new JRadioButton("Default Queries");
		rdbtnDefaultQueries.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		rdbtnDefaultQueries.setBounds(644, 263, 140, 23);
		rdbtnDefaultQueries.addActionListener(this);
		this.add(rdbtnDefaultQueries);
		
		ButtonGroup radioButtons = new ButtonGroup();
		radioButtons.add(rdbtnCustomQuery);
		radioButtons.add(rdbtnDefaultQueries);
		
		textareaQuery = new JTextArea();
		textareaQuery.setFont(new Font("Courier New", Font.PLAIN, 13));
		textareaQuery.setEnabled(false);
		textareaQuery.addKeyListener(this);
		textareaQuery.addMouseListener(this);
		scrollpaneCustomQuery = new JScrollPane(textareaQuery, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpaneCustomQuery.setBounds(10, 287, 620, 121);
		this.add(scrollpaneCustomQuery);
		
		defaultListModel = new DefaultListModel<>();
		addQueryListToListModel();
		listDefaultQuery = new JList<String>(defaultListModel);
		listDefaultQuery.setFont(new Font("Courier New", Font.PLAIN, 13));
		listDefaultQuery.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listDefaultQuery.setLayoutOrientation(JList.VERTICAL);
		listDefaultQuery.setEnabled(false);
		listDefaultQuery.addMouseListener(this);
		scrollpaneDefaultQuery = new JScrollPane(listDefaultQuery, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpaneDefaultQuery.setBounds(644, 287, 620, 121);
		this.add(scrollpaneDefaultQuery);
		
		labelDatabase = new JLabel("Database");
		labelDatabase.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		labelDatabase.setBounds(10, 419, 75, 14);
		this.add(labelDatabase);
		
		ckboxCentral = new JCheckBox("Central");
		ckboxCentral.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		ckboxCentral.setBounds(95, 415, 75, 23);
		this.add(ckboxCentral);
		
		ckboxMarinduque = new JCheckBox("Marinduque");
		ckboxMarinduque.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		ckboxMarinduque.setBounds(172, 415, 97, 23);
		this.add(ckboxMarinduque);
		
		ckboxPalawan = new JCheckBox("Palawan");
		ckboxPalawan.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		ckboxPalawan.setBounds(271, 415, 97, 23);
		this.add(ckboxPalawan);
		
		ButtonGroup checkboxes = new ButtonGroup();
		checkboxes.add(ckboxCentral);
		checkboxes.add(ckboxMarinduque);
		checkboxes.add(ckboxPalawan);
		
		labelIsolationLevel = new JLabel("Isolation Level");
		labelIsolationLevel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		labelIsolationLevel.setBounds(12, 450, 89, 14);
		this.add(labelIsolationLevel);
		
		String[] isolation = { "Read Uncommitted", "Read Committed", "Repeatable Read", "Serializable" };
		dcbmIsolation = new DefaultComboBoxModel<>(isolation);
		cmboxIsolation = new JComboBox<String>(dcbmIsolation);
		cmboxIsolation.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		cmboxIsolation.setBounds(95, 446, 140, 22);
		this.add(cmboxIsolation);
		
		labelQueryAction = new JLabel("Query Action");
		labelQueryAction.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		labelQueryAction.setBounds(245, 450, 75, 14);
		this.add(labelQueryAction);
		
		String[] action = { "Commit", "Rollback" };
		dcbmAction = new DefaultComboBoxModel<>(action);
		cmboxQueryAction = new JComboBox<String>(dcbmAction);
		cmboxQueryAction.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		cmboxQueryAction.setBounds(323, 446, 82, 22);
		this.add(cmboxQueryAction);
		
		setDatabase();
	}

	private void addQueryListToListModel()
	{
		for( int i = 0; i < defaultQueries.length; i++ )
		{
			defaultListModel.addElement(defaultQueries[i]);
		}
	}
	
	private void populateTable( ArrayList<String> columns, Object[][] rows )
	{
		defaultTableModel = new DefaultTableModel(rows, columns.toArray(new String[0]));
		table.setModel(defaultTableModel);
		
		labelRowsReturned.setText("Rows returned: " + rows.length + " rows");
		//labelQueryRuntime.setText("Query Runtime: " + controller.getQueryTime() / 1000 + " seconds");
	}
	
	private void setDatabase()
	{
		String type = "";
		if( controller != null )
		{
			type = controller.getType();
		}
		System.out.println(type);
		
		if( type.equalsIgnoreCase("Central") )
		{
			ckboxCentral.setSelected(true);
		}
		else if( type.equalsIgnoreCase("Marinduque") )
		{
			ckboxMarinduque.setSelected(true);
		}
		else if( type.equalsIgnoreCase("Palawan") )
		{
			ckboxPalawan.setSelected(true);
		}
	}
	
	public List<String> getInput()
	{
		if( rdbtnCustomQuery.isSelected() )
		{
			ArrayList<String> query = new ArrayList<String>(0);
			query.add(textareaQuery.getText().toString());
			
			return query;
		}
		else if( rdbtnDefaultQueries.isSelected() )
		{
			return listDefaultQuery.getSelectedValuesList();
		}
		
		return new ArrayList<String>();
	}
	
	public String getDatabase()
	{
		if ( ckboxCentral.isSelected() )
		{
			return ckboxCentral.getText().toString();
		}
		else if ( ckboxMarinduque.isSelected() )
		{
			return ckboxMarinduque.getText().toString();
		}
		else if ( ckboxPalawan.isSelected() )
		{
			return ckboxPalawan.getText().toString();
		}
		else
		{
			return "";
		}
	}
	
	public String getIsolationLevel()
	{
		return cmboxIsolation.getSelectedItem().toString();
	}
	
	public String getQueryAction()
	{
		return cmboxQueryAction.getSelectedItem().toString();
	}
	
	// ACTION LISTENER
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if( e.getSource() == rdbtnCustomQuery )
		{
			textareaQuery.setEnabled(true);
			listDefaultQuery.setEnabled(false);
			textareaQuery.requestFocus();
		}
		else if( e.getSource() == rdbtnDefaultQueries )
		{
			textareaQuery.setEnabled(false);
			listDefaultQuery.setEnabled(true);
		}
	}

	// KEY LISTENER
	@Override
	public void keyPressed(KeyEvent e)
	{
		checkCustomQueryInput();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		checkCustomQueryInput();
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		checkCustomQueryInput();
	}
	
	private void checkCustomQueryInput()
	{
		/*if( textareaQuery.getText().isEmpty() && rdbtnCustomQuery.isSelected() )
		{
			buttonExecute.setEnabled(false);
		}
		else
		{
			buttonExecute.setEnabled(true);
		}*/
	}
	
	// MOUSE LISTENER
	@Override
	public void mouseClicked(MouseEvent e)
	{
		setEnabledComponents(e);
	}

	@Override
	public void mouseEntered(MouseEvent arg0){}

	@Override
	public void mouseExited(MouseEvent arg0){}

	@Override
	public void mousePressed(MouseEvent e)
	{
		setEnabledComponents(e);
	}

	@Override
	public void mouseReleased(MouseEvent arg0){}
	
	private void setEnabledComponents( MouseEvent e )
	{
		if( e.getSource() == textareaQuery )
		{
			rdbtnCustomQuery.setSelected(true);
			textareaQuery.setEnabled(true);
			listDefaultQuery.setEnabled(false);
			textareaQuery.requestFocus();
			
			checkQueryInputs();
		}
		else if( e.getSource() == listDefaultQuery )
		{
			rdbtnDefaultQueries.setSelected(true);
			textareaQuery.setEnabled(false);
			listDefaultQuery.setEnabled(true);
			
			checkQueryInputs();
		}
	}
	
	private void checkQueryInputs()
	{
		/*if( rdbtnCustomQuery.isSelected() && !textareaQuery.getText().isEmpty() )
		{
			buttonExecute.setEnabled(true);
		}
		else if( rdbtnDefaultQueries.isSelected() && listDefaultQuery.getSelectedIndex() != -1 )
		{
			buttonExecute.setEnabled(true);
		}
		else
		{
			buttonExecute.setEnabled(false);
		}*/
	}
}
