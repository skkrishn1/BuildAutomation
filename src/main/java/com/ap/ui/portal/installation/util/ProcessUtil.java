/**
 * 
 */
package com.ap.ui.portal.installation.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.ap.ui.portal.installation.command.exception.CommandExecutionException;
import com.ap.ui.portal.installation.command.model.ExecutionResult;

/**
 * This class contains APIs and helper methods 
 * to execute a command on bash/command line.
 *
 * @author Abhishek Tembe
 *
 */
public class ProcessUtil {
	/**
	 * Private Constructor
	 */
	private ProcessUtil() {
		// Private constructor.
	}
	
	/**
	 * Retrieves the command to be executed on the command line/bash
	 * based on the key passed as input. The key is supposed to be 
	 * defined in &quot;server.properties&quot;.
	 * 
	 * @param key key representing the command to be executed.
	 * @param replacementParameters replacement parameters, to be 
	 * 								replaced in the value fetched against
	 * 							    the key, passed as input.
	 * 
	 * @return actual command (based on the OS as well as 
	 * 		   all the replacement parameters replaced. 
	 */
	public static String getExecutionCommand(String key, String... replacementParameters) {
		String baseDirectory = getBaseDirectory();
		
		String command = PropertiesUtil.getProperty(key, replacementParameters);
		
		// Prepend baseDirectory
		command = baseDirectory + command;
		
		return command;
		
	}

	/**
	 * Executes the command.
	 * 
	 * @param command command to execute.
	 * @param completionMessage message, which when displayed, the 
	 * 						    command is expected to be completed.
	 * 
	 * @return {@link ExecutionResult} instance, representing the 
	 * 		   response of the execution of the command.
	 */
	public static ExecutionResult executeCommand(final String command, String completionMessage) throws CommandExecutionException {
		// Execute the command in new thread.
		new Thread(
				new Runnable() {
					public void run() {
						try {
							String[] commandParams = getExecutionCommandParameters(command);
							MessageUtil.logDebugMessage(getPrintableCommand(commandParams));
							Runtime.getRuntime().exec(commandParams);
						} catch (IOException e) {
							MessageUtil.logErrorMessage("Could not execute Command " + command + " Reason: " + e);
						} 
						
					}
				}
		).start();
		
		// Wait for 10 seconds
		try {
			Thread.sleep(10*1000);
		} catch (InterruptedException e) {
			// Do nothing.
		}
		
		return new ExecutionResult(command, "Started execution of command " + command, 0);
	}
	
	
	/**
	 * Executes the command.
	 * 
	 * @param command command to execute.
	 * @param completionMessage message, which when displayed, the 
	 * 						    command is expected to be completed.
	 * 
	 * @return {@link ExecutionResult} instance, representing the 
	 * 		   response of the execution of the command.
	 */
	public static ExecutionResult executeCommandSynchronously(String command, String completionMessage) throws CommandExecutionException {
		try {
			
			String[] commandParams = getExecutionCommandParameters(command);
			MessageUtil.logDebugMessage(getPrintableCommand(commandParams));
			
			// Execute Process
			Process process = Runtime.getRuntime().exec(commandParams);
			// process.waitFor();
			
			// Wait till the process execution completes
			// Meanwhile, print the output to console.
			InputStream in = process.getInputStream();
			BufferedReader reader =  new BufferedReader(new InputStreamReader(in));
			
			String line = reader.readLine();
			while(line != null) {
				
				/*if (completionMessage != null && line.toLowerCase().contains(completionMessage.toLowerCase())) {
					break;
				}*/
				/*if (!line.contains("DEBUG")) { */
				MessageUtil.logDebugMessage(line);
				/*}*/
				line =  reader.readLine();
			}
			
			return new ExecutionResult(command, "Started execution of command " + command, 0);
			
		} catch (IOException e) {
			throw new CommandExecutionException("Could not execute Command " + command, e);
		}  /*catch (InterruptedException e) {
			throw new CommandExecutionException("Could not execute Command " + command, e);
		}*/
	}
	
	/**
	 * Determines if the OS is Windows based OS.
	 * 
	 * @return <code>true</code> if the underlying OS
	 * 		   is (determinable) Windows OS.
	 */
	public static boolean isWindowsOS() {
		return System.getProperty("os.name").startsWith("Windows");
	}
	
	/**
	 * Retrieves the execution command parameters, in format to be 
	 * passed to Runtime.getRuntime().exec(String[]) command.
	 * 
	 * @param command actual executable command.
	 * 
	 * @return the execution command parameters, in format to be 
	 * 		   passed to Runtime.getRuntime().exec(String[]) command.
	 */
	private static String[] getExecutionCommandParameters(String command) {
		if (isWindowsOS()) {
			return new String[]{"cmd", "/C", "start cmd /C", command};
		}
		return new String[]{"bash","-c", command};
	}

	/**
	 * Determines the base directory value.
	 * @return the base directory value.
	 */
	private static String getBaseDirectory() {
		// target.weblogic.domain.base.directory takes preference.
		// If target.weblogic.domain.base.directory i not defined,
		// <target.weblogic.domain.base.directory>/bin/ takes preference.
		// If target.weblogic.domain.base.directory is not defined,
		// current directory is considered to be base directory 
		
		String binDirectory = PropertiesUtil.getProperty("target.weblogic.domain.bin.directory");
		String baseDirValue = PropertiesUtil.getProperty("target.weblogic.domain.base.directory");
		
		if (baseDirValue == null) {
			baseDirValue = "./";
		} else {
			baseDirValue = baseDirValue + File.separator + "bin/";
		}
		
		binDirectory = (binDirectory != null) ? (	binDirectory.endsWith(File.separator) 
													? binDirectory 
													: binDirectory + File.separator
												)
						  				      : baseDirValue;
		
		return binDirectory;
	}
	
	/**
	 * Returns the command in printable format, so that it can be logged.
	 * @param commandParams parameters, representing command to be executed.
	 * @return the command in printable format, so that it can be logged.
	 */
	private static String getPrintableCommand (String[] commandParams) {
		StringBuffer result = new StringBuffer();
		result.append("Runtime.getRuntime().exec(new String[]{");
		for (int i = 0; i < commandParams.length; i++) {
		   result.append( "\"" + commandParams[i] + "\"," );
		}
		result.append("})");
		return result.toString();
	}
//	/**
//	 * Determines the extension of the command based on the
//	 * underlying OS. Currently supports .cmd and .sh only.
//	 * @return the extension of the command based on the
//	 * 		   underlying OS. Currently supports 
//	 * .cmd and .sh only.
//	 */
//	private static String getExtension() {
//		boolean isWindowsOS = isWindowsOS();
//		if (isWindowsOS) {
//			return "cmd";
//		} else {
//			return "sh";
//		}
//	}
//	/**
//	 * Process the command, prepend and append strings if required 
//	 * by the underlying OS. 
//	 * 
//	 * @param command command to execute.
//	 * 
//	 * @return final command to execute.
//	 */
//	private static String prePendAppend(String command) {
//		return command + "&";
//	}
}
