package cs213.photoAlbum.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class HintTextField extends JTextField implements FocusListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String hint;
	private boolean showingHint;

	/**
	 * Create a textfield with automatic hints
	 * 
	 * @param String
	 *            hint to display when not focused and text empty
	 */
	public HintTextField(final String hint) {
		super(hint);
		this.hint = hint;
		this.showingHint = true;
		this.setForeground(Color.GRAY);
		this.setHorizontalAlignment(CENTER);
		this.setBorder(new RoundedCornerBorder(Color.LIGHT_GRAY));
		super.addFocusListener(this);
	}

	/**
	 * Perform a few things with the box gains focus
	 * 
	 * @param focus
	 *            event contain variables pertaining the focus of the box
	 */
	@Override
	public void focusGained(FocusEvent e) {
		if (this.getText().isEmpty()) {
			super.setText("");
			this.setForeground(Color.BLACK);
			this.setHorizontalAlignment(LEFT);
			this.setBorder(new RoundedCornerBorder(new Color(108, 153, 231)));
			showingHint = false;
		}
	}

	/**
	 * Perform a few things with the box loses focus
	 * 
	 * @param focus
	 *            event contain variables pertaining the focus of the box
	 */
	@Override
	public void focusLost(FocusEvent e) {
		if (this.getText().isEmpty()) {
			super.setText(hint);
			this.setForeground(Color.GRAY);
			this.setHorizontalAlignment(CENTER);
			this.setBorder(new RoundedCornerBorder(Color.LIGHT_GRAY));
			showingHint = true;
		}
	}

	/**
	 * Get the min size of the PhotoViewer
	 * 
	 * @return Dimension object containing width and height
	 */
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	/**
	 * Return the text of the box
	 * 
	 * @return String of textbox's text
	 */
	@Override
	public String getText() {
		return showingHint ? "" : super.getText();
	}
}