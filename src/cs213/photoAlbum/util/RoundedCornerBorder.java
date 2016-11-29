package cs213.photoAlbum.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.border.AbstractBorder;

public class RoundedCornerBorder extends AbstractBorder {
	/**
	 * 
	 */
	Color borderColor = Color.LIGHT_GRAY;

	private static final long serialVersionUID = 1L;

	/**
	 * Create a Rounded Corner Border object
	 * 
	 * @param borderColor
	 *            , color of the border
	 */
	public RoundedCornerBorder(Color borderColor) {
		super();
		this.borderColor = borderColor;

	}

	/**
	 * Paints the border on the component using the parameters passed in
	 * 
	 */
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int r = height - 20;
		RoundRectangle2D round = new RoundRectangle2D.Float(x, y, width - 1,
				height - 1, r, r);
		Container parent = c.getParent();
		if (parent != null) {
			g2.setColor(parent.getBackground());
			Area corner = new Area(new Rectangle2D.Float(x, y, width, height));
			corner.subtract(new Area(round));
			g2.fill(corner);
		}
		g2.setColor(borderColor);
		g2.draw(round);
		g2.dispose();
	}

	/**
	 * Get the insets of the border
	 * 
	 * @param componenet
	 *            to get the borders from
	 * @return Insets objects on the components
	 */
	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(4, 8, 4, 8);
	}

	/**
	 * Get the insets of the border
	 * 
	 * @param componenet
	 *            to get the borders from
	 * @return Insets objects on the components
	 */
	@Override
	public Insets getBorderInsets(Component c, Insets insets) {
		insets.set(4, 8, 4, 8);
		return insets;
	}

	/**
	 * Get the color of the border
	 * 
	 * @return Color object of the border of the component
	 */
	public Color getBorderColor() {
		return borderColor;
	}

	/**
	 * Set the color of the border
	 */
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}
}