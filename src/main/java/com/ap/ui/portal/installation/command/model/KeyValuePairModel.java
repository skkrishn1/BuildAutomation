package com.ap.ui.portal.installation.command.model;

/**
 * Wrapper object to hold a key value pair.
 * 
 * @author Abhishek Tembe
 * 
 */
public class KeyValuePairModel {
	private String key;
	private String value;
	
	/**
	 * Constructor
	 *
	 * @param key key
	 * @param value value
	 */
	public KeyValuePairModel(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	/**
	 * Returns the value of  key.
	 *
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Set the value of input parameter to field key.
	 *
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Returns the value of  value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the value of input parameter to field value.
	 *
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * String representation of the key-value
	 * @return String representation of the key-value
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "KeyValue<" + key + ":" + value + ">";
	}
	
}
