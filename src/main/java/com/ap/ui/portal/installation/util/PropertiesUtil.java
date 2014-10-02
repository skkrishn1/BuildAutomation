package com.ap.ui.portal.installation.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * This utility class contains APIs to 
 * process the properties specified to the system.
 *
 * The class reads all the properties files 
 * required by the system, and stores the union of all
 * the properties. 
 *  
 * @author Abhishek Tembe
 *
 */
public class PropertiesUtil {

	
	/**
	 *  {@link Properties} instance, to hold all the properties.
	 */
	private static final Properties _properties = new Properties();
	
	// Initialisation.
	static {
		// Load nexus.properties
		loadNexusProperties();
		// Load server.properties
		loadServerProperties();
	}
	
	/**
	 * Private Constructor
	 *
	 */
	private PropertiesUtil() {
		// Private constructor.
	}
	/**
	 * Loads the properties mentioned in the file, name of which is 
	 * specified as input parameter.
	 *   
	 * @param filePath path of the properties file to be read.
	 * 
	 * @throws PropertiesFileNotFoundException in case of error.
	 */
	public static void loadProperties(String filePath) throws PropertiesFileNotFoundException {
		try {
			_properties.load(new FileInputStream(filePath));
			
			MessageUtil.logInfoMessage("Read properties from file: " + filePath);
			if (MessageUtil.isDebugEnabled()) {
				logPropertiesRead();
			}
		} catch (FileNotFoundException e) {
			throw new PropertiesFileNotFoundException(e);
		} catch (IOException e) {
			throw new PropertiesFileNotFoundException(e);
		}
	}
	
	/**
	 * Returns the value associated with the key. 
	 * In case the key is not defined, 
	 * <code>null</code> is returned.
	 * 
	 * @param key key, for which the value is to 
	 * 			  be fetched.
	 * @return the value associated with the key. 
	 * 		   In case the key is not defined, 
	 *         <code>null</code> is returned.
	 */
	public static String getProperty(String key) {
		return _properties.getProperty(key);
	}
	
	/**
	 * Enables or disables debug mode. 
	 * 
	 * @param enable debug mode is enabled 
	 * 			     if <code>true</code> is passed. Else
	 * 				 debug mode is disabled. 
	 */
	public static void enableDebugMode (boolean enable) {
		_properties.setProperty("enable.debug.mode", new Boolean(enable).toString());
	}

	/**
	 * Returns the value associated with the key. 
	 * In case the key is not defined, 
	 * <code>null</code> is returned.
	 * 
	 * @param key key, for which the value is to 
	 * 			  be fetched.
	 * @return the value associated with the key. 
	 * 		   In case the key is not defined, 
	 *         <code>null</code> is returned.
	 */
	public static String getProperty(String key, String... replacementParam) {
		String value = getProperty(key);
		if (replacementParam != null) {
			for (int i = 0; i < replacementParam.length; i++) {
				String param = replacementParam[i];
				value = value.replaceAll("\\{" + i + "\\}", param);
			}
		}
		
		return value;
	}
	
	
	/**
	 * Returns the value associated with the key. 
	 * In case the key is not defined,default value
	 * which is passed as input, is returned.
	 * 
	 * @param key key, for which the value is to 
	 * 			  be fetched.
	 * @param defaultValue default value, returned 
	 * 		  if the key is not defined. The 
	 * 		  default value is *<b>NOT</b>*
	 * 		  saved.
	 * 
	 * @return the value associated with the key. 
	 * 		   In case the key is not defined, 
	 *         defaultValue, passed as input, 
	 *         is returned.
	 */
	public static String getProperty(String key, String defaultValue) {
		String value = getProperty(key);
		return (value == null || "".equals(value)) ? defaultValue : value;
	}
	
	/**
	 * Load the properties defined in &quot;nexus.properties&quot;
	 * file. The file &quot;nexus.properties&quot; should be 
	 * present in classpath.
	 */
	private static void loadNexusProperties() {
		loadClassPathProperties("nexus.properties");
	}
	/**
	 * Load the properties defined in &quot;server.properties&quot;
	 * file. The file &quot;server.properties&quot; should be 
	 * present in classpath.
	 */
	private static void loadServerProperties() {
		loadClassPathProperties("server.properties");
	}
	/**
	 * Load properties file from classpath.
	 * 
	 * @param fileName name of the file to load.
	 */
	private static void loadClassPathProperties(String fileName) {
		try {
			InputStream nexusFile = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
			_properties.load(nexusFile);
		} catch (IOException e) {
			MessageUtil.logWarningMessage("Could not load the properties file : " + fileName);
		}
	}

	/**
	 * Log all the properties defined in the system.
	 */
	private static void logPropertiesRead() {
		
		MessageUtil.logDebugMessage("Properties defined so far are:");
		
		// Get the keys of the properties defined.
		Set<Object> keys = _properties.keySet();
		// Get them in ascending order of keys.
		TreeSet<Object> sortedKeys = new TreeSet<Object>(keys);
		
		// Log "key=value".
		for (Object key : sortedKeys) {
			MessageUtil.logDebugMessage("\t" + key + "=" +_properties.get(key));
		}
	}
}
