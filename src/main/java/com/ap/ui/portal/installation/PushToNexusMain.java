package com.ap.ui.portal.installation;

import java.io.File;
import java.nio.file.Paths;

import com.ap.ui.portal.installation.command.InstallationCommandExecutor;
import com.ap.ui.portal.installation.command.exception.CommandExecutionException;
import com.ap.ui.portal.installation.command.model.Input;
import com.ap.ui.portal.installation.command.model.KeyValuePairModel;
import com.ap.ui.portal.installation.command.model.Result;
import com.ap.ui.portal.installation.data.Attributes;
import com.ap.ui.portal.installation.data.Commands;
import com.ap.ui.portal.installation.util.MessageUtil;
import com.ap.ui.portal.installation.util.PropertiesUtil;



/**
 * This is the start point for the push to Nexus 
 * automation.
 * 
 * Here, the tool asks for all the required inputs.
 *
 * @author Abhishek Tembe
 *
 */
public class PushToNexusMain {
	/*
	 * Default values for the various parameters entered by the 
	 * user.
	 */
	private static final String DEFAULT_MILESTONE_VERSION = "7.0";
	private static final String DEFAULT_SVN_URL_VERSION = "http://10.105.34.61/alternative-payments-svn/AP/Liferay/";
	private static final String DEFAULT_SVN_TAG_VERSION = "ap-portal-";
	private static final String DEFAULT_REPOSITORY_ID_VERSION = "releases";

	/**
	 * This is the start point for the Nexus upload 
	 * automation. 
	 * 
	 * @param args arguments for Nexus upload 
	 * 			   automation.
	 */
	public static void main(String[] args) {
		// Enable debug mode, so that debug messages are displayed.
		PropertiesUtil.enableDebugMode(true);
		
		
		// Print the Welcome message.
		printWelcomeMessage();
		
		// Read the value for milestone
		String milestoneValue = readMilestoneValue();
		// Get the SVN details.
		String svnURL = readSVNurl();
		// Read the tag name to be applied to the code.
		String svnTag = readSVNTag(milestoneValue);
		// Read the SVN user name.
		String svnUserName = readSVNUsername();
		// Read the SVN password.
		String svnUserPassword = readSVNPassword();
		// Read the directory entire build is to be executed
		String buildDirectory = readBuildDirectory();
		// Read repository ID.
		String reporsitoryID = readRepositoryID();
		// Read the path of the enhanced war directory.
		String enhancedWarDirectory = readEnhancedWarDirectory();
		
		System.out.println();
		
		// Collect the user inputs into Input object.
		Input input = createInputInstance(milestoneValue, svnURL, svnTag, svnUserName, svnUserPassword, buildDirectory, reporsitoryID, enhancedWarDirectory);
		// Verify the user Inputs.
		verifyUserInputs(input);
		
		//Perform actions required for pushing 
		// artifacts to Nexus.
		performActions(input);
		
	}

	/**
	 * Prints Welcome message.
	 */
	private static void printWelcomeMessage() {
		StringBuilder msg = new StringBuilder();
		msg.append("This tool will upload the Zapp Portal artifacts to Nexus.\n");
		msg.append("Please enter the following details in order to proceed.");
		System.out.println(msg.toString());
	}
	

	/**
	 * Read the value for milestone version
	 * @return value entered by user.
	 */
	private static String readMilestoneValue() {
		// Display question and message.
		System.out.print("Please enter the milestone version \n[Press enter to use \"" + DEFAULT_MILESTONE_VERSION + "\"]:");
		return readValueFromConsole(DEFAULT_MILESTONE_VERSION);
	}
	/**
	 * Read the value for SVN URL
	 * @return value entered by user.
	 */
	private static String readSVNurl() {
		// Display question and message.
		System.out.print("Please enter the milestone version \n[Press enter to use \"" + DEFAULT_SVN_URL_VERSION + "\"]:");
		return readValueFromConsole(DEFAULT_SVN_URL_VERSION);
	}
	/**
	 * Read the value for SVN tag
	 * @param milestoneValue Milestone version value, which will
	 * 						 be appended to the svn tag's default 
	 * 					     value.
	 * @return value entered by user.
	 */
	private static String readSVNTag(String milestoneValue) {
		// Display question and message.
		System.out.print("Please enter the SVN tag that will be applied to the code \n[Press enter to use \"" + DEFAULT_SVN_TAG_VERSION + milestoneValue + "\"]:");
		return readValueFromConsole(DEFAULT_SVN_TAG_VERSION + milestoneValue);
	}
	/**
	 * Read the value for SVN user
	 * @return value entered by user.
	 */
	private static String readSVNUsername() {
		// Display question and message.
		System.out.println("Please enter the SVN credentials used to checkout the code.");
		System.out.print("SVN username:");
		return readValueFromConsole("");
	}
	/**
	 * Read the value for SVN Password
	 * @return value entered by user.
	 */
	private static String readSVNPassword() {
		// Display question and message.
		System.out.print("Password:");
		return readPasswordValueFromConsole("");
	}
	
