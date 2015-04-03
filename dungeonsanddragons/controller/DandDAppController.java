package dungeonsanddragons.controller;

import dungeonsanddragons.view.DandDFrame;

public class DandDAppController
{
	private DandDFrame appFrame;
	private DandDDatabaseController database;
	
	public DandDAppController()
	{
		database = new DandDDatabaseController(this);
		appFrame = new DandDFrame(this);
		try
		{
			Thread.sleep(2000);
		}
		catch (InterruptedException exception)
		{
			exception.printStackTrace();
		}
		appFrame.ChangeToSearchPanel();
	}
	
	public void start()
	{
		
	}
	
	public DandDFrame getFrame()
	{
		return appFrame;
	}
	
	public DandDDatabaseController getDBController()
	{
		return database;
	}
}
