/**
 * 
 */
package com.ap.ui.portal.installation.command.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.ap.ui.portal.installation.command.exception.ArtifactDownloadException;
import com.ap.ui.portal.installation.command.exception.CommandExecutionException;
import com.ap.ui.portal.installation.command.model.Input;
import com.ap.ui.portal.installation.data.NexusArtifacts;
import com.ap.ui.portal.installation.util.IOUtil;
import com.ap.ui.portal.installation.util.MessageUtil;
import com.ap.ui.portal.installation.util.PropertiesUtil;

/**
 * This command replaces &quot;portal-ext.properties&quot; from NEXUS 
 * to the base directory of the weblogic's domain.The target location 
 * is to be defined in the settings.properties using key
 * &quot;target.weblogic.domain.base.directory&quot;.
 *
 * @author Abhishek Tembe
 *
 */
public class ReplacePortalExtProperties extends DownloadNexusArtifacts {

	/**
	 * Target download location. The value is read from settings.properties
	 * In case the value is not defined, the system picks up default value of 
	 * "artifacts" folder in current directory.
	 */
	private static final String TARGET_DOWNLOAD_LOCATION = PropertiesUtil.getProperty("target.weblogic.domain.base.directory", 
																					  getDefaultDownloadDirectory());
	/**
	 * Constant for name of the file: portal-ext.properties
	 */
	private static final String PORTAL_EXT_PROPERTIES = "portal-ext.properties";

	/**
	 * {@inheritDoc}
	 *
	 * @see com.ap.ui.portal.installation.command.impl.DownloadNexusArtifacts#initialize(com.ap.ui.portal.installation.command.model.Input, java.lang.String)
	 */
	@Override
	protected void initialize(Input input, String downloadLocationKey)
	throws CommandExecutionException {
		// check if the target location is present.
		checkTargetLocationPresent();
		// Call parent method.
		super.initialize(input, downloadLocationKey);
	}
	/**
	 *
	 *{@inheritDoc}
	 *
	 * @see com.ap.ui.portal.installation.command.impl.DownloadNexusArtifacts#getTargetLocation()
	 */
	@Override
	protected String getTargetLocation() {
		return TARGET_DOWNLOAD_LOCATION;
	}
	/**
	 * {@inheritDoc}. 
	 */
	@Override
	protected String getDownloadLocationKey() {
		return "target.weblogic.domain.base.directory";
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.ap.ui.portal.installation.command.impl.DownloadNexusArtifacts#downloadArtifacts()
	 */
	@Override
	protected void downloadArtifacts(Input input) {
		// Take a backup of the existing file.
		backup(PORTAL_EXT_PROPERTIES);
		
		
		try {
			// download the portal-ext.properties artifact.
			downloadArtifact(NexusArtifacts.ZAPP_PORTAL_EXT);
			// rename the file.
			renameFile();
			
			MessageUtil.logInfoMessage("Successfully copied file " + PORTAL_EXT_PROPERTIES + " to " + getTargetLocation());
			
		} catch (ArtifactDownloadException e) {
			MessageUtil.logErrorMessage("Could not download artifact: portal-ext.properties. Reason:" + e);
			MessageUtil.logDebugMessage("Failed Artifact URL:" + NexusArtifacts.ZAPP_PORTAL_EXT.getDownloadURL());
			MessageUtil.logDebugMessage("Failed to copy the artifact to " + getTargetLocation());
		}
	}
	
	/**
	 * Rename the file downloaded to portal-ext.properties.
	 * 
	 */
	private void renameFile() {
		String fileName = getFileName(NexusArtifacts.ZAPP_PORTAL_EXT.getDownloadURL());
		
		String renameFrom = getTargetLocation() + File.separator + fileName;
		String renameTo = getTargetLocation() + File.separator + PORTAL_EXT_PROPERTIES;
		
		IOUtil.deleteIfAlreadyExists(renameTo);
		IOUtil.renameFile(renameFrom, renameTo);
	}
	/**
	 * Takes backup of the file, if it is present.
	 * 
	 * @param fileName file to be backedup.
	 */
	private void backup(String fileName) {
		if (checkIfArtifactExists(fileName)) {
			// since the file exists, create copy of the file.
			createCopyOfFile(fileName);
		}
	}
	
	/**
	 * Creates copy of the file.
	 * @param fileName
	 */
	private void createCopyOfFile(String fileName) {
		boolean success = IOUtil.copyFile(getFullPath(fileName), getCopyPath(fileName));
		if (success) {
			MessageUtil.logInfoMessage("Created backup: " + getCopyPath(fileName));
		}
		
	}
	/**
	 * Checks if the artifact exists.
	 * 
	 * @param fileName file to check.
	 * 
	 * @return <code>true</code> if the file exists.
	 */
	private boolean checkIfArtifactExists(String fileName) {
		return IOUtil.checkIfAlreadyExists(getFullPath(fileName));
	}
	/**
	 * Returns the full path of the file. 
	 * @param fileName name of the file.
	 * @return the full path of the file. 
	 */
	private String getFullPath(String fileName) {
		return TARGET_DOWNLOAD_LOCATION + File.separator + fileName;
	}
	/**
	 * Returns the path of the copy of the file. 
	 * @param fileName name of the file.
	 * @return the full path of the copy of the file. 
	 */
	private String getCopyPath(String fileName) {
		String path = getFullPath(fileName.replace('.', '-'));
		String todaysDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().getTime());
		return path + "-" + todaysDate;
	}
	
	/**
	 * Check if the target location is specified.
	 * @throws CommandExecutionException in case the target location is not specified.
	 */
	private void checkTargetLocationPresent() throws CommandExecutionException {
		String targetLocation = PropertiesUtil.getProperty("target.weblogic.domain.base.directory");
		if (targetLocation == null || "".equals(targetLocation)) {
			String message = "Property missing!! Property \"target.weblogic.domain.base.directory\" not defined in settings properties file.";
			MessageUtil.logErrorMessage(message);
			throw new CommandExecutionException(message);
		}
	}

	
	/**
	 * Reads the file name from the URL.
	 * @param sourceFileUrl
	 * @return
	 */
	private static String getFileName(String sourceFileUrl) {
		int slashIndex = sourceFileUrl.lastIndexOf('/');
		if (slashIndex != -1) {
			return sourceFileUrl.substring(slashIndex + 1);
		}
		return null;
	}
}