	/**
	 * Read the value for reporsitoryID
	 * @return value entered by user.
	 */
	private static String readRepositoryID() {
		// Display question and message.
		System.out.print("Please enter repository ID defined in the settings.xml, to be used for pushing artifacts to Nexus \n[Press enter to use \"" + DEFAULT_REPOSITORY_ID_VERSION + "\"]:");
		return readValueFromConsole(DEFAULT_REPOSITORY_ID_VERSION);
	}
	
	/**
	 * Read the value for Build Directory
	 * @return value entered by user.
	 */
	private static String readBuildDirectory() {
		// Display question and message.
		String currentDirectory = Paths.get("").toAbsolutePath().toString();
		System.out.print("Please enter folder path where entire code is to be checked-out and built \n[Press enter to use \"" + currentDirectory + "\"]:");
		return readValueFromConsole(currentDirectory);
	}
	/**
	 * Read the value for milestone version
	 * @return value entered by user.
	 */
	private static String readEnhancedWarDirectory() {
		// Display question and message.
		String currentDirectory = Paths.get("").toAbsolutePath().toString();
		System.out.print("Please enter the path of the folder, where ehnaced war files will be placed \n[Press enter to use \"" + currentDirectory + "\"]:");
		return readValueFromConsole(currentDirectory);
	}
	
	/**
	 * Reads value from console. In case the user does not specify the 
	 * value, use default value.
	 * 
	 * @param defaultValue default value to use in case the user does not
	 * 					   specify any input value.
	 * 
	 * @return value entered by user or default value, as applicable.
	 */
	private static String readValueFromConsole(String defaultValue) {
		// Read the value entered by user
		String consoleValue = readFromConsole();
		// If the user pressed enter without specifying value, 
		// use the default value.
		if (consoleValue == null || "".equals(consoleValue)) {
			return defaultValue;
		}
		
		// trim the input and use it.
		return consoleValue.trim();
	}
	
	/**
	 * Reads value from console. In case the user does not specify the 
	 * value, use default value.
	 * 
	 * @param defaultValue default value to use in case the user does not
	 * 					   specify any input value.
	 * 
	 * @return value entered by user or default value, as applicable.
	 */
	private static String readPasswordValueFromConsole(String defaultValue) {
		// Read the value entered by user
		String consoleValue = readPasswordValueFromConsole();
		// If the user pressed enter without specifying value, 
		// use the default value.
		if (consoleValue == null || "".equals(consoleValue)) {
			return defaultValue;
		}
		
		// trim the input and use it.
		return consoleValue.trim();
	}

	/**
	 * Reads value from console.
	 * @return value entered by user on console.
	 */
	private static String readFromConsole() {
		return MessageUtil.readFromCommandLine();
	}
	
