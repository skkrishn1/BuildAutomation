package com.ap.ui.portal.installation.command;

import com.ap.ui.portal.installation.command.exception.CommandExecutionException;
import com.ap.ui.portal.installation.command.model.Input;
import com.ap.ui.portal.installation.command.model.Result;

/**
 * Interface defining a command. This command (its descendants) can be 
 * executed using {@link InstallationCommandExecutor}.
 *
 * @see InstallationCommandExecutor
 * 
 * @author Abhishek Tembe
 *
 */
public interface InstallationCommand {
	
	/**
	 * Execute a command. A command can have input parameters passed
	 * in form of {@link Input} object. This method is supposed 
	 * to return the result/response of this command execution 
	 * as {@link Result} object. 
	 * 
	 * @param input {@link Input} object, to contain input consumable 
	 * 				by the command class's execute method.
	 * 
	 * @return the result/response of this command execution 
	 * 		   as {@link Result} object.
	 * 
	 * @throws CommandExecutionException in case of error.
	 * 
	 * @see InstallationCommandExecutor
	 */
	Result execute (Input input) throws CommandExecutionException; 
}
