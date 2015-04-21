package dungeonsanddragons.view;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

public class JComboBoxTableEditor extends DefaultCellEditor
{
	public JComboBoxTableEditor(String[] items)
	{
		super(new JComboBox(items));
	}
}
