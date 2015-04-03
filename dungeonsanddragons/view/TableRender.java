package dungeonsanddragons.view;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TableRender extends JTextArea implements  TableCellRenderer
{
	public int[] largestHeight;
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		this.setText(value.toString());
		this.setWrapStyleWord(true);
		this.setLineWrap(true);
		int fontHeight = this.getFontMetrics(this.getFont()).getHeight();
		int textPixelLength = this.getFontMetrics(this.getFont()).stringWidth(this.getText());
		TableColumn columnSelceted = table.getColumnModel().getColumn(column); 
		int lines = (textPixelLength / (columnSelceted.getWidth())) + 1;         
		int height = fontHeight * lines;
		if(height > largestHeight[row])
		{
			table.setRowHeight(row, height);
			largestHeight[row] = height;
		}
		this.setFont(new Font("TimesNewRoman", Font.PLAIN, 12));
		
		return this;
	}
	
	public void buildLargestHeight(int numberOfRows)
	{
		largestHeight = new int[numberOfRows];
		for(int row = 0; row < numberOfRows; row++)
		{
			largestHeight[row] = 0;
		}
	}
}
