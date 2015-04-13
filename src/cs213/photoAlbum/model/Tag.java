/* Neil Hussain 
 * Matt Kaiser
 */

package cs213.photoAlbum.model;

import java.io.Serializable;

public class Tag implements Serializable {

	private String type;
	private String value;

	public Tag(String type, String value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * Get the type (key) of the tag
	 * 
	 * @return String type (key) of tag
	 */
	public String getType() {
		return type;
	}

	/**
	 * Get the value of the tag
	 * 
	 * @return String value of tag
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the type (key) of the tag
	 * 
	 * @param type
	 *            String of the type (key) of tag
	 * @return true if successful, false otherwise
	 */
	public boolean setType(String type) {

		/*
		 * Need to define the set of type values that are possible and check for
		 * errors
		 */

		this.type = type;
		return true;
	}

	/**
	 * Set the value of the tag
	 * 
	 * @param value
	 *            String of the value of the tag
	 * @return true if successful, false otherwise
	 */
	public boolean setValue(String value) {

		/*
		 * Have to define the types of values that are possible for each type
		 * and do error checking
		 */

		this.value = value;
		return true;
	}

	/**
	 * Formatted string of the tag
	 * 
	 * @return String describing the tag
	 */
	public String toString() {
		String tagInfo = null;

		tagInfo = this.getType() + ":" + this.getValue();

		return tagInfo;
	}

	/**
	 * Check equivalence of tag against current
	 * 
	 * @param tagToCompare
	 *            to the current tag
	 * @return true of they are equal, false otherwise
	 */
	public boolean equals(Tag tagToCompare) {

		if ((tagToCompare.getType().equals(this.type) || tagToCompare.getType()
				.equals("")) && tagToCompare.getValue().equals(this.value)) {
			return true;
		}
		return false;
	}

}
