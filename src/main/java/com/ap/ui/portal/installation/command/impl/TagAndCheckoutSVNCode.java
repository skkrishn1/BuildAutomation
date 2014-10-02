/**
 * 
 */
package com.ap.ui.portal.installation.command.impl;

import com.ap.ui.portal.installation.command.InstallationCommand;
import com.ap.ui.portal.installation.command.InstallationCommandExecutor;
import com.ap.ui.portal.installation.command.exception.CommandExecutionException;
import com.ap.ui.portal.installation.command.model.Input;
import com.ap.ui.portal.installation.command.model.Result;
import com.ap.ui.portal.installation.data.Attributes;
import com.ap.ui.portal.installation.util.MessageUtil;

/**
 * This command creates SVN tag to the codebase and 
 * checks out this newly created tagged codebase to 
 * specified folder.
 *
 * @author Abhishek Tembe
 *
 */
public class TagAndCheckoutSVNCode implements InstallationCommand {

	/**
	 * This command creates SVN tag to the code base and 
	 * checks out this newly created tagged code base to 
	 * specified folder.
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
		String svnTag = input.getAttribute(Attributes.SVN_TAG.getParamName()).getValue();
		String svnURL = input.getAttribute(Attributes.SVN_URL.getParamName()).getValue();
		String svnTagCompleteURL = getSVNTagURL(svnURL, svnTag);
		
		MessageUtil.logInfoMessage("Attempting to apply tag: " + svnTag);
		
		String message = new StringBuilder().append("Created new tag ")
										    .append(svnTag)
										    .append("URL is ")
										    .append(svnTagCompleteURL)
						 .toString();
		
		Result result = new Result();
		result.copyAttributes(input);
		result.setCompletionStatus(Result.SUCCESS);
		result.setCompletionMessage(message);
		return result;
	}

	private String getSVNTagURL(String svnURL, String svnTag) {
		return svnURL + "/tags/" + svnTag + "/";
	}
	
}
