package cs213.photoAlbum.view;

public interface View {

	/**
	 * Updates the values of corresponding variables related to photo objects
	 * and refreshes the screen properly
	 */
	public void refreshPhotoView();

	/**
	 * Updates the values of corresponding variables related to albums objects
	 * and refreshes the screen properly
	 */
	public void refreshAlbumView();

	/**
	 * Updates the values of corresponding variables related to User objects and
	 * refreshes the screen properly
	 */
	public void refreshUserView();

	/**
	 * List all users in system
	 */
	public static void listUsers() {
	}

	/**
	 * Create a new user
	 */
	public static void newUser() {
	}

	/**
	 * Delete a user
	 */
	public static void deleteUser() {
	}

	/**
	 * Login a user (switch to interactive mode)
	 */
	public static void login() {
	}

	/**
	 * Create an album
	 */
	public static void createAlbum() {
	}

	/**
	 * Delete an album
	 */
	public static void deleteAlbum() {
	}

	/**
	 * List all albums
	 */
	public static void listAlbums() {
	}

	/**
	 * List all photos
	 */
	public static void listPhotos() {
	}

	/**
	 * Add Photo
	 */
	public static void addPhoto() {
	}

	/**
	 * Move photo to different album
	 */
	public static void movePhoto() {
	}

	/**
	 * Remove a photo
	 */
	public static void removePhoto() {
	}

	/**
	 * Add a tag
	 */
	public static void addTag() {
	}

	/**
	 * Delete a tag
	 */
	public static void deleteTag() {
	}

	/**
	 * List photo information
	 */
	public static void listPhotoInfo() {
	}

	/**
	 * List photos ordered by date
	 */
	public static void getPhotosByDate() {
	}

	/**
	 * List photos ordered by tag
	 */
	public static void getPhotosByTag() {
	}

	/**
	 * Logout user (switch to basic mode)
	 */
	public static void logout() {
	}

	/**
	 * Report an error
	 */
	public static void reportError() {
	}

}