package cs213.photoAlbum.util;

import javax.swing.JComponent;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MenuButton extends JComponent implements MouseListener {
	private static final long serialVersionUID = 1L;

	private static Dimension size = new Dimension(150, 100);

	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();

	private int val;
	private int pos;

	private boolean mouseEntered = false;
	private boolean mousePressed = false;

	// ############ Customization Stuff ##############
	private Color lightGray = new Color(230, 230, 230);
	private Color darkGray = new Color(204, 204, 204);
	private Color lineColor = new Color(210, 210, 210);

	private Color selectedTextColor = new Color(130, 130, 130);
	private Color unSelectedTextColor = new Color(255, 255, 255);
	private Color highlightColor = new Color(225, 225, 225);

	private String label;

	private Font font = new Font("Ariel", Font.PLAIN, 25);

	private boolean isSelected = false;
	private boolean isHovered = false;

	public MenuButton(String label) {
		this(null, label, size.width, size.height);
	}

	public MenuButton(String label, int width, int height) {
		this(null, label, width, height);
	}

	/**
	 * Create a menu button
	 * 
	 * @param e
	 *            , actionlister used to capture events
	 * @param label
	 *            , String that the button should display
	 * @param width
	 *            , int width of the button
	 * @param height
	 *            , int height of the button
	 */
	public MenuButton(ActionListener e, String label, int width, int height) {
		super();

		size.setSize(width, height);
		this.label = label;

		enableInputMethods(true);
		addMouseListener(this);

		setVisible(true);
		setSize(size.width, size.height);
		setFocusable(true);

	}

	/**
	 * Draws the button on a canvas
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// System.out.println("hey");
		Graphics2D antiAlias = (Graphics2D) g;
		antiAlias.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		if (isSelected) {
			Point2D start = new Point2D.Float(0, 0);
			Point2D end = new Point2D.Float(0, (float) size.getHeight());
			float[] dist = { 0.0f, 0.99f, 1.0f };
			Color[] colors = { darkGray, lightGray, lineColor };
			LinearGradientPaint p = new LinearGradientPaint(start, end, dist,
					colors);
			antiAlias.setPaint(p);

			// g.setColor(Color.RED);

			antiAlias.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setColor(selectedTextColor);

		} else {
			Point2D start = new Point2D.Float(0, 0);
			Point2D end = new Point2D.Float(0, (float) size.getHeight());
			float[] dist = { 0.0f, 0.99f, 1.0f };
			// Color[] colors;

			if (!mouseEntered) {
				Color[] colors = { lightGray, lightGray, lineColor };

				LinearGradientPaint p = new LinearGradientPaint(start, end,
						dist, colors);
				antiAlias.setPaint(p);

				// g.setColor(Color.RED);
				antiAlias.fillRect(0, 0, this.getWidth(), this.getHeight());
			} else {
				Color[] colors = { highlightColor, highlightColor, lineColor };

				LinearGradientPaint p = new LinearGradientPaint(start, end,
						dist, colors);
				antiAlias.setPaint(p);

				// g.setColor(Color.RED);
				antiAlias.fillRect(0, 0, this.getWidth(), this.getHeight());

			}
			g.setColor(unSelectedTextColor);
		}

		antiAlias.setFont(font);

		FontMetrics metrics = antiAlias.getFontMetrics(font);

		// int stringHeight = metrics.getHeight();

		int stringWidth = metrics.stringWidth(this.label);

		antiAlias.drawString(this.label, this.getWidth() / 2 - stringWidth / 2,
				this.getHeight() / 2 + 5);

	}

	/**
	 * Get the color of the base line
	 * 
	 * @return Color object of the baseline
	 */
	public Color getLineColor() {
		return lineColor;
	}

	/**
	 * Set the color of the base line
	 * 
	 * @param Color
	 *            object of the baseline
	 */
	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	/**
	 * Get the highlight color of the base line
	 * 
	 * @return Color object containing the highlight color of the button
	 */
	public Color getHighlightColor() {
		return highlightColor;
	}

	/**
	 * Set the highlight color of the base line
	 * 
	 * @param Color
	 *            object containing the highlight color of the button
	 */
	public void setHighlightColor(Color highlightColor) {
		this.highlightColor = highlightColor;
	}

	/**
	 * Capture mouse clicked events on the PhotoViewer
	 * 
	 * @param MouseEvent
	 *            e containing the mouse clicked event variables
	 */
	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * Capture mouse entered events on the PhotoViewer
	 * 
	 * @param MouseEvent
	 *            e containing the mouse entered event variables
	 */
	public void mouseEntered(MouseEvent e) {
		mouseEntered = true;
		if (val != -1) {
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		repaint();
	}

	/**
	 * Capture mouse dragged exited on the PhotoViewer
	 * 
	 * @param MouseEvent
	 *            e containing the mouse dragged exited variables
	 */
	public void mouseExited(MouseEvent e) {
		mouseEntered = false;
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		repaint();
	}

	/**
	 * Capture mouse pressed events on the PhotoViewer
	 * 
	 * @param MouseEvent
	 *            e containing the mouse pressed event variables
	 */
	public void mousePressed(MouseEvent e) {
		notifyListeners(e);
		// System.out.println("Clicked");
		mousePressed = true;
		repaint();
	}

	/**
	 * Capture mouse dragged released on the PhotoViewer
	 * 
	 * @param MouseEvent
	 *            e containing the mouse released event variables
	 */
	public void mouseReleased(MouseEvent e) {
		mousePressed = false;
		if (val != 0) {

			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		repaint();
	}

	/**
	 * Add a listener to this object
	 * 
	 * @param listener
	 *            to add
	 */
	public void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}

	/**
	 * Send actions to all listeners of this object
	 * 
	 * @param e
	 *            , MouseEvent to send to listeners
	 */
	private void notifyListeners(MouseEvent e) {
		ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
				new String(), e.getWhen(), e.getModifiers());
		synchronized (listeners) {
			for (int i = 0; i < listeners.size(); i++) {
				ActionListener tmp = listeners.get(i);
				tmp.actionPerformed(evt);
			}
		}
	}

	/**
	 * Get the preferred size of the PhotoViewer
	 * 
	 * @return Dimension object containing width and height
	 */
	public Dimension getPreferredSize() {
		return size;
	}

	/**
	 * Get the minimum size of the PhotoViewer
	 * 
	 * @return Dimension object containing minimum width and height
	 */
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	/**
	 * Get the maximum size of the PhotoViewer
	 * 
	 * @return Dimension object containing maximum width and height
	 */
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	/**
	 * Check whether the button is currently selected
	 * 
	 * @return true if currently selected, false otherwise
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * Select this button
	 * 
	 * @param isSelected
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	/**
	 * Get the font of the button text
	 * 
	 * @return font object of the button text's font
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * Set the font of the button text
	 * 
	 * @param font
	 *            object of the button text's font
	 */
	public void setFont(Font font) {
		this.font = font;
	}

	/**
	 * Get the text of the button
	 * 
	 * @return String of the text on the button
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set the text of the button
	 * 
	 * @param String
	 *            of the text to put on the button
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Check whether the button is being hovers
	 * 
	 * @return true if the cursor is over the button, false otherwise
	 */
	public boolean isHovered() {
		return isHovered;
	}

	public void setHovered(boolean isHovered) {
		this.isHovered = isHovered;
	}
}