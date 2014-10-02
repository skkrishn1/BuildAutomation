#!/bin/bash
# .-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-."
# NOTE: CHANGE THE FOLLOWING VALUES BEFORE GOING FORWARD.
# .-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-."

# milestone version (like 7.0)
milestoneVersion=7.0
# SVN base URL (like http://10.105.34.61/alternative-payments-svn/AP/Liferay)
svnURL=http://10.105.34.61/alternative-payments-svn/AP/Liferay
# SVN tag that will be applied to the code (like ap-portal-7.0)
svnTag=ap-portal-7.0
# SVN credentials used to checkout the code
# SVN username
svnUsername=apuser
#Password
svnPassword=apuser
# Folder path where entire code is to be checked-out and built
buildDirectory=./buildDirectory
# Repository ID defined in the settings.xml, to be used for 
# pushing artifacts to Nexus (like releases)
repositoryID=releases
# The path of the folder, where enhanced war files will be placed:"
enhancedWarFolder=/fs01/app/dt210/wlserver1036/domain/autodeploy
# .-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-."

# ----------------------------------FUNCTION DECLARATION----------------------------------------------------------
function pushArtifactToNexus {
fileName=$1
groupId=$2
artifactId=$3
packaging=$4
COMMANDoutput=$(mvn deploy:deploy-file -DgroupId=$groupId -DartifactId=$artifactId -Dversion=$milestoneVersion -Dpackaging=$packaging -Dfile=$fileName -Durl=$nexusURL -DrepositoryId=$repositoryID -e)
if [ $? -eq 0 ]; then
	echo $COMMANDoutput
    echo "Pushed " $fileName " successfully to Nexus."
else
    echo "Pushing " $fileName " to Nexus failed."
    echo "Exiting the application."
    exit 1
fi
} 
# ------------------------------------FUNCTION DECLARATION COMPLETED----------------------------------------------


# ----------------------------------------------INITIALIZATION----------------------------------------------------
echo "-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-."
echo "                                               PUSH TO NEXUS"
echo "-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-."

echo "This tool will upload the Zapp Portal artifacts to Nexus."
echo "Pleas ensure that the following details in order to proceed."
echo
echo "The tool will be using following parameters:"
echo "Milestone version: " $milestoneVersion
echo "SVN URL          : " $svnURL
echo "SVN Tag          : " $svnTag
echo "SVN Username     : " $svnUsername
echo "Build Directory  : " $buildDirectory
echo "Repository ID    : " $repositoryID
echo "Enhanced War files path:" $enhancedWarFolder
echo 
echo "Ensure the following:"
echo "1. SVN is installed and svn commands are available to this shell script."
echo "2. Maven is installed and mvn command is available to this shell script."
echo "3. In Maven's settings.xml, " \
		"<server>..<server><id>"$repositoryID"</id>...<server>...</servers> "\
		"is defined."
echo "4. Maven command \"mvn clean install package liferay:deploy\" is "\
		"assumed to deploy the artifacts in enhanced form in "\ 
		"the enhancedWarFolder=" $enhancedWarFolder
echo
echo "Press any key to continue, or press Ctrl+C to abort. " 
echo "In case you abort, please edit this file to correct the erroneous entries."
read -n 1 userEntered

# Create the directory if not arleady crated.
[[ -d $buildDirectory ]] || mkdir $buildDirectory
# --------------------------------------------INITIALIZATION COMPLETED--------------------------------------------

#Create tag 
echo "Attempting to creating tag $svnTag" 
completeTagURL=$svnURL/$svnTag
svnOutput=$(svn $svnURL/trunk/ $completeTagURL -m "Creating tag for $svnURL")
if [ $? -eq 0 ]; then
	echo $SVNoutput
    echo "Created tag successfully. The URL is:" $completeTagURL
else
    echo "Failed to created the tag:" $svnTag
    echo "Exiting the application."
    exit 1
fi

# Checking out the SVN code
svnOutput=$(svn checkout $completeTagURL $buildDirectory)
if [ $? -eq 0 ]; then
	echo $SVNoutput
    echo "Code checked out at location: " $buildDirectory "URL: " $completeTagURL
else
    echo "Failed to checkout the code at location: " $buildDirectory "URL: " $completeTagURL
    echo "Exiting the application."
    exit 1
fi

# Preparing the code for release
echo 
echo "Changing the release number in all the pom.xmls"
cd $buildDirectory
COMMANDoutput=$(mvn -DpreparationGoals="install -DskipTests" -DautoVersionSubmodules=true release:prepare -Dusername=$svnUsername -Dpassword=$svnPassword -DscmCommentPrefix=APPortal- -e)
if [ $? -eq 0 ]; then
	echo $COMMANDoutput
    echo "Changing the release number in all the pom.xmls"
else
    echo "Failed to change the release number in all the pom.xmls"
fi

#Checkin 
echo "Checkin the changes"
COMMANDoutput=$(svn commit -m "Added the release numbers in pom.xmls")
if [ $? -eq 0 ]; then
	echo $COMMANDoutput
    echo "Checked in the pom.xml files"
else
    echo "Failed to check-in all the pom.xmls"
fi

# Deploy the application locally.
echo 
echo "Deploying the application locally"
COMMANDoutput=$(mvn clean package liferay:deploy -DskipTests=true)
if [ $? -eq 0 ]; then
	echo $COMMANDoutput
    echo "Deployed the application locally. War files will be available at " $enhancedWarFolder
else
    echo "Local deployment failed."
    echo "Exiting the application."
    exit 1
fi

# Wait till the files are enhanced
echo  
echo "Wait till the files are enhanced."
sleep 10


# Copy the enhanced war files to Nexus
cd $enhancedWarFolder/..
echo 
echo "Uploading portal-ext.properties"
pushArtifactToNexus "portal-ext.properties" "com.ap.ui.ap-portal.portal-config" "portal-ext-properties" "properties"

cd %enhancedWarFolder%
echo 
echo "Uploading cfi-portlets.war"
pushArtifactToNexus "cfi-portlets.war" "com.ap.ui.ap-portal.portal-plugins.portlets" "cfi-portlets" "war"

echo 
echo "Uploading common-portlets.war"
pushArtifactToNexus "common-portlets.war" "com.ap.ui.ap-portal.portal-plugins.portlets" "common-portlets" "war"

echo 
echo "Uploading distributor-portlets.war"
pushArtifactToNexus "distributor-portlets.war" "com.ap.ui.ap-portal.portal-plugins.portlets" "distributor-portlets" "war"

echo 
echo "Uploading ops-portlets.war"
pushArtifactToNexus "ops-portlets.war" "com.ap.ui.ap-portal.portal-plugins.portlets" "ops-portlets" "war"

echo 
echo "Uploading Zapp-Hook.war"
pushArtifactToNexus "Zapp-Hook.war" "com.ap.ui.ap-portal.portal-plugins.hooks" "Zapp-Hook" "war"

echo 
echo "Uploading ap-audit.war"
pushArtifactToNexus "ap-audit.war" "com.ap.ui.ap-portal.portal-plugins.hooks" "ap-audit" "war"

echo 
echo "Uploading ap-audit.war"
pushArtifactToNexus "zapp-portal-theme.war" "com.ap.ui.ap-portal.portal-plugins.themes" "zapp-portal-theme" "war"

echo 
echo "Completed the pushing required artifacts to NEXUS."
exit


