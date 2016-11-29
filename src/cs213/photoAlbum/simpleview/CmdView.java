/* Neil Hussain 
 * Matt Kaiser
 */
package cs213.photoAlbum.simpleview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cs213.photoAlbum.control.AlbumEditorController;
import cs213.photoAlbum.control.BackendController;
import cs213.photoAlbum.control.PhotoEditorController;
import cs213.photoAlbum.control.UserEditorController;
import cs213.photoAlbum.model.User;
import cs213.photoAlbum.view.View;

public class CmdView implements View {

	static BackendController backend = new BackendController();
	static UserEditorController user;
	static AlbumEditorController album;
	static PhotoEditorController photo;
	static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/y-kk:mm:ss",
			Locale.US);

	public static void main(String[] args) {

		if (args.length > 0) {
			switch (args[0]) {

			case ("listusers"):

				listUsers();

				break;
			case ("adduser"):

				if (args.length != 3) {
					reportError("Please provide a user ID and full name for the user");
					System.exit(-1);

				} else {
					newUser(args[1], args[2]);
				}
				break;
			case ("deleteuser"):

				if (args.length != 2) {
					reportError("Please specify a user to delete.");
				} else {

					deleteUser(args[1]);
				}

				break;
			case ("login"):

				if (args.length == 2) {
					login(args[1]);
				} else {
					reportError("Please specify a user to login");

				}
				break;
			case ("build"):
				newUser("Matt", "Matt");
				login("Matt");
				createAlbum("Six Flags");
				addPhoto("kingda.jpg", "Kingda Ka", "Six Flags");
				addPhoto("medusa.jpg", "Medusa", "Six Flags");

				addTag("kingda.jpg", "type:coaster");
				addTag("medusa.jpg", "type:coaster");
				createAlbum("Disney");
				logout();

				newUser("Neil", "Neil");

				login("Neil");

				createAlbum("bmw");

				pl("");

				addPhoto("1m.png", "1M Pic", "bmw");

				pl("");

				addPhoto("335.jpg", "335 Pic", "bmw");
				pl("");
				addPhoto("m4.jpg", "Bmw M4", "bmw");
				pl("");
				createAlbum("Porsche");
				addPhoto("918.jpg", "918 Super Car", "Porsche");
				pl("");
				addPhoto("2015m4.jpg", "New M4 concept!", "bmw");
				pl("");
				createAlbum("McLaren");
				pl("");
				addPhoto("p1.jpg", "Beautiful P1!", "McLaren");
				pl("");
				createAlbum("Lamborghini");
				pl("");
				addPhoto("lambo.jpg", "White lambo", "Lamborghini");
				pl("");
				addTag("lambo.jpg", "pic:car");
				addTag("335.jpg", "pic:car");
				addTag("1m.png", "pic:car");
				addTag("m4.jpg", "pic:car");
				addTag("2015m4.jpg", "pic:car");
				addTag("p1.jpg", "pic:car");
				addPhoto("veneno.jpg", "Red lambo Veneno", "Lamborghini");

				addTag("veneno.jpg", "pic:car");
				addTag("918.jpg", "pic:car");
				addTag("918.jpg", "car:grey");
				addTag("918.jpg", "type:supercar");
				addTag("p1.jpg", "type:supercar");
				addTag("veneno.jpg", "type:supercar");

				pl("");

				addTag("335.jpg", "car:red");
				addTag("veneno.jpg", "car:red");
				addTag("p1.jpg", "car:orange");
				pl("");
				addTag("car:grey", "pic:gray");
				pl("");
				addTag("335.jpg", "car:red");
				pl("");
				addTag("lambo.jpg", "car:white");
				pl("");
				addTag("1m.png", "car:red");
				pl("");
				logout();

				System.exit(0);
				break;
			default:
				reportError("Command not recognized");
				System.exit(-1);
				break;
			}
		} else {

			reportError("No arguments given");
			System.exit(-1);

		}
	}

	@Override
	public void refreshPhotoView() {

	}

	@Override
	public void refreshAlbumView() {

	}

	@Override
	public void refreshUserView() {

	}

	public static void listUsers() {

		pl(backend.getUsers());
	}

	public static void newUser(String userID, String userName) {

		if (backend.addUser(userID, userName)) {
			pl("created user " + userID + " with name " + userName);
			backend.save();

		} else {

			pl("user " + userID + " already exists with name " + userName);

		}

	}

	public static void deleteUser(String userID) {

		if (backend.deleteUser(userID)) {

			System.out.println("deleted user " + userID);
			backend.save();
		} else {

			pl("user " + userID + " does not exist");
		}

	}

	public static void login(String userID) {

		user = new UserEditorController(backend.loginUser(userID));

		if (user != null) {

			/*
			 * try { //InteractiveView(logged); } catch (IOException e) {
			 * e.printStackTrace(); }
			 */

		} else {
			System.out.println("user " + userID + " does not exist");
		}
	}

	private static void InteractiveView(User logged) throws IOException {

		user = new UserEditorController(logged);

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String input = null;

		while (!(input = in.readLine()).trim().equals("logout")) {

			if (!input.equals("")) {
				// List<String> list = new ArrayList<String>();

				String[] inputTokens = trimInputs(input
						.split("\"?( |$)(?=(([^\"]*\"){2})*[^\"]*$)\"?"));

				// Matcher m =
				// Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(
				// input);

				/*
				 * boolean addTag = false;
				 * 
				 * while (m.find()) { list.add(m.group(1));
				 * 
				 * if (list.get(0).equals("addTag")) { addTag = true; } }
				 * 
				 * String[] inputTokens = trimInputs(list.toArray(new
				 * String[list .size()]));
				 * 
				 * if (addTag == true) {
				 * 
				 * if (inputTokens.length > 3) {
				 * 
				 * for (int i = 0; i < inputTokens.length - 3; i++) {
				 * 
				 * inputTokens[2] = inputTokens[2] + " " + inputTokens[3 + i];
				 * 
				 * inputTokens[3 + i] = null; }
				 * 
				 * }
				 * 
				 * System.out.println(inputTokens[2] + " Length: " +
				 * inputTokens.length);
				 * 
				 * }
				 */

				switch (inputTokens[0]) {

				case ("createAlbum"):

					if (inputTokens.length == 2) {

						createAlbum(inputTokens[1]);
					} else {
						reportError("Please provide a \"<name>\".");
					}

					break;
				case ("deleteAlbum"):

					if (inputTokens.length == 2) {

						deleteAlbum(inputTokens[1]);
					} else {
						reportError("Please provide  \"<album name>\".");
					}

					break;

				case ("listAlbums"):

					listAlbums();
					break;

				case ("listPhotos"):
					if (inputTokens.length == 2) {

						listPhotos(inputTokens[1]);
					}

					break;
				case ("addPhoto"):

					if (inputTokens.length == 4) {

						addPhoto(inputTokens[1], inputTokens[2], inputTokens[3]);

					} else {

						reportError("Please provide \"<fileName>\" \"<caption>\" and \"<albumName>\"");
					}

					break;
				case ("movePhoto"):
					if (inputTokens.length == 4) {

						movePhoto(inputTokens[1], inputTokens[2],
								inputTokens[3]);

					} else {

						reportError("Please provide \"<fileName>\" \"<oldAlbumName>\" and \"<newAlbumName>\"");
					}
					break;
				case ("removePhoto"):
					if (inputTokens.length == 3) {

						removePhoto(inputTokens[1], inputTokens[2]);

					} else {

						reportError("Please provide a \"<filename>\" \"<albumName>\"");
					}
					break;
				case ("addTag"):
					if (inputTokens.length > 2) {

						addTag(inputTokens[1], inputTokens[2]);

					} else {

						reportError("Please provide \"<fileName>\" \"<tagType>:<tagValue>\"");
					}
					break;
				case ("deleteTag"):
					if (inputTokens.length == 3) {

						deleteTag(inputTokens[1], inputTokens[2]);

					} else {

						reportError("Please provide \"<fileName>\" \"<tagType>:<tagValue>\"");
					}
					break;
				case ("listPhotoInfo"):
					if (inputTokens.length == 2) {

						listPhotoInfo(inputTokens[1]);

					} else {

						reportError("Please provide a \"<filename>\"");
					}
					break;
				case ("getPhotosByDate"):
					if (inputTokens.length == 3) {

						getPhotosByDate(inputTokens[1], inputTokens[2]);

					} else {

						reportError("Please provide a \"<start date>\" and an \"<end date>\"");
					}
					break;
				case ("getPhotosByTag"):
					if (inputTokens.length == 2) {
						// This isn't how the project spec laid it out actually
						// like this:
						// car:lambo,car:2door,car:white

						String[] tags = inputTokens[1].split(",");

						/*
						 * String[] tags = new String[inputTokens.length - 1];
						 * for (int i = 0; i < tags.length; i++) {
						 * 
						 * tags[i] = inputTokens[i + 1];
						 * 
						 * }
						 */

						getPhotosByTag(tags);

					} else {

						reportError("Please format as <tagType>:\"<tagValue>\",<tag2Type>\"<tag2Value>\"... ");
					}

					break;
				case ("1"):

					createAlbum("a1");
					pl("");
					addPhoto("1m.png", "1M Pic", "a1");
					pl("");
					addPhoto("335.jpg", "335 Pic", "a1");
					pl("");
					createAlbum("tester");
					pl("");
					addPhoto("settings.png", "test this stuff", "tester");
					pl("");
					addPhoto("lambo.jpg", "lambo!", "a1");
					pl("");

					// getPhotosByTag("car:m1");

					break;
				default:
					reportError("Unrecognized command");
				}

			}
		}

		logout();

	}

	public static void createAlbum(String albumName) {

		if (user.addUserAlbum(albumName)) {
			pl("created album for user " + user.getFullName() + ":\n"
					+ albumName);

		} else {

			pl("album exists for user " + user.getFullName() + ":\n"
					+ albumName);
		}

	}

	public static void deleteAlbum(String albumName) {

		if (user.removeUserAlbum(albumName)) {
			pl("deleted album from user " + user.getFullName() + ":\n"
					+ albumName);
		} else {

			pl("album does not exist for user " + user.getFullName() + ":\n"
					+ albumName);
		}

	}

	public static void listAlbums() {

		if (!user.listAlbums().equals("")) {

			p("Albums for user " + user.getUserID() + ":");

			pl(user.listAlbums());

		} else {

			pl("No albums exist for user " + user.getUserID());
		}
	}

	public static void listPhotos(String albumName) {
		AlbumEditorController album = user.editAlbum(albumName);

		if (album != null) {

			pl(album.listPhotos());

		} else {

			pl("Album " + albumName + " does not exist");

		}

	}

	public static void addPhoto(String fileName, String caption,
			String albumName) {

		// return 1 on success
		// 0 on filename not existing
		// -1 on file already existing not existing

		AlbumEditorController album = user.editAlbum(albumName);

		if (album != null) {

			int error = album.addAlbumPhoto("", fileName, caption);

			if (error == 0) {

				pl("File " + fileName + " does not exist");

			} else if (error == -1) {

				pl("Photo " + fileName + " already exists in album "
						+ albumName);
			} else if (error == 1) {

				pl("Added photo " + fileName + ":\n" + caption + " - Album: "
						+ albumName);
				album.addAlbumPhoto("", fileName, caption);
			}

		} else {

			pl("Album " + albumName + " does not exist");

		}

	}

	public static void movePhoto(String filename, String oldAlbumName,
			String newAlbumName) {

		AlbumEditorController oldAlbum = user.editAlbum(oldAlbumName);
		AlbumEditorController newAlbum = user.editAlbum(newAlbumName);

		if (oldAlbum == null) {

			pl("Album " + oldAlbumName + " does not exist");
		} else if (newAlbum == null) {

			pl("Album " + newAlbumName + " does not exist");

		} else if (oldAlbum.getPhoto(filename) == null) {
			pl("Photo " + filename + " does not exist in " + oldAlbumName);
		} else {
			user.movePhoto(filename, oldAlbum, newAlbum);
			pl("Moved Photo " + filename + ":\n" + filename + " - From album "
					+ oldAlbumName + " to Album " + newAlbumName);

		}
	}

	public static void removePhoto(String filename, String albumName) {
		album = user.editAlbum(albumName);

		if (album != null) {

			if (album.removeAlbumPhoto(filename)) {
				// should be good to go!

				System.out.println("Removed photo: " + filename
						+ " - From album " + albumName);

			} else {

				pl("Photo " + filename + " is not in album " + albumName);

			}

		} else {

			pl("Album " + albumName + " does not exist");

		}

	}

	public static void addTag(String filename, String tag) {

		/*
		 * addTag returns -1 if tag already exists, 0 if photo doesn't exist
		 */
		int error = user.addTag(filename, tag);
		if (error == 0) {
			pl("Photo " + filename + " does not exist");

		} else if (error == -1) {

			pl("Tag already exists for " + filename + " " + tag.trim() + "\"");

		} else {

			pl("Added tag:\n" + filename + " " + tag.trim() + "\"");

		}

	}

	public static void deleteTag(String filename, String tag) {

		/*
		 * deleteTag returns -1 if tag doesn't exists, 0 if photo doesn't exist
		 */

		int error = user.deleteTag(filename, tag);

		if (error == 0) {
			pl("Photo " + filename + " does not exist");

		} else if (error == -1) {

			pl("Tag does not exist for " + filename + " " + tag);

		} else {

			pl("Deleted tag:\n" + filename + " " + tag);

		}

	}

	public static void listPhotoInfo(String filename) {

		String photoInfo = user.listPhotoInfo(filename);

		if (photoInfo != null) {

			pl(photoInfo);
		} else {

			pl("Photo " + filename + " does not exist");
		}

	}

	public static void getPhotosByDate(String startDate, String endDate) {

		// calendar.setTime(sdf.parse("Mon Mar 14 16:02:37 GMT 2011"));

		Date begin = null;
		Date end = null;
		try {
			begin = sdf.parse(startDate);
		} catch (ParseException e) {
			reportError("Date not formatted correctly");
		}

		try {
			end = sdf.parse(endDate);
		} catch (ParseException e) {
			reportError("Date not formatted correctly");
		}

		if (end != null && begin != null) {

			String finds[] = user.getPhotosByDate(begin, end);
			if (finds != null && finds.length != 0) {

				String toPrint = "Photos for user " + user.getUserID()
						+ " in range " + sdf.format(begin) + " to "
						+ sdf.format(end) + ":";

				if (toPrint.trim().endsWith(",")) {

					toPrint = toPrint.substring(0, toPrint.length() - 2);
				}

				pl(toPrint);

				for (String find : finds) {

					// Still need to sort these
					pl(find);
				}
			} else {

			}
		}

	}

	public static void getPhotosByTag(String[] tags) {

		String finds[] = user.getPhotosByTags(tags);
		if (finds.length != 0) {

			String toPrint = "Photos for user " + user.getUserID()
					+ " containing ";

			for (String tag : tags) {
				toPrint = toPrint + tag + ", ";

			}

			if (toPrint.trim().endsWith(",")) {

				toPrint = toPrint.substring(0, toPrint.length() - 2);
			}

			pl(toPrint);

			for (String find : finds) {

				// Still need to sort these
				pl(find);
			}
		} else {

		}

	}

	public static String[] trimInputs(String[] inputs) {

		for (int i = 0; i < inputs.length; i++) {

			if (inputs[i].charAt(0) == '"'
					&& inputs[i].charAt(inputs[i].length() - 1) == '"') {
				inputs[i] = inputs[i].substring(1, inputs[i].length() - 1);
			}

		}

		return inputs;

	}

	public static void logout() {

		// Save users and stuff

		backend.save();
		// System.exit(1);

	}

	public static void reportError(String errorText) {
		System.out.println("ERROR: " + errorText);
		// System.exit(-1);
	}

	private static void pl(String print) {
		System.out.println(print);
	}

	private static void p(String print) {
		System.out.print(print);
	}

}
