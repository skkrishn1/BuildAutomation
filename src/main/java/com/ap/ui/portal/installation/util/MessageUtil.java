package com.ap.ui.portal.installation.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * This utiltiy class helps in displaying 
 * various messages.
 *
 * @author Abhishek Tembe
 *
 */
public class MessageUtil {
	
	/**
	 * Debug flag. In case this is set to true, system will log debug messages.
	 * This flag is to be set in settings.properties.<br/>
	 *  	enable.debug.mode=true
	 */
	private final static boolean DEBUG_ENABLED = new Boolean(PropertiesUtil.getProperty("enable.debug.mode")).booleanValue();
	
	/**
	 * Marks start of execution.
	 */
	private static Date start;
	/**
	 * Marks end of execution.
	 */
	private static Date end;
	
	/**
	 * Private Constructor
	 *
	 */
	private MessageUtil() {
		// Private Constructor.
	}
	
	/**
	 * Log the message in debug mode.
	 * @param message message to log.
	 */
	public static void  logDebugMessage(String message) {
		if (DEBUG_ENABLED) {
			System.out.println("[DEBUG]    " + message);
		}
	}
	/**
	 * Log the message in info mode.
	 * @param message message to log.
	 */
	public static void  logInfoMessage (String message) {
		System.out.println("[INFO]     " + message);
	}
	/**
	 * Log the message in warning mode.
	 * @param message message to log.
	 */
	public static void  logWarningMessage(String message) {
		System.err.println("[WARNING]  " + message);
	}
	/**
	 * Log the message in error mode.
	 * @param message message to log.
	 */
	public static void  logErrorMessage (String message) {
		System.err.println("[ERROR]    " + message);
	}
	
	/**
	 * Display the help message, in case any parameters are missing.
	 */
	public static void displayHelpMessage() {
		StringBuffer str = new StringBuffer();
		str.append("Following is the format of the command to run the application\n");
		str.append("\tjava -jar <Name of the jar file> -Dfile:<Path of settings.properties file> \n\n");
		// Provide an option to the user to create an empty template of settings.properties
		str.append("Do you want to create a template/sample settings.properties?\n[Press Y/y to create the file] ");
		System.out.print(str.toString());
		
		// In case the user says yes to create settings.properties
		if (didUserPress('y')) {
			// Check if the file already exists in current directory.
			boolean fileExist = checkFileExists("settings.properties");
			if (fileExist) {
				// In case the file already exists, 
				// then ask the user permission to 
				// over-write the file.
				System.err.println();
				logWarningMessage("\"settings.properties\" exists. Do you want to replace the file? ");
				System.err.print("           [Press Y/y to replace the file] ");
				if (!didUserPress('y')) {
					// In case the user pressed anything other than y/Y,
					// do not over-write the file.
					return;
				}
			} 
			// Create/over-write the existing file
			// with the template of settings.properties
			// present in "src/main/resources/settings.properties"
			createAndCopyFileContents("settings.properties");
		}
	}
	/**
	 * Retrieves the difference between two dates in form of 
	 * &quot;X hours, Y minutes and Z seconds&quot;
	 * @param start start date
	 * @param end end date
	 * 
	 * @return difference in form 
	 * 		   &quot;X hours, Y minutes and Z seconds&quot;
	 */
	public final static String getTimeDifference(Date start, Date end) {
		// Get the difference in milli-seconds.
		long startMillis = start.getTime();
		long endMillis = end.getTime();
		long diff = endMillis - startMillis;
		// Get milliseconds 
		long millis = diff % 1000;
		// convert into actual seconds.
		diff = diff/1000;
		
		// Get the total number of hours 
		long hours = diff / 3600;
		
		// Get the total number of minutes
		diff = diff % 3600;
		long mins = diff / 60;
		
		// Get total number of seconds.
		long seconds = diff % 60;
		
		StringBuffer str = new StringBuffer();
		str.append(hours);
		str.append(" hours, ");
		str.append(mins);
		str.append(" minutes, ");
		str.append(seconds);
		str.append(" seconds and ");
		str.append(millis);
		str.append(" milliseconds");
		return str.toString();
	}
	
	/**
	 * Returns the value of flag enable.debug.mode
	 * defined in settings.properties.
	 * 
	 * @return value of flag enable.debug.mode
	 */
	public static final boolean isDebugEnabled() {
		return DEBUG_ENABLED;
	}
	
