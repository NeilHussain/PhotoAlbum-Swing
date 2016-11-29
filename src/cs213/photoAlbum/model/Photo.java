/* Neil Hussain 
 * Matt Kaiser
 */
package cs213.photoAlbum.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cs213.photoAlbum.util.ImageUtilities;

import javax.imageio.ImageIO;
import javax.tools.FileObject;

@SuppressWarnings("serial")
public class Photo implements Serializable {

	static String containerFolder = "data/thumbnails";
	private File filename;
	// private File filePath;
	private File thumbnailFile;
	private String caption;
	private Calendar calendar;
	private ArrayList<Tag> tags = new ArrayList<Tag>();
	private ArrayList<Album> allAlbums = new ArrayList<Album>();
	static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/y-kk:mm:ss",
			Locale.US);
	private String name;

	public Photo(Album album) {

		// System.out.println("lastModifiedTime: " + attr.lastModifiedTime());

		calendar = Calendar.getInstance();
		calendar.set(Calendar.MILLISECOND, 0);

		this.addAlbum(album);
		// calendar = new Calendar();
		// calendar.set(Calendar.MILLISECOND, 0);

	}

	/**
	 * Find the photo in the directories and set the filename of the photo
	 * object as well as the edited date
	 * 
	 * @param filename
	 *            to search for
	 * @return true if the file was found, false otherwise
	 */
	public boolean loadPhoto(String filename) {

		Path p = Paths.get(filename);

		File file = new File(p.toString());

		// System.out.println(file.getAbsolutePath());

		if (file.exists()) {
			new Date(file.lastModified());
			calendar.setTime(new Date(file.lastModified()));

			this.filename = file;
			return true;

		}

		return false;
	}

	/**
	 * Get photo's caption
	 * 
	 * @return String caption of photo
	 */
	public String getCaption() {
		return this.caption;
	}

	/**
	 * Get photo's filename
	 * 
	 * @return String filename of photo
	 */
	public String getFilename() {

		// System.out.println(filename.toString());
		return filename.getName().toString();
	}

	/**
	 * Get photo's date
	 * 
	 * @return String date of photo
	 */
	public Date getDate() {
		return calendar.getTime();
	}

	/**
	 * Get photo's time
	 * 
	 * @return String time of photo
	 */
	public String getTime() {
		return null;
	}

	/**
	 * Get photo's tags.
	 * 
	 * @return Array of tags
	 */
	public Tag[] getTags() {

		return this.tags.toArray(new Tag[tags.size()]);
	}

	/**
	 * Give photo a new caption
	 * 
	 * @param caption
	 *            String
	 * @return true if successful, false otherwise
	 */
	public boolean setCaption(String caption) {

		return (this.caption = caption) != null;

	}

	/**
	 * Give photo a different location/filename.
	 * 
	 * @param filename
	 *            String location of file
	 * @return true if successful, false otherwise
	 */
	public boolean setFilename(String filename) {

		File file = new File(filename);

		try {
			this.filename = file.getCanonicalFile();
			return true;
		} catch (IOException e) {

			e.printStackTrace();
			return false;

		}
	}

	/**
	 * Give photo a new date
	 * 
	 * @param date
	 *            object
	 * @return true if successful, false otherwise
	 */
	public void setDate(Date date) {
		calendar.setTime(date);
	}

	/**
	 * Give photo a new time
	 * 
	 * @param time
	 *            String of new time
	 * @return true if successful, false otherwise
	 */
	public boolean setTime(String time) {

		// Date dateTime = null;

		// Try to parse time (what format is it in?)

		// calendar.setTime(sdf.parse("time"));

		/*
		 * if (dateTime != null) { calendar.setTime(dateTime); return true; }
		 * else {
		 * 
		 * return false; }
		 */
		return false;
	}

	/**
	 * Get the formatted string of the time
	 * 
	 * @return String of the last modified time
	 */
	public String getTimeString() {

		return sdf.format(calendar.getTime());
	}

	/**
	 * Get the calendar object
	 * 
	 * @return Calendar object of the photo
	 */
	public Calendar getCalendar() {
		return calendar;
	}

	/**
	 * Set the calendar of the photo to the current time
	 * 
	 * @param timeString
	 */
	public void setCalendarToCurrentTime() {

		calendar = Calendar.getInstance();
		calendar.set(Calendar.MILLISECOND, 0);

	}

	/**
	 * Set the calendar of the photo to inputed string's date
	 * 
	 * @param true on success, false otherwise
	 */
	public boolean setCalendar(String timeString) {

		try {
			calendar.setTime(sdf.parse(timeString));
		} catch (ParseException e) {

			return false;

		}

		return true;
	}

	/**
	 * Set the calendar object of the photo
	 * 
	 * @param calendar
	 *            to set to
	 */
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	/**
	 * Get the full path of the photo
	 * 
	 * @return Photo file
	 */
	public File getFilePath() {

		try {
			return filename.getCanonicalFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Add new Tag to photo
	 * 
	 * @param type
	 *            String
	 * @param value
	 *            String
	 * @return true if successful, false otherwise
	 */
	public boolean addTag(String type, String value) {
		return this.addTag(new Tag(type, value));
	}

	/**
	 * Add new tag to photo, with an already created <code>Tag</code> object
	 * 
	 * @param tag
	 *            <code>Tag</code> object
	 * @return true if successful, false otherwise
	 */
	public boolean addTag(Tag tag) {

		boolean found = false;
		for (int i = 0; i < tags.size(); i++) {

			if (tags.get(i).equals(tag)) {

				found = true;

			}

		}

		if (found) {
			return false;

		} else {

			this.tags.add(tag);
			return true;
		}

		// return this.tags.add(tag);
	}

	/**
	 * Remove a tag from the photo based on type and value
	 * 
	 * @param type
	 *            String
	 * @param value
	 *            String
	 * @return true if successful, false otherwise
	 */
	public boolean removeTag(String type, String value) {
		return removeTag(new Tag(type, value));
		// legacy code, returning int in the same pattern as addtag()
		/*
		 * int index = -1;
		 * 
		 * for (int i = 0; i < tags.size(); i++) { if (tags.get(i).getType() ==
		 * type && tags.get(i).getValue() == value) index = i; }
		 * 
		 * if (index != -1) { this.tags.remove(index); return true; } else {
		 * return false; }
		 */
	}

	/**
	 * Remove a tag from the photo
	 * 
	 * @param tag
	 *            <code>Tag</code> object to remove
	 * @return true if successful, false otherwise
	 */
	public boolean removeTag(Tag tag) {

		for (int i = 0; i < tags.size(); i++) {

			if (tags.get(i).equals(tag)) {

				//System.out.println("Perform remove");
				return this.tags.remove(getTag(tag));

			}

		}

		return false;

		// Don't need this, need more error checking
		// return this.tags.remove(tags.indexOf(tag)) != null;
	}

	/**
	 * Get actual tag object of the photo
	 * 
	 * @param tag
	 *            that belongs to the photo
	 * @return tag of the photo
	 */
	private Tag getTag(Tag tag) {
		for (int i = 0; i < tags.size(); i++) {

			if (tags.get(i).equals(tag)) {

				return tags.get(i);
			}

		}

		return null;
	}

	/**
	 * Get tag based on type and value
	 * 
	 * @param String
	 *            type of tag
	 * @param String
	 *            value of tag
	 * @return tag of the photo
	 */
	private Tag getTag(String type, String value) {

		return getTag(new Tag(type, value));
	}

	public boolean equals(Photo photoToCompare) {

		if (photoToCompare.getFilename().equals(this.getFilename())
				&& photoToCompare.getCaption().equals(this.getCaption())) {

			return true;
		}

		return false;

	}

	/**
	 * Check of the photo has a give tag based on tag type and value
	 * 
	 * @param String
	 *            type of tag
	 * @param String
	 *            value of tag
	 * @return true if found, false otherwise
	 */
	public boolean hasTag(String type, String value) {

		for (int i = 0; i < tags.size(); i++) {

			if (tags.get(i).equals(new Tag(type, value))) {

				return true;
			}

		}

		return false;
	}

	/**
	 * Remove album from the user
	 * 
	 * @param Album
	 *            to remove
	 * @return true on success, false otherwise
	 */
	public boolean removeAlbum(Album albumToRemove) {

		for (Album album : allAlbums) {

			if (album.equals(albumToRemove)) {

				return allAlbums.remove(album);

			}

		}

		return false;
	}

	/**
	 * Get a pre-formatted string of all the tags of a photo
	 * 
	 * @return String of all the tags
	 */
	public String tagsToString() {
		String toReturn = "";

		for (Tag tag : tags) {

			if (toReturn.equals("")) {

				toReturn = tag.getType() + ":\"" + tag.getValue() + "\"";

			} else {

				toReturn = toReturn + ", " + tag.getType() + ":\""
						+ tag.getValue() + "\"";
			}

		}

		return toReturn;
	}

	/**
	 * Get an array of all the albums the user has
	 * 
	 * @return Album array of all the albums
	 */
	public Album[] getAlbums() {
		return allAlbums.toArray(new Album[allAlbums.size()]);
	}

	/**
	 * Add album to this user
	 * 
	 * @param album
	 *            to add
	 * @return true on success, false otherwise
	 */
	public boolean addAlbum(Album album) {
		return allAlbums.add(album);
	}

	/**
	 * Get the photo's thumbnail
	 * @return File object of the photo's thumbnail
	 */
	public File getThumbnail() {
		try {
			return thumbnailFile.getCanonicalFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Set the photos thumbnail
	 * @param width of the new thumbnail
	 * @param height of the new thumbnail
	 */
	public void setThumbnail(int width, int height) {

		// System.out.println("Got here");
		try {

			File path = new File(containerFolder + "/thumbnail+"
					+ this.filename.getName());
			path = path.getCanonicalFile();
			//System.out.println(path.toString());

			BufferedImage photo = ImageIO.read(this.filename);
			ImageUtilities.makePhotoThumbnail(photo, path, width, height);
			this.thumbnailFile = new File(containerFolder + "/thumbnail+"
					+ this.filename.getName());

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Get the user's name for the photo
	 * @return String, photo's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the photo's name
	 * @param String name to name the photo
	 */
	public void setName(String name) {
		this.name = name;
	}

}