package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class SelectPanel extends JPanel implements ActionListener
{
	private DefaultComboBoxModel<String> dcbmColumn, dcbmAggregate;
	
	private JButton buttonRemove;
	private JComboBox<String> cmboxColumn, cmboxAggregate;
	private JLabel labelArrow;
	
	private String[] columns = { "hpq_hh_id", "id", "crop_line", "croptype", "croptype_o", "crop_vol" };
	private String[] aggregate = { "", "AVG", "COUNT", "MAX", "MIN", "SUM" };
	
	private TransactionPanel transactionPanel;
	
	public SelectPanel( TransactionPanel transactionPanel )
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
		
		this.transactionPanel = transactionPanel;
		
		this.setLayout(null);
		this.setSize(280, 52);
		
		dcbmColumn = new DefaultComboBoxModel<>(columns);
		cmboxColumn = new JComboBox<String>(dcbmColumn);
		cmboxColumn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		cmboxColumn.setBounds(10, 11, 100, 30);
		((JLabel)cmboxColumn.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		this.add(cmboxColumn);
		
		labelArrow = new JLabel("<html><div style='text-align: center;'>-></html>");
		labelArrow.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		labelArrow.setBounds(120, 11, 35, 30);
		this.add(labelArrow);;
		
		dcbmAggregate = new DefaultComboBoxModel<>(aggregate);
		cmboxAggregate = new JComboBox<String>(dcbmAggregate);
		cmboxAggregate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		cmboxAggregate.setBounds(140, 11, 75, 30);
		((JLabel)cmboxAggregate.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		this.add(cmboxAggregate);
		
		buttonRemove = new JButton("x");
		buttonRemove.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		buttonRemove.setBounds(225, 11, 45, 30);
		buttonRemove.addActionListener(this);
		this.add(buttonRemove);
		
		
	}

	public boolean isAggregate()
	{
		if( cmboxAggregate.getSelectedIndex() == 0 )
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public String getSelect()
	{
		if( cmboxAggregate.getSelectedIndex() == 0 )
		{
			return cmboxColumn.getSelectedItem().toString();
		}
		else
		{
			return cmboxAggregate.getSelectedItem().toString() + "(" + cmboxColumn.getSelectedItem().toString() + ")";
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if( e.getSource() == buttonRemove )
		{
			transactionPanel.removeSelect(this);
		}
	}
}
