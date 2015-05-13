package dungeonsanddragons.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import dungeonsanddragons.controller.DandDAppController;

public class DandDStartPanel extends JPanel
{
	/**
	 * reference to the app controller
	 */
	private DandDAppController baseController;
	/**
	 * the layout used for the DandDStartPanel
	 */
	private SpringLayout layout;
	/**
	 * The image used for the background
	 */
	private ImageIcon backgroundImage = new ImageIcon(DandDSearchPanel.class.getResource("/dungeonsanddragons/view/images/dd_wallpaper.jpg"));
	/**
	 * The label that contains the background image
	 */
	private JLabel background;

	public DandDStartPanel(DandDAppController baseController)
	{
		this.baseController = baseController;
		layout = new SpringLayout();
		background = new JLabel(backgroundImage);

		setupPanel();
	}

	/**
	 * adds components to the panel and sets their properties
	 */
	private void setupPanel()
	{
		this.setSize(1195, 775);
		this.setFocusable(true);
		this.setLayout(layout);
		this.add(background);
	}
}
