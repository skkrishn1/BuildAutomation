package com.ap.ui.portal.installation.command.exception;


/**
 * Exception representing any exceptions/issues 
 * in execution of a {@link com.ap.ui.portal.installation.command.InstallationCommand}.
 * 
 * @see com.ap.ui.portal.installation.command.InstallationCommand
 * @see com.ap.ui.portal.installation.command.InstallationCommandExecutor
 * @author Abhishek Tembe
 *
 */
public class CommandExecutionException extends Exception {

	/**
	 * Default Serial Version UUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 *
	 * @param message exception message.
	 */
	public CommandExecutionException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 *
	 * @param cause root cause.
	 */
	public CommandExecutionException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor
	 *
	 * @param message exception message.
	 * @param cause root cause.
	 */
	public CommandExecutionException(String message, Throwable cause) {
		super(message, cause);
	}
}