	/**
	 * Reads the values entered by user from command prompt.
	 * 
	 * @return value entered by the user on command prompt.
	 */
	@SuppressWarnings("resource")
	public static String readFromCommandLine() {
		// Read from the console.
		if (System.console() == null) {
			Scanner in = new Scanner(System.in);
			String inputString = in.nextLine();
			return inputString;
		}
		return System.console().readLine();
	}

	
	/**
	 * Reads the values entered by user from command prompt.
	 * 
	 * @return value entered by the user on command prompt.
	 */
	@SuppressWarnings("resource")
	public static String readPasswordValueFromCommandLine() {
		// Read from the console.
		if (System.console() == null) {
			Scanner in = new Scanner(System.in);
			String inputString = in.nextLine();
			return inputString;
		}
		char[] inputString = System.console().readPassword();
		return new String(inputString);
	}
	/**
	 * Log start of execution.
	 */
	public static void logStartOfExecution() {
		start = new Date(System.currentTimeMillis());
		MessageUtil.logInfoMessage("Execution started at : " + getTime());
	}
	
	/**
	 * Log end of execution.
	 */
	public static void logEndOfExecution() {
		end = new Date(System.currentTimeMillis());
		MessageUtil.logInfoMessage("Execution completed at : " + getTime());
		MessageUtil.logInfoMessage("Excution took: " + MessageUtil.getTimeDifference(start, end));
	}

	/**
	 * Check if the user pressed the specified character.
	 * @param c character to check.
	 * 
	 * @return <code>true</code> if the user entered the character
	 * 			or word starting with the character.
	 */
	private static boolean didUserPress(char c) {
		// Read from the console.
		 String inputString = readFromCommandLine();
		 // check if the value enter is the character, or
		 // the word starts with the character.
		 if (inputString.toLowerCase().startsWith(new String(new char[]{c}))) {
			 return true;
		 }
		return false;
	}

	/**
	 * Reads the content of the file specified in input parameter. The 
	 * file should be present in the classpath. This code also creates 
	 * a new file with the same name in the current directory and 
	 * copies the contents of the file found in classpath.
	 *  
	 * @param fileName name of the file
	 */
	private static void createAndCopyFileContents(String fileName) {
		// Get the contents of the file in classpath.
		String fileContent = getFileContentAsString(fileName);
		// Create an empty file with same name in the current directory.
		File file = createEmptyFile(fileName);
		// Copy the contents of the classpath file into the 
		// newly created file.
		copyContent(file, fileContent);
	}

	/**
	 * Copy the contents into target file.
	 * 
	 * @param file {@link File} object representing the target file.
	 * @param fileContent content to be written in the file.
	 */
	private static void copyContent(File file, String fileContent) {
		if (file != null) {
			try {
				// Open out stream to the target file.
				FileOutputStream out = new FileOutputStream(file);
				// Write the contents to the file.
				out.write(fileContent.getBytes());
				out.close();
				
				logInfoMessage("File " + file.getAbsolutePath() + " created successfully");
				
			} catch (IOException e) {
				logErrorMessage("Could not create file :" + file.getName());
			}
		}
	}
	
	/**
	 * Gets the contents of the file as string. The file is fetched 
	 * from classpath. 
	 * 
	 * @param fileName name of the file to read.
	 * 
	 * @return contents of the file in form of String.
	 */
	private static String getFileContentAsString(String fileName) {
		try {
			// Get the file from classpath.
			InputStream file = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
			// Initialise the buffer to read the file content. 
			int totalFileSize = file.available();
			byte[] fileContent = new byte[totalFileSize];
			// read the file contents.
			file.read(fileContent);
			// Convert the content into a string. The content may
			// contain non-ASCII characters. Hence, the content
			// is read into the String object using 
			// UTF-8 character set.
			String fileContentStr = new String(fileContent, "UTF-8");
			return fileContentStr;
		} catch (UnsupportedEncodingException e) {
			// Could not get actual file. Hence, skipping it. 
		} catch (IOException e) {
			// Could not get actual file. Hence, skipping it.
		}
		return "";
	}
	
	/**
	 * Checks if the file exists.
	 * @param fileName name of the file.
	 * @return <code>true</code> if the file exists.
	 */
	private static boolean checkFileExists(String fileName) {
		return new File(fileName).exists();
	}
	
	/**
	 * Creates an empty file, with the name specified, in
	 * current directory.
	 *  
	 * @param fileName name of the file to be created.
	 * 
	 * @return {@link File} object representing the 
	 * 		   newly created file.
	 */
	private static File createEmptyFile(String fileName) {
		try {
			File file = new File(fileName);
			file.createNewFile();
			return file;
		} catch (IOException e) {
			logErrorMessage("Could not create file " + fileName + " in current directory!!");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Get current time in form of String.
	 * 
	 * @return current time in form of String.
	 */
	private static String getTime() {
		return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
	}

}
