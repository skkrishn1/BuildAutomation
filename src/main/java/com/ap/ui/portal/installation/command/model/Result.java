/**
 * 
 */
package com.ap.ui.portal.installation.command.model;


/**
 * This class represents the output entity 
 * after execution of the  
 * {@link com.ap.ui.portal.installation.command.InstallationCommand} 
 * object.
 * 
 * @see com.ap.ui.portal.installation.command.InstallationCommand#execute(Input)
 *
 * @author Abhishek Tembe
 *
 */
public class Result extends AbstractModel {
	/**
	 * Represents successful execution.
	 */
	public final static int SUCCESS = 1;
	/**
	 * Represents execution failure.
	 */
	public final static int FAILURE = -1;
	
	/**
	 * Status of execution of the command.
	 */
	private int completionStatus;
	/**
	 * Any message / extra information
	 * associated with the execution of the 
	 * command.
	 */
	private String completionMessage;
	/**
	 * Returns the value of  completionStatus.
	 *
	 * @return the completionStatus
	 */
	public int getCompletionStatus() {
		return completionStatus;
	}
	/**
	 * Set the value of input parameter to field completionStatus.
	 *
	 * @param completionStatus the completionStatus to set
	 */
	public void setCompletionStatus(int completionStatus) {
		this.completionStatus = completionStatus;
	}
	/**
	 * Returns the value of  completionMessage.
	 *
	 * @return the completionMessage
	 */
	public String getCompletionMessage() {
		return completionMessage;
	}
	/**
	 * Set the value of input parameter to field completionMessage.
	 *
	 * @param completionMessage the completionMessage to set
	 */
	public void setCompletionMessage(String completionMessage) {
		this.completionMessage = completionMessage;
	}
}
