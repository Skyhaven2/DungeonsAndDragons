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
	private DandDAppController baseController;
	private TableRender myRender;
	private SpringLayout layout;
	private JButton toEditPanelButton;
	private ImageIcon DandDLogo;
	private JLabel DandDLogoLabel;
	private JLabel title;
	private JTable searchTable;
	private DefaultTableModel searchTableModel;
	private JScrollPane searchTablePane;
	private JComboBox tablesComboBox;
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
					String[][] newData = baseController.getDBController().runSELECTQueryTableGetTable(query);
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
