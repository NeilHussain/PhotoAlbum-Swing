package cs213.photoAlbum.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

import javax.swing.JComponent;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class CollectionView extends JComponent implements KeyListener {

	private int selection = -1, width, height;

	private int perLine = 5;
	private int xMargin = 5, yMargin = 5;
	private int spacing = 2;
	private int initThumbnailWidth = 0, initThumbnailHeight = 0;
	private int thumbnailWidth = 0, thumbnailHeight = 0;

	int[] years = null;

	private int lineWidth, lineHeight;

	private double scrollValue = 0;
	private float scale = 1.0f;

	private Rectangle selectionRect;
	private Thumbnail[] thumbnails;
	private Thumbnail selected;

	private String tag;
	private Font defaultFont = new Font("Ariel", Font.BOLD, 150);
	private Color tagColor = new Color(245, 245, 245);

	int[] yearYValues;
	int[] yearNums;
	int[] yearsCondensed;

	BufferedImage[] thumbs;

	int numThumbs;

	// Color selectionColor = new Color(46, 146, 253);
	Color selectionColor = Color.black;

	public CollectionView(BufferedImage[] thumbs, int width, int height) {

		// Supplemental constructor call pass in width and height of the
		// thumbnails

		this(thumbs, thumbs[0].getWidth(), thumbs[0].getHeight(), width,
				height, null);

	}

	public CollectionView(BufferedImage[] thumbs, int thumbWidth,
			int thumbHeight, int width, int height) {
		this(thumbs, thumbWidth, thumbHeight, width, height, null);
	}

	/**
	 * Make a collection view
	 * 
	 * @param thumbs
	 *            , BufferedImage[] of the thumbnails to display
	 * @param thumbWidth
	 *            , width that the thumbnails should have
	 * @param thumbHeight
	 *            , height that the thumbanils should have
	 * @param width
	 *            of the full collection view
	 * @param height
	 *            of the displayable area of the collection view
	 * @param years
	 *            , optional array to pass in the years for the years collection
	 */
	public CollectionView(BufferedImage[] thumbs, int thumbWidth,
			int thumbHeight, int width, int height, int[] years) {
		addKeyListener(this);
		this.thumbs = thumbs;
		this.years = years;

		if (years != null) {

			ArrayList<Integer> tempYears = new ArrayList<Integer>();

			tempYears.add(years[0]);

			for (int i = 0; i < years.length; i++) {

				if (years[i] != tempYears.get(tempYears.size() - 1)) {

					tempYears.add(years[i]);

				}

			}

			yearsCondensed = convertIntegers(tempYears);
			yearYValues = new int[years.length];
		}

		// do necessary stuff to make thumbnails for the photos array
		this.thumbnailWidth = thumbWidth;
		this.thumbnailHeight = thumbHeight;
		this.initThumbnailWidth = thumbWidth;
		this.initThumbnailHeight = thumbHeight;

		this.numThumbs = thumbs.length;

		if (numThumbs > 0) {

			this.width = width;
			this.height = height;
			setOpaque(true);
			setDoubleBuffered(true);
			perLine = (int) Math.max(1,
					((width - xMargin) / (thumbnailWidth + spacing)));
			lineWidth = perLine * (thumbnailWidth + spacing);

			if (lineWidth < width) {
				xMargin = yMargin;
			} else {
				xMargin = (width - lineWidth) / 2;
			}

			if (years != null) {

				xMargin = 150;
				perLine = (int) Math.max(1,
						((width - xMargin) / (thumbnailWidth + spacing)));
				// get the number photos for each year
				int buffer = years[0];// 2014

				yearNums = new int[years[years.length - 1] - years[0] + 1]; // 2025
																			// -
																			// 2014

				// System.out.println(years[years.length - 1] - years[0] + 1);

				// System.out.println(yearNums.length);

				int columnCount = perLine;
				int rowCount = 0;

				for (int i = 0; i < years.length; i++) {
					yearNums[years[i] - buffer]++;
				}

				for (int i = 0; i < yearNums.length; i++) {
					rowCount += Math.ceil((double) (yearNums[i]) / perLine);
					// System.out.println(Math.ceil((double) (yearNums[i])/
					// perLine));
				}

				// System.out.println("Thumbcount: " + (rowCount *
				// columnCount));

				// fill with dummy thumbnails first
				this.thumbnails = new Thumbnail[rowCount * columnCount];
				for (int i = 0; i < thumbnails.length; i++) {

					this.thumbnails[i] = new Thumbnail(
							true,
							xMargin
									+ ((i % perLine) * (thumbnailWidth + spacing)),
							yMargin
									+ (int) (Math.floor((i / perLine) * 100) / 100)
									* (thumbnailHeight + spacing),
							thumbnailWidth, thumbnailHeight);
				}

				// need to fill appropriate values
				int counter = 0;
				int thumbsCount = 0;
				for (int i = 0; i < yearNums.length; i++) {
					for (int j = 0; j < yearNums[i]; j++) {

						// System.out.println(yearNums[i]);
						thumbnails[counter] = new Thumbnail(
								thumbs[thumbsCount],
								xMargin
										+ ((counter % perLine) * (thumbnailWidth + spacing)),
								yMargin
										+ (int) (Math
												.floor((counter / perLine) * 100) / 100)
										* (thumbnailHeight + spacing),
								thumbnailWidth, thumbnailHeight);
						thumbsCount++;
						counter++;

					}

					// Skip the ones that are supposed to be blank
					if ((yearNums[i] & perLine) != 0) {
						counter += perLine - (yearNums[i] & perLine);
					}
				}

				// this.thumbnails = new Thumbnail[thumbs.length];
			} else {

				this.thumbnails = new Thumbnail[thumbs.length];
				for (int i = 0; i < thumbnails.length; i++) {

					thumbnails[i] = new Thumbnail(
							thumbs[i],
							xMargin
									+ ((i % perLine) * (thumbnailWidth + spacing)),
							yMargin
									+ (int) (Math.floor((i / perLine) * 100) / 100)
									* (thumbnailHeight + spacing),
							thumbnailWidth, thumbnailHeight);

				}
			}

			lineHeight = thumbnails[thumbnails.length - 1].getY()
					+ thumbnailHeight + spacing + yMargin;

		}
	}

	/**
	 * Get the length of the full collection view
	 * 
	 * @return
	 */
	public int getLineHeight() {

		return this.lineHeight;
	}

	/**
	 * Get the width of a thumbnail
	 * 
	 * @return int of the width of each thumbnail
	 */
	public int getThumbnailWidth() {
		return thumbnailWidth;
	}

	/**
	 * Set the width of the thumbnails
	 * 
	 * @param thumbnailWidth
	 *            , int to set the width to
	 */
	public void setThumbnailWidth(int thumbnailWidth) {
		this.thumbnailWidth = thumbnailWidth;
	}

	/**
	 * Get the height of the thumbnails
	 * 
	 * @return int of the height of a thumbnail
	 */
	public int getThumbnailHeight() {
		return thumbnailHeight;
	}

	/**
	 * Set the height of each thumbnail
	 * 
	 * @param thumbnailHeight
	 *            , int to set the thumbnail height to
	 */
	public void setThumbnailHeight(int thumbnailHeight) {
		this.thumbnailHeight = thumbnailHeight;
	}

	/**
	 * Get the index of the selected thumbnail
	 * 
	 * @return int of the index
	 */
	public int getSelection() {

		if (years == null) {
			return selection;
		} else {
			int correctedSelected = selection;
			for (int i = 0; i < selection; i++) {
				if (thumbnails[i].dummy) {
					correctedSelected--;
				}
			}

			return correctedSelected;

		}
	}

	/**
	 * Set the selected thumbnail
	 * 
	 * @param Index
	 *            of selection
	 */
	public void setSelection(int selection) {
		this.selection = selection;
	}

	/**
	 * Get the width of the collection view int of the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Set the width of the collection view
	 * 
	 * @param width
	 *            to set to
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Get the length of the visible collection view
	 * 
	 * @param int of the length of the collection view
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Set the visible height of the collection view
	 * 
	 * @param height
	 *            to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Get the scale of the collection thumbnails
	 * 
	 * @return double of the scale
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * Get the spacing on the top
	 * 
	 * @return int, spacing on the top
	 */
	public int getyMargin() {
		return yMargin;
	}

	/**
	 * Set top spacing
	 * 
	 * @param yMargin
	 *            , int spacing for the top
	 */
	public void setyMargin(int yMargin) {
		this.yMargin = yMargin;
	}

	/**
	 * Get the space between each thumbnail
	 * 
	 * @return int of the spacing between the thumbnail
	 */
	public int getSpacing() {
		return spacing;
	}

	/**
	 * Set the spacing between thumbnails
	 * 
	 * @param int space between
	 */
	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}

	/**
	 * Get the scroll value of the Collection
	 * 
	 * @return double scroll value
	 */
	public double getScrollValue() {
		return scrollValue;
	}

	/**
	 * Change the Collection's scroll value
	 * 
	 * @param scrollValue
	 *            to change to
	 */
	public void setScrollValue(double scrollValue) {
		this.scrollValue = scrollValue;
	}

	/**
	 * Get the collection's selection rectangle
	 * 
	 * @return Collection's selection rectangle
	 */
	public Rectangle getSelectionRect() {
		return selectionRect;
	}

	/**
	 * Change the Collection's selection rectangle
	 * 
	 * @param new selection rectangle
	 */
	public void setSelectionRect(Rectangle selectionRect) {
		this.selectionRect = selectionRect;
	}

	/**
	 * Get the collections thumbnails
	 * 
	 * @return Thumbnail[] of the collections thumbnails
	 */
	public Thumbnail[] getThumbnails() {
		return thumbnails;
	}

	/**
	 * Change the collections thumbnails
	 * 
	 * @param thumbnails
	 *            to change to
	 */
	public void setThumbnails(Thumbnail[] thumbnails) {
		this.thumbnails = thumbnails;
	}

	/**
	 * Get the selected thumbnail
	 * 
	 * @return thumbnail that is selected
	 */
	public Thumbnail getSelected() {
		return selected;
	}

	/**
	 * Set the selected thumbnail
	 * 
	 * @param selected
	 *            index of thumbnail to selected
	 */
	public void setSelected(Thumbnail selected) {
		this.selected = selected;
	}

	/**
	 * Set the scale of the collection view
	 * 
	 * @param scale
	 *            to set the collection view to
	 */
	public void setScale(float scale) {

		if (numThumbs > 0) {
			this.scale = scale;

			this.thumbnailWidth = (int) (this.initThumbnailWidth * scale);
			this.thumbnailHeight = (int) (this.initThumbnailHeight * scale);

			perLine = (int) Math.max(1,
					((width - xMargin) / (thumbnailWidth + spacing)));
			lineWidth = perLine * (thumbnailWidth + spacing);

			int newYearSpacing = 0;

			if (years != null) {

				xMargin = 150;
				perLine = (int) Math.max(1,
						((width - xMargin) / (thumbnailWidth + spacing)));
				int columnCount = perLine;
				int rowCount = 0;

				// get how many lines are needed
				for (int i = 0; i < yearNums.length; i++) {
					rowCount += Math.ceil((double) (yearNums[i]) / perLine);
				}

				/*
				 * System.out.println("perLine: " + perLine + ", line width: " +
				 * lineWidth + ", rowCount: " + rowCount);
				 */

				// fill with dummy thumbnails first
				this.thumbnails = new Thumbnail[rowCount * columnCount];
				for (int i = 0; i < thumbnails.length; i++) {

					this.thumbnails[i] = new Thumbnail(
							true,
							xMargin
									+ ((i % perLine) * (thumbnailWidth + spacing)),
							yMargin
									+ (int) (Math.floor((i / perLine) * 100) / 100)
									* (thumbnailHeight + spacing),
							thumbnailWidth, thumbnailHeight);
				}

				// need to fill appropriate values
				int counter = 0;
				int thumbsCount = 0;
				int newYearCounter = 0;

				for (int i = 0; i < yearNums.length; i++) {

					if (yearNums[i] != 0) {
						if (yearNums[i] / perLine >= 1) {
							newYearSpacing = 0;
						} else {
							newYearSpacing += 20;

							yearYValues[newYearCounter] = 20
									+ yMargin
									+ newYearSpacing
									+ (int) (Math
											.floor((counter / perLine) * 100) / 100)
									* (thumbnailHeight + spacing);
							newYearCounter++;
						}
					}

					for (int j = 0; j < yearNums[i]; j++) {

						// System.out.println("Count this");
						thumbnails[counter] = new Thumbnail(
								thumbs[thumbsCount],
								xMargin
										+ ((counter % perLine) * (thumbnailWidth + spacing)),
								yMargin
										+ newYearSpacing
										+ (int) (Math
												.floor((counter / perLine) * 100) / 100)
										* (thumbnailHeight + spacing),
								thumbnailWidth, thumbnailHeight);
						// System.out.println("thumbs = " + thumbsCount);

						thumbsCount++;
						counter++;
					}

					// Skip the ones that are supposed to be blank
					if ((yearNums[i] % perLine) != 0) {
						counter += perLine - (yearNums[i] % perLine);
					}
				}

				// this.thumbnails = new Thumbnail[thumbs.length];
			} else {

				for (int i = 0; i < thumbnails.length; i++) {
					thumbnails[i]
							.setBounds(
									xMargin
											+ ((i % perLine) * (thumbnailWidth + spacing)),
									yMargin
											+ (int) (Math
													.floor((i / perLine) * 100) / 100)
											* (thumbnailHeight + spacing),
									thumbnailWidth, thumbnailHeight);
				}

			}

			if (selectionRect != null) {
				if (years == null) {
					this.selectionRect.setBounds(thumbnails[selection]
							.getBounds());
				} else {
					this.selectionRect = null;
					this.selection = -1;

				}
			}

			lineHeight = thumbnails[thumbnails.length - 1].getY()
					+ thumbnailHeight + spacing + yMargin + newYearSpacing;

			if (scrollValue > lineHeight - height) {

				scrollValue = lineHeight - height;

			}

			if (lineHeight < height) {

				scrollValue = 0;
			}
		}

		repaint();

	}

	/**
	 * Get the preferred size of the PhotoViewer
	 * 
	 * @return Dimension object containing width and height
	 */
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	/**
	 * Size the collection view
	 * 
	 * @param width
	 *            to resize to
	 * @param height
	 *            to resize to
	 */
	public void resized(int width, int height) {
		this.width = width;
		this.height = height;
		perLine = (int) Math.max(1, (width / (thumbnailWidth + spacing)));

		lineWidth = perLine * (thumbnailWidth + spacing);

		if (lineWidth < width) {
			xMargin = yMargin;
		} else {
			xMargin = (width - lineWidth) / 2;
		}

		int newYearSpacing = 0;

		if (years != null) {

			xMargin = 150;
			int columnCount = perLine;
			int rowCount = 0;

			// get how many lines are needed
			for (int i = 0; i < yearNums.length; i++) {
				rowCount += Math.ceil((double) (yearNums[i]) / perLine);
			}

			/*
			 * System.out.println("perLine: " + perLine + ", line width: " +
			 * lineWidth + ", rowCount: " + rowCount);
			 */

			// fill with dummy thumbnails first
			this.thumbnails = new Thumbnail[rowCount * columnCount];
			for (int i = 0; i < thumbnails.length; i++) {

				this.thumbnails[i] = new Thumbnail(true, xMargin
						+ ((i % perLine) * (thumbnailWidth + spacing)), yMargin
						+ (int) (Math.floor((i / perLine) * 100) / 100)
						* (thumbnailHeight + spacing), thumbnailWidth,
						thumbnailHeight);
			}

			// need to fill appropriate values
			int counter = 0;
			int thumbsCount = 0;
			int newYearCounter = 0;

			for (int i = 0; i < yearNums.length; i++) {

				if (yearNums[i] != 0) {
					if (yearNums[i] / perLine >= 1) {
						newYearSpacing = 0;
					} else {
						newYearSpacing += 20;

						yearYValues[newYearCounter] = 20
								+ yMargin
								+ newYearSpacing
								+ (int) (Math.floor((counter / perLine) * 100) / 100)
								* (thumbnailHeight + spacing);
						newYearCounter++;
					}
				}

				for (int j = 0; j < yearNums[i]; j++) {

					// System.out.println("Count this");
					thumbnails[counter] = new Thumbnail(
							thumbs[thumbsCount],
							xMargin
									+ ((counter % perLine) * (thumbnailWidth + spacing)),
							yMargin
									+ newYearSpacing
									+ (int) (Math
											.floor((counter / perLine) * 100) / 100)
									* (thumbnailHeight + spacing),
							thumbnailWidth, thumbnailHeight);
					// System.out.println("thumbs = " + thumbsCount);

					thumbsCount++;
					counter++;
				}

				// Skip the ones that are supposed to be blank
				if ((yearNums[i] % perLine) != 0) {
					counter += perLine - (yearNums[i] % perLine);
				}
			}

			// this.thumbnails = new Thumbnail[thumbs.length];
		} else {

			if (thumbnails != null) {
				for (int i = 0; i < thumbnails.length; i++) {
					thumbnails[i]
							.setBounds(
									xMargin
											+ ((i % perLine) * (thumbnailWidth + spacing)),
									yMargin
											+ (int) (Math
													.floor((i / perLine) * 100) / 100)
											* (thumbnailHeight + spacing),
									thumbnailWidth, thumbnailHeight);
				}
			}
		}

		if (selectionRect != null) {
			if (years == null) {
				this.selectionRect.setBounds(thumbnails[selection].getBounds());
			} else {
				this.selectionRect = null;
				this.selection = -1;

			}
		}

		lineHeight = thumbnails[thumbnails.length - 1].getY() + thumbnailHeight
				+ spacing + yMargin;

		if (scrollValue > lineHeight - height) {

			scrollValue = lineHeight - height;

		}

		if (lineHeight < height) {

			scrollValue = 0;
		}

		repaint();

	}

	/**
	 * Add a thumbnail to the collection view
	 * 
	 * @param thumbnail
	 *            to added
	 */
	public void addThumbnail(BufferedImage thumb) {

		Thumbnail[] temp = new Thumbnail[this.thumbnails.length + 1];
		for (int i = 0; i < this.thumbnails.length; i++) {
			temp[i] = this.thumbnails[i];

		}
		temp[this.thumbnails.length] = new Thumbnail(thumb, thumbnailWidth,
				thumbnailHeight, 0, 0);

		// this.resize();
	}

	/**
	 * Draws the collection view
	 */
	@Override
	public void paint(Graphics g) {

		// AffineTransform tx = new AffineTransform();
		// tx.translate(translateX, translateY);
		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		if (years != null) {
			for (int i = 0; i < yearsCondensed.length; i++) {

				g2d.setColor(Color.GRAY);
				g2d.setFont(new Font("Ariel", Font.BOLD, 40));
				g2d.drawString(yearsCondensed[i] + "", 10, yearYValues[i] + 25);

			}
		}
		g2d.setColor(Color.WHITE);

		if (this.tag != null) {
			g2d.setFont(defaultFont);

			FontMetrics metrics = g2d.getFontMetrics(defaultFont);

			// int stringHeight = metrics.getHeight();

			int stringWidth = metrics.stringWidth(this.tag);

			g2d.setColor(tagColor);

			g2d.drawString(this.tag, (int) (this.width) - stringWidth - 40,
					this.height);

		}

		g2d.setColor(Color.white);
		// AffineTransform oldXForm = g2d.getTransform();

		for (int i = 0; i < thumbnails.length; i++) {

			if (thumbnails[i].getY() - scrollValue > -500
					&& thumbnails[i].getY() - scrollValue < height + 500) {
				g2d.drawImage(thumbnails[i].getImage(), thumbnails[i].getX(),
						(int) (thumbnails[i].getY() - scrollValue),
						thumbnailWidth, thumbnailHeight, null);
			}

		}

		if (selectionRect != null) {
			g2d.setColor(selectionColor);
			g2d.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.5f));
			g2d.fillRect((int) (selectionRect.getX()),
					(int) (selectionRect.getY() - scrollValue),
					(int) (selectionRect.getWidth()),
					(int) (selectionRect.getHeight()));
			g2d.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 1.0f));
			g2d.setColor(Color.white);
		}

	}

	/**
	 * Get text of the tag
	 * 
	 * @return String of the tag text
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Set the tag of the colleciton view
	 * 
	 * @param String
	 *            tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * Convert an int ArrayList to an int[]
	 * 
	 * @param integers
	 *            in the arraylist as int[]
	 * @return
	 */
	public static int[] convertIntegers(List<Integer> integers) {
		int[] ret = new int[integers.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = integers.get(i).intValue();
		}
		return ret;
	}

	/**
	 * Get the number of thumbnails
	 * 
	 * @return number of thumbnails in collection
	 */
	public int numThumbs() {

		return this.thumbnails.length;
	}

	/**
	 * Capture keys typed on the collection view
	 * 
	 * @param KeyEvent
	 *            e
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		// System.out.println("here!");
	}

	/**
	 * Capture key presses on the collection view
	 * 
	 * @param KeyEvent
	 *            e
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// System.out.println("here!");

		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_UP:
			// handle up
			setScrollValue(Math.max(
					0.0,
					(Math.min((getLineHeight() - getHeight()), getScrollValue()
							+ (1.3 * 10)))));
			repaint();
			break;
		case KeyEvent.VK_DOWN:
			setScrollValue(Math.max(
					0.0,
					(Math.min((getLineHeight() - getHeight()), getScrollValue()
							- (1.3 * 10)))));
			repaint();
			// handle down
			break;
		case KeyEvent.VK_LEFT:
			// handle left
			break;
		case KeyEvent.VK_RIGHT:
			// handle right
			break;
		}

	}

	/**
	 * Capture key releases on the collection view
	 * 
	 * @param KeyEvent
	 *            e
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		// System.out.println("here!");
	}

}
