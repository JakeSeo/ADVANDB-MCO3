package view;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import controller.Controller;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

public class TransactionPanel extends JPanel implements ActionListener
{
	private Controller controller;
	
	private DefaultComboBoxModel<String> dcbmIsolation, dcbmAction;
	private DefaultTableModel defaultTableModel;
	
	private JButton buttonAddCondition, buttonSetValue;
	private JComboBox<String> cmboxIsolation, cmboxQueryAction;
	private JCheckBox ckboxRead, ckboxWrite, ckboxCentral, ckboxMarinduque, ckboxPalawan;
	private JLabel labelResults, labelRowsReturned, labelQueryRuntime, labelDatabase, labelIsolationLevel, labelQueryAction;
	private JPanel panelRead, panelWrite;
	private JScrollPane scrollpaneTable, scrollpaneRead, scrollpaneWrite;
	private JTable table;
	
	private String transactionName;
	
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
		
		ckboxRead = new JCheckBox("Read Operation");
		ckboxRead.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		ckboxRead.setBounds(10, 263, 153, 23);
		ckboxRead.setEnabled(false);
		ckboxRead.addActionListener(this);
		this.add(ckboxRead);
		
		ckboxWrite = new JCheckBox("Write Operation");
		ckboxWrite.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		ckboxWrite.setBounds(644, 263, 140, 23);
		ckboxWrite.setEnabled(false);
		ckboxWrite.addActionListener(this);
		this.add(ckboxWrite);
		
		panelRead = new JPanel();
		panelRead.setLayout(new BoxLayout(panelRead, BoxLayout.Y_AXIS));
		scrollpaneRead = new JScrollPane(panelRead, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpaneRead.setBounds(10, 288, 624, 121);
		scrollpaneRead.setAlignmentX(LEFT_ALIGNMENT);
		this.add(scrollpaneRead);
		
		panelWrite = new JPanel();
		panelWrite.setLayout(new BoxLayout(panelWrite, BoxLayout.Y_AXIS));
		scrollpaneWrite = new JScrollPane(panelWrite, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpaneWrite.setBounds(644, 287, 624, 121);
		scrollpaneWrite.setAlignmentX(LEFT_ALIGNMENT);
		this.add(scrollpaneWrite);
		
		buttonAddCondition = new JButton("Add Condition");
		buttonAddCondition.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		buttonAddCondition.setBounds(10, 418, 123, 25);
		buttonAddCondition.addActionListener(this);
		this.add(buttonAddCondition);
		
		buttonSetValue = new JButton("Set Value");
		buttonSetValue.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		buttonSetValue.setBounds(644, 414, 123, 25);
		buttonSetValue.addActionListener(this);
		this.add(buttonSetValue);
		
		labelDatabase = new JLabel("Database");
		labelDatabase.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		labelDatabase.setBounds(10, 454, 75, 14);
		this.add(labelDatabase);
		
		ckboxCentral = new JCheckBox("Central");
		ckboxCentral.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		ckboxCentral.setBounds(95, 450, 75, 23);
		this.add(ckboxCentral);
		
		ckboxMarinduque = new JCheckBox("Marinduque");
		ckboxMarinduque.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		ckboxMarinduque.setBounds(172, 450, 97, 23);
		this.add(ckboxMarinduque);
		
		ckboxPalawan = new JCheckBox("Palawan");
		ckboxPalawan.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		ckboxPalawan.setBounds(271, 450, 97, 23);
		this.add(ckboxPalawan);
		
		ButtonGroup checkboxes = new ButtonGroup();
		checkboxes.add(ckboxCentral);
		checkboxes.add(ckboxMarinduque);
		checkboxes.add(ckboxPalawan);
		
		labelIsolationLevel = new JLabel("Isolation Level");
		labelIsolationLevel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		labelIsolationLevel.setBounds(374, 454, 89, 14);
		this.add(labelIsolationLevel);
		
		String[] isolation = { "Read Uncommitted", "Read Committed", "Repeatable Read", "Serializable" };
		dcbmIsolation = new DefaultComboBoxModel<>(isolation);
		cmboxIsolation = new JComboBox<String>(dcbmIsolation);
		cmboxIsolation.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		cmboxIsolation.setBounds(457, 450, 140, 22);
		this.add(cmboxIsolation);
		
		labelQueryAction = new JLabel("Query Action");
		labelQueryAction.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		labelQueryAction.setBounds(607, 454, 75, 14);
		this.add(labelQueryAction);
		
		String[] action = { "Commit", "Rollback" };
		dcbmAction = new DefaultComboBoxModel<>(action);
		cmboxQueryAction = new JComboBox<String>(dcbmAction);
		cmboxQueryAction.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		cmboxQueryAction.setBounds(685, 450, 82, 22);
		this.add(cmboxQueryAction);
		
		setDatabase();
	}
	
	// populate the result table with the given column and row data
	public void populateTable(String[] columns, Object[][] rows )
	{
		defaultTableModel = new DefaultTableModel(rows, columns);
		table.setModel(defaultTableModel);
		
		labelRowsReturned.setText("Rows returned: " + rows.length + " rows");
		//labelQueryRuntime.setText("Query Runtime: " + controller.getQueryTime() / 1000 + " seconds");
	}
	
	// return the generated query based on user input
	public String getQuery()
	{
		if( ckboxRead.isSelected() && ckboxWrite.isSelected() )
		{
			return getWriteQuery();
		}
		else
		{
			return getReadQuery();
		}
	}
	
	// return generated select statement
	private String getReadQuery()
	{
		String query = "SELECT * FROM hpq_crop ";
		
		if( panelRead.getComponentCount() != 0 )
		{
			query += getConditions();
		}
		
		query += ";";
		
		System.out.println("Query READ: " + query);
			
		return query;
	}
	
	// return generated update statement
	private String getWriteQuery()
	{
		String query = "UPDATE hpq_crop ";
		
		query += getSetValues() + getConditions() + ";";
		
		System.out.println("Query WRITE: " + query);
		
		return query;
	}
	
	// return where clause based on user input
	private String getConditions()
	{
		String query = "WHERE ";
		
		for( int i = 0; i < panelRead.getComponentCount(); i++ )
		{
			if( i + 1 < panelRead.getComponentCount() )
			{
				query += ((ConditionPanel) panelRead.getComponent(i)).getCondition() + " AND ";
			}
			else
			{
				query += ((ConditionPanel) panelRead.getComponent(i)).getCondition() + " ";
			}
		}
		
		return query;
	}
	
	// return set clause based on user input
	private String getSetValues()
	{
		String query = "SET ";
		
		for( int i = 0; i < panelWrite.getComponentCount(); i++ )
		{
			if( i + 1 < panelWrite.getComponentCount() )
			{
				query += ((SetValuePanel) panelWrite.getComponent(i)).getSetValue() + " , ";
			}
			else
			{
				query += ((SetValuePanel) panelWrite.getComponent(i)).getSetValue() + " ";
			}
		}
		
		return query;
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
	
	public String getIsolationLevel()
	{
		return cmboxIsolation.getSelectedItem().toString();
	}
	
	public String getQueryAction()
	{
		return cmboxQueryAction.getSelectedItem().toString();
	}
	
	public String getTransactionName()
	{
		return transactionName;
	}
	
	public void setTransactionName( String transactionName )
	{
		this.transactionName = transactionName;
	}
	
	// ACTION LISTENER
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if ( e.getSource() == buttonAddCondition )
		{
			ConditionPanel newPanel = new ConditionPanel(this);
			newPanel.setPreferredSize(new Dimension(506, 52));
			panelRead.add(newPanel);
			
			ckboxRead.setSelected(true);
		}
		else if( e.getSource() == buttonSetValue )
		{
			SetValuePanel newPanel = new SetValuePanel(this);
			newPanel.setPreferredSize(new Dimension(506, 52));
			panelWrite.add(newPanel);
			
			ckboxRead.setSelected(true);
			ckboxWrite.setSelected(true);
		}
		
		
	}
	
	public void removeCondition( ConditionPanel conditionPanel )
	{
		panelRead.remove(conditionPanel);
		panelRead.revalidate();
		panelRead.repaint();
		
		if( panelRead.getComponentCount() == 0 )
		{
			ckboxRead.setSelected(false);
		}
	}
	
	public void removeChangeValue( SetValuePanel setValuePanel )
	{
		panelWrite.remove(setValuePanel);
		panelWrite.revalidate();
		panelWrite.repaint();
		
		if( panelWrite.getComponentCount() == 0 )
		{
			ckboxWrite.setSelected(false);
		}
		
		if( panelRead.getComponentCount() == 0 )
		{
			ckboxRead.setSelected(false);
		}
	}
}
