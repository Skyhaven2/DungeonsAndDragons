package dungeonsanddragons.view;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class JComboBoxTableRender extends JComboBox implements TableCellRenderer
{
	public JComboBoxTableRender(String[] items)
	{
		super(items);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		if (isSelected)
		{
			setForeground(table.getSelectionForeground());
			super.setBackground(table.getSelectionBackground());
		}
		else
		{
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
		setSelectedItem(value);
		if(table.getRowHeight(row) < this.getPreferredSize().height)
		{
			table.setRowHeight(this.getPreferredSize().height);
		}
		return this;
	}
}
