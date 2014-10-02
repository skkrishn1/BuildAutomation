/**
 * 
 */
package com.ap.ui.portal.installation.command.impl;

import java.io.File;

import com.ap.ui.portal.installation.command.InstallationCommand;
import com.ap.ui.portal.installation.command.InstallationCommandExecutor;
import com.ap.ui.portal.installation.command.exception.ArtifactDownloadException;
import com.ap.ui.portal.installation.command.exception.CommandExecutionException;
import com.ap.ui.portal.installation.command.model.Input;
import com.ap.ui.portal.installation.command.model.Result;
import com.ap.ui.portal.installation.data.NexusArtifacts;
import com.ap.ui.portal.installation.util.IOUtil;
import com.ap.ui.portal.installation.util.MessageUtil;
import com.ap.ui.portal.installation.util.PropertiesUtil;

/**
 * This command downloads the required artifacts from NEXUS into 
 * specified target location. The target location is to be defined 
 * in the settings.properties using key
 * &quot;target.build.artifacts.download.location&quot;.
 * Information of NEXUS is provided in &quot;nexus.properties&quot;.
 * Details of the artifacts to be downloaded is defined in enum
 * {@link com.ap.ui.portal.installation.data.NexusArtifacts}.
 *
 * @author Abhishek Tembe
 *
 */
public class DownloadNexusArtifacts implements InstallationCommand {
	
	/**
	 * Target download location. The value is read from settings.properties
	 * In case the value is not defined, the system picks up default value of 
	 * "artifacts" folder in current directory.
	 */
	private static final String TARGET_DOWNLOAD_LOCATION = PropertiesUtil.getProperty("target.build.artifacts.download.location", 
																					  getDefaultDownloadDirectory());

	/**
	 * This command downloads the required artifacts from NEXUS into 
	 * specified target location. The target location is to be defined 
	 * in the settings.properties using key
	 * &quot;target.build.artifacts.download.location&quot;.
	 * Information of NEXUS is provided in &quot;nexus.properties&quot;.
	 * Details of the artifacts to be downloaded is defined in enum
	 * {@link com.ap.ui.portal.installation.data.NexusArtifacts}.
	 * 
	 *  @param input {@link Input} object, to contain input consumable 
	 * 				 by the command class's execute method.
	 * 
	 * @return the result/response of this command execution 
	 * 		   as {@link Result} object.
	 * 
	 * @throws CommandExecutionException in case of error.
	 * 
	 * @see InstallationCommandExecutor
	 * @see com.ap.ui.portal.installation.command.InstallationCommand#execute(com.ap.ui.portal.installation.command.model.Input)
	 */
	public Result execute(Input input) throws CommandExecutionException {
		// Routine checks and initialisations.
		initialize(input, getDownloadLocationKey());
		// Download the nexus artifacts.
		downloadArtifacts(input);
		
		Result result = new Result();
		result.setCompletionMessage("Completed execution of " + this.getClass().getName());
		result.setCompletionStatus(Result.SUCCESS);
		result.copyAttributes(input);
		return result;
	}

	/**
	 * This method returns the key representing the target location where 
	 * the artifacts are to be downloaded.
	 * 
	 * @return the key representing the target location where 
	 * 		   the artifacts are to be downloaded. 
	 */
	protected String getDownloadLocationKey() {
		return "target.build.artifacts.download.location";
	}

