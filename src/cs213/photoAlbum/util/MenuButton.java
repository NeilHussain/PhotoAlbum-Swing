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

	public Color getLineColor() {
		return lineColor;
	}

	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	public Color getHighlightColor() {
		return highlightColor;
	}

	public void setHighlightColor(Color highlightColor) {
		this.highlightColor = highlightColor;
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		mouseEntered = true;
		if (val != -1) {
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		repaint();
	}

	public void mouseExited(MouseEvent e) {
		mouseEntered = false;
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		repaint();
	}

	public void mousePressed(MouseEvent e) {
		notifyListeners(e);
		// System.out.println("Clicked");
		mousePressed = true;
		repaint();
	}

	public void mouseReleased(MouseEvent e) {
		mousePressed = false;
		if (val != 0) {

			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		repaint();
	}

	public void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}

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

	public Dimension getPreferredSize() {
		return size;
	}

	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isHovered() {
		return isHovered;
	}

	public void setHovered(boolean isHovered) {
		this.isHovered = isHovered;
	}
}