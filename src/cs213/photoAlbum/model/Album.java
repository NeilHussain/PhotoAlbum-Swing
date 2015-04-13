/* Neil Hussain 
 * Matt Kaiser
 */
package cs213.photoAlbum.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Album implements Serializable {

	private ArrayList<Photo> photos = new ArrayList<Photo>();
	private String name;
	private Date earliest;
	private Date latest;
	private String caption;

	public Album(String name) {

		// photos = new ArrayList<Photo>(10);
		this.name = name;

	}

	/**
	 * Add photo to album
	 * 
	 * @param photo
	 *            <code>Photo</code> object to add
	 * @return true if successful, false otherwise
	 */
	public boolean addPhoto(Photo photo) {

		if (earliest == null && latest == null) {
			earliest = photo.getDate();
			latest = photo.getDate();
		} else if (photo.getDate().before(earliest)) {
			earliest = photo.getDate();
		} else if (photo.getDate().after(latest)) {
			latest = photo.getDate();
		}
		return photos.add(photo);

	}

	/**
	 * Remove photo from album
	 * 
	 * @param photo
	 *            <code>Photo</code> object to remove
	 * @return true if successful, false otherwise
	 */
	public boolean removePhoto(Photo photo) {

		return photos.remove(photo);

	}

	/**
	 * Give specified photo a new caption
	 * 
	 * @param photo
	 *            <code>Photo</code> object to re-caption
	 * @param caption
	 *            String of new caption
	 * @return true if successful, false otherwise
	 */
	public boolean recaptionPhoto(Photo photo, String caption) {

		return photos.get(photos.indexOf(photo)).setCaption(caption);

	}

	/**
	 * Get the name of the album
	 * 
	 * @return Name of the album
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the album name
	 * 
	 * @param name
	 *            to name the album
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get number of photos in the album
	 * 
	 * @return int size of album
	 */
	public int size() {
		return photos.size();
	}

	/**
	 * Get an array of photos
	 * 
	 * @return Array of photos
	 */
	public Photo[] getPhotos() {
		return photos.toArray(new Photo[photos.size()]);
	}

	/**
	 * Gets the photo object based on the filename
	 * 
	 * @param String
	 *            of the filename
	 * @return Photo object with that filename
	 */
	public Photo getPhoto(String filename) {

		for (int i = 0; i < photos.size(); i++) {
			if (photos.get(i).getFilename().equals(filename)) {
				return photos.get(i);
			}

		}

		return null;
	}

	/**
	 * Get components of the photo info list (Legacy)
	 * 
	 * @param filename
	 *            to get the info for
	 * @return String [] of components
	 */
	public String[] listInfo(String filename) {
		String[] info = new String[5];

		Photo photo = getPhoto(filename);

		if (photo != null) {

			info[0] = filename;
			info[1] = this.getName() + "[," + this.getName() + "]";
			info[2] = photo.getTimeString();
			info[3] = photo.getCaption();

			Tag[] tags = photo.getTags();
			for (int i = 0; i < tags.length; i++) {
				info[4] = info[4] + tags[i].toString();
			}

		}

		return info;

	}

	/**
	 * Change the photo array list
	 * 
	 * @param ArrayList
	 *            of new photos
	 */
	public void setPhotos(ArrayList<Photo> photos) {
		this.photos = photos;
	}

	/**
	 * Compare this album to another
	 * 
	 * @param albumToCompare
	 *            to the current album
	 * @return true if the albums are the same, false otherwise
	 */
	public boolean equals(Album albumToCompare) {

		if (getName().equals(albumToCompare.getName())) {

			return true;
		}

		return false;
	}

	/**
	 * Get the earliest taken photo's date
	 * 
	 * @return Date of the earliest photo
	 */
	public Date getEarliest() {
		return earliest;
	}

	/**
	 * Set the earliest photo date
	 * 
	 * @param earliest
	 *            date to change to
	 */
	public void setEarliest(Date earliest) {
		this.earliest = earliest;
	}

	/**
	 * Get the latest taken photo's date
	 * 
	 * @return Date of the latest photo
	 */
	public Date getLatest() {
		return latest;
	}

	/**
	 * sSet the latest photo date
	 * 
	 * @param latest
	 *            date to change to
	 */
	public void setLatest(Date latest) {
		this.latest = latest;
	}
	
	public void setCaption(String caption){
	this.caption = caption;
	}
	
	public String getCaption(){
	
	return this.caption;
	}

}
