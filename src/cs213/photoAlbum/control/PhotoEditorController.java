/* Neil Hussain 
 * Matt Kaiser
 */
package cs213.photoAlbum.control;

import java.util.ArrayList;
import java.util.Date;

import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.Tag;
import cs213.photoAlbum.simpleview.CmdView;
import cs213.photoAlbum.view.View;

public class PhotoEditorController {

	private Photo model;
	private View view;

	/**
	 * AlbumEditorController class constructor
	 * 
	 * @param model
	 *            <code>Photo</code> model for fetching photo data
	 * @param view
	 *            <code>View</code> view to display data on
	 */
	public PhotoEditorController(Photo model, View view) {
		this.model = model;
		this.view = view;
	}

	/**
	 * Get the <code>Photo</code>'s caption
	 * 
	 * @return String containing the caption of the <code>Photo</code>
	 */
	public String getPhotoCaption() {
		return model.getCaption();
	}

	/**
	 * Get the <code>Photo</code>'s filename
	 * 
	 * @return String containing the filename of the <code>Photo</code>
	 */
	public String getPhotoFilename() {
		return model.getFilename();
	}

	/**
	 * Get the <code>Photo</code>'s date
	 * 
	 * @return String containing the data of the <code>Photo</code>
	 */
	public String getPhotoDate() {
		return model.getTimeString();
	}

	/**
	 * Get the time the <code>Photo</code> was taken
	 * 
	 * @return String containing the time the <code>Photo</code> was taken
	 */
	public String getPhotoTime() {
		return model.getTime();
	}

	/**
	 * Get the <code>Photo</code>'s tags
	 * 
	 * @return Array of <code>Tag</code>s that the photo has
	 */
	public Tag[] getPhotoTags() {
		return model.getTags();
	}

	/**
	 * Set the <code>Photo</code>'s caption
	 * 
	 * @param caption
	 *            String containing the new caption
	 * @return True if the change was successful, false otherwise
	 */
	public boolean setPhotoCaption(String caption) {
		return model.setCaption(caption);
	}

	/**
	 * Set the <code>Photo</code>'s filename
	 * 
	 * @param filename
	 *            String containing the new filename
	 * @return True if the change was successful, false otherwise
	 */
	public boolean setPhotoFilename(String filename) {
		return model.setFilename(filename);
	}

	/**
	 * Set the <code>Photo</code>'s date
	 * 
	 * @param date
	 *            String containing the new date
	 * @return True if the change was successful, false otherwise
	 */
	public boolean setPhotoDate(Date date) {

		// Error checking is done in here

		model.setDate(date);
		return true;
	}

	/**
	 * Set the <code>Photo</code>'s time
	 * 
	 * @param time
	 *            String containing time the photo was taken
	 * @return True if the change was successful, false otherwise
	 */
	public boolean setPhotoTime(String time) {
		return model.setTime(time);
	}

	/**
	 * Add and new <code>Tag</code> to the <code>Photo</code>
	 * 
	 * @param type
	 *            String containing the type of tag to be added
	 * @param value
	 *            String containing the value of the new tag to be added
	 * @return True if the change was successful, false otherwise
	 */
	public boolean addPhotoTag(String type, String value) {
		return model.addTag(type, value);
	}

	/**
	 * Add and new <code>Tag</code> to the <code>Photo</code>
	 * 
	 * @param tag
	 *            The new <code>Tag</code> object that should be added
	 * @return True if the change was successful, false otherwise
	 */
	public boolean addPhotoTag(Tag tag) {
		return model.addTag(tag);
	}

	/**
	 * Remove a <code>Tag</code> from a <code>Photo</code>
	 * 
	 * @param type
	 *            String containing the type of the tag to be removed
	 * @param value
	 *            String containing the value of the tag to be removed
	 * @return True if the change was successful, false otherwise
	 */
	public boolean removePhotoTag(String type, String value) {
		return model.removeTag(type, value);
	}

	/**
	 * Remove a <code>Tag</code> from a <code>Photo</code>
	 * 
	 * @param tag
	 *            The <code>Tag</code> object that should be removed from the
	 *            Photo
	 * @return True if the change was successful, false otherwise
	 */
	public boolean removePhotoTag(Tag tag) {
		return model.removeTag(tag);
	}

	/**
	 * Get <code>View</code> view associated with this
	 * <code>PhotoEditorController</code>
	 *
	 * @return <code>View</code> that this <code>PhotoEditorController</code>
	 *         owns
	 */
	public View getPhotoView() {
		return view;
	}

	/**
	 * Get <code>CmdView</code> view associated with this
	 * <code>PhotoEditorController</code>
	 * 
	 * @param <code>CmdView</code> to associate with this
	 *        <code>PhotoEditorController</code>
	 */
	public void setPhotoView(CmdView view) {
		this.view = view;
	}

	/**
	 * Calls the refresh method in the <code>View</code> that will update values
	 * and reload the view
	 */
	public void refreshPhotoView() {

		// call the refresh method in the view
		view.refreshPhotoView();
	}

	/**
	 * Get the tags of this photo
	 * 
	 * @return String of all tags
	 */
	public String getTagsString() {
		String tagsString = null;
		if (this.model.getTags().length == 0) {
			return "No tags yet.";
		} else {
			tagsString = "";
			for (Tag tag : model.getTags()) {
				tagsString += tag.toString() + ", ";
			}
			tagsString = tagsString.trim();
			if (tagsString.charAt(tagsString.length() - 1) == ',') {

				tagsString = tagsString.substring(0, tagsString.length() - 1);
			}

		}
		return tagsString;
	}

	/**
	 * Get the tags of this photo
	 * 
	 * @return String[] of all tags
	 */
	public String[] getTagStrings() {

		ArrayList<String> tagStrings = new ArrayList<String>();

		for (Tag tag : model.getTags()) {
			tagStrings.add((tag.getValue() + ":" + tag.getType()));
		}

		return tagStrings.toArray(new String[tagStrings.size()]);
	}

	public String getName() {

		return model.getName();
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
	public int addTag(String tag) {

		/*
		 * addTag returns -1 if tag already exists, 0 if photo doesn't exist
		 */

		// returns number of times the tag was added to
		int error = -1;

		String[] tagParts = parseTag(tag);

		if (!tagParts[0].equals("") && !tagParts[1].equals("")
				&& model.addTag(tagParts[0], tagParts[1])) {

			error = 1;

		} else {

			return -1;

		}

		return error;
	}

	/**
	 * Remove a tag
	 * 
	 * @param Valid
	 *            filename to add the tag to remove
	 * @param String
	 *            of tag contents, formatted correctly
	 * @return -1 if tag doesn't exist, 1 else
	 */
	public int removeTag(String tag) {

		/*
		 * addTag returns -1 if tag already exists, 0 if photo doesn't exist
		 */

		// returns number of times the tag was added to
		int error = -1;

		String[] tagParts = parseTag(tag);

		if (!tagParts[0].equals("") && !tagParts[1].equals("")
				&& model.removeTag(tagParts[1], tagParts[0])) {

			error = 1;

		} else {

			return -1;

		}

		return error;
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

	/**
	 * Get the album of this photo
	 * 
	 * @return String album name
	 */
	public String getAlbum() {
		return model.getAlbums()[0].getName();

	}
}
