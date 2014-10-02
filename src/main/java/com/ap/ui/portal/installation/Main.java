/**
 * 
 */
package com.ap.ui.portal.installation;

import com.ap.ui.portal.installation.command.InstallationCommandExecutor;
import com.ap.ui.portal.installation.command.exception.CommandExecutionException;
import com.ap.ui.portal.installation.command.model.Input;
import com.ap.ui.portal.installation.command.model.Result;
import com.ap.ui.portal.installation.data.Commands;
import com.ap.ui.portal.installation.util.MessageUtil;
import com.ap.ui.portal.installation.util.ParameterHelperUtil;
import com.ap.ui.portal.installation.util.PropertiesFileNotFoundException;
import com.ap.ui.portal.installation.util.PropertiesUtil;

/**
 * This is the "Main" class. Run this class in order to
 * start the build automation.
 *
 * @author Abhishek Tembe
 *
 */
public class Main {

	
	/**
	 * This is the start point for the build automation process.
	 * 
	 * @param args arguments passed to the application
	 */
	public static void main(String[] args) {
		
		// Check if the necessary parameters are passed from command line.
		// The caller is expected to pass the settings file as an input. 
		boolean isSettingsFileProvided = ParameterHelperUtil.checkParameterExists(args, "file");
		// In case the settings file is not passed, provide an option for the 
		// user to create a template settings file. This is done along with
		// displaying the help message.
		if (!isSettingsFileProvided) {
			// Display the error message.
			System.err.println("Settings file is required. Please specify the parameter -Dfile:<Path of settings.properties file>.\n");
			// Display the help message. Also, provide option to create
			// or overwrite the settings.properties file.
			MessageUtil.displayHelpMessage();
			return;
		}
		
		// Here, all the required parameters are gathered. Now the 
		// actual automation is to begin.
		
		// First, initialise the system to get it ready for automation.
		initialize(args);
		
		// Perform the actions required to automate the build deployment
		// process.
		performActions(); 
	}


	/**
	 * Perform the actions required to automate the build deployment
	 * process.
	 */
	private static void performActions() {
		// Log start of execution.
		MessageUtil.logStartOfExecution();
		
		// Step 1: Stop the weblogic instance.
		executeCommand(Commands.SHUTDOWN_WEBLOGIC_INSTANCE, null);
		// Step 2: Download the required Nexus artifacts.
		executeCommand(Commands.DOWNLOAD_NEXUS_ARTIFACTS, null);
		// Step 3: Replace the portal ext.properties 
		executeCommand(Commands.REPLACE_PORTAL_EXT_PROPERTIES, null);
		// Step 4: Start the weblogic instance.
		executeCommand(Commands.START_WEBLOGIC_INSTANCE, null);
		
		// Log end of execution.
		MessageUtil.logEndOfExecution();
	}


	/**
	 * Execute a specific command specified as input parameter.
	 * 
	 * @param command {@link Commands} instance, specifying which command 
	 * 				  to execute.
	 * @param input {@link Input} instance, passed while execution of the 
	 * 				command.
	 * 
	 * @return {@link Result} instance, as response of the execution 
	 * 		   of the command.
	 */
	private static Result executeCommand(Commands command, Input input) {
		Result result = null;
		// Execute the command.
		try {
			// Execute the command.
			result = InstallationCommandExecutor.executeCommand(command.getFullyQualifiedName(), input);
			// Log successful execution.
			MessageUtil.logDebugMessage("Executed command " + command.name() + " successfully.");
		} catch (CommandExecutionException e) {
			MessageUtil.logErrorMessage("Could not execute command " + command.name() + ". Reason: " + e);
			// In case the execution was unsuccessful, 
			// appropriate values in the result object.
			result = new Result();
			result.copyAttributes(input);
			result.setCompletionStatus(Result.FAILURE);
			result.setCompletionMessage("Failed due to : " + e);
		}
		
		return result;
	}


	/**
	 * Initialise the system to start the build automation process.
	 * @param args argumets passed from command line.
	 */
	private static void initialize(String[] args) {
		initSettingsFile(args);
	}
	
	/**
	 * Read the &quot;file&quot; parameter passed from command line, and
	 * process the parameters mentioned in the file.
	 * 
	 * @param args command line arguments.
	 */
	private static void initSettingsFile(String[] args) {
		// Read the name of the file passed as command line parameter "-Dfile:"
		String settingsFile = ParameterHelperUtil.getParamValue(args, "file");
		// Load the properties file.
		loadPropertiesFile(settingsFile);
	}
	/**
	 * Register the properties file with the {@link PropertiesUtil} utility class
	 * so that it can be referenced via {@link PropertiesUtil} APIs.
	 * 
	 * @param settingsFile properties file to load.
	 */
	private static void loadPropertiesFile(String settingsFile) {
		try {
			PropertiesUtil.loadProperties(settingsFile);
		} catch (PropertiesFileNotFoundException e) {
			MessageUtil.logErrorMessage("Could not load the settings file:" + settingsFile);
			System.exit(-1);
		}
	}

}
