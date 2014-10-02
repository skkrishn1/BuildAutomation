/**
 * 
 */
package com.ap.ui.portal.installation.command.impl;

import com.ap.ui.portal.installation.command.exception.CommandExecutionException;
import com.ap.ui.portal.installation.command.model.Input;
import com.ap.ui.portal.installation.command.model.Result;
import com.ap.ui.portal.installation.util.MessageUtil;
import com.ap.ui.portal.installation.util.ProcessUtil;

/**
 * This command starts the local weblogic instance.
 *
 * @author Abhishek Tembe
 *
 */
public class StartWebLogicInstance extends AbstractExecuteCommand {

	/**
	 * {@inheritDoc}
	 *
	 * @see com.ap.ui.portal.installation.command.impl.AbstractExecuteCommand#execute(com.ap.ui.portal.installation.command.model.Input)
	 */
	@Override
	public Result execute(Input input) throws CommandExecutionException {
		MessageUtil.logInfoMessage("Starting the weblogic server.");
		
		return super.execute(input);
	}
	
	/**
	 * Returns the key, using which the value of the execution command
	 * is to be retrieved. The key would represent startup command. 
	 * 
	 * @return the key, using which the value of the execution command
	 * 		   is to be retrieved. 
	 */
	@Override
	protected String getExecutionCommandKey() {
		if (ProcessUtil.isWindowsOS()) {
			return "windows.weblogic.startup.command";
		}
		
		return "weblogic.startup.command";
	}
	
	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see com.ap.ui.portal.installation.command.impl.AbstractExecuteCommand#getCommandCompletionMessage()
	 */
	@Override
	public String getCommandCompletionMessage() {
		// return "<Server started in RUNNING mode>";
		return null;
	}
	
}