	/**
	 * Reads value from console.
	 * @return value entered by user on console.
	 */
	private static String readPasswordValueFromConsole() {
		return MessageUtil.readPasswordValueFromCommandLine();
	}
	/**
	 * Perform the actions required to automate push to
	 * Nexus.
	 * @param input {@link Input} object.
	 */
	private static void performActions(Input input) {
		// Log start of execution.
		MessageUtil.logStartOfExecution();
		
		// Step 1: Tag the SVN code.
		Result result = executeCommand(Commands.TAG_SVN_CODE, input);
		input.copyAttributes(result);
		// Step 2: Build the code.
		result = executeCommand(Commands.BUILD_CHECKED_OUT_CODE, input);
		input.copyAttributes(result);
		// Step 3: Upload Artifacts to nexus
		result = executeCommand(Commands.PUSH_ATRIFACTS_TO_NEXUS, input);
		input.copyAttributes(result);
		
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
			MessageUtil.logDebugMessage("Started execution of command " + command.name());
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
	
	
	private static Input createInputInstance(String milestoneValue,
											 String svnURL, String svnTag, 
											 String svnUserName,
											 String svnUserPassword, 
											 String buildDirectory, 
											 String reporsitoryID,
											 String enhancedWarDirectory) {
		Input input = new Input();
		input.addAttribute(Attributes.MILESTONE_VERSION.getParamName(), new KeyValuePairModel(Attributes.MILESTONE_VERSION.getParamName(), milestoneValue));
		input.addAttribute(Attributes.BUILD_DIRECTORY_NAME.getParamName(), new KeyValuePairModel(Attributes.BUILD_DIRECTORY_NAME.getParamName(), svnURL));
		input.addAttribute(Attributes.ENHANCED_WAR_DIRECTORY.getParamName(), new KeyValuePairModel(Attributes.ENHANCED_WAR_DIRECTORY.getParamName(), svnTag));
		input.addAttribute(Attributes.SVN_URL.getParamName(), new KeyValuePairModel(Attributes.SVN_URL.getParamName(), svnUserName));
		input.addAttribute(Attributes.SVN_TAG.getParamName(), new KeyValuePairModel(Attributes.SVN_TAG.getParamName(), svnUserPassword));
		input.addAttribute(Attributes.SVN_USER_NAME.getParamName(), new KeyValuePairModel(Attributes.SVN_USER_NAME.getParamName(), buildDirectory));
		input.addAttribute(Attributes.SVN_PASSWORD.getParamName(), new KeyValuePairModel(Attributes.SVN_PASSWORD.getParamName(), buildDirectory));
		input.addAttribute(Attributes.REPOSITORY_ID.getParamName(), new KeyValuePairModel(Attributes.REPOSITORY_ID.getParamName(), buildDirectory));
		return input;
	}
	
	/**
	 * Verify the user inputs. Exists the application if any of the 
	 * inputs are incorrect.
	 * 
	 * @param input user inputs collected in {@link Input} object.
	 */
	private static void verifyUserInputs(Input input) {
		verifyIfExist(input, Attributes.BUILD_DIRECTORY_NAME.getParamName(), "Build Directory.");
		verifyIfExist(input, Attributes.ENHANCED_WAR_DIRECTORY.getParamName(), "path to enhanced war files.");
		verifyDefined(input, Attributes.MILESTONE_VERSION.getParamName());
		verifyDefined(input, Attributes.SVN_URL.getParamName());
		verifyDefined(input, Attributes.SVN_TAG.getParamName());
		verifyDefined(input, Attributes.SVN_USER_NAME.getParamName());
		verifyDefined(input, Attributes.SVN_PASSWORD.getParamName());
		verifyDefined(input, Attributes.REPOSITORY_ID.getParamName());
	}

	/**
	 * Checks if the directory exists. If it does not exist, it
	 * is created. If the passed input points to a file or 
	 * something other than a directory, the system halts with
	 * appropriate error message.
	 * 
	 * @param input input user inputs collected in {@link Input} object.
	 * @param key key for directory to check.
	 * @param messageFragment used in log.
	 */
	private static void verifyIfExist(Input input, String key, String messageFragment) {
		// Read the directory name.
		String directoryName = input.getAttribute(key).getValue();
		
		File directory = new File(directoryName);
		
		if (!directory.exists()) {
			MessageUtil.logWarningMessage("Directory " + directoryName + " doesnot exist. Creating it.");
			directory.mkdirs();
			MessageUtil.logDebugMessage("Directory created: " + directory.getAbsolutePath());
		}
		if (directory.isFile()) {
			MessageUtil.logErrorMessage("\"" + directoryName + "\" is supposed to be a directory, but was found to be a file.");
			MessageUtil.logErrorMessage("Application cannot proceed. Halting with error!!!");
			System.exit(1);
		}
		MessageUtil.logDebugMessage("Using the directory \"" + directoryName + "\" as " + messageFragment);
	}

	/**
	 * Verifies if the value is defined in Input object.
	 * In case the value is not defined, the system will
	 * exit, providing appropriate error message.
	 * 
	 * @param input Input object.
	 * @param key key for which the value should exist.
	 */
	private static void verifyDefined(Input input, String key) {
		if (isEmpty(input.getAttribute(key).getValue())) {
			MessageUtil.logErrorMessage("Parameter \"" + key + "\" is mandatory. Please specify it.");
			MessageUtil.logErrorMessage("Application cannot proceed. Halting with error!!!");
			System.exit(1);
		}
	}

	/**
	 * Checks if the value is empty.
	 * @param value value to check
	 * @return <code>true</code> if the value is null or empty
	 * 		   or contains only whitespaces.
	 */
	private static boolean isEmpty(String value) {
		return value == null || "".equals(value.trim());
	}
}



