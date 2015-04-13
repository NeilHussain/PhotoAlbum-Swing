package cs213.photoAlbum.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import cs213.photoAlbum.guiview.GuiView;

public class ImageUtilities {

	static String containerFolder = "data/thumbnails/";

	public static BufferedImage makePhotoThumbnail(String photo, int width,
			int height) throws IOException {

		System.out.println(photo);
		return makePhotoThumbnail(new File(photo), width, height);
	}

	public static BufferedImage makePhotoThumbnail(File photo, int width,
			int height) throws IOException {
		return makePhotoThumbnail(ImageIO.read(photo), photo, width, height);
	}

	public static BufferedImage[] getMultipleThumbnails(BufferedImage[] photos,
			int width, int height) throws IOException {

		BufferedImage thumbnails[] = new BufferedImage[photos.length];
		for (int i = 0; i < thumbnails.length; i++) {

			thumbnails[i] = makePhotoThumbnailWithoutSave(photos[i], width,
					height);

		}
		return thumbnails;

	}

	public static BufferedImage makePhotoThumbnail(BufferedImage photo,
			File path, int width, int height) throws IOException {

		path = new File(containerFolder + path.getName());

		double ratio = width / height;

		BufferedImage combined = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);

		Graphics g = combined.getGraphics();

		double scale = 0.0;

		// Means that it is taller than wider
		if (photo.getWidth() / photo.getHeight() < ratio) {
			double sWidth = width;

			scale = sWidth / photo.getWidth();

			g.drawImage(photo.getScaledInstance(
					(int) (photo.getWidth() * scale),
					(int) (photo.getHeight() * scale),
					Image.SCALE_AREA_AVERAGING), 0, (int) ((height - (photo
					.getHeight() * scale)) / 2), null);

		} else {
			// Wider than tall

			double sHeight = height;

			scale = sHeight / photo.getHeight();

			g.drawImage(photo.getScaledInstance(
					(int) (photo.getWidth() * scale),
					(int) (photo.getHeight() * scale),
					Image.SCALE_AREA_AVERAGING), (int) ((width - (photo
					.getWidth() * scale)) / 2), 0, null);

		}

		saveImage(toBufferedImage(combined), path);

		return combined;
	}

	public static BufferedImage makePhotoThumbnailWithoutSave(
			BufferedImage photo, int width, int height) throws IOException {

		// System.out.println("HEere");

		double ratio = width / height;

		BufferedImage combined = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);

		Graphics g = combined.getGraphics();

		double scale = 0.0;

		// Means that it is taller than wider

		if (photo.getWidth() / photo.getHeight() < ratio) {
			double sWidth = width;

			scale = sWidth / photo.getWidth();

			g.drawImage(photo.getScaledInstance(
					(int) (photo.getWidth() * scale),
					(int) (photo.getHeight() * scale),
					Image.SCALE_AREA_AVERAGING), 0, (int) ((height - (photo
					.getHeight() * scale)) / 2), null);

		} else {
			// Wider than tall

			double sHeight = height;

			scale = sHeight / photo.getHeight();

			g.drawImage(photo.getScaledInstance(
					(int) (photo.getWidth() * scale),
					(int) (photo.getHeight() * scale),
					Image.SCALE_AREA_AVERAGING), (int) ((width - (photo
					.getWidth() * scale)) / 2), 0, null);

		}

		// saveImage(toBufferedImage(combined), "thumbnail+" +
		// path.getName().toString());

		return combined;
	}

	public static BufferedImage createAlbumThumbnail(BufferedImage[] photos,
			int density, int width, int height) throws IOException {

		// System.out.println(photos[0].getWidth());

		// density = 4, 9, 16, 25 etc.
		// width = 250, height = 155 (ish)

		// Assume density is 4
		// perLine is the number of rows and colomns of thumbnails

		int perLine = (int) Math.min(
				Math.max(Math.ceil(Math.sqrt((double) (photos.length))),
						Math.sqrt(density)), 6);
		// int perLine = (int) Math.sqrt(density);

		// double ratio = width / height;

		BufferedImage combined = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);

		Graphics g = combined.getGraphics();

		g.setColor(Color.white);
		g.drawRect(0, 0, combined.getWidth(), combined.getHeight());

		if(photos.length == 0) {
			BufferedImage albumBackground = ImageIO.read(new File(GuiView.containerFolder + "/blankAlbum.png"));
			g.drawImage(albumBackground, 0, 0, width, height, null);
		}
		else {
		
			int eachWidth = width / perLine;
			int eachHeight = height / perLine;
	
			for (int i = 0; i < photos.length && i < perLine * perLine; i++) {
	
				if (photos[i] != null) {
					g.drawImage(
							makePhotoThumbnailWithoutSave(photos[i], eachWidth,
									eachHeight), ((i) % perLine) * eachWidth,
							(int) (Math.floor((i / perLine) * 100) / 100)
									* eachHeight, null);
				}
			}
		}

		return combined;
	}

	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bufferedImage = new BufferedImage(img.getWidth(null),
				img.getHeight(null), BufferedImage.TYPE_INT_RGB);

		Graphics g = bufferedImage.createGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();

		// Return the buffered image
		return bufferedImage;
	}

	private static Path saveImage(BufferedImage image, File path)
			throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, "png", out);
		Files.write(path.toPath(), out.toByteArray());
		return path.toPath();

	}

}