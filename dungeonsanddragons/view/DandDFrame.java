package dungeonsanddragons.view;

import javax.swing.JFrame;

import dungeonsanddragons.controller.DandDAppController;

public class DandDFrame extends JFrame
{
	private DandDSearchPanel searchPanel;
	private DandDEditPanel editPanel;
	private DandDStartPanel startPanel;

	
	public DandDFrame(DandDAppController baseController)
	{
		startPanel = new DandDStartPanel(baseController);
		searchPanel = new DandDSearchPanel(baseController);
		editPanel = new DandDEditPanel(baseController);

		setupFrame();
	}

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
	
	public void ChangeToSearchPanel()
	{
		this.setContentPane(searchPanel);
	}
	
	public void ChangeToEditPanel()
	{
		this.setContentPane(editPanel);
	}
}
