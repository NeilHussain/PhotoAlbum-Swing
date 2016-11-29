/* Neil Hussain 
 * Matt Kaiser
 */
package cs213.photoAlbum.control;

import cs213.photoAlbum.model.User;
import cs213.photoAlbum.view.View;

public class BackendController {

	private View view;

	/**
	 * Create a backend
	 */
	public BackendController() {
		User.load();
		// User.loadUsers();
	}

	/**
	 * Get the users of the program
	 * @return String of all the users
	 */
	public String getUsers() {

		String userList = "";

		User[] list = User.getUsers();

		if (list != null) {

			// Convert to list to string to be displayed by the view
			for (int i = 0; i < list.length; i++) {

				if (userList.equals("")) {

					userList = list[i].getUserID();
				} else {
					userList = userList + "\n" + list[i].getUserID();
				}

			}
			return userList;
		}

		return "no users exist";

	}

	/**
	 * Add a user to the program
	 * @param userID to add
	 * @param userName to add
	 * @return true if add, false otherwise
	 */
	public boolean addUser(String userID, String userName) {

		// do error checking on the user that should be added
		// can't have the same user ID!
		if (userName.charAt(0) == '"'
				&& userName.charAt(userName.length()) == '"') {
			userName = userName.substring(1, userName.length() - 1);
		}

		if (getUsers() != null && getUsers().contains(userID)) {
			return false;
		} else {

			User newUser = new User(userID, userName);
			return User.add(newUser);

		}

	}

	/**
	 * Log a user in
	 * @param userID to log in
	 * @return true if logged in, false otherwise
	 */
	public User loginUser(String userID) {

		if (userID.charAt(0) == '"' && userID.charAt(userID.length()) == '"') {
			userID = userID.substring(1, userID.length() - 1);
		}

		String users = getUsers();

		if (users != null && users.contains(userID)) {
			// log user in
			User logged = User.login(userID);

			return logged;
		} else {

			return null;
		}

	}

	/**
	 * Save the user data
	 * @return true if saved, false otherwise
	 */
	public boolean save() {
		// save the users to a file some that they can be loaded again.
		// Do we have to save the albums/photos too? Or will they be saved
		// automatically?
		User.save();
		// System.out.println("here");

		return false;
	}

	/**
	 * Delete a user
	 * @param user to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteUser(String user) {

		if (getUsers().contains(user)) {

			return User.delete(user);

		} else {

			return false;
		}
	}

	/**
	 * Get the view associated with this controller
	 * @return View object 
	 */
	public View getView() {
		return view;
	}

	/**
	 * Set the view associated with this controller
	 * @param View object 
	 */
	public void setView(View view) {
		this.view = view;
	}

}
