/**
 * 
 */
package com.ap.ui.portal.installation.data;

/**
 * This enum defines the various commands defined in the system.
 * This is defined just as a means to facilitate the execution of the
 * commands. Registering a command in this enum is NOT REQUIRED. 
 *
 * @author Abhishek Tembe
 *
 */
public enum Commands {

	/**
	 * Command to shutdown weblogic instance.
	 */
	SHUTDOWN_WEBLOGIC_INSTANCE("com.ap.ui.portal.installation.command.impl.ShutdownWebLogicInstance"),
	/**
	 * Command to download the artifacts from NEXUS. 
	 */
	DOWNLOAD_NEXUS_ARTIFACTS("com.ap.ui.portal.installation.command.impl.DownloadNexusArtifacts"),
	/**
	 * Command to replace the portal-ext.properties.
	 */
	REPLACE_PORTAL_EXT_PROPERTIES("com.ap.ui.portal.installation.command.impl.ReplacePortalExtProperties"),
	/**
	 * Command to start weblogic instance.
	 */
	START_WEBLOGIC_INSTANCE("com.ap.ui.portal.installation.command.impl.StartWebLogicInstance"),
	/**
	 * Command to tag and checkout the SVN code.
	 */
	TAG_SVN_CODE("com.ap.ui.portal.installation.command.impl.TagAndCheckoutSVNCode"),
	/**
	 * Command to build checked out code.
	 */
	BUILD_CHECKED_OUT_CODE("com.ap.ui.portal.installation.command.impl.BuildCheckOutCode"),
	/**
	 * Command to push artifacts to Nexus.
	 */
	PUSH_ATRIFACTS_TO_NEXUS("com.ap.ui.portal.installation.command.impl.PushArtifactsToNexus");
	
	
	/**
	 * Fully qualified name of the InstallationCommand class implementation.
	 */
	private String fullyQualifiedName;

	/**
	 * Constructor
	 *
	 * @param fullyQualifiedName Fully qualified name
	 * 							 of the InstallationCommand 
	 * 							 class implementation.
	 */
	private Commands(String fullyQualifiedName) {
		this.fullyQualifiedName = fullyQualifiedName;
	}

	/**
	 * Returns the value of  fullyQualifiedName.
	 *
	 * @return the fullyQualifiedName
	 */
	public String getFullyQualifiedName() {
		return fullyQualifiedName;
	}

	/**
	 * Set the value of input parameter to field fullyQualifiedName.
	 *
	 * @param fullyQualifiedName the fullyQualifiedName to set
	 */
	public void setFullyQualifiedName(String fullyQualifiedName) {
		this.fullyQualifiedName = fullyQualifiedName;
	}
}
