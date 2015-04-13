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

	public int getLineHeight() {

		return this.lineHeight;
	}

	public int getThumbnailWidth() {
		return thumbnailWidth;
	}

	public void setThumbnailWidth(int thumbnailWidth) {
		this.thumbnailWidth = thumbnailWidth;
	}

	public int getThumbnailHeight() {
		return thumbnailHeight;
	}

	public void setThumbnailHeight(int thumbnailHeight) {
		this.thumbnailHeight = thumbnailHeight;
	}

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

	public void setSelection(int selection) {
		this.selection = selection;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public double getScale() {
		return scale;
	}

	public int getyMargin() {
		return yMargin;
	}

	public void setyMargin(int yMargin) {
		this.yMargin = yMargin;
	}

	public int getSpacing() {
		return spacing;
	}

	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}

	public double getScrollValue() {
		return scrollValue;
	}

	public void setScrollValue(double scrollValue) {
		this.scrollValue = scrollValue;
	}

	public Rectangle getSelectionRect() {
		return selectionRect;
	}

	public void setSelectionRect(Rectangle selectionRect) {
		this.selectionRect = selectionRect;
	}

	public Thumbnail[] getThumbnails() {
		return thumbnails;
	}

	public void setThumbnails(Thumbnail[] thumbnails) {
		this.thumbnails = thumbnails;
	}

	public Thumbnail getSelected() {
		return selected;
	}

	public void setSelected(Thumbnail selected) {
		this.selected = selected;
	}

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

	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

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

			for (int i = 0; i < thumbnails.length; i++) {
				thumbnails[i].setBounds(xMargin
						+ ((i % perLine) * (thumbnailWidth + spacing)), yMargin
						+ (int) (Math.floor((i / perLine) * 100) / 100)
						* (thumbnailHeight + spacing), thumbnailWidth,
						thumbnailHeight);
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

	public void addThumbnail(BufferedImage thumb) {

		Thumbnail[] temp = new Thumbnail[this.thumbnails.length + 1];
		for (int i = 0; i < this.thumbnails.length; i++) {
			temp[i] = this.thumbnails[i];

		}
		temp[this.thumbnails.length] = new Thumbnail(thumb, thumbnailWidth,
				thumbnailHeight, 0, 0);

		// this.resize();
	}

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

	public String getTag() {
		return tag;
	}

	public void setTag(String albumName) {
		this.tag = albumName;
	}

	public static int[] convertIntegers(List<Integer> integers) {
		int[] ret = new int[integers.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = integers.get(i).intValue();
		}
		return ret;
	}

	public int numThumbs() {

		return this.thumbnails.length;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("here!");
	}

	@Override
	public void keyPressed(KeyEvent e) {
System.out.println("here!");
		
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_UP:
			// handle up
			setScrollValue(Math.max(0.0,
					(Math.min((getLineHeight() - 
							getHeight()), getScrollValue()
							+ (1.3 * 10)))));
			repaint();
			break;
		case KeyEvent.VK_DOWN:
			setScrollValue(Math.max(0.0,
					(Math.min((getLineHeight() - 
							getHeight()), getScrollValue()
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

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("here!");
	}

}
