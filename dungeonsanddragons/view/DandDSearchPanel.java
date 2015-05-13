package dungeonsanddragons.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;

import dungeonsanddragons.controller.DandDAppController;

public class DandDSearchPanel extends JPanel
{
	/**
	 * the reference to the app controller
	 */
	private DandDAppController baseController;
	/**
	 * Table column renderer used with JTextAreas
	 * all data is rendered with this renderer on this page
	 */
	private TableRender myRender;
	/**
	 * the layout for DandDSearchPanel
	 */
	private SpringLayout layout;
	/**
	 * button that switches to the edit page
	 */
	private JButton toEditPanelButton;
	/**
	 * The image used at the top of each page
	 */
	private ImageIcon DandDLogo;
	/**
	 * contains the d and d logo, to be placed at the top of the panel
	 */
	private JLabel DandDLogoLabel;
	/**
	 * The label that contains the title of the page
	 * this is the search page
	 */
	private JLabel title;
	/**
	 * This is the table that contains the data retrieved from the database
	 */
	private JTable searchTable;
	/**
	 * Model used with the generated data table
	 */
	private DefaultTableModel searchTableModel;
	/**
	 * contains the data table
	 */
	private JScrollPane searchTablePane;
	/**
	 * Contains the table names of the tables available in the d and d database
	 * generates a table when a table is selected
	 */
	private JComboBox<String> tablesComboBox;
	/**
	 * Contains the table names of the tables available in the d and d database
	 */
	private String[] tablesComboBoxArray;

	public DandDSearchPanel(DandDAppController baseController)
	{
		this.baseController = baseController;
		myRender = new TableRender();
		buildTableComboBoxArray();
		layout = new SpringLayout();
		DandDLogo = new ImageIcon(DandDEditPanel.class.getResource("/dungeonsanddragons/view/images/dd_logo.png"));

		toEditPanelButton = new JButton("To Edit Database Page");
		DandDLogoLabel = new JLabel(DandDLogo);
		title = new JLabel("Search Database");
		searchTableModel = new DefaultTableModel()
		{
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		searchTable = new JTable(searchTableModel);
		searchTablePane = new JScrollPane(searchTable);
		Dimension searchTablePaneDimension = new Dimension(700, 400);
		searchTablePane.setPreferredSize(searchTablePaneDimension);
		tablesComboBox = new JComboBox(tablesComboBoxArray);

		setupPanel();
		setupLayout();
		setupListeners();
	}

	/**
	 * adds the components to the panel and sets their properties
	 */
	private void setupPanel()
	{
		this.setBackground(Color.BLACK);
		title.setForeground(Color.WHITE);
		searchTablePane.getViewport().setBackground(Color.BLACK);
		searchTablePane.setBorder(BorderFactory.createEmptyBorder());
		this.setSize(1195, 775);
		this.setFocusable(true);
		this.setLayout(layout);
		toEditPanelButton.setPreferredSize(new Dimension(200, 25));
		title.setFont(title.getFont().deriveFont(40.0f));

		this.add(toEditPanelButton);
		this.add(DandDLogoLabel);
		this.add(title);
		this.add(searchTablePane);
		this.add(tablesComboBox);
	}

	/**
	 * sets the location of the components
	 */
	private void setupLayout()
	{
		layout.putConstraint(SpringLayout.SOUTH, toEditPanelButton, -10, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, toEditPanelButton, -10, SpringLayout.EAST, this);
		layout.putConstraint(layout.HORIZONTAL_CENTER, title, 0, layout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.NORTH, title, 6, SpringLayout.SOUTH, DandDLogoLabel);
		layout.putConstraint(SpringLayout.NORTH, tablesComboBox, 6, SpringLayout.SOUTH, title);
		layout.putConstraint(layout.HORIZONTAL_CENTER, tablesComboBox, 0, layout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.NORTH, searchTablePane, 6, SpringLayout.SOUTH, tablesComboBox);
		layout.putConstraint(layout.HORIZONTAL_CENTER, searchTablePane, 0, layout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.NORTH, DandDLogoLabel, 15, SpringLayout.NORTH, this);
	}

	/**
	 * adds listeners to the components that require one
	 */
	private void setupListeners()
	{
		toEditPanelButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent click)
			{
				baseController.getFrame().ChangeToEditPanel();
			}

		});

		tablesComboBox.addItemListener(new ItemListener()
		{

			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED && !(tablesComboBox.getSelectedItem().toString().equals("...")))
				{
					String query = baseController.getDBController().buildSELECTQuery(tablesComboBox.getSelectedItem().toString());
					String[][] newData = baseController.getDBController().runSELECTQueryGetTable(query);
					String[] columnHeaders = baseController.getDBController().runSELECTQueryGetColumnNames(query);
					searchTableModel.setDataVector(newData, columnHeaders);
					searchTable.setModel(searchTableModel);
					searchTable.getTableHeader().setFont(new Font(null, Font.BOLD, 12));
					myRender.buildLargestHeight(newData.length);
					for (int col = 0; col < searchTable.getColumnCount(); col++)
					{
						searchTable.getColumnModel().getColumn(col).setCellRenderer(myRender);
					}
				}
			}

		});
	}

	/**
	 * fills the tablesComboBoxArray with the available d and d table names from the database
	 * the first entry is always the blank table "..."
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
}
