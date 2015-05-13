package dungeonsanddragons.view;

import javax.swing.JFrame;

import dungeonsanddragons.controller.DandDAppController;

public class DandDFrame extends JFrame
{
	/**
	 * search page panel
	 */
	private DandDSearchPanel searchPanel;
	/**
	 * edit page panel
	 */
	private DandDEditPanel editPanel;
	/**
	 * start screen/loading screen
	 */
	private DandDStartPanel startPanel;

	
	public DandDFrame(DandDAppController baseController)
	{
		startPanel = new DandDStartPanel(baseController);
		searchPanel = new DandDSearchPanel(baseController);
		editPanel = new DandDEditPanel(baseController);

		setupFrame();
	}

	/**
	 * sets up the properties of this frame
	 */
	private void setupFrame()
	{
		this.setContentPane(startPanel);
		this.setSize(1200, 800);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public DandDSearchPanel getSearchPanel()
	{
		return searchPanel;
	}
	
	public DandDEditPanel getEditPanel()
	{
		return editPanel;
	}
	
	/**
	 * switches to the search page
	 */
	public void ChangeToSearchPanel()
	{
		this.setContentPane(searchPanel);
	}
	
	/**
	 * switches to the edit page
	 */
	public void ChangeToEditPanel()
	{
		this.setContentPane(editPanel);
	}
}
