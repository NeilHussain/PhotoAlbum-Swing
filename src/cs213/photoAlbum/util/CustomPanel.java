package cs213.photoAlbum.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

public class CustomPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Color lightGray = new Color(237, 237, 237);
	// Color lightGray = new Color(255, 0, 0);
	private Color darkGray = new Color(204, 204, 204);
	private Color lineColor = new Color(210, 210, 210);
	private float opacity = 1.0f;
	private boolean reversed = false;

	private LinearGradientPaint p;

	/**
	 * Paints the panel
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		drawBackground(g2);
	}

	/**
	 * Draw a gradient on the background of the panel
	 * 
	 * @param g2d
	 *            to draw to
	 */
	public void drawBackground(Graphics2D g2d) {

		if (p == null) {
			if (!reversed) {
				Point2D start = new Point2D.Float(0, 0);
				Point2D end = new Point2D.Float(0, (float) this.getHeight());
				float[] dist = { 0.0f, 0.99f, 1.0f };
				Color[] colors = { lightGray, lightGray, lineColor };
				p = new LinearGradientPaint(start, end, dist, colors);
			} else {

				Point2D start = new Point2D.Float(0, 0);
				Point2D end = new Point2D.Float(0, (float) this.getHeight());
				float[] dist = { 0.00f, 0.01f, 1.0f };
				Color[] colors = { lineColor, lightGray, lightGray };
				p = new LinearGradientPaint(start, end, dist, colors);

			}
		}
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				this.opacity));
		g2d.setPaint(p);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				1.0f));

	}

	/**
	 * Set the gradient of the background
	 * 
	 * @param LinearGradientPaint
	 *            p
	 */
	public void setGradientPain(LinearGradientPaint p) {
		this.p = p;
	}

	/**
	 * Get the opacity of the panel
	 * 
	 * @return float opacity of the panel
	 */
	public float getOpacity() {
		return opacity;
	}

	/**
	 * Set the opacity of the panel
	 * 
	 * @param float opacity of the panel
	 */
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}

	/**
	 * Gradient is reversed?
	 * @return true if reversed false otherwise
	 */
	public boolean isReversed() {
		return reversed;
	}

	/**
	 * Reverse the gradient
	 * @param true if should be reversed false otherwise
	 */
	public void setReversed(boolean reversed) {
		this.reversed = reversed;
	}
}
