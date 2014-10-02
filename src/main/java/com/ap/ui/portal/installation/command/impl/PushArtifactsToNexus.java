/**
 * 
 */
package com.ap.ui.portal.installation.command.impl;

import com.ap.ui.portal.installation.command.InstallationCommand;
import com.ap.ui.portal.installation.command.InstallationCommandExecutor;
import com.ap.ui.portal.installation.command.exception.CommandExecutionException;
import com.ap.ui.portal.installation.command.model.Input;
import com.ap.ui.portal.installation.command.model.Result;
import com.ap.ui.portal.installation.util.MessageUtil;

/**
 * This command pushes the enhanced war files and other 
 * artifacts to Nexus.
 *
 * @author Abhishek Tembe
 *
 */
public class PushArtifactsToNexus implements InstallationCommand {

	/**
	 * This command pushes the enhanced war files and other 
	 * artifacts to Nexus.
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
	 * @see com.ap.ui.portal.installation.command.InstallationCommand#execute(com.ap.ui.portal.installation.command.model.Input)
	 */
	public Result execute(Input input) throws CommandExecutionException {
		MessageUtil.logInfoMessage("Starting the weblogic server.");
		
		return null;
	}
	
}
