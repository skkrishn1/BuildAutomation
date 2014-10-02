@echo OFF
REM ----------------------------------------------INITIALIZATION----------------------------------------------------
echo -.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.
echo.                               PUSH TO NEXUS
echo -.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-
echo This tool will upload the Zapp Portal artifacts to Nexus.
echo Pleas ensure that the following details in order to proceed.
echo.
REM .-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.
REM NOTE: CHANGE THE FOLLOWING VALUES BEFORE GOING FORWARD.
REM .-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.

REM Nexus URL
set "nexusURL=http://10.105.34.64:8081/nexus/content/repositories/releases/"
REM milestone version (like 7.0)
set "milestoneVersion=7.0"
REM SVN base URL (like http://10.105.34.61/alternative-payments-svn/AP/Liferay)
set "svnURL=http://10.105.34.61/alternative-payments-svn/AP/Liferay"
REM SVN tag that will be applied to the code (like ap-portal-7.0)
set "svnTag=ap-portal-7.0"
REM SVN credentials used to checkout the code
REM SVN username
set "svnUsername=apuser"
REM Password
set "svnPassword=apuser"
REM Folder path where entire code is to be checked-out and built
set "buildDirectory=./buildDirectory"
REM Repository ID defined in the settings.xml, to be used for 
REM pushing artifacts to Nexus (like releases)
set "repositoryID=releases"
REM The path of the folder, where enhanced war files will be placed:"
set "enhancedWarFolder=/fs01/app/dt210/wlserver1036/domain/autodeploy"
REM .-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-."

echo The tool will be using following parameters:
echo Milestone version:  %milestoneVersion%
echo SVN URL          :  %svnURL%
echo SVN Tag          :  %svnTag%
echo SVN Username     :  %svnUsername%
echo Build Directory  :  %buildDirectory%
echo Repository ID    :  %repositoryID%
echo Enhanced War files path: %enhancedWarFolder%
echo.
echo Ensure the following:
echo 1. SVN is installed and svn commands are available to this shell script.
echo 2. Maven is installed and mvn command is available to this shell script.
echo 3. In Maven's settings.xml, \<server\>..\<server\>\<id\>"$repositoryID"\</id\>...\<server\>...\</servers\> is defined."
echo 4. Maven command "mvn clean install package liferay:deploy" is assumed to deploy the artifacts in enhanced form in the enhancedWarFolder=%enhancedWarFolder%
echo.
echo Press any key to continue, or press Ctrl+C to abort.  
echo In case you abort, please edit this file to correct the erroneous entries.
set /p id="Press  any key to continue..." %=%

REM Create the directory 
if not exist %buildDirectory% mkdir %buildDirectory%
REM Create tag 
echo Attempting to creating tag %svnTag%
set completeTagURL=%svnURL%/%svnTag%
svn copy %svnURL%/trunk/ %completeTagURL% -m "Creating tag for $svnURL"
IF ERRORLEVEL 1 GOTO ByeBye

REM Checking out the SVN code
echo.
echo Checking out using the code
svn checkout %completeTagURL% %buildDirectory%
IF ERRORLEVEL 1 GOTO ByeBye

REM preparing the code for release
echo.
echo Changing the release number in all the pom.xmls
cd %buildDirectory%
mvn -DpreparationGoals="install -DskipTests" -DautoVersionSubmodules=true release:prepare -Dusername=%svnUsername% -Dpassword=%svnPassword% -DscmCommentPrefix=APPortal- -e
REM Checkin 
echo Checkin the changes
svn commit -m "Added the release numbers in pom.xmls"

REM deploy the application locally.
echo.
echo Deploying the application locally
mvn clean package liferay:deploy -DskipTests=true
IF ERRORLEVEL 1 GOTO ByeBye

REM Wait till the files are enhanced
echo. 
echo Wait till the files are enhanced.
sleep 10

REM copy the enhanced war files to Nexus
cd %enhancedWarFolder%/..
echo.
echo Uploading portal-ext.properties
set "fileName=portal-ext.properties"
set "groupId=com.ap.ui.ap-portal.portal-config"
set "artifactId=portal-ext-properties"
set "packaging=properties"
mvn deploy:deploy-file -DgroupId=%groupId% -DartifactId=%artifactId% -Dversion=%milestoneVersion% -Dpackaging=%packaging% -Dfile=%fileName% -Durl=%nexusURL% -DrepositoryId=%repositoryID% -e
IF ERRORLEVEL 1 GOTO ByeBye

cd %enhancedWarFolder%
echo.
echo Uploading cfi-portlets.war
set "fileName=cfi-portlets.war"
set "groupId=com.ap.ui.ap-portal.portal-plugins.portlets"
set "artifactId=cfi-portlets"
set "packaging=war"
mvn deploy:deploy-file -DgroupId=%groupId% -DartifactId=%artifactId% -Dversion=%milestoneVersion% -Dpackaging=%packaging% -Dfile=%fileName% -Durl=%nexusURL% -DrepositoryId=%repositoryID% -e
IF ERRORLEVEL 1 GOTO ByeBye

echo.
echo Uploading common-portlets.war
set "fileName=common-portlets.war"
set "groupId=com.ap.ui.ap-portal.portal-plugins.portlets"
set "artifactId=common-portlets"
set "packaging=war"
mvn deploy:deploy-file -DgroupId=%groupId% -DartifactId=%artifactId% -Dversion=%milestoneVersion% -Dpackaging=%packaging% -Dfile=%fileName% -Durl=%nexusURL% -DrepositoryId=%repositoryID% -e
IF ERRORLEVEL 1 GOTO ByeBye

echo.
echo Uploading distributor-portlets.war
set "fileName=distributor-portlets.war"
set "groupId=com.ap.ui.ap-portal.portal-plugins.portlets"
set "artifactId=distributor-portlets"
set "packaging=war"
mvn deploy:deploy-file -DgroupId=%groupId% -DartifactId=%artifactId% -Dversion=%milestoneVersion% -Dpackaging=%packaging% -Dfile=%fileName% -Durl=%nexusURL% -DrepositoryId=%repositoryID% -e
IF ERRORLEVEL 1 GOTO ByeBye

echo.
echo Uploading ops-portlets.war
set "fileName=ops-portlets.war"
set "groupId=com.ap.ui.ap-portal.portal-plugins.portlets"
set "artifactId=ops-portlets"
set "packaging=war"
mvn deploy:deploy-file -DgroupId=%groupId% -DartifactId=%artifactId% -Dversion=%milestoneVersion% -Dpackaging=%packaging% -Dfile=%fileName% -Durl=%nexusURL% -DrepositoryId=%repositoryID% -e
IF ERRORLEVEL 1 GOTO ByeBye

echo.
echo Uploading Zapp-Hook.war
set "fileName=Zapp-Hook.war"
set "groupId=com.ap.ui.ap-portal.portal-plugins.hooks"
set "artifactId=Zapp-Hook"
set "packaging=war"
mvn deploy:deploy-file -DgroupId=%groupId% -DartifactId=%artifactId% -Dversion=%milestoneVersion% -Dpackaging=%packaging% -Dfile=%fileName% -Durl=%nexusURL% -DrepositoryId=%repositoryID% -e
IF ERRORLEVEL 1 GOTO ByeBye

echo.
echo Uploading ap-audit.war
set "fileName=ap-audit.war"
set "groupId=com.ap.ui.ap-portal.portal-plugins.hooks"
set "artifactId=ap-audit"
set "packaging=war"
mvn deploy:deploy-file -DgroupId=%groupId% -DartifactId=%artifactId% -Dversion=%milestoneVersion% -Dpackaging=%packaging% -Dfile=%fileName% -Durl=%nexusURL% -DrepositoryId=%repositoryID% -e
IF ERRORLEVEL 1 GOTO ByeBye

echo.
echo Uploading ap-audit.war
set "fileName=zapp-portal-theme.war"
set "groupId=com.ap.ui.ap-portal.portal-plugins.themes"
set "artifactId=zapp-portal-theme"
set "packaging=war"
mvn deploy:deploy-file -DgroupId=%groupId% -DartifactId=%artifactId% -Dversion=%milestoneVersion% -Dpackaging=%packaging% -Dfile=%fileName% -Durl=%nexusURL% -DrepositoryId=%repositoryID% -e
IF ERRORLEVEL 1 GOTO ByeBye

echo.
echo Completed the pushing required artifacts to NEXUS.
exit


:ByeBye
echo.
echo Could not complete the task due to previous errors.
echo Exiting the application
exit 1;


REM cmd /C "mvn clean install"
REM java -cp target\build-automation-0.0.1-SNAPSHOT.jar com.ap.ui.portal.installation.PushToNexusMain