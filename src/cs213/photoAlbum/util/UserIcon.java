package cs213.photoAlbum.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;

public class UserIcon extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Dimension size = new Dimension(60, 60);
	private Color color = new Color(108, 153, 231);
	private Color fontColor = new Color(255, 255, 255);
	Font font = new Font("Ariel", Font.PLAIN, 40);
	private char initial;

	int x = 0, y = 0;

	/**
	 * Constuctor to build a UserIcon object
	 * @param initial to display on the icon
	 */
	public UserIcon(char initial) {
		// super();
		this.setPreferredSize(this.size);
		this.initial = initial;

	}


	@Override
	protected void paintComponent(Graphics g) {

		Graphics2D antiAlias = (Graphics2D) g;
		antiAlias.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		antiAlias.setColor(color);
		antiAlias.fillRect(0, 0, 100, 100);
		antiAlias.setColor(fontColor);

		antiAlias.setFont(font);

		FontMetrics metrics = antiAlias.getFontMetrics(font);

		int stringHeight = metrics.getHeight();

		int stringWidth = metrics.stringWidth(this.initial + "");

		antiAlias.drawString(this.initial + "", this.getWidth() / 2
				- stringWidth / 2, this.getHeight() / 2 + stringHeight / 2 - 7);

		super.paintComponent(g);
	}

	/**
	 * Returns the width of the UserIcon
	 * @return Int containing the width of the User Icon
	 */
	public int getWidth() {

		return getPreferredSize().width;
	}

	/**
	 * Returns the Height of the UserIcon
	 * @return Int containing the height of the User Icon
	 */
	public int getHeight() {

		return getPreferredSize().height;
	}

	/**Get the preferred size of the component
	 * @return Dimension object with preferred width and height
	 */
	public Dimension getPreferredSize() {
		return this.size;
	}

	/**
	 * Get the minimum displayable size
	 * @return Dimension object with minimum width and height
	 */
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	/**
	 * Get the maximum displayable size
	 * @return Dimension object with maximum width and height
	 */
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	
	/**
	 * Get the initial displayed on the Icon
	 * @return char initial
	 */
	public char getInitial() {
		return initial;
	}

	/**
	 * Set the initial displayed on the Icon
	 * @param char initial to display
	 */
	public void setInitial(char initial) {
		this.initial = initial;
	}
}
