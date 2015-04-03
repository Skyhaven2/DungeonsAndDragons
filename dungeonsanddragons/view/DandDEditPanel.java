package dungeonsanddragons.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import dungeonsanddragons.controller.DandDAppController;

public class DandDEditPanel extends JPanel
{
	private DandDAppController baseController;
	private TableRender myRender;
	private JComboBoxTableRender myJComboBoxTableRender;
	private SpringLayout layout;
	private JButton toSearchPanelButton;
	private ImageIcon DandDLogo;
	private JLabel DandDLogoLabel;
	private JLabel title;
	private DefaultTableModel editTableModel;
	private JTable editTable;
	private JComboBox tablesComboBox;
	private JScrollPane editTablePane;
	private String[] tablesComboBoxArray;
	private String currentSelectedTable;

	public DandDEditPanel(DandDAppController baseController)
	{
		this.baseController = baseController;
		myRender = new TableRender();
		layout = new SpringLayout();
		buildTableComboBoxArray();
		DandDLogo = new ImageIcon(DandDEditPanel.class.getResource("/dungeonsanddragons/view/images/dd_logo.png"));

		toSearchPanelButton = new JButton("To Search Database Page");
		DandDLogoLabel = new JLabel(DandDLogo);
		title = new JLabel("Edit Database");
		editTableModel = new DefaultTableModel();
		editTable = new JTable(editTableModel);
		editTablePane = new JScrollPane(editTable);
		Dimension searchTablePaneDimension = new Dimension(700, 400);
		editTablePane.setPreferredSize(searchTablePaneDimension);
		tablesComboBox = new JComboBox(tablesComboBoxArray);

		setupPanel();
		setupLayout();
		setupListeners();
	}

	private void setupPanel()
	{
		this.setSize(1195, 775);
		this.setFocusable(true);
		this.setLayout(layout);
		this.setBackground(Color.BLACK);
		title.setForeground(Color.WHITE);
		editTablePane.getViewport().setBackground(Color.BLACK);
		editTablePane.setBorder(BorderFactory.createEmptyBorder());
		toSearchPanelButton.setPreferredSize(new Dimension(200, 25));
		title.setFont(title.getFont().deriveFont(40.0f));
		this.add(toSearchPanelButton);
		this.add(DandDLogoLabel);
		this.add(title);
		this.add(editTablePane);
		this.add(tablesComboBox);
	}

	private void setupLayout()
	{
		layout.putConstraint(SpringLayout.SOUTH, toSearchPanelButton, -10, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, toSearchPanelButton, -10, SpringLayout.EAST, this);
		layout.putConstraint(layout.HORIZONTAL_CENTER, title, 0, layout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.NORTH, title, 6, SpringLayout.SOUTH, DandDLogoLabel);
		layout.putConstraint(SpringLayout.NORTH, tablesComboBox, 6, SpringLayout.SOUTH, title);
		layout.putConstraint(layout.HORIZONTAL_CENTER, tablesComboBox, 0, layout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.NORTH, editTablePane, 6, SpringLayout.SOUTH, tablesComboBox);
		layout.putConstraint(layout.HORIZONTAL_CENTER, editTablePane, 0, layout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.NORTH, DandDLogoLabel, 15, SpringLayout.NORTH, this);
	}

	private void setupListeners()
	{
		toSearchPanelButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent click)
			{
				baseController.getFrame().ChangeToSearchPanel();
			}

		});

		tablesComboBox.addItemListener(new ItemListener()
		{

			@Override
			public void itemStateChanged(ItemEvent e)
			{
				// if (e.getStateChange() == ItemEvent.SELECTED &&
				// !(tablesComboBox.getSelectedItem().toString().equals("...")))
				// {
				// currentSelectedTable =
				// tablesComboBox.getSelectedItem().toString();
				// String query = "SELECT * FROM " + currentSelectedTable;
				// String[][] newData =
				// baseController.getDBController().runSELECTQueryTableGetTable(query);
				// String[] columnHeaders =
				// baseController.getDBController().runSELECTQueryGetColumnNames(query);
				// editTableModel.setDataVector(newData, columnHeaders);
				// editTable.setModel(editTableModel);
				// editTable.getTableHeader().setFont(new Font(null, Font.BOLD,
				// 12));
				// myRender.buildLargestHeight(newData.length);
				// for (int col = 0; col < editTable.getColumnCount(); col++)
				// {
				// editTable.getColumnModel().getColumn(col).setCellRenderer(myRender);
				// }
				// }

				if (e.getStateChange() == ItemEvent.SELECTED && !(tablesComboBox.getSelectedItem().toString().equals("...")))
				{
					currentSelectedTable = tablesComboBox.getSelectedItem().toString();
					String query = baseController.getDBController().buildSELECTQuery(currentSelectedTable);
					String[][] newData = baseController.getDBController().runSELECTQueryTableGetTable(query);
					String[] columnHeaders = baseController.getDBController().runSELECTQueryGetColumnNames(query);
					editTableModel = new DefaultTableModel();

					// editTableModel.setDataVector(newData, columnHeaders);
					myRender.buildLargestHeight(newData.length);
					for (int col = 0; col < columnHeaders.length; col++)
					{
						if (baseController.getDBController().checkIfComboBoxForEdit(col))
						{
							String[] newComboBoxData = baseController.getDBController().getComboBoxForEdit(col);
							
							editTableModel.addColumn(columnHeaders[col]);
						}
						else
						{
							String[] newStringData = new String[newData.length];
							for (int row = 0; row < newStringData.length; row++)
							{
								newStringData[row] = newData[row][col];
							}
							editTableModel.addColumn(columnHeaders[col], newStringData);
						}
					}

					editTable.setModel(editTableModel);

					for (int col = 0; col < columnHeaders.length; col++)
					{
						if (baseController.getDBController().checkIfComboBoxForEdit(col))
						{
							String[] newComboBoxData = baseController.getDBController().getComboBoxForEdit(col);
							JComboBoxTableRender myJComboBoxTableRender = new JComboBoxTableRender(newComboBoxData);
							editTable.getColumnModel().getColumn(col).setCellRenderer(myJComboBoxTableRender);
						}
						else
						{
							editTable.getColumnModel().getColumn(col).setCellRenderer(myRender);
						}
					}

					editTable.getTableHeader().setFont(new Font(null, Font.BOLD, 12));

				}
			}

		});

		editTableModel.addTableModelListener(new TableModelListener()
		{

			@Override
			public void tableChanged(TableModelEvent change)
			{
				// This gets fired when the SELECT statement is sent too!
				if (change.getType() == TableModelEvent.UPDATE)
				{
					try
					{
						int row = change.getFirstRow();
						int column = change.getColumn();
						Object newData = editTableModel.getValueAt(row, column);

						baseController.getDBController().runUPDATEQuery(newData.toString(), column, row, currentSelectedTable);
					}
					catch (Exception currentException)
					{

					}

				}
			}

		});
	}

	private void buildTableComboBoxArray()
	{
		String[] tablesAvailable = baseController.getDBController().findTablesDandD();
		tablesComboBoxArray = new String[tablesAvailable.length + 1];
		tablesComboBoxArray[0] = "...";
		for (int col = 0; col < tablesAvailable.length; col++)
		{
			tablesComboBoxArray[col + 1] = tablesAvailable[col];
		}
	}
}
