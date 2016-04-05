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
import java.util.Arrays;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;

public class TransactionPanel extends JPanel implements ActionListener
{
	private Controller controller;
	
	private DefaultComboBoxModel<String> dcbmIsolation, dcbmAction;
	private DefaultTableModel defaultTableModel;
	
	private JButton buttonAddCondition;
	private JComboBox<String> cmboxIsolation, cmboxQueryAction;
	private JLabel labelResults, labelRowsReturned, labelQueryRuntime, labelHpqcropQueryConditions, labelIsolationLevel, labelQueryAction;
	private JPanel panelConditions;
	private JScrollPane scrollpaneTable, scrollpaneCondition;
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
		
		labelHpqcropQueryConditions = new JLabel("hpq_crop Query Conditions");
		labelHpqcropQueryConditions.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		labelHpqcropQueryConditions.setBounds(10, 262, 196, 26);
		this.add(labelHpqcropQueryConditions);
		
		panelConditions = new JPanel();
		panelConditions.setLayout(new BoxLayout(panelConditions, BoxLayout.Y_AXIS));
		scrollpaneCondition = new JScrollPane(panelConditions, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpaneCondition.setBounds(10, 288, 624, 151);
		scrollpaneCondition.setAlignmentX(LEFT_ALIGNMENT);
		this.add(scrollpaneCondition);
		
		buttonAddCondition = new JButton("Add Condition");
		buttonAddCondition.setBounds(644, 330, 130, 60);
		buttonAddCondition.addActionListener(this);
		this.add(buttonAddCondition);
		
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
	}
	
	public void populateTable(String[] columns, Object[][] rows )
	{
		defaultTableModel = new DefaultTableModel(rows, columns);
		table.setModel(defaultTableModel);
		
		labelRowsReturned.setText("Rows returned: " + rows.length + " rows");
		//labelQueryRuntime.setText("Query Runtime: " + controller.getQueryTime() / 1000 + " seconds");
	}
	
	public String getQuery()
	{
		String query = "SELECT * FROM hpq_crop ";
		
		if( panelConditions.getComponentCount() != 0 )
		{
			query += "WHERE ";
		}
		
		for( int i = 0; i < panelConditions.getComponentCount(); i++ )
		{
			if( i + 1 < panelConditions.getComponentCount() )
			{
				query += ((ConditionPanel) panelConditions.getComponent(i)).getCondition() + " AND ";
			}
			else
			{
				query += ((ConditionPanel) panelConditions.getComponent(i)).getCondition() + " ";
			}
		}
		
		query += ";";
		
		System.out.println("Query: " + query);
		
		
		return query;
	}
	
	public List<String> getConditions()
	{
		ArrayList<String> conditions = new ArrayList<>(0);
		
		for( int i = 0; i < panelConditions.getComponentCount(); i++ )
		{
			conditions.add(((ConditionPanel) panelConditions.getComponent(i)).getCondition());
		}
		
		/*System.out.println("TRANSACION PANEL GET INPUT START");
		for( int i = 0; i < conditions.size(); i++ )
		{
			System.out.println(conditions.get(i));
		}
		System.out.println("TRANSACION PANEL GET INPUT END");
		*/
		return conditions;
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
			newPanel.setPreferredSize(new Dimension(506, 47));
			panelConditions.add(newPanel);
		}
	}
	
	public void removeCondition( ConditionPanel conditionPanel )
	{
		panelConditions.remove(conditionPanel);
		panelConditions.revalidate();
		panelConditions.repaint();
	}
}
