package view;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;

public class MainGUI extends JFrame implements ActionListener, KeyListener, MouseListener
{
	private Controller controller;
	
	private DefaultComboBoxModel<String> dcbmIsolation, dcbmAction;
	private DefaultListModel<String> defaultListModel;
	private DefaultTableModel defaultTableModel;
	
	private JButton buttonExecute;
	private JComboBox<String> cmboxIsolation, cmboxQueryAction;
	private JLabel labelResults, labelRowsReturned, labelQueryRuntime, labelIsolationLevel, labelQueryAction;
	private JList<String> listDefaultQueries;
	private JPanel jpanel;
	private JRadioButton rdbtnCustomQuery, rdbtnDefaultQueries;
	private JScrollPane tableScrollPane, queryScrollPane, defaultQueryScrollPane;
	private JTable table;
	private JTextArea textareaQuery;
	
	private final String[] defaultQueries = { "Query 1", "Query 2", "Query 3 Query 3 Query 3 Query 3 Query 3", "Query 4", "Query 5", "Query 6"
			, "Query 7"};
	
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
		
		jpanel = new JPanel();
		jpanel.setSize(1300, 700);
		jpanel.setLayout(null);
		
		defaultTableModel = new DefaultTableModel();
		
		labelResults = new JLabel("Results");
		labelResults.setFont(new Font("Segoe UI", Font.BOLD, 15));
		labelResults.setBounds(10, 11, 103, 26);
		jpanel.add(labelResults);
		
		table = new JTable(defaultTableModel);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		tableScrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableScrollPane.setBounds(10, 38, 1274, 338);
		jpanel.add(tableScrollPane);
		
		labelRowsReturned = new JLabel("Rows Returned: ");
		labelRowsReturned.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		labelRowsReturned.setBounds(11, 375, 276, 26);
		jpanel.add(labelRowsReturned);
		
		labelQueryRuntime = new JLabel("Query Runtime: ");
		labelQueryRuntime.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		labelQueryRuntime.setBounds(654, 375, 377, 26);
		jpanel.add(labelQueryRuntime);
		
		rdbtnCustomQuery = new JRadioButton("Custom Query");
		rdbtnCustomQuery.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		rdbtnCustomQuery.setBounds(10, 414, 153, 23);
		rdbtnCustomQuery.addActionListener(this);
		jpanel.add(rdbtnCustomQuery);
		
		textareaQuery = new JTextArea();
		textareaQuery.setFont(new Font("Courier New", Font.PLAIN, 15));
		textareaQuery.setEnabled(false);
		textareaQuery.addKeyListener(this);
		textareaQuery.addMouseListener(this);
		queryScrollPane = new JScrollPane(textareaQuery, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		queryScrollPane.setBounds(10, 438, 630, 157);
		jpanel.add(queryScrollPane);
		
		rdbtnDefaultQueries = new JRadioButton("Default Queries");
		rdbtnDefaultQueries.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		rdbtnDefaultQueries.setBounds(654, 414, 140, 23);
		rdbtnDefaultQueries.addActionListener(this);
		jpanel.add(rdbtnDefaultQueries);
		
		ButtonGroup radioButtons = new ButtonGroup();
		radioButtons.add(rdbtnCustomQuery);
		radioButtons.add(rdbtnDefaultQueries);
		
		defaultListModel = new DefaultListModel<>();
		addQueryListToListModel();
		listDefaultQueries = new JList<String>(defaultListModel);
		listDefaultQueries.setFont(new Font("Courier New", Font.PLAIN, 15));
		listDefaultQueries.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listDefaultQueries.setLayoutOrientation(JList.VERTICAL);
		listDefaultQueries.setEnabled(false);
		listDefaultQueries.addMouseListener(this);
		defaultQueryScrollPane = new JScrollPane(listDefaultQueries, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		defaultQueryScrollPane.setBounds(654, 438, 630, 157);
		jpanel.add(defaultQueryScrollPane);
		
		labelIsolationLevel = new JLabel("Isolation Level");
		labelIsolationLevel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		labelIsolationLevel.setBounds(10, 606, 89, 14);
		jpanel.add(labelIsolationLevel);
		
		String[] isolation = { "Read Uncommitted", "Read Committed", "Repeatable Read", "Serializable" };
		dcbmIsolation = new DefaultComboBoxModel<>(isolation);
		cmboxIsolation = new JComboBox<String>(dcbmIsolation);
		cmboxIsolation.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		cmboxIsolation.setBounds(109, 601, 140, 22);
		jpanel.add(cmboxIsolation);
		
		labelQueryAction = new JLabel("Query Action");
		labelQueryAction.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		labelQueryAction.setBounds(10, 637, 82, 14);
		jpanel.add(labelQueryAction);
		
		String[] action = { "Commit", "Rollback" };
		dcbmAction = new DefaultComboBoxModel<>(action);
		cmboxQueryAction = new JComboBox<String>(dcbmAction);
		cmboxQueryAction.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		cmboxQueryAction.setBounds(109, 632, 140, 22);
		jpanel.add(cmboxQueryAction);
		
		buttonExecute = new JButton("Execute");
		buttonExecute.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		buttonExecute.setBounds(259, 600, 89, 54);
		buttonExecute.setEnabled(false);
		buttonExecute.addActionListener(this);
		jpanel.add(buttonExecute);
		
		this.setTitle("ADVANDB MCO3");
		this.setSize(1300, 700);
		this.setContentPane(jpanel);
		getContentPane().setLayout(null);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
	}

	private void addQueryListToListModel()
	{
		for( int i = 0; i < defaultQueries.length; i++ )
		{
			defaultListModel.addElement(defaultQueries[i]);
		}
	}
	
	private void populateTable()
	{
		
	}
	
	// ACTION LISTENER
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if( e.getSource() == rdbtnCustomQuery )
		{
			textareaQuery.setEnabled(true);
			listDefaultQueries.setEnabled(false);
			buttonExecute.setEnabled(true);
		}
		else if( e.getSource() == rdbtnDefaultQueries )
		{
			textareaQuery.setEnabled(false);
			listDefaultQueries.setEnabled(true);
		}
		else if( e.getSource() == buttonExecute )
		{
			if( rdbtnCustomQuery.isSelected() )
			{
				controller.READ(textareaQuery.getText());
			}
			else if( rdbtnDefaultQueries.isSelected() )
			{
				
			}
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
		if( textareaQuery.getText().isEmpty() && rdbtnCustomQuery.isSelected() )
		{
			buttonExecute.setEnabled(false);
		}
		else
		{
			buttonExecute.setEnabled(true);
		}
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
			listDefaultQueries.setEnabled(false);
			textareaQuery.requestFocus();
			
			checkQueryInputs();
		}
		else if( e.getSource() == listDefaultQueries )
		{
			rdbtnDefaultQueries.setSelected(true);
			textareaQuery.setEnabled(false);
			listDefaultQueries.setEnabled(true);
			
			checkQueryInputs();
		}
	}
	
	private void checkQueryInputs()
	{
		if( rdbtnCustomQuery.isSelected() && !textareaQuery.getText().isEmpty() )
		{
			buttonExecute.setEnabled(true);
		}
		else if( rdbtnDefaultQueries.isSelected() && listDefaultQueries.getSelectedIndex() != -1 )
		{
			buttonExecute.setEnabled(true);
		}
		else
		{
			buttonExecute.setEnabled(false);
		}
	}
}
