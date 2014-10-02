/**
 * 
 */
package com.ap.ui.portal.installation.command.impl;

import com.ap.ui.portal.installation.command.InstallationCommand;
import com.ap.ui.portal.installation.command.InstallationCommandExecutor;
import com.ap.ui.portal.installation.command.exception.CommandExecutionException;
import com.ap.ui.portal.installation.command.model.ExecutionResult;
import com.ap.ui.portal.installation.command.model.Input;
import com.ap.ui.portal.installation.command.model.Result;
import com.ap.ui.portal.installation.util.MessageUtil;
import com.ap.ui.portal.installation.util.ProcessUtil;
import com.ap.ui.portal.installation.util.PropertiesUtil;

/**
 * This command start/stop the local weblogic instance or execute 
 * the commands.
 *
 * @author Abhishek Tembe
 *
 */
public abstract class AbstractExecuteCommand implements InstallationCommand {
	
	/**
	 * This command start/stop the local weblogic instance or execute 
	 * the commands.
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
		try {
			// Routine initialisations
			initialize(input);
			
		} catch (CommandExecutionException e) {
			String message = "Could compete command " + this.getClass().getName() + ". Reason: " + e.getMessage();
			MessageUtil.logErrorMessage(message);
			
			// Create message with appropriate completion status. 
			Result result = new Result();
			result.setCompletionMessage(message);
			result.setCompletionStatus(Result.FAILURE);
			result.copyAttributes(input);
			return result;
		}
		
		// Run command
		String command = getCommand();
		
		ExecutionResult status = null;
		
		if (ProcessUtil.isWindowsOS()) {
			status = ProcessUtil.executeCommand(command, getCommandCompletionMessage());
		} else {
			status = ProcessUtil.executeCommandSynchronously(command, getCommandCompletionMessage());
		}
		
		// Log messages.
		MessageUtil.logInfoMessage("Command executed: " + command);
		MessageUtil.logInfoMessage("Exit code: " + status.getExitValue());
		
		// Return result.
		Result result = new Result();
		result.setCompletionMessage("Completed execution of " + this.getClass().getName() + "\nResponse:\n" + status.getResponse());
		result.setCompletionStatus(Result.SUCCESS);
		result.copyAttributes(input);
		return result;
	}

	/**
	 * Initialise the command
	 * @param input input to command.
	 * @throws CommandExecutionException in case the command was not found.
	 */
	protected void initialize(Input input) throws CommandExecutionException {
		/*String command = getCommand();
		boolean doesCommandExist = IOUtil.checkIfAlreadyExists(command);
		if (!doesCommandExist) {
			throw new CommandExecutionException("Command " + command + " could not be found.");
		}*/
	}

	/**
	 * Returns the key, using which the value of the execution command
	 * is to be retrieved. 
	 * 
	 * @return the key, using which the value of the execution command
	 * 		   is to be retrieved. 
	 */
	protected abstract String getExecutionCommandKey();

	
	/**
	 * Returns the completion message for the command.
	 * This message, when displayed, it can be deduced that 
	 * the command execution is complete.
	 * @return the completion message for the command.
	 */
	public abstract String getCommandCompletionMessage();
	
	/**
	 * Wait for specified seconds.
	 * @param seconds seconds to wait.
	 */
	protected void waitForSeconds(int seconds) {
		try {
		    Thread.sleep(1000);
		} catch(InterruptedException ex) {
			return;
		}
	}

	/**
	 * Get command to execute.
	 * @return command to execute.
	 */
	private String getCommand() {
		String managedServerNames = PropertiesUtil.getProperty("target.weblogic.managed.server.names", "");
		return ProcessUtil.getExecutionCommand(getExecutionCommandKey(), new String[]{managedServerNames});
	}
	
	
}
