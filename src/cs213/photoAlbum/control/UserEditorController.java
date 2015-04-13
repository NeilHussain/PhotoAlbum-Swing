/* Neil Hussain 
 * Matt Kaiser
 */

package cs213.photoAlbum.control;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.imageio.ImageIO;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.Tag;
import cs213.photoAlbum.model.User;
import cs213.photoAlbum.util.ImageUtilities;
import cs213.photoAlbum.view.View;

public class UserEditorController {

	private User model;
	private View view;
	static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/y-kk:mm:ss",
			Locale.US);

	/**
	 * UserEditorController class constructor
	 * 
	 * @param model
	 *            <code>User</code> model for fetching user data
	 * @param view
	 *            <code>View</code> view to display data on
	 */
	public UserEditorController(User model) {
		this.model = model;
	}

	/**
	 * Get the <code>User</code> ID
	 * 
	 * @return String containing the user ID
	 */
	public String getUserID() {
		return model.getUserID();
	}

	public User getModel() {
		return model;
	}

	public void setModel(User model) {
		this.model = model;
	}

	/**
	 * Set the <code>User</code> ID
	 * 
	 * @param ID
	 *            String of new ID
	 * @return True if the change was successful, false otherwise
	 */
	public boolean setUserID(String ID) {
		return model.setUserID(ID);
	}

	/**
	 * Get the <code>User</code>'s full name
	 * 
	 * @return String containing the <code>User</code>'s full name
	 */
	public String getFullName() {
		return model.getFullName();
	}

	/**
	 * Set the <code>User</code>'s Full name
	 * 
	 * @param name
	 *            String of new name
	 * @return True if the change was successful, false otherwise
	 */
	public boolean setFullName(String name) {
		return model.setFullName(name);
	}

	/**
	 * Check if there is already an album with this name
	 * 
	 * @param albumName
	 *            to check
	 * @return true if album name is already taken, false if untaken
	 */
	private boolean nameTaken(String albumName) {

		String[] albumNames = getAlbumNames();

		for (int i = 0; i < albumNames.length; i++) {

			if (albumNames[i].equals(albumName)) {

				return true;
			}

		}

		return false;
	}

	/**
	 * Get all of the album names in string format
	 * 
	 * @return String[] of all album names
	 */
	public String[] getAlbumNames() {

		Album[] albums = getUserAlbums();
		String[] albumNames = new String[albums.length];

		for (int i = 0; i < albumNames.length; i++) {

			albumNames[i] = albums[i].getName();
		}

		return albumNames;

	}

	/**
	 * Get the <code>User</code>'s stored albums
	 * 
	 * @return <code>Album</code> array containing the albums that the user has
	 */
	public Album[] getUserAlbums() {

		return model.getAlbums();
	}

	public Album getAlbum(String albumName) {

		Album[] albums = model.getAlbums();
		for (int i = 0; i < albums.length; i++) {

			if (albums[i].getName().equals(albumName)) {
				return albums[i];
			}

		}

		return null;
	}

	/**
	 * Create a new AlbumEditorController to operate on
	 * 
	 * @param Valid
	 *            album name of the controller you want to create
	 * @return AlbumEditorController object
	 */
	public AlbumEditorController editAlbum(String albumName) {

		// Trim the album name
		if (albumName.charAt(0) == '"'
				&& albumName.charAt(albumName.length()) == '"') {
			albumName = albumName.substring(1, albumName.length() - 1);
		}

		// Find the album and return a controller
		if (getAlbum(albumName) != null) {
			return new AlbumEditorController(getAlbum(albumName), model);
		} else {

			// Didn't find the album
			return null;
		}
	}

	/**
	 * Add an <code>Album</code> to a <code>User</code>'s collection
	 * 
	 * @param albumName
	 *            String album name to add
	 * @return True if the change was successful, false otherwise
	 */
	public boolean addUserAlbum(String albumName) {

		if (albumName.charAt(0) == '"'
				&& albumName.charAt(albumName.length()) == '"') {
			albumName = albumName.substring(1, albumName.length() - 1);
		}

		if (!nameTaken(albumName)) {
			Album newAlbum = new Album(albumName);
			return model.addAlbum(newAlbum);
		} else {
			return false;
		}
	}

	/**
	 * Rename an album in a <code>User</code>'s collection
	 * 
	 * @param album
	 *            <code>Album</code> to rename
	 * @param name
	 *            String of new name
	 * @return True if the change was successful, false otherwise
	 */
	public boolean renameUserAlbum(Album album, String name) {

		return model.renameAlbum(album, name);

	}

	/**
	 * Remove an album from a <code>User</code>'s collection
	 * 
	 * @param An
	 *            <code>Album</code> to remove
	 * @return True if the change was successful, false otherwise
	 **/
	public boolean removeUserAlbum(String albumName) {

		if (albumName.charAt(0) == '"'
				&& albumName.charAt(albumName.length()) == '"') {
			albumName = albumName.substring(1, albumName.length() - 1);
		}

		for (Photo photo : model.getAlbum(albumName).getPhotos()) {
			photo.removeAlbum(model.getAlbum(albumName));

			if (photo.getAlbums().length == 0) {
				model.removePhoto(photo);

			}
		}

		return model.removeAlbum(model.getAlbum(albumName));
	}

	/**
	 * Get <code>View</code> view associated with this
	 * <code>UserEditorController</code>
	 *
	 * @return <code>View</code> that this <code>UserEditorController</code>
	 *         owns
	 */
	public View getUserView() {
		return view;
	}

	/**
	 * Get pre-formatted string to display the albums and contents
	 * 
	 * @return String or albums, number of photos in each and their time range
	 */
	public String listAlbums() {
		String toReturn = "";
		Album[] albums = model.getAlbums();
		for (int i = 0; i < albums.length; i++) {

			String early = "";
			String late = "";
			if (albums[i].getEarliest() != null) {

				early = sdf.format(albums[i].getEarliest());
				late = sdf.format(albums[i].getLatest());

				toReturn = toReturn + "\n" + albums[i].getName()
						+ " number of photos: " + albums[i].size() + ", "
						+ early + " - " + late;
			} else {

				toReturn = toReturn + "\n" + albums[i].getName()
						+ " number of photos: " + albums[i].size();

			}
		}

		return toReturn;
	}

	/**
	 * Get a preformatted string of photo details
	 * 
	 * @param Valid
	 *            filename to display
	 * @return String of photo details including: Name, Albums, Caption and
	 *         Tags.
	 */
	public String listPhotoInfo(String filename) {

		String toReturn = null;
		boolean found = false;

		String albumsString = "";
		for (Photo photo : model.getAllPhotos()) {

			if (photo.getFilename().equals(filename)) {

				found = true;
				for (Album album : photo.getAlbums()) {

					if (albumsString.equals("")) {
						albumsString = album.getName();
					} else {
						albumsString = albumsString + ", " + album.getName();
					}

				}

				if (found) {

					toReturn = "Photo file name: " + filename + "\nAlbum: "
							+ albumsString + "\nDate: " + photo.getTimeString()
							+ "\nCaption: " + photo.getCaption() + "\nTags:\n"
							+ photo.tagsToString();

					return toReturn;

				} else {

					return null;
				}

			}

		}

		return null;
		// Legacy... New design, user holds an array of all photos
		/*
		 * ArrayList<String[]> info = new ArrayList<String[]>(10); Album[]
		 * albums = model.getAlbums();
		 * 
		 * for (int i = 0; i < albums.length; i++) {
		 * 
		 * String[] singleInfo = albums[i].listInfo(filename);
		 * 
		 * if (singleInfo != null) {
		 * 
		 * info.add(singleInfo);
		 * 
		 * }
		 * 
		 * }
		 * 
		 * if (info.size() > 0) { String albumStrings = "Album: ";
		 * 
		 * for (int i = 0; i < info.size(); i++) {
		 * 
		 * albumStrings = albumStrings + " " + info.get(i)[1];
		 * 
		 * } // This shit definitely isn't going to work toReturn =
		 * "Photo file name: " + filename + "\n" + albumStrings + "\nDate: " +
		 * info.get(0)[2] + "\nCaption: " + info.get(0)[3] + "\nTags:\n" +
		 * info.get(0)[4];
		 * 
		 * } else {
		 * 
		 * return null; } return toReturn;
		 */
	}

	/**
	 * Get <code>View</code> view associated with this
	 * <code>UserEditorController</code>
	 * 
	 * @param <code>View</code> to associate with this
	 *        <code>UserEditorController</code>
	 */
	public void setUserView(View view) {
		this.view = view;
	}

	/**
	 * Calls the refresh method in the <code>View</code> that will update values
	 * and reload the view
	 */
	public void refreshUserView() {
		// Will call the method in the view class to update the entire view.
		view.refreshUserView();
	}

	/**
	 * Add a tag to a file
	 * 
	 * @param Valid
	 *            filename to add the tag to
	 * @param String
	 *            of tag contents, formatted correctly
	 * @return -1 if tag already exists, 0 if photo doesn't exist
	 */
	public int addTag(String filename, String tag) {

		/*
		 * addTag returns -1 if tag already exists, 0 if photo doesn't exist
		 */

		// returns number of times the tag was added to

		int numAlbums = 0;

		Album[] albums = model.getAlbums();

		for (int i = 0; i < albums.length; i++) {

			Photo toAdd = albums[i].getPhoto(filename);
			if (toAdd != null) {

				String[] tagParts = parseTag(tag);

				if (toAdd.addTag(tagParts[0], tagParts[1])) {

					numAlbums++;

				} else {

					return -1;

				}

			}

		}

		return numAlbums;
	}

	/**
	 * Add a tag to a file
	 * 
	 * @param Valid
	 *            filename to add the tag to
	 * @param String
	 *            of tag contents, formatted correctly
	 * @return -1 if tag already exists, 0 if photo doesn't exist
	 */
	public int addTag(Photo photo, String tag) {

		/*
		 * addTag returns -1 if tag already exists, 0 if photo doesn't exist
		 */

		// returns number of times the tag was added to
		int error = -1;
		Photo toAdd = photo;
		if (toAdd != null) {

			String[] tagParts = parseTag(tag);

			if (toAdd.addTag(tagParts[0], tagParts[1])) {

				error++;

			} else {

				return -1;

			}

		}

		return error;
	}

	/**
	 * Delete a tag that is associated with a photo file
	 * 
	 * @param Valid
	 *            filename to delete the tag from
	 * @param String
	 *            of the tag contents to delete, formatted correctly
	 * @return -1 if tag doesn't exists, 0 if photo doesn't exist
	 */
	public int deleteTag(String filename, String tag) {
		int numAlbums = 0;

		Album[] albums = model.getAlbums();

		for (int i = 0; i < albums.length; i++) {

			Photo toDelete = albums[i].getPhoto(filename);

			if (toDelete != null) {

				String[] tagParts = parseTag(tag);

				if (toDelete.removeTag(tagParts[0], tagParts[1])) {

					numAlbums++;

				} else {

					return -1;

				}

			}

		}

		return numAlbums;
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
	 * Get the photos from all albums that were taken between the dates
	 * 
	 * @param Begin
	 *            date to search for
	 * @param End
	 *            date to search for
	 * @return String array of pre-formatted photo details
	 */

	public String[] getPhotosByDate(Date begin, Date end) {

		ArrayList<String> toReturn = new ArrayList<String>();

		ArrayList<Photo> goodPhotos = new ArrayList<Photo>();
		Photo[] photos = model.getAllPhotos();

		// Debugging

		/*
		 * Date date = new Date(); date = photos[1].getDate();
		 * date.setMinutes(30);
		 * 
		 * photos[1].setDate(date);
		 */

		for (Photo photo : photos) {

			// Debugging
			/*
			 * System.out.println(begin.toString() + ", " + end.toString() +
			 * ", " + photo.getDate().toString());
			 */

			if (begin.before(photo.getDate()) && photo.getDate().before(end)) {

				goodPhotos.add(photo);

			}
		}

		Photo[] sorted = sortPhotos(goodPhotos.toArray(new Photo[goodPhotos
				.size()]));

		for (Photo photo : sorted) {

			String photoAlbumsString = "";
			Album[] albums = photo.getAlbums();

			// #########################################
			// System.out.println(albums.toString());
			for (Album album : albums) {
				if (photoAlbumsString.equals("")) {
					photoAlbumsString = album.getName();
				} else {
					photoAlbumsString = photoAlbumsString + ", "
							+ album.getName();
				}

			}

			toReturn.add(photo.getCaption() + " - Album: " + photoAlbumsString
					+ " - Date: " + photo.getTimeString());
		}

		return toReturn.toArray(new String[toReturn.size()]);

	}

	/**
	 * Get the photos from all albums that were taken between the dates
	 * 
	 * @param Begin
	 *            date to search for
	 * @param End
	 *            date to search for
	 * @return String array of pre-formatted photo details
	 */

	public Photo[] getPhotosObjsByDate(Date begin, Date end) {

		ArrayList<String> toReturn = new ArrayList<String>();

		ArrayList<Photo> goodPhotos = new ArrayList<Photo>();
		Photo[] photos = model.getAllPhotos();

		// Debugging

		/*
		 * Date date = new Date(); date = photos[1].getDate();
		 * date.setMinutes(30);
		 * 
		 * photos[1].setDate(date);
		 */

		for (Photo photo : photos) {

			// Debugging
			/*
			 * System.out.println(begin.toString() + ", " + end.toString() +
			 * ", " + photo.getDate().toString());
			 */

			if (begin.before(photo.getDate()) && photo.getDate().before(end)) {

				goodPhotos.add(photo);

			}
		}

		Photo[] sorted = sortPhotos(goodPhotos.toArray(new Photo[goodPhotos
				.size()]));

		return sorted;

	}

	/**
	 * Get the photo details of all photos that contain all of the supplied tags
	 * 
	 * @param String
	 *            array of correctly formatted tags
	 * @return String array of pre-formatted photo details
	 */
	public String[] getPhotosByTags(String[] tags) {

		// <caption> - Album: <albumName>[,<albumName>]... - Date: <date>

		ArrayList<String> toReturn = new ArrayList<String>();
		ArrayList<Photo> goodPhotos = new ArrayList<Photo>();

		Photo[] photos = model.getAllPhotos();

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
		for (Photo photo : sorted) {

			String photoAlbumsString = "";
			Album[] albums = photo.getAlbums();

			// #########################################
			// System.out.println(albums.toString());

			for (Album album : albums) {
				if (photoAlbumsString.equals("")) {
					photoAlbumsString = album.getName();
				} else {
					photoAlbumsString = photoAlbumsString + ", "
							+ album.getName();
				}

			}

			toReturn.add(photo.getCaption() + " - Album: " + photoAlbumsString
					+ " - Date: " + photo.getTimeString());

		}
		return toReturn.toArray(new String[toReturn.size()]);
		/**
		 * Removed because of a design change, users now have an array of all
		 * photos added to their albums
		 **/

		/*
		 * ArrayList<String> toReturn = new ArrayList<String>();
		 * 
		 * Album[] albums = model.getAlbums();
		 * 
		 * // Loop through albums that current user has for (int i = 0; i <
		 * albums.length; i++) {
		 * 
		 * Photo[] photos = albums[i].getPhotos();
		 * 
		 * // Loop through photos in the album for (int y = 0; y <
		 * photos.length; i++) {
		 * 
		 * // Loop through input tags for (int g = 0; g < tags.length; g++) {
		 * 
		 * String[] tagParts = parseTag(tags[g]); // Check if photo has the tag
		 * if (photos[y].hasTag(tagParts[0], tagParts[1])) {
		 * 
		 * // make the string to return
		 * 
		 * }
		 * 
		 * }
		 * 
		 * } }
		 * 
		 * return toReturn.toArray(new String[toReturn.size()]);
		 */
	}

	/**
	 * Get the photo details of all photos that contain all of the supplied tags
	 * 
	 * @param String
	 *            array of correctly formatted tags
	 * @return String array of pre-formatted photo details
	 */
	public Photo[] getPhotoObjsByTags(String[] tags) {

		// <caption> - Album: <albumName>[,<albumName>]... - Date: <date>

		ArrayList<String> toReturn = new ArrayList<String>();
		ArrayList<Photo> goodPhotos = new ArrayList<Photo>();

		Photo[] photos = model.getAllPhotos();

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
	 * Sort the inputed photos in chronologically order
	 * 
	 * @param photos
	 *            array of the photos to sort
	 * @return Sorted photos array
	 */
	public Photo[] sortPhotos() {

		Photo[] photos = getAllPhotos();
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

	/**
	 * Moves file from one album to another
	 * 
	 * @param filename
	 *            of photo to move
	 * @param oldAlbum
	 *            to move photo from
	 * @param newAlbum
	 *            to move photo to
	 */
	public void movePhoto(String filename, AlbumEditorController oldAlbum,
			AlbumEditorController newAlbum) {

		Photo toMove = oldAlbum.getPhoto(filename);

		if (oldAlbum.removePhoto(toMove)) {
			if (newAlbum.addPhoto(toMove)) {

				toMove.removeAlbum(oldAlbum.getAlbum());
				toMove.addAlbum(newAlbum.getAlbum());
			}
		}
	}

	/**
	 * Return all photo objects owned by model user
	 * 
	 * @return Photo array of all users Photos
	 */
	public Photo[] getAllPhotos() {
		return model.getAllPhotos();
	}

	/**
	 * Return all thumbnails for all photos of this user
	 * 
	 * @return File array of paths
	 */
	public File[] getAllThumbnails() {
		ArrayList<File> thumbFiles = new ArrayList<File>();
		for (Photo photo : getAllPhotos()) {
			thumbFiles.add(photo.getThumbnail());
		}

		return thumbFiles.toArray(new File[thumbFiles.size()]);
	}

	/**
	 * Return all thumbnails for all photos array passed in
	 * 
	 * @param Photos
	 *            array to get the thumbnails for
	 * @return File array of paths
	 */
	public File[] getThumbnails(Photo[] photos) {
		ArrayList<File> thumbFiles = new ArrayList<File>();
		for (Photo photo : photos) {
			thumbFiles.add(photo.getThumbnail());
		}

		return thumbFiles.toArray(new File[thumbFiles.size()]);
	}

	public int numPhotos() {

		return getAllPhotos().length;
	}

	public BufferedImage[] getAlbumThumbnails(int density, int width, int height)
			throws IOException {

		Album[] albums = getUserAlbums();
		int numAlbums = albums.length;

		BufferedImage[] photoIcons = new BufferedImage[numAlbums];

		for (int i = 0; i < numAlbums; i++) {

			Photo[] photoObjs = albums[i].getPhotos();

			// System.out.println(photoObjs.length);

			BufferedImage[] photos = new BufferedImage[photoObjs.length];

			for (int j = 0; j < photos.length; j++) {

				photos[j] = ImageIO.read(photoObjs[j].getFilePath());

			}

			// Change these magic numbers
			photoIcons[i] = ImageUtilities.createAlbumThumbnail(photos,
					density, width, height);

		}

		return photoIcons;
	}

	public Photo getPhotoByIndex(int index) {

		return model.getAllPhotos()[index];

	}

	public Album getAlbumByIndex(int index) {

		return model.getAlbums()[index];

	}

	public File[] getAllPhotoPaths() {

		Photo[] photos = getAllPhotos();
		File[] paths = new File[photos.length];

		int x = 0;
		for (Photo photo : photos) {
			paths[x] = photo.getFilePath();

			x++;
		}

		return paths;

	}

	/**
	 * Get an Array of all of the photos years in sorted order
	 * 
	 * @return Int array of sequential years of each photo
	 */
	public int[] getYearsArray() {

		Photo[] photo = sortPhotos();
		int[] yearsArray = new int[photo.length];
		for (int i = 0; i < photo.length; i++) {

			yearsArray[i] = photo[i].getCalendar().get(Calendar.YEAR);
		}

		return yearsArray;
	}

	public boolean removePhoto(Photo photo) {

		for (Album album : photo.getAlbums()) {

			album.removePhoto(photo);
		}
		return model.removePhoto(photo);
	}
	
}
