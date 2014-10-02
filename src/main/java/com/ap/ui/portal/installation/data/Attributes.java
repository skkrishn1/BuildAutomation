/**
 * 
 */
package com.ap.ui.portal.installation.data;

/**
 * This enum contains the names of the attributes used 
 * in &quot;push to nexus&quot; tool.
 * 
 *
 * @author Abhishek Tembe
 *
 */
public enum Attributes {
	
	/**
	 * Attribute: Milestone version 
	 */
	MILESTONE_VERSION("milestoneValue"),
	/**
	 * Attribute: SVN URL
	 */
	SVN_URL("svnURL"),
	/**
	 * Attribute: SVN Tag
	 */
	SVN_TAG("svnTag"),
	/**
	 * Attribute: SVN user name
	 */
	SVN_USER_NAME("svnUserName"),
	/**
	 * Attribute: SVN user password
	 */
	SVN_PASSWORD("svnUserPassword"),
	/**
	 * Attribute: build directory
	 */
	BUILD_DIRECTORY_NAME("buildDirectory"),
	/**
	 * Attribute: Repository ID
	 */
	REPOSITORY_ID("reporsitoryID"),
	/**
	 * Attribute: Enhanced war directory
	 */
	ENHANCED_WAR_DIRECTORY("enhancedWarDirectory");
	
	/**
	 * Attributes name
	 */
	private String paramName;
	/**
	 * Constructor
	 * @param paramName name of the parameter.
	 */
	private Attributes(String paramName) {
		this.paramName = paramName;
	}
	/**
	 * Returns the value of  paramName.
	 *
	 * @return the paramName
	 */
	public String getParamName() {
		return paramName;
	}
	/**
	 * Set the value of input parameter to field paramName.
	 *
	 * @param paramName the paramName to set
	 */
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
}
