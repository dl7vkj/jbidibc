@call set_properties.cmd

@set RELEASEDIR=%CD%
@echo release directory is %RELEASEDIR%

@cd /D %LOCAL_RELEASE_BASE%

echo rollback release %PROJECT_NAME%, releaseVersion: %TAG_NAME%, next dev version: %NEXT_DEV_VERSION%
cd %PROJECT_NAME%
call mvn release:rollback -B -Dusername=%SVN_USERNAME% -Dpassword=%SVN_PASSWORD% -Dtag=%TAG_NAME% -DreleaseVersion=%TAG_NAME% -DdevelopmentVersion=%NEXT_DEV_VERSION% 

@goto END

:ERROR
@echo ERROR DURING RELEASE BUILD
@echo BUILD FAILED

:END
@cd %RELEASEDIR%
pause