package com.ap.ui.portal.installation.data;

import com.ap.ui.portal.installation.util.PropertiesUtil;

/**
 * This enum contains the details of the NEXUS artifacts to be 
 * downloaded.
 *
 * @author Abhishek Tembe
 *
 */
public enum NexusArtifacts {
	/**
	 * Information of Zapp Hook.
	 */
	ZAPP_HOOK(ArtifactType.HOOK, "Zapp-Hook", "com.ap.ui.ap-portal.portal-plugins.hooks", "war"),
	/**
	 * Information of Ap Audit Hook.
	 */
	AP_AUDIT_HOOK(ArtifactType.HOOK, "ap-audit", "com.ap.ui.ap-portal.portal-plugins.hooks", "war"),
	/**
	 * Information of CFI Portlets.
	 */
	CFI_PORTLETS(ArtifactType.PORTLET, "cfi-portlets", "com.ap.ui.ap-portal.portal-plugins.portlets", "war"),
	/**
	 * Information of Common portlets.
	 */
	COMMON_PORTLETS(ArtifactType.PORTLET, "common-portlets", "com.ap.ui.ap-portal.portal-plugins.portlets", "war"),
	/**
	 * Information of Distributor portlets.
	 */
	DISTRIBUTOR_PORTLETS(ArtifactType.PORTLET, "distributor-portlets", "com.ap.ui.ap-portal.portal-plugins.portlets", "war"),
	/**
	 * Information of OPS portlets.
	 */
	OPS_PORTLETS(ArtifactType.PORTLET, "ops-portlets", "com.ap.ui.ap-portal.portal-plugins.portlets", "war"),
	/**
	 * Information of Zapp Theme.
	 */
	ZAPP_THEME(ArtifactType.THEME, "zapp-theme", "com.ap.ui.ap-portal.portal-plugins.themes", "war"),
	/**
	 * Information of Config: portal-ext.properties.
	 */
	ZAPP_PORTAL_EXT(ArtifactType.CONFIG_PORTAL_EXT, "portal-ext-properties", "com.ap.ui.ap-portal.portal-plugins.portal-config", "properties");
	
//=============================================================================	
	
	/**
	 * Enum describing the type of Liferay plugin or config.
	 *
	 * @author Abhishek Tembe
	 *
	 */
	public enum ArtifactType {
		/**
		 * Artifact type: Liferay hook plugin.
		 */
		HOOK("hook.download.url.fragment"), 
		/**
		 * Artifact type: Liferay Portlet plugin.
		 */
		PORTLET("portlet.download.url.fragment"), 
		/**
		 * Artifact type: Liferay Theme plugin.
		 */
		THEME("theme.download.url.fragment"),
		/**
		 * Artifact type: Liferay Portal config
		 */
		CONFIG_PORTAL_EXT("portal-ext-properties.download.url.fragement");
		/**
		 * Holds the template of the download URL fragment
		 */
		private String downloadURLFragmentKey;
		/**
		 * Constructor
		 *
		 * @param downloadURLFragmentKey template of the download URL fragment
		 * 								 to be read from nexus.properties.
		 */
		private ArtifactType(String downloadURLFragmentKey) {
			this.downloadURLFragmentKey = downloadURLFragmentKey;
		}
		/**
		 * Returns the value of  downloadURLFragment.
		 *
		 * @return the downloadURLFragment
		 */
		public String getDownloadURLFragmentKey() {
			return downloadURLFragmentKey;
		}
	}
	
	/**
	 * This URL contains the download URL for the artifact.
	 */
	private String downloadURL;
	/**
	 * Type of the artifact.
	 */
	private ArtifactType artifactType;
	/**
	 * Name of the artifact.
	 */
	private String name;
	private String groupID;
	private String extension;
	/**
	 * Constructor
	 *
	 * @param type Type of the plugin.
	 * @param artifactName name of the artifact. 
	 * 					   Will be used to construct the
	 * 					   download URL. 
	 * @param groupID group ID associated with the artifact. 
	 * 				  Used for Nexus upload.
	 * @param extension extension of the artifact file.
	 * 					Used for Nexus upload.
	 */
	private NexusArtifacts(ArtifactType type, String artifactName, String groupID, String extension) {
		this.downloadURL = constructDownloadURL(type.getDownloadURLFragmentKey(), artifactName);
		this.artifactType = type;
		this.name = artifactName;
		this.groupID = groupID;
		this.extension = extension;
	}

	/**
	 * Get the base nexus URL.
	 * 
	 * @return the base NEXUS URL.
	 */
	public final static String getBaseNexusURL() {
		String hostName = PropertiesUtil.getProperty("nexus.hostname");
		String portNumber = PropertiesUtil.getProperty("nexus.port.number");
		return PropertiesUtil.getProperty("nexus.release.base.url", hostName, portNumber);
	}
	/**
	 * Returns the value of  downloadURL.
	 *
	 * @return the downloadURL
	 */
	public String getDownloadURL() {
		return downloadURL;
	}
	/**
	 * Returns the value of  artifactType.
	 *
	 * @return the artifactType
	 */
	public ArtifactType getArtifactType() {
		return artifactType;
	}
	/**
	 * Returns the value of  name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Returns the value of  groupID.
	 *
	 * @return the groupID
	 */
	public String getGroupID() {
		return groupID;
	}
	/**
	 * Returns the value of  extension.
	 *
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * Constructs the actual download URL required by this artifact.
	 * 
	 * @param downloadURLFragmentKey key for the URL template fragment for the
	 * 								 artifact.
	 * @param artifactName name of the artifact.
	 * 
	 * @return Complete download URL of the artifact.
	 */
	private String constructDownloadURL(String downloadURLFragmentKey, String artifactName) {
		// The name of the artifact can be overridden through 
		// settings.properties.
		// Check if the property to override 
		// 		"<artifactName>.artifact.name" is present.
		String nameUrlFragment = PropertiesUtil.getProperty(artifactName + ".artifact.name");
		if (nameUrlFragment == null || "".equals(nameUrlFragment)) {
			nameUrlFragment = artifactName;
		}
		// The name of the artifact can be overridden through 
		// settings.properties.
		// Check if the property to override 
		// 		"<artifactName>.artifact.name" is present.
		String versionFragment = PropertiesUtil.getProperty(artifactName + ".artifact.release.version.number");
		if (versionFragment == null || "".equals(nameUrlFragment)) {
			versionFragment = PropertiesUtil.getProperty("milestone.release.version");
		}
		// Read the URL template from nexus.properties
		String urlFragment = PropertiesUtil.getProperty(downloadURLFragmentKey, nameUrlFragment, versionFragment);
		
		// Build entire URL.
		StringBuilder url = new StringBuilder();
		url.append(getBaseNexusURL());
		url.append(urlFragment);
		return url.toString();
		
	}
}
