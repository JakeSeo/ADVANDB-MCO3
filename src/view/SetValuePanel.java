package view;

import java.awt.Dimension;
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
import java.awt.Font;

public class SetValuePanel extends JPanel implements ActionListener
{
	private DefaultComboBoxModel<String> dcbmColumn;
	
	private JButton buttonRemove;
	private JComboBox<String> cmboxColumn;
	private JLabel labelArrow;
	private JTextField textfieldInput;
	
	private String[] columns =  { "crop_line", "croptype", "croptype_o", "crop_vol" };
	
	private TransactionPanel transactionPanel;
	
	public SetValuePanel( TransactionPanel transactionPanel )
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
		this.setSize(370, 52);
		
		dcbmColumn = new DefaultComboBoxModel<>(columns);
		cmboxColumn = new JComboBox<String>(dcbmColumn);
		cmboxColumn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		cmboxColumn.setBounds(10, 11, 100, 30);
		((JLabel)cmboxColumn.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		this.add(cmboxColumn);
		
		labelArrow = new JLabel("<html><div style='text-align: center;'>>></html>");
		labelArrow.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		labelArrow.setBounds(130, 11, 35, 30);
		this.add(labelArrow);

		textfieldInput = new JTextField();
		textfieldInput.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		textfieldInput.setBounds(165, 11, 140, 30);
		this.add(textfieldInput);
		textfieldInput.setColumns(10);	
		
		buttonRemove = new JButton("x");
		buttonRemove.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		buttonRemove.setBounds(315, 11, 45, 30);
		buttonRemove.addActionListener(this);
		this.add(buttonRemove);
	}

	public String getUpdate()
	{
		boolean isInt;
		
		try
		{
			Integer.parseInt(textfieldInput.getText().toString());
			isInt = true;
		}
		catch( NumberFormatException e )
		{
			isInt = false;
		}
		
		if( isInt )
		{
			return cmboxColumn.getSelectedItem().toString() + " = " + textfieldInput.getText().toString();
		}
		else
		{
			return cmboxColumn.getSelectedItem().toString() + " = '" + textfieldInput.getText().toString() + "'";
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if( e.getSource() == buttonRemove )
		{
			transactionPanel.removeUpdate(this);
		}
	}
}
