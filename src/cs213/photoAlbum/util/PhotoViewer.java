package cs213.photoAlbum.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class PhotoViewer extends JComponent implements MouseListener,
		MouseMotionListener, MouseWheelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2660533177543700842L;

	private BufferedImage currentPhoto;

	private File[] filepaths;
	private int width;
	private int height;
	private double scale = 0.0;

	private double maxZoom = 2.0;
	private double minZoom = 0.5;
	private double scaleStep = .01;

	private int picX;
	private int picY;

	private boolean drawX = false;
	private int index;

	private int switchArrow = -1;

	private boolean zoom = false;
	private boolean pan = false;

	private float bottomBarOpacity = 0.5f;
	private int bottomBarHeight = 30;

	private BufferedImage leftArrow;
	private BufferedImage rightArrow;

	public static String containerFolder = "data/AppPhotos/";

	Font defaultFont = new Font("Ariel", Font.PLAIN, 14);

	public PhotoViewer(File[] filepaths, int index, int width, int height) {

		this.index = index;
		try {
			currentPhoto = ImageIO.read(filepaths[index]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.filepaths = filepaths;
		this.width = width;
		this.height = height;
		setOpaque(true);
		setDoubleBuffered(true);

		try {
			leftArrow = ImageIO.read(new File(containerFolder + "arrowLeft.png"));
			rightArrow = ImageIO.read(new File(containerFolder + "arrowRight.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);

	}

	@Override
	public void paint(Graphics g) {

		Point2D start = new Point2D.Float(this.getHeight() / 2,
				this.getWidth() / 2);
		float end = (float) this.getWidth();
		float[] dist = { 0.0f, 1.0f };
		Color[] colors = { Color.DARK_GRAY, Color.BLACK };
		RadialGradientPaint p = new RadialGradientPaint(start, end, dist,
				colors);

		// AffineTransform tx = new AffineTransform();
		// tx.translate(translateX, translateY);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(p);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		double ratio = width / height;

		// Means that it is taller than wider
		if (zoom == true) {

			double sWidth = currentPhoto.getWidth();
			double sHeight = currentPhoto.getHeight();

			// System.out.println(sWidth * scale);
			// System.out.println(sHeight * scale);

			g2d.drawImage(currentPhoto.getScaledInstance(
					(int) (sWidth * scale), (int) (sHeight * scale),
					Image.SCALE_AREA_AVERAGING),
					(int) ((width / 2 - (sWidth * scale) / 2)),
					(int) ((height / 2 - (sHeight * scale) / 2)), null);

		} else if (currentPhoto.getWidth() < width
				&& currentPhoto.getHeight() < height) {

			g2d.drawImage(currentPhoto,
					width / 2 - currentPhoto.getWidth() / 2, height / 2
							- currentPhoto.getHeight() / 2, null);
			scale = 1.0;

		} else if (currentPhoto.getWidth() / currentPhoto.getHeight() > ratio) {
			double sWidth = width;

			scale = sWidth / currentPhoto.getWidth();

			g2d.drawImage(currentPhoto.getScaledInstance(
					(int) (currentPhoto.getWidth() * scale),
					(int) (currentPhoto.getHeight() * scale),
					Image.SCALE_AREA_AVERAGING), 0,
					(int) ((height - (currentPhoto.getHeight() * scale)) / 2),
					null);

		} else {
			// Wider than tall

			double sHeight = height;

			scale = sHeight / currentPhoto.getHeight();

			g2d.drawImage(currentPhoto.getScaledInstance(
					(int) (currentPhoto.getWidth() * scale),
					(int) (currentPhoto.getHeight() * scale),
					Image.SCALE_AREA_AVERAGING), (int) ((width - (currentPhoto
					.getWidth() * scale)) / 2), 0, null);

		}

		if (filepaths.length > 1) {
			g2d.setColor(Color.white);
			if (switchArrow == 0) {
				// draw on left
				g2d.drawImage(leftArrow, 0, height / 2 - leftArrow.getHeight()
						/ 2, null);
			} else if (switchArrow == 1) {
				// draw on right
				g2d.drawImage(rightArrow, width - rightArrow.getWidth(), height
						/ 2 - leftArrow.getHeight() / 2, null);
			}
		}
		// g2d.drawImage(currentPhoto, 0, 0, width, height, null);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				bottomBarOpacity));
		g2d.setColor(Color.black);
		g2d.fillRect(0, this.getHeight() - bottomBarHeight, this.getWidth(),
				bottomBarHeight);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				1.0f));
		g2d.setColor(Color.white);

		g2d.setFont(defaultFont);

		FontMetrics metrics = g2d.getFontMetrics(defaultFont);

		// int stringHeight = metrics.getHeight();

		int stringWidth = metrics.stringWidth(filepaths[index].getName());

		g2d.drawString(filepaths[index].getName(), this.getWidth() / 2
				- stringWidth / 2, this.getHeight() - 10);

		g2d.setColor(Color.GRAY);
		g2d.drawString(index + 1 + " of " + filepaths.length, 20,
				this.getHeight() - 10);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("hey");

		if (filepaths.length > 1) {
			if (e.getX() > width / 2) {
				index++;
				index %= filepaths.length;

			} else {
				index--;
				if (index < 0) {
					index = filepaths.length - 1;
				}
			}

			try {
				currentPhoto = ImageIO.read(filepaths[index]);
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}

			scale = 1.0;
			zoom = false;
			repaint();

		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		drawX = true;
		System.out.println("Mouse entered");
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		switchArrow = -1;
		repaint();

	}

	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {

		if (filepaths.length > 1) {
			if (e.getX() > width / 2) {
				if (switchArrow != 1) {
					switchArrow = 1;
					System.out.println("MouseMoved Right");
					repaint();
				}

			} else {
				if (switchArrow != 0) {
					switchArrow = 0;
					System.out.println("MouseMoved Left");

					repaint();
				}
			}
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		zoom = true;
		scale = (Math.max(minZoom, (Math.min((maxZoom),
				scale - (scaleStep * e.getWheelRotation())))));

		// System.out.println("Zoom: " + scale);
		repaint();
	}

}
