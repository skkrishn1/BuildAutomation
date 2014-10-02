/**
 * 
 */
package com.ap.ui.portal.installation.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import com.ap.ui.portal.installation.command.exception.ArtifactDownloadException;

/**
 * This utility class contains APIs for 
 * IO functions likes write/read to/from folder or file. 
 *
 * @author Abhishek Tembe
 *
 */
public class IOUtil {

	/**
	 * Private Constructor
	 */
	private IOUtil() {
		// Private constructor.
	}

	/**
	 * Downloads the artifact  from specified URL to target folder.
	 * 
	 * @param targetFolderName target folder name.
	 * @param sourceFileUrl  source file URL, to download.
	 * 
	 * @throws ArtifactDownloadException in case there were problems caused 
	 * 									 while downloading the files from NEXUS  
	 * 									 to local target folder.
	 */
	public static void downloadFile(String targetFolderName, String sourceFileUrl)   
	throws ArtifactDownloadException {
		// Process the source information
		URL sourceFile = null;
		try {
			sourceFile = new URL(sourceFileUrl);
		} catch (MalformedURLException e) {
			MessageUtil.logErrorMessage("Could not download the artifact from Nexus: " + sourceFileUrl);
			throw new ArtifactDownloadException("Malformed URL " + sourceFileUrl, e);
		}
		// Process the target information
		String targetLocation = getFileNameFromURL(targetFolderName, sourceFileUrl);
		if (targetLocation == null) {
			MessageUtil.logErrorMessage("Could not determine the artifact to download. URL: " + sourceFileUrl);
			throw new ArtifactDownloadException("Could not determine the artifact to download. URL: " + sourceFileUrl);
		}
		
		// Download the actual file locally.
		downloadFile(targetLocation, sourceFile);
	}
	
	/**
	 * Renames a file. 
	 * @param renameFrom Original file name
	 * @param renameTo new file name
	 * 
	 * @return <code>true</code> if successful.
	 */
	public static boolean renameFile(String renameFrom, String renameTo) {
		return new File(renameFrom).renameTo(new File(renameTo));
	}
	
	/**
	 * Deletes a file if already exists.
	 * 
	 * @param renameFrom file to delete.
	 *  
	 * @return <code>true</code> if file deleted successfully.
	 */
	public static boolean deleteIfAlreadyExists(String renameFrom) {
		File file = new File(renameFrom);
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}
	
	/**
	 * Checks if the file already exists.
	 * 
	 * @param fileName complete path of file to check.
	 * 
	 * @return <code>true</code> if the file exists.
	 */
	public static boolean checkIfAlreadyExists(String fileName) {
		return new File(fileName).exists();
	}
	
	/**
	 * Copies source file at destination.
	 * 
	 * @param sourceFilePath source
	 * @param destinationFilePath destination
	 * 
	 * @return <code>true</code> if successful.
	 */
	public static boolean copyFile (String sourceFilePath, String destinationFilePath) {
		if (sourceFilePath.equals(destinationFilePath)) {
			MessageUtil.logErrorMessage("Cannot copy file, since source and destination are same. File: " + sourceFilePath );
			return false;
		}
		
		FileChannel sourceFileChannel = null;
		FileChannel destinationFileChannel = null;
	    try {
	    	
			sourceFileChannel = new FileInputStream(sourceFilePath).getChannel();
			destinationFileChannel = new FileOutputStream(destinationFilePath).getChannel();
			
			destinationFileChannel.transferFrom(sourceFileChannel, 0, sourceFileChannel.size());
			
			sourceFileChannel.close();
			destinationFileChannel.close();
			
			return true;
			
		} catch (FileNotFoundException e) {
			StringBuffer msg = new StringBuffer();
			msg.append("Could not create copy of ")
			   .append(sourceFilePath)
			   .append(" and ")
			   .append(destinationFilePath)
			   .append(" Reason: ")
			   .append(e);
			MessageUtil.logErrorMessage(msg.toString());
		} catch (IOException e) {
			StringBuffer msg = new StringBuffer();
			msg.append("Could not create copy of ")
			   .append(sourceFilePath)
			   .append(" and ")
			   .append(destinationFilePath)
			   .append(" Reason: ")
			   .append(e);
			MessageUtil.logErrorMessage(msg.toString());
		} 
	    return false;
	}
	
	/**
	 * Downloads the artifact  from specified URL to target folder.
	 * 
	 * @param targetLocation 	target file location to be copied.
	 * @param sourceFileUrl  	source file URL, to download.
	 * 
	 * @throws ArtifactDownloadException in case there were problems caused 
	 * 									 while downloading the files from NEXUS  
	 * 									 to local target folder.
	 */
	private static void downloadFile(String targetLocation, URL sourceFileUrl)  
	throws ArtifactDownloadException {
		try {
			ReadableByteChannel rbc  = Channels.newChannel(sourceFileUrl.openStream());
			FileOutputStream fos = new FileOutputStream(targetLocation);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
		} catch (IOException e) {
			MessageUtil.logErrorMessage("Could not download the artifact from Nexus: " + sourceFileUrl.getPath());
			throw new ArtifactDownloadException(e);
		}

	}
	
	/**
	 * Get the file name from the source URL and appends the 
	 * parent folder name. In short, retrieves the fully qualified
	 * path for the file, on local file system. 
	 * 
	 * @param sourceFileUrl source URL.
	 * 
	 * @return file name from the source URL. Returns 
	 * 		   <code>null</code> if the file name 
	 * 		   cannot be determined.
	 */
	private static String getFileNameFromURL(String parentFolder, String sourceFileUrl) {
		// Get the file name.
		String fileName = getFileName(sourceFileUrl);
		
		// Construct the full path, appending the 
		// parent folder to the file name.
		StringBuffer fullPath = new StringBuffer();
		fullPath.append(parentFolder);
		fullPath.append(File.separator);
		fullPath.append(fileName);
		
		return fullPath.toString();
	}

	/**
	 * Reads the file name from the URL.
	 * @param sourceFileUrl
	 * @return
	 */
	private static String getFileName(String sourceFileUrl) {
		int slashIndex = sourceFileUrl.lastIndexOf('/');
		if (slashIndex != -1) {
			return sourceFileUrl.substring(slashIndex + 1);
		}
		return null;
	}
}
