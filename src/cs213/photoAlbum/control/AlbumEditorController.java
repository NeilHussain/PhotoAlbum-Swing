/* Neil Hussain 
 * Matt Kaiser
 */
package cs213.photoAlbum.control;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.User;
import cs213.photoAlbum.view.View;

public class AlbumEditorController {

	private Album model;
	private User userModel;
	private View view;

	/**
	 * AlbumEditorController class constructor
	 * 
	 * @param model
	 *            <code>Album</code> model for fetching data
	 * @param view
	 *            <code>View</code> view to display data on
	 */
	public AlbumEditorController(Album model, User userModel) {
		this.model = model;
		this.userModel = userModel;
	}

	/**
	 * Add a <code>Photo</code> to an <code>Album</code> object
	 * 
	 * @param filename
	 *            String filename of photo
	 * @param caption
	 *            String caption of photo
	 * @return return 1 on success, 0 on filename not existing, -1 on file
	 *         already existing
	 */
	public int addAlbumPhoto(String name, String filename, String caption) {
		// return 1 on success
		// 0 on filename not existing
		// -1 on file already existing

		Photo[] photos = model.getPhotos();

		// Checks if the photo is already in the album
		if (photos != null) {

			for (int i = 0; i < photos.length; i++) {
				if (photos[i].getFilename().equals(filename)) {
					return -1;
				}
			}

		}

		// makes a new photo
		Photo toAdd = new Photo(model);
		if (toAdd.loadPhoto(filename)) {

			toAdd.setThumbnail(200, 150);

			toAdd.setCaption(caption);

			boolean found = false;

			for (Photo photo : userModel.getAllPhotos()) {

				if (photo.getFilePath().equals(toAdd.getFilePath())) {

					// System.out.println("got here");
					photo.addAlbum(model);
					model.addPhoto(photo);
					toAdd = photo;
					found = true;

				}

			}

			if (found == false) {
				model.addPhoto(toAdd);
				userModel.addPhoto(toAdd);
			}

			return 1;
		} else {

			return 0;

		}
	}

	/**
	 * Remove a <code>Photo</code> from an <code>Album</code> object
	 * 
	 * @param filename
	 *            String filename to be removed
	 * @return True on success, false otherwise
	 */
	public boolean removeAlbumPhoto(String filename) {

		Photo photo = model.getPhoto(filename);

		if (photo != null) {

			photo.removeAlbum(model);
			if (photo.getAlbums().length == 0) {
				// System.out.println(photo.getAlbums().length);
				userModel.removePhoto(photo);
			}
			//
			return model.removePhoto(photo);

		} else {

			return false;
		}
	}

	public String listPhotos() {

		String toReturn = "";

		Photo[] photos = model.getPhotos();

		if (photos.length >= 1) {
			toReturn = "Photos for album " + model.getName() + ":";
		}

		for (int i = 0; i < photos.length; i++) {

			toReturn = toReturn
					+ ("\n" + photos[i].getFilename() + " - " + photos[i]
							.getTimeString());
		}

		return toReturn;
	}

	/**
	 * Change the caption of a photo in the album
	 * 
	 * @param The
	 *            <code>Photo</code> object that you want to change caption of.
	 * @param String
	 *            of the new caption
	 * @return True on success, false otherwise
	 */
	public boolean recaptionAlbumPhoto(Photo photo, String caption) {
		return model.recaptionPhoto(photo, caption);
	}

	/**
	 * Returns the photo object that has the filename
	 * 
	 * @param filename
	 *            string of the photo
	 * @return Photo Object with correct filename
	 */
	public Photo getPhoto(String filename) {

		return model.getPhoto(filename);

	}

	/**
	 * Adds a photo to the album
	 * 
	 * @param photo
	 *            to be added
	 * @return true on success, false otherwise
	 */
	public boolean addPhoto(Photo photo) {
		return model.addPhoto(photo);

	}

	/**
	 * Get the album that this controller operates on.
	 * 
	 * @return Album object
	 */
	public Album getAlbum() {

		return model;
	}

	/**
	 * Remove a photo from the album
	 * 
	 * @param photo
	 *            to be removed
	 * @return true on success, false otherwise
	 */
	public boolean removePhoto(Photo photo) {

		return model.removePhoto(photo);
	}

	/**
	 * Get the number of <code>Photo</code>s in an album
	 * 
	 * @return Int of the number of photos in the album
	 */
	public int getAlbumSize() {
		return model.size();
	}

	/**
	 * Calls the refresh method in the <code>View</code> that will update values
	 * and reload the view
	 */
	public void refreshAlbumView() {

		view.refreshAlbumView();

	}

	public String getName() {

		return model.getName();
	}

	public void setName(String name) {

		model.setName(name);
	}

