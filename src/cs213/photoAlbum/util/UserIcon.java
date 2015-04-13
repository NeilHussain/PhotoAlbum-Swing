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

	public int getWidth() {

		return getPreferredSize().width;
	}

	public int getHeight() {

		return getPreferredSize().height;
	}

	public Dimension getPreferredSize() {
		return this.size;
	}

	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	

	public char getInitial() {
		return initial;
	}

	public void setInitial(char initial) {
		this.initial = initial;
	}
}
