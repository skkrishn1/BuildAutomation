/**
 * 
 */
package com.ap.ui.portal.installation.command;

import com.ap.ui.portal.installation.command.exception.CommandExecutionException;
import com.ap.ui.portal.installation.command.model.Input;
import com.ap.ui.portal.installation.command.model.Result;

/**
 * This class provides API to execute a particular command.
 * 
 * @see InstallationCommand
 *
 * @author Abhishek Tembe
 *
 */
public class InstallationCommandExecutor {

	/**
	 * Private Constructor
	 */
	private InstallationCommandExecutor() {
	}
	
	/**
	 * Executes a particular command. The command should be descendant of the 
	 * {@link InstallationCommand} interface. Fully qualified name of the 
	 * command class is to be specified as input parameter. This executor class
	 * fetches the command object via reflection, and executes 
	 * the &quot;{@link InstallationCommand#execute(Input)};&quot; method.
	 * 
	 * @param fullyQualifiedName fully qualified name of the Command object.
	 * @param input {@link Input} object required for  
	 * 				&quot;{@link InstallationCommand#execute(Input)};&quot;
	 * 				method. 
	 * 
	 * @return {@link Result} object, representing the execution response of 
	 * 		   the command object.
	 * 
	 * @throws CommandExecutionException in case of issues in execution of the
	 * 									 command.
	 * @see InstallationCommand#execute(Input)
	 */
	public static final Result executeCommand(String fullyQualifiedName, Input input) throws CommandExecutionException {
		try {
			// Get the instance of the Command object, via Reflection.
			InstallationCommand command = getCommand(fullyQualifiedName);
			// Execute the "execute" method.
			final Result result = command.execute(input);
			// Return the result.
			return result;
		} catch (InstantiationException e) {
			throw new CommandExecutionException(e);
		} catch (IllegalAccessException e) {
			throw new CommandExecutionException(e);
		} catch (ClassNotFoundException e) {
			throw new CommandExecutionException(e);
		}
	}
	
	
	/**
	 * Fetches new instance of the {@link InstallationCommand} object, via  
	 * Reflection.
	 * 
	 * @param fullyQualifiedName fully qualified name of the command class.
	 * 
	 * @return new instance of the command object.
	 * 
	 * @throws InstantiationException issues in fetching new Command instance
	 * @throws IllegalAccessException issues in fetching new Command instance
	 * @throws ClassNotFoundException issues in fetching new Command instance
	 */
	private static InstallationCommand getCommand(String fullyQualifiedName) 
	throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		InstallationCommand _instance = (InstallationCommand) Class.forName(fullyQualifiedName).newInstance();
		return _instance;
	}

}
