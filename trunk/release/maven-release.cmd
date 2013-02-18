@call set_properties.cmd

@set RELEASEDIR=%CD%
@echo release directory is %RELEASEDIR%

@echo switch to local relase base dir: %LOCAL_RELEASE_BASE%
@cd /D %LOCAL_RELEASE_BASE%

@echo remove directory: %PROJECT_NAME%
@rmdir /S /Q %PROJECT_NAME%
@if ERRORLEVEL 1 GOTO ERROR

@echo checkout %PROJECT_NAME%
svn co %SVN_BASE_PATH% %PROJECT_NAME% --username %SVN_USERNAME% --password %SVN_PASSWORD% -q
@if ERRORLEVEL 1 GOTO ERROR


@echo prepare release %PROJECT_NAME%, releaseVersion: %TAG_NAME%, next dev version: %NEXT_DEV_VERSION%
@cd %PROJECT_NAME%
call mvn release:clean release:prepare -B -Dusername=%SVN_USERNAME% -Dpassword=%SVN_PASSWORD% -Dtag=%TAG_NAME% -DreleaseVersion=%TAG_NAME% -DdevelopmentVersion=%NEXT_DEV_VERSION%
@if ERRORLEVEL 1 GOTO ERROR

@echo perform release (export, build, deploy)
call mvn release:perform -B -Dusername=%SVN_USERNAME% -Dpassword=%SVN_PASSWORD% -DconnectionUrl=scm:svn:%SVN_TAGS_PATH%/%TAG_NAME%
@if ERRORLEVEL 1 GOTO ERROR

@echo ================================
@echo RELEASE OF %PROJECT_NAME% PASSED
@echo ================================

@goto END

:ERROR
@echo ERROR DURING RELEASE BUILD
@echo BUILD FAILED

:END
@cd %RELEASEDIR%
pause