	/**
	 * Downloads all the artifacts to be download.
	 * 
	 * @param input {@link Input} object, to contain input consumable 
	 * 				 by the command class's execute method.
	 */
	protected void downloadArtifacts(Input input) {
		// Get the list of artifacts.
		NexusArtifacts[] artifacts = getArtifacts();
		// Iterate over list
		for (int i = 0; i < artifacts.length; i++) {
			NexusArtifacts artifact = artifacts[i];
			if (isConfigArtifact(artifact)) {
				continue;
			}
			// Download artifact.
			try {
				downloadArtifact(artifact);
			} catch (ArtifactDownloadException e) {
				MessageUtil.logErrorMessage("Could not download artifact: " + artifact.getName() + " Reason:" + e);
				MessageUtil.logDebugMessage("Failed Artifact URL:" + artifact.getDownloadURL());
				MessageUtil.logDebugMessage("Failed to copy the artifact to " + getTargetLocation());
			}
		}
	}
	/**
	 * This method returns the target location where 
	 * the artifacts are to be downloaded.
	 * 
	 * @return target location where the artifacts are to be
	 * 		   downloaded. 
	 */
	protected String getTargetLocation() {
		return TARGET_DOWNLOAD_LOCATION; 
	}
	/**
	 * Initialisation for the command. 
	 * 
	 * @param input input for the command.
	 * @param downloadLocationKey key representing the download folder value.
	 * 
	 * @throws CommandExecutionException in case of error.
	 */
	protected void initialize(Input input, String downloadLocationKey) throws CommandExecutionException {
		// Check the target folder is defined.
		if (getTargetLocation().equals(getDefaultDownloadDirectory())) {
			MessageUtil.logWarningMessage("Property " + downloadLocationKey + " may not be defined in the settings properties file.");
		}
		MessageUtil.logInfoMessage("Using target artifact download folder as :" + getTargetLocation());
		
		// Create the target folder, if it does not exit.
		createFolderIfNotExist();
		
		// Check if the milestone release number is specified.
		checkMilestoneRelaseNumberMentioned();
	}
	/**
	 * This is the default directory for download, to be used only if the 
	 * value is not defined explicitly by the end-user.
	 *   
	 * @return folder name "./artifact"
	 */
	protected static final String getDefaultDownloadDirectory() {
		return "."+ File.separator + "artifacts";
	}
	/**
	 * Check if the milestone release number is specified.
	 * @throws CommandExecutionException in case the version number is not specified.
	 */
	private void checkMilestoneRelaseNumberMentioned() 
	throws CommandExecutionException {
		String milestoneVersionNumber = PropertiesUtil.getProperty("milestone.release.version");
		if (milestoneVersionNumber == null || "".equals(milestoneVersionNumber)) {
			String message = "Property missing!! Property \"milestone.release.version\" not defined in settings properties file.";
			MessageUtil.logErrorMessage(message);
			throw new CommandExecutionException(message);
		}
	}
	/**
	 * Creates the folder, if it does not exist.
	 * 
	 * @throws CommandExecutionException in case the target folder 
	 * 									 was something other than folder.
	 */
	private void createFolderIfNotExist() throws CommandExecutionException {
		File targetFolder = new File(getTargetLocation());
		if (!targetFolder.exists()) {
			// Create the folder, if the folder does not exist.
			targetFolder.mkdirs();
			MessageUtil.logInfoMessage(getTargetLocation() + " does not exist. Created empty folder structure.");
		} else if (!targetFolder.isDirectory()) {
			// The target folder was not a folder. 
			throw new CommandExecutionException(getTargetLocation() + "should be a folder, found it to be a file.");
		}
	}
	/**
	 * Checks if the artifact is a config level artifact.
	 * 
	 * @param artifact artifact to check.
	 * 
	 * @return <code>true</code> if the artifact is config type of artifact.
	 */
	private boolean isConfigArtifact(NexusArtifacts artifact) {
		// List all the config level artifacts here.
		return NexusArtifacts.ArtifactType.CONFIG_PORTAL_EXT.equals(artifact.getArtifactType());
	}
	/**
	 * Download a particular plugin artifact.
	 * 
	 * @param artifact information artifact tot download.
	 * @throws ArtifactDownloadException  in case the artifact cannot be downloaded.
	 */
	protected void downloadArtifact(NexusArtifacts artifact) throws ArtifactDownloadException {
		// Pre-logging.
		MessageUtil.logDebugMessage("Starting download of " + artifact.getName());
		MessageUtil.logDebugMessage("Download URL is: " + artifact.getDownloadURL());
		
		// Download the file.
		IOUtil.downloadFile(getTargetLocation(), artifact.getDownloadURL());
		
		// Post-logging.
		MessageUtil.logInfoMessage("Successfully downloaded " + artifact.getName() + " from " + artifact.getDownloadURL());
		MessageUtil.logInfoMessage("Artifact copied successfully to " +getTargetLocation());
	}

	/**
	 * Retrieves all the artifacts to be downloaded.
	 *  
	 * @return Array of {@link NexusArtifacts} objects.
	 */
	private NexusArtifacts[] getArtifacts() {
		NexusArtifacts[] allArtifacts = NexusArtifacts.values();
		return allArtifacts;
	}
}
