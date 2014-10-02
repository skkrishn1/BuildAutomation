/**
 * 
 */
package com.ap.ui.portal.installation.command.model;

/**
 * Represents the execution result.
 *
 * @author Abhishek Tembe
 *
 */
public class ExecutionResult {

	/**
	 * Information of the command to be executed.
	 */
	private String command;
	/**
	 * Response of the command
	 */
	private String response;
	/**
	 * Exit value of the command.
	 */
	private int exitValue;
	
	/**
	 * Constructor
	 *
	 */
	public ExecutionResult() {
		super();
	}
	/**
	 * Constructor
	 *
	 * @param command command executed
	 * @param response response of the command execution
	 * @param exitValue exit value of the command execution
	 */
	public ExecutionResult(String command, String response, int exitValue) {
		super();
		this.command = command;
		this.response = response;
		this.exitValue = exitValue;
	}


	/**
	 * Returns the value of  command.
	 *
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Set the value of input parameter to field command.
	 *
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * Returns the value of  response.
	 *
	 * @return the response
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * Set the value of input parameter to field response.
	 *
	 * @param response the response to set
	 */
	public void setResponse(String response) {
		this.response = response;
	}

	/**
	 * Returns the value of  exitValue.
	 *
	 * @return the exitValue
	 */
	public int getExitValue() {
		return exitValue;
	}

	/**
	 * Set the value of input parameter to field exitValue.
	 *
	 * @param exitValue the exitValue to set
	 */
	public void setExitValue(int exitValue) {
		this.exitValue = exitValue;
	}
	
	

}
