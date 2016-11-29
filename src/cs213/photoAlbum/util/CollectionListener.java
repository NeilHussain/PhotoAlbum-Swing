package cs213.photoAlbum.util;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.Observable;

public class CollectionListener extends Observable implements MouseListener,
		MouseMotionListener, MouseWheelListener {

	CollectionView collection;
	Thumbnail selected;
	int selectedIndex = 0;

	/**
	 * Make a new collection listener
	 * 
	 * @param collection
	 *            to listen to
	 */
	public CollectionListener(CollectionView collection) {
		this.collection = collection;
	}

	/**
	 * Capture mouse pressed events on the PhotoViewer
	 * 
	 * @param MouseEvent
	 *            e containing the mouse pressed event variables
	 */
	public void mousePressed(MouseEvent e) {
		// capture starting point
		int mouseX = e.getX();
		int mouseY = (int) (e.getY() + collection.getScrollValue());

		if (collection.getThumbnails() != null) {
			int x = 0;
			for (Thumbnail thumb : collection.getThumbnails()) {
				if (thumb.getY() - collection.getScrollValue() > -500
						&& thumb.getY() - collection.getScrollValue() < collection
								.getHeight() + 500) {
					if (new Rectangle(mouseX, mouseY, 1, 1).intersects(thumb
							.getBounds()) && thumb.isDummy() == false) {

						collection.setSelectionRect(new Rectangle(thumb.getX(),
								(int) (thumb.getY()), collection
										.getThumbnailWidth(), collection
										.getThumbnailHeight()));

						selected = thumb;
						selectedIndex = x;
						collection.repaint();
						break;
					}

				}
				x++;
			}
		}

	}

	/**
	 * Capture mouse dragged events on the PhotoViewer
	 * 
	 * @param MouseEvent
	 *            e containing the mouse dragged event variables
	 */
	public void mouseDragged(MouseEvent e) {

		int mouseX = e.getX();
		int mouseY = (int) (e.getY() + collection.getScrollValue());

		// System.out.println(mouseX + ", " + mouseY);

		// Seems a bit inefficient (Could just calcualte where it would have
		// hit)
		if (collection.getThumbnails() != null) {
			int x = 0;
			for (Thumbnail thumb : collection.getThumbnails()) {

				if (thumb.getY() - collection.getScrollValue() > -500
						&& thumb.getY() - collection.getScrollValue() < collection
								.getHeight() + 500) {
					if (new Rectangle(mouseX, mouseY, 1, 1).intersects(thumb
							.getBounds()) && thumb.isDummy() == false) {

						// collection.selectionRect.setRect(thumb.getX(),
						// thumb.getY(),
						// collection.thumbnailWidth,
						// collection.thumbnailHeight);

						// collection.selectionRect.setLocation(thumb.getX(),
						// (int)(thumb.getY() +collection.scrollValue));
						// collection.selectionRect.setSize(collection.thumbnailWidth,
						// collection.thumbnailHeight);

						collection.setSelectionRect(new Rectangle(thumb.getX(),
								(int) (thumb.getY()), collection
										.getThumbnailWidth(), collection
										.getThumbnailHeight()));
						selected = thumb;
						selectedIndex = x;
						collection.repaint();
						break;
					}

				}
				x++;
			}
		}
	}

	/**
	 * Capture mouse clicked events on the PhotoViewer
	 * 
	 * @param MouseEvent
	 *            e containing the mouse clicked event variables
	 */
	public void mouseClicked(MouseEvent e) {
		// System.out.println(this.countObservers());

		// Should I do more stuff to see if I actually hit something?

		if (e.getClickCount() == 2) {
			// collection.selected = selected;
			// collection.selection = selectedIndex;
			if (collection.getThumbnails() != null) {
				for (Thumbnail thumb : collection.getThumbnails()) {

					if (thumb.getY() - collection.getScrollValue() > -500
							&& thumb.getY() - collection.getScrollValue() < collection
									.getHeight() + 500) {
						if (new Rectangle(e.getX(), e.getY(), 1, 1)
								.intersects(thumb.getBounds())
								&& thumb.isDummy() == false) {

							setChanged();
							notifyObservers(true);
						}
					}
				}
			}
		}

	}

	/**
	 * Capture mouse entered events on the PhotoViewer
	 * 
	 * @param MouseEvent
	 *            e containing the mouse entered event variables
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * Capture mouse exited events on the PhotoViewer
	 * 
	 * @param MouseEvent
	 *            e containing the mouse exited event variables
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Capture mouse moved events on the PhotoViewer
	 * 
	 * @param MouseEvent
	 *            e containing the mouse moved event variables
	 */
	public void mouseMoved(MouseEvent e) {
	}

	/**
	 * Capture mouse released events on the PhotoViewer
	 * 
	 * @param MouseEvent
	 *            e containing the mouse released event variables
	 */
	public void mouseReleased(MouseEvent e) {
		// collection.selectionRect = null;
		// System.out.println("Huh");
		// collection.repaint();
		if (selectedIndex != collection.getSelection()) {

			collection.setSelected(selected);
			collection.setSelection(selectedIndex);
			setChanged();

			// System.out.println(collection.getSelection());
			notifyObservers(false);

		}
	}

	/**
	 * Capture mouse wheel events on the PhotoViewer
	 * 
	 * @param MouseEvent
	 *            e containing the mouse wheel event variables
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {

			// make it a reasonable amount of zoom
			// .1 gives a nice slow transition
			// canvas.scale += (.1 * e.getWheelRotation());
			// don't cross negative threshold.
			// also, setting scale to 0 has bad effects

			collection.setScrollValue(Math.max(0.0,
					(Math.min((collection.getLineHeight() - collection
							.getHeight()), collection.getScrollValue()
							+ (1.3 * e.getWheelRotation())))));

			// collection.scrollValue = collection.scrollValue
			// + (1.3 * e.getWheelRotation());

			collection.repaint();
		}
	}
}

/*
 * class ScaleHandler implements MouseWheelListener { CollectionView collection;
 * 
 * @Override public void mouseWheelMoved(MouseWheelEvent e) { // TODO
 * Auto-generated method stub if (e.getScrollType() ==
 * MouseWheelEvent.WHEEL_UNIT_SCROLL) {
 * 
 * // make it a reasonable amount of zoom // .1 gives a nice slow transition //
 * canvas.scale += (.1 * e.getWheelRotation()); // don't cross negative
 * threshold. // also, setting scale to 0 has bad effects
 * 
 * collection.scrollValue = Math.max(0.0, (Math.min( (collection.lineHeight -
 * collection.height), collection.scrollValue + (1.3 * e.getWheelRotation()))));
 * 
 * // collection.scrollValue = collection.scrollValue // + (1.3 *
 * e.getWheelRotation());
 * 
 * collection.repaint(); } } }
 */

class Thumbnail {

	int x, y, width, height;
	BufferedImage image;
	boolean dummy = false;

	/**
	 * Create a thumbnail container
	 * 
	 * @param image
	 *            of the thumbnail
	 * @param x
	 *            position of the thumbnail
	 * @param y
	 *            position of the thumbnail
	 * @param width
	 *            of the thumbnail
	 * @param height
	 *            of the thumbnail
	 */
	Thumbnail(BufferedImage image, int x, int y, int width, int height) {

		this.image = image;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

	}

	Thumbnail(boolean dummy, int x, int y, int width, int height) {

		this.dummy = dummy;
		this.image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

	}

	/**
	 * Get the thumbnail image
	 * 
	 * @return BufferedImage that the thumbnail displays
	 */
	public BufferedImage getImage() {
		return this.image;
	}

	/**
	 * Flag to disregard in selection checks
	 * 
	 * @return true to disregard, false otherwise
	 */
	public boolean isDummy() {
		return this.dummy;
	}

	/**
	 * Disregard this thumbnail
	 * 
	 * @param boolean
	 */
	public void setDummy(Boolean dummy) {
		this.dummy = dummy;

	}

	/**
	 * Get the x position of the thumbnail
	 * 
	 * @return int of the x position
	 */
	public int getX() {
		return x;
	}

	/**
	 * Set the x position of the thumbnail
	 * 
	 * @param position
	 *            to set the thumbnail to
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Get the Y position of the thumbnail
	 * 
	 * @return int of the y position of the thumbnail
	 */
	public int getY() {
		return y;
	}

	/**
	 * Set the y position of the thumbnail
	 * 
	 * @param y
	 *            to set the thumbnail's position to
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Set the bounds of the thumbnail
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public void setBounds(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;

	}

	/**
	 * Get the bounds of the thumbnail
	 * 
	 * @return bounding rectangle
	 */
	public Rectangle getBounds() {

		return new Rectangle(x, y, width, height);

	}

}