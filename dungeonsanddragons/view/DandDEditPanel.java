package dungeonsanddragons.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import dungeonsanddragons.controller.DandDAppController;

public class DandDEditPanel extends JPanel
{
	/**
	 * Reference to the app controller
	 */
	private DandDAppController baseController;
	/**
	 * Table column renderer used for JTextAreas will be used for columns that
	 * do not contain foreign keys
	 */
	private TableRender myRender;
	/**
	 * Table column renderer used for JComboBoxes will be used for columns that
	 * do contain foreign keys
	 */
	private JComboBoxTableRender myJComboBoxTableRender;
	/**
	 * DandDEditPanel layout
	 */
	private SpringLayout layout;
	/**
	 * button used to travel to the search page
	 */
	private JButton toSearchPanelButton;
	/**
	 * D&D logo placed at the top of each page
	 */
	private ImageIcon DandDLogo;
	/**
	 * The label that contains the D&D logo
	 */
	private JLabel DandDLogoLabel;
	/**
	 * The pages title, in this case it is the edit page
	 */
	private JLabel title;
	/**
	 * the model to be used with the generated data table
	 */
	private DefaultTableModel editTableModel;
	/**
	 * the table to be used with the generated data table
	 */
	private JTable editTable;
	/**
	 * Contains the table names of the tables available in the d and d database
	 * generates a table when a table is selected
	 */
	private JComboBox<String> tablesComboBox;
	/**
	 * Contains the data table
	 */
	private JScrollPane editTablePane;
	/**
	 * Contains the table names of the tables available in the d and d database
	 */
	private String[] tablesComboBoxArray;
	/**
	 * the name of the current table being displayed
	 */
	private String currentSelectedTable;
	private ArrayList<JTextField> insertFields;
	private JButton insertButton;

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
		insertFields = new ArrayList<JTextField>();
		insertButton = new JButton("Insert");

		setupPanel();
		setupLayout();
		setupListeners();
	}

	/**
	 * adds components to the panel and sets the properties of them
	 */
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
		this.add(insertButton);
		insertButton.setVisible(false);
	}

	/**
	 * sets the components location on the panel
	 */
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
		layout.putConstraint(SpringLayout.NORTH, insertButton, 5, SpringLayout.SOUTH, editTablePane);
		layout.putConstraint(layout.HORIZONTAL_CENTER, insertButton, 0, layout.HORIZONTAL_CENTER, this);
	}

	/**
	 * adds the listeners to the components that require one
	 */
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

				if (e.getStateChange() == ItemEvent.SELECTED && !(tablesComboBox.getSelectedItem().toString().equals("...")))
				{
					buildInsertTextFields(0);
					insertButton.setVisible(true);
					currentSelectedTable = tablesComboBox.getSelectedItem().toString();
					String query = baseController.getDBController().buildSELECTQuery(currentSelectedTable);
					String[][] newData = baseController.getDBController().runSELECTQueryGetTable(query);
					String[] columnHeaders = baseController.getDBController().runSELECTQueryGetColumnNames(query);
					editTableModel.setDataVector(newData, columnHeaders);
					editTable.setModel(editTableModel);

					myRender.buildLargestHeight(newData.length);

					for (int col = 0; col < columnHeaders.length; col++)
					{
						if (baseController.getDBController().checkIfComboBoxForEdit(col))
						{
							String[] newComboBoxData = baseController.getDBController().getComboBoxForEdit(col);
							JComboBoxTableRender myJComboBoxTableRender = new JComboBoxTableRender(newComboBoxData);
							editTable.getColumnModel().getColumn(col).setCellRenderer(myJComboBoxTableRender);
							JComboBoxTableEditor myJComboBoxTableEditor = new JComboBoxTableEditor(newComboBoxData);
							editTable.getColumnModel().getColumn(col).setCellEditor(myJComboBoxTableEditor);
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
					boolean isJCombobox = false;
					try
					{
						if (editTable.getColumnModel().getColumn(change.getColumn()).getCellEditor() instanceof JComboBoxTableEditor)
						{
							isJCombobox = true;
						}
					}
					catch (Exception currentException)
					{

					}

					if (isJCombobox)
					{
						try
						{
							int row = change.getFirstRow();
							int column = change.getColumn();
							Object newData = editTableModel.getValueAt(row, column);
							String id = baseController.getDBController().findIdInRelatedTable(newData.toString(), column);

							baseController.getDBController().runUPDATEQuery(id, column, row, currentSelectedTable);
						}
						catch (Exception currentException)
						{

						}
					}
					else
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
			}

		});

		insertButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent click)
			{
				buildInsertTextFields(editTable.getColumnCount());
			}
		});
	}

	/**
	 * finds the tables available in the d and d database and puts them into the
	 * tablesComboBoxArray. The first entry will always be the blank table "..."
	 */
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

	private void buildInsertTextFields(int numberOfColumns)
	{
		for (int location = 0; location < insertFields.size(); location++)
		{
			insertFields.get(location).setVisible(false);
			this.remove(insertFields.get(location));
		}
		insertFields.clear();
		this.revalidate();

		for (int col = 0; col < numberOfColumns; col++)
		{
			JTextField currentField = new JTextField();
			int width = editTablePane.getWidth() / numberOfColumns;
			if (editTablePane.getVerticalScrollBar().isVisible())
			{
				System.out.println(editTablePane.getVerticalScrollBar().getWidth());
				width -= (editTablePane.getVerticalScrollBar().getWidth() / numberOfColumns);
			}
			Dimension currentFieldDimension = new Dimension(width, insertButton.getHeight());
			currentField.setPreferredSize(currentFieldDimension);
			layout.putConstraint(SpringLayout.NORTH, currentField, 20, SpringLayout.SOUTH, insertButton);
			if (col == 0)
			{
				layout.putConstraint(SpringLayout.WEST, currentField, 0, SpringLayout.WEST, editTablePane);
			}
			else
			{
				layout.putConstraint(SpringLayout.WEST, currentField, 0, SpringLayout.EAST, insertFields.get(col - 1));
			}
			this.add(currentField);
			currentField.setVisible(true);
			insertFields.add(currentField);
			this.revalidate();
		}

	}
}
