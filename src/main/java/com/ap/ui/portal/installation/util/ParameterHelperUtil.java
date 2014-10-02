package com.ap.ui.portal.installation.util;

/**
 * This utility class helps in processing the 
 * parameters/command line inputs.
 *
 * @author Abhishek Tembe
 *
 */
public class ParameterHelperUtil {

	/**
	 * Colon &quot;:&quot; as separator between the 
	 * parameter name and parameter value. 
	 */
	private static final String PARAM_NAME_VALUE_SEPARATOR = ":";
	/**
	 * Parameter prefix, the command line arguments 
	 * prefixed/starting with this pattern will be 
	 * processed by the system.
	 */
	private static final String PARAM_PREFIX = "-D";

	/**
	 * Private Constructor
	 *
	 */
	private ParameterHelperUtil() {
		// Private constructors.
	}
	
	/**
	 * Checks if the parameter was passed in command line. It is expected that 
	 * the parameter is to be passed in the format 
	 * &quot;-D&lt;parameter name&gt;:&lt;value of the parameter&gt;&quot;
	 * 
	 * @param args command line arguments
	 * @param paramName name of the parameter 
	 * @return 	<code>true</code> if the parameter was indeed passed as 
	 * 			command line parameter. Else, <code>false</code> is passed.
	 */
	public static boolean checkParameterExists(String[] args, String paramName) {
		if (args.length == 0) {
			return false;
		}
		// Iterate over the command line arguments.
		for (int i = 0; i < args.length; i++) {
			// Check if the argument starts with the 
			// pattern -D<param name>:
			String arg = args[i];
			if (arg.startsWith(getParameterPattern(paramName))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Fetches the value of the parameter was passed in command line. 
	 * It is expected that the parameter is to be passed in the format 
	 * &quot;-D&lt;parameter name&gt;:&lt;value of the parameter&gt;&quot; 
	 *  
	 * @param args command line parameters.
	 * @param paramName name of the parameter to fetch.
	 * @return the value of the parameter if found in the 
	 * 		   command line input arguments.
	 */
	public static String getParamValue(String[] args, String paramName) {
		if (args.length == 0) {
			return null;
		}
		// Iterate over the command line arguments.
		for (int i = 0; i < args.length; i++) {
			// Check if the argument is of the pattern 
			// -D<param name>:<param value>
			String arg = args[i];
			if (arg.startsWith(getParameterPattern(paramName))) {
				String value = arg.replace(getParameterPattern(paramName), "");
				return value;
			}
		}
		// Parameter not found.
		return null;
	}
	
	/**
	 * Defines the pattern of the parameter expected in the
	 * command line arguments.
	 * The patter is 
	 * &quot;-D&lt;parameter name&gt;:&lt;value of the parameter&gt;&quot;
	 *  
	 * @param paramName name of the parameter.
	 * 
	 * @return the pattern &quot;-D&lt;parameter name&gt;:&quot;
	 */
	private static String getParameterPattern(String paramName) {
		return PARAM_PREFIX + paramName + PARAM_NAME_VALUE_SEPARATOR;
	}

}