	public Photo[] getPhotoObjsByTags(String[] tags) {

		// <caption> - Album: <albumName>[,<albumName>]... - Date: <date>
		ArrayList<Photo> goodPhotos = new ArrayList<Photo>();

		Photo[] photos = model.getPhotos();

		for (Photo photo : photos) {

			boolean hasAllTags = true;
			for (String tag : tags) {

				String[] tagParts = parseTag(tag); // Check if photo has the tag

				if (photo.hasTag(tagParts[0], tagParts[1])) {
					// These are all photos that have the correct tags.

				} else {
					hasAllTags = false;

				}
			}

			if (hasAllTags) {

				goodPhotos.add(photo);

			}
		}

		Photo[] sorted = sortPhotos(goodPhotos.toArray(new Photo[goodPhotos
				.size()]));
		return sorted;

	}

	/**
	 * Sort the inputed photos in chronologically order
	 * 
	 * @param photos
	 *            array of the photos to sort
	 * @return Sorted photos array
	 */
	public Photo[] sortPhotos(Photo[] photos) {

		int n = photos.length;
		for (int j = 1; j < n; j++) {
			Photo key = photos[j];
			int i = j - 1;
			while ((i > -1) && !(photos[i].getDate().before(key.getDate()))) {
				photos[i + 1] = photos[i];
				i--;
			}
			photos[i + 1] = key;
		}

		return photos;

	}

	/**
	 * Helper method to parse tags strings quickly.
	 * 
	 * @param tagString
	 *            car:bmw car:"bmw"
	 * @return String array of the correct parts of the tag
	 */
	private String[] parseTag(String tagString) {
		// person:"Alex Buschman"

		// System.out.println(tagString);
		String[] tagParts = new String[2];

		if (tagString.contains(":")) {
			tagParts[0] = tagString.substring(0, tagString.indexOf(':'));

			tagParts[1] = trimInput(tagString.substring(
					tagString.indexOf(':') + 1, tagString.length()));

			if (tagParts[1].charAt(0) == '"') {

				tagParts[1] = tagParts[1].substring(1, tagParts[1].length())
						.trim();

			}
		} else {

			tagParts[0] = "";
			tagParts[1] = trimInput(tagString);
		}

		// System.out.println(tagParts[0] + " " + tagParts[1]);

		return tagParts;
	}

	/**
	 * Trims the beginning and trailing space and quotation marks
	 * 
	 * @param String
	 *            to trim
	 * @return Trimmed string
	 */
	public static String trimInput(String input) {

		if (input.charAt(0) == '"' && input.charAt(input.length() - 1) == '"') {
			input = input.substring(1, input.length() - 1);
		}

		return input;

	}

	public File[] getAllPhotoPaths() {

		Photo[] photos = model.getPhotos();
		File[] paths = new File[photos.length];

		int x = 0;
		for (Photo photo : photos) {
			paths[x] = photo.getFilePath();

			x++;
		}

		return paths;

	}

	/**
	 * Return all thumbnails for all photos array passed in
	 * 
	 * @param Photos
	 *            array to get the thumbnails for
	 * @return File array of paths
	 */
	public File[] getThumbnails() {
		Photo[] photos = model.getPhotos();
		ArrayList<File> thumbFiles = new ArrayList<File>();
		for (Photo photo : photos) {
			thumbFiles.add(photo.getThumbnail());
		}

		return thumbFiles.toArray(new File[thumbFiles.size()]);
	}

	/**
	 * Get the photo at the given array index
	 * 
	 * @param index
	 *            of photo to get
	 * @return Photo object at the index
	 */
	public Photo getPhotoByIndex(int index) {

		return model.getPhotos()[index];

	}

	/**
	 * Get the caption of the album
	 * 
	 * @return String of the caption text
	 */
	public String getCaption() {

		return model.getCaption();
	}

	/**
	 * Set the caption of the album
	 * 
	 * @param String
	 *            to set the album's caption to
	 */
	public void setCaption(String caption) {

		model.setCaption(caption);
	}

	/**
	 * Get the newest photo entered into the album
	 * 
	 * @return String of the date of the newest photo
	 */
	public String getLatest() {

		Calendar calendar = Calendar.getInstance();
		if (model.getLatest() != null) {
			calendar.setTime(model.getLatest());

			return calendar.get(Calendar.MONTH) + "/"
					+ calendar.get(Calendar.DATE) + "/"
					+ calendar.get(Calendar.YEAR);
		} else {
			return null;
		}
	}

	/**
	 * Get the oldest photo entered into the album
	 * 
	 * @return String of the date of the oldest photo
	 */
	public String getEarliest() {
		Calendar calendar = Calendar.getInstance();
		if (model.getEarliest() != null) {
			calendar.setTime(model.getEarliest());

			return calendar.get(Calendar.MONTH) + "/"
					+ calendar.get(Calendar.DATE) + "/"
					+ calendar.get(Calendar.YEAR);
		} else {

			return null;
		}
	}

}
