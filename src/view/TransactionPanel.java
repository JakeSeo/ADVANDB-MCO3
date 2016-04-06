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
	
	private JButton buttonAddSelect, buttonAddCondition, buttonSetValue;
	private JComboBox<String> cmboxIsolation, cmboxQueryAction;
	private JCheckBox ckboxReadSelect, ckboxReadWhere, ckboxWrite, ckboxCentral, ckboxMarinduque, ckboxPalawan;
	private JLabel labelResults, labelRowsReturned, labelQueryRuntime, labelDatabase, labelIsolationLevel, labelQueryAction;
	private JPanel panelSelect, panelWhere, panelUpdate;
	private JScrollPane scrollpaneTable, scrollpaneSelect, scrollpaneWhere, scrollpaneUpdate;
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
		
		labelRowsReturned = new JLabel("Rows Returned: ");
		labelRowsReturned.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		labelRowsReturned.setBounds(306, 11, 276, 26);
		this.add(labelRowsReturned);
		
		labelQueryRuntime = new JLabel("Query Runtime: ");
		labelQueryRuntime.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		labelQueryRuntime.setBounds(868, 11, 377, 26);
		this.add(labelQueryRuntime);
		
		defaultTableModel = new DefaultTableModel();
		table = new JTable(defaultTableModel);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		scrollpaneTable = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpaneTable.setBounds(10, 38, 1254, 186);
		this.add(scrollpaneTable);
		
		ckboxReadSelect = new JCheckBox("SELECT *");
		ckboxReadSelect.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		ckboxReadSelect.setEnabled(false);
		ckboxReadSelect.setSelected(true);
		ckboxReadSelect.setBounds(9, 231, 140, 23);
		add(ckboxReadSelect);
		
		buttonAddSelect = new JButton("Add Select");
		buttonAddSelect.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		buttonAddSelect.setBounds(205, 231, 115, 25);
		buttonAddSelect.addActionListener(this);
		add(buttonAddSelect);
		
		ckboxReadWhere = new JCheckBox("WHERE");
		ckboxReadWhere.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		ckboxReadWhere.setBounds(365, 231, 210, 23);
		ckboxReadWhere.setEnabled(false);
		ckboxReadWhere.addActionListener(this);
		this.add(ckboxReadWhere);
		
		buttonAddCondition = new JButton("Add Condition");
		buttonAddCondition.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		buttonAddCondition.setBounds(710, 231, 115, 25);
		buttonAddCondition.addActionListener(this);
		this.add(buttonAddCondition);
		
		ckboxWrite = new JCheckBox("UPDATE");
		ckboxWrite.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		ckboxWrite.setBounds(868, 231, 225, 23);
		ckboxWrite.setEnabled(false);
		ckboxWrite.addActionListener(this);
		this.add(ckboxWrite);
		
		buttonSetValue = new JButton("Set Value");
		buttonSetValue.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		buttonSetValue.setBounds(1149, 231, 115, 25);
		buttonSetValue.addActionListener(this);
		this.add(buttonSetValue);
		
		panelSelect = new JPanel();
		panelSelect.setLayout(new BoxLayout(panelSelect, BoxLayout.Y_AXIS));
		scrollpaneSelect = new JScrollPane(panelSelect, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpaneSelect.setBounds(10, 261, 310, 182);
		scrollpaneSelect.setAlignmentX(LEFT_ALIGNMENT);
		this.add(scrollpaneSelect);
		
		panelWhere = new JPanel();
		panelWhere.setLayout(new BoxLayout(panelWhere, BoxLayout.Y_AXIS));
		scrollpaneWhere = new JScrollPane(panelWhere, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpaneWhere.setBounds(365, 261, 460, 178);
		scrollpaneWhere.setAlignmentX(LEFT_ALIGNMENT);
		this.add(scrollpaneWhere);
		
		panelUpdate = new JPanel();
		panelUpdate.setLayout(new BoxLayout(panelUpdate, BoxLayout.Y_AXIS));
		scrollpaneUpdate = new JScrollPane(panelUpdate, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpaneUpdate.setBounds(869, 261, 395, 178);
		scrollpaneUpdate.setAlignmentX(LEFT_ALIGNMENT);
		this.add(scrollpaneUpdate);
		
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
		if( ckboxReadWhere.isSelected() && ckboxWrite.isSelected() )
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
		String query = "";
		String groupby = "";
		
		if( panelSelect.getComponentCount() == 0 )
		{
			query += "SELECT * ";
		}
		else
		{
			query += getSelect();
			groupby = getGroupBy();
		}
		
		query += " FROM hpq_crop ";
		
		if( panelWhere.getComponentCount() != 0 )
		{
			query += getWhere();
		}
		
		query += groupby + ";";
		
		System.out.println("Query READ: " + query);
			
		return query;
	}
	
	// return generated update statement
	private String getWriteQuery()
	{
		String query = "UPDATE hpq_crop ";
		
		query += getUpdate() + getWhere() + ";";
		
		System.out.println("Query WRITE: " + query);
		
		return query;
	}
	
	private String getSelect()
	{
		String query = "SELECT ";
		
		for( int i = 0; i < panelSelect.getComponentCount(); i++ )
		{
			if( i + 1 < panelSelect.getComponentCount() )
			{
				query += ((SelectPanel) panelSelect.getComponent(i)).getSelect() + " , ";
			}
			else
			{
				query += ((SelectPanel) panelSelect.getComponent(i)).getSelect() + " ";
			}
		}
		
		return query;
	}
	
	private String getGroupBy()
	{
		String query = "GROUP BY ";
		
		for( int i = 0; i < panelSelect.getComponentCount(); i++ )
		{
			if( !((SelectPanel) panelSelect.getComponent(i)).isAggregate() )
			{
				if( i + 1 < panelSelect.getComponentCount() )
				{
					query += ((SelectPanel) panelSelect.getComponent(i)).getSelect() + " , ";
				}
				else
				{
					query += ((SelectPanel) panelSelect.getComponent(i)).getSelect() + " ";
				}
			}
		}
		
		if( query.equals("GROUP BY ") )
		{
			query = "";
		}
		
		if( query.endsWith(" , ") )
		{
			query = query.substring(0, query.length() - 2);
		}
		
		return query;
	}
	
	// return where clause based on user input
	private String getWhere()
	{
		String query = "WHERE ";
		
		for( int i = 0; i < panelWhere.getComponentCount(); i++ )
		{
			if( i + 1 < panelWhere.getComponentCount() )
			{
				query += ((ConditionPanel) panelWhere.getComponent(i)).getWhere() + " AND ";
			}
			else
			{
				query += ((ConditionPanel) panelWhere.getComponent(i)).getWhere() + " ";
			}
		}
		
		return query;
	}
	
	// return set clause based on user input
	private String getUpdate()
	{
		String query = "SET ";
		
		for( int i = 0; i < panelUpdate.getComponentCount(); i++ )
		{
			if( i + 1 < panelUpdate.getComponentCount() )
			{
				query += ((SetValuePanel) panelUpdate.getComponent(i)).getUpdate() + " , ";
			}
			else
			{
				query += ((SetValuePanel) panelUpdate.getComponent(i)).getUpdate() + " ";
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
		if( e.getSource() == buttonAddSelect )
		{
			SelectPanel selectPanel = new SelectPanel(this);
			selectPanel.setPreferredSize(new Dimension(280, 52));
			panelSelect.add(selectPanel);
			
			ckboxReadSelect.setSelected(true);
			ckboxReadSelect.setText("SELECT");
		}
		else if ( e.getSource() == buttonAddCondition )
		{
			ConditionPanel conditionPanel = new ConditionPanel(this);
			conditionPanel.setPreferredSize(new Dimension(435, 52));
			panelWhere.add(conditionPanel);
			
			ckboxReadSelect.setSelected(true);
			ckboxReadWhere.setSelected(true);
		}
		else if( e.getSource() == buttonSetValue )
		{
			SetValuePanel setValuePanel = new SetValuePanel(this);
			setValuePanel.setPreferredSize(new Dimension(370, 52));
			panelUpdate.add(setValuePanel);
			
			ckboxReadSelect.setSelected(false);
			ckboxReadWhere.setSelected(true);
			ckboxWrite.setSelected(true);
		}
	}
	
	public void removeSelect( SelectPanel selectPanel )
	{
		panelSelect.remove(selectPanel);
		panelSelect.revalidate();
		panelSelect.repaint();
		
		if( panelSelect.getComponentCount() == 0 )
		{
			ckboxReadSelect.setText("SELECT *");
		}
	}
	
	public void removeWhere( ConditionPanel conditionPanel )
	{
		panelWhere.remove(conditionPanel);
		panelWhere.revalidate();
		panelWhere.repaint();
		
		if( panelWhere.getComponentCount() == 0 )
		{
			ckboxReadWhere.setSelected(false);
		}
	}
	
	public void removeUpdate( SetValuePanel setValuePanel )
	{
		panelUpdate.remove(setValuePanel);
		panelUpdate.revalidate();
		panelUpdate.repaint();
		
		if( panelUpdate.getComponentCount() == 0 )
		{
			ckboxWrite.setSelected(false);
			ckboxReadSelect.setSelected(true);
		}
		
		if( panelWhere.getComponentCount() == 0 )
		{
			ckboxReadWhere.setSelected(false);
		}
	}
}
