/* Neil Hussain 
 * Matt Kaiser
 */
package cs213.photoAlbum.control;

import cs213.photoAlbum.model.User;
import cs213.photoAlbum.view.View;

public class BackendController {

	private View view;

	public BackendController() {
		User.load();
		// User.loadUsers();
	}

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

	public boolean save() {
		// save the users to a file some that they can be loaded again.
		// Do we have to save the albums/photos too? Or will they be saved
		// automatically?
		User.save();
		// System.out.println("here");

		return false;
	}

	public boolean deleteUser(String user) {

		if (getUsers().contains(user)) {

			return User.delete(user);

		} else {

			return false;
		}
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

}
