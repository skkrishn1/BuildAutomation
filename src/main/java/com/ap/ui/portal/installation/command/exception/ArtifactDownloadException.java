/**
 * 
 */
package com.ap.ui.portal.installation.command.exception;

/**
 * This exception represents the problems caused while
 * downloading the files from NEXUS  to local target
 * folder.
 *
 * @author Abhishek Tembe
 *
 */
public class ArtifactDownloadException extends CommandExecutionException {

	/**
	 * Default serial version UUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 */
	public ArtifactDownloadException(String message) {
		super(message);
	}

	/**
	 * {@inheritDoc}
	 */
	public ArtifactDownloadException(Throwable cause) {
		super(cause);
	}

	/**
	 * {@inheritDoc}
	 */
	public ArtifactDownloadException(String message, Throwable cause) {
		super(message, cause);
	}

}
