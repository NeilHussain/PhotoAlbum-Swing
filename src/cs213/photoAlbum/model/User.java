/* Neil Hussain 
 * Matt Kaiser
 */
package cs213.photoAlbum.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class User implements Serializable {

	private static ArrayList<User> users = new ArrayList<User>(10);

	private ArrayList<Album> albums = new ArrayList<Album>();
	private ArrayList<Photo> allPhotos = new ArrayList<Photo>();
	private String userID;
	private String fullName;

	public User(String userID, String fullName) {

		this.userID = userID;
		this.fullName = fullName;

	}

	/**
	 * Get the user ID
	 * 
	 * @return String user ID
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * Give user a new ID
	 * 
	 * @param userID
	 *            String of new ID
	 * @return true if successful, false otherwise
	 */
	public boolean setUserID(String userID) {

		if (userID != null) {
			this.userID = userID;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get user's full name
	 * 
	 * @return String of user's name
	 */
	public String getFullName() {
		return this.fullName;
	}

	/**
	 * Give user a new full name
	 * 
	 * @param name
	 *            String of new name
	 * @return true if successful, false otherwise
	 */
	public boolean setFullName(String name) {

		if (name != null) {
			this.fullName = name;
			return true;
		}
		return false;
	}

	/**
	 * Get a list of albums this user has
	 * 
	 * @return Array of <code>Album</code>s
	 */
	public Album[] getAlbums() {
		return albums.toArray(new Album[albums.size()]);
	}

	public Album getAlbum(String albumName) {

		for (int i = 0; i < albums.size(); i++) {
			if (albums.get(i).getName().equals(albumName)) {
				return albums.get(i);
			}
		}

		return null;
	}

	/**
	 * Add an album to this user's list of albums
	 * 
	 * @param album
	 *            <code>Album</code> object to add
	 * @return true if successful, false otherwise
	 */
	public boolean addAlbum(Album album) {
		return albums.add(album);
	}

	/**
	 * Rename an album
	 * 
	 * @param album
	 *            <code>Album</code> object to rename
	 * @param name
	 *            String of new name
	 * @return true if successful, false otherwise
	 */
	public boolean renameAlbum(Album album, String name) {

		if (album != null && !name.equals("")) {

			albums.get(albums.indexOf(album)).setName(name);
			return true;
		}

		return false;
	}

	/**
	 * Remove an album from this user's list of albums
	 * 
	 * @param album
	 *            <code>Album</code> object to remove
	 * @return true if successful, false otherwise
	 */
	public boolean removeAlbum(Album album) {

		return albums.remove(album);

	}

	public boolean addPhoto(Photo photoToAdd) {

		// If the photo already exists, just add the album, if the photo doesn't
		// exist add it to the photos arraylist

		boolean found = false;
		for (Photo photo : allPhotos) {

			if (photoToAdd.getFilePath().equals(photo.getFilePath())) {
				found = true;
				photo.addAlbum(photoToAdd.getAlbums()[0]);
				for (Album album : albums) {

					if (album.getName().equals(photoToAdd.getAlbums()[0])) {
						System.out.println("here");
						album.addPhoto(photo);
					}

				}

			}

		}
		if (found == false) {
			return allPhotos.add(photoToAdd);

		} else {
			return true;

		}
	}

	public Photo[] getAllPhotos() {

		return allPhotos.toArray(new Photo[allPhotos.size()]);

	}

	/**
	 * Compare an User to this user object
	 * 
	 * @param userToCompare
	 *            User to compare to this user object
	 * @return true if successful, false otherwise
	 */
	public boolean equals(User userToCompare) {

		if (this.userID == userToCompare.getUserID()
				&& this.fullName == userToCompare.getFullName()) {
			return true;
		}

		return false;
	}

	public static boolean save() {
		FileOutputStream fout;
		ObjectOutputStream oos;
		try {
			fout = new FileOutputStream("data" + File.separator + "users.dat");
			oos = new ObjectOutputStream(fout);
			oos.writeObject(users);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings({ "resource", "unchecked" })
	public static boolean load() {
		FileInputStream fin;
		ObjectInputStream ois;
		try {
			fin = new FileInputStream("data" + File.separator + "users.dat");
			ois = new ObjectInputStream(fin);
			users = (ArrayList<User>) ois.readObject();
		} catch (FileNotFoundException e) {
			users = new ArrayList<User>(10);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Static methods

	/**
	 * Get a list of user IDs that are saved in data
	 * 
	 * @return User array of user IDs
	 */
	public static User[] getUsers() {
		return users.toArray(new User[users.size()]);
	}

	/**
	 * Loads a <code>User</code> object into memory from a serialized file based
	 * on the userID
	 * 
	 * @param userID
	 *            the String user ID of the user to load
	 * @return User object loaded
	 */
	public static User login(String userID) {
		return getUser(userID);
	}

	/**
	 * Get the use object of the userID
	 * 
	 * @param userID
	 *            to get
	 * @return User object with user ID
	 */
	public static User getUser(String userID) {

		for (int i = 0; i < users.size(); i++) {

			if (users.get(i).getUserID().equals(userID)) {

				return users.get(i);

			}

		}

		return null;

	}

	/**
	 * Add a user to the program
	 * 
	 * @param user
	 *            to add
	 * @return true on success, false otherwise
	 */
	public static boolean add(User user) {

		return users.add(user);

	}

	/**
	 * Delete a user file from data
	 * 
	 * @param userID
	 *            the String user ID of the user to load
	 * @return true if successful, false otherwise
	 */
	public static boolean delete(String userID) {

		return users.remove(getUser(userID));

	}

	/**
	 * Gets all the users from memory
	 * 
	 */
	public static void loadUsers() {

		// grab the users/info from the save file and load them in.
		users.add(new User("testuser", "Neil Hussain"));

	}

	/**
	 * Removes photo from the array
	 * 
	 * @param photo
	 *            to remove
	 * @return true on success, false otherwise
	 */
	public boolean removePhoto(Photo photo) {

		return allPhotos.remove(photo);

	}

}
