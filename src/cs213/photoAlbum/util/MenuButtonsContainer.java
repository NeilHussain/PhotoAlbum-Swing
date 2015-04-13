package cs213.photoAlbum.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

public class MenuButtonsContainer extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5351857450375510424L;

	// which tab is selected
	private int tabbed = 0;
	private MenuButton buttons[];
	private Dimension size = new Dimension(150, 100);

	private int buttonWidth = 150, buttonHeight = 100;
	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();

	public MenuButtonsContainer(String[] labels, int buttonWidth,
			int buttonHeight, Color color, int initSelected) {
		this(null, labels, buttonWidth, buttonHeight, color, initSelected);
	}

	MenuButtonsContainer(ActionListener e, String[] labels, int buttonWidth,
			int buttonHeight, Color color, int initSelected) {

		size = new Dimension(labels.length * buttonWidth, buttonHeight);

		this.buttonHeight = buttonHeight;
		this.buttonWidth = buttonWidth;

		this.buttons = new MenuButton[labels.length];
		for (int i = 0; i < labels.length; i++) {
			this.buttons[i] = new MenuButton(labels[i], this.buttonWidth,
					this.buttonHeight);
			this.buttons[i].setLocation(buttonWidth * i, 0);

			this.buttons[i].setToolTipText(labels[i]);

			// Don't Change
			this.add(buttons[i]);
		}

		buttons[0].setSelected(true);

		for (int i = 0; i < labels.length; i++) {
			buttons[i].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent evt) {
					MenuButton sender = (MenuButton) evt.getSource();

					for (int i = 0; i < labels.length; i++) {
						buttons[i].setSelected(false);
						buttons[i].repaint();
						if (sender.equals(buttons[i])) {

							buttons[i].setSelected(true);
							setTabbed(i);

							notifyListeners(evt);
							// send action back to the main class.

						}
					}

				}
			});
		}

		this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

	}

	public Dimension getPreferredSize() {
		return size;
	}

	public void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}

	private void notifyListeners(ActionEvent evt) {
		// ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
		// new String(), e.getWhen(), e.getModifiers());
		synchronized (listeners) {
			for (int i = 0; i < listeners.size(); i++) {
				ActionListener tmp = listeners.get(i);
				tmp.actionPerformed(evt);
			}
		}
	}

	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	public int whichTabbed() {
		return tabbed;
	}

	public void setTabbed(int tabbed) {
		this.tabbed = tabbed;
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setSelected(false);
			if (i == tabbed) {
				buttons[i].setSelected(true);
			}

		}

	}

}