/**
 * 
 */
package com.ap.ui.portal.installation.util;

/**
 * Exception, representing the error that 
 * properties file mentioned was not found.
 *
 * @author Abhishek Tembe
 *
 */
public class PropertiesFileNotFoundException extends Exception {

	/**
	 *  Default serial version UUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 *
	 * @param message error message.
	 */
	public PropertiesFileNotFoundException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 *
	 * @param cause root cause.
	 */
	public PropertiesFileNotFoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor
	 *
	 * @param message error message.
	 * @param cause root cause.
	 */
	public PropertiesFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}


}
