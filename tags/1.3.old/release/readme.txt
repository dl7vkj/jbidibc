Release preparations
====================

The file set_properties.cmd contains the host-specific data like location of JAVA_HOME, M2_HOME, ANT_HOME and SVN_HOME as 
well as the version and the next development version. The user-account and password for sourceforge SVN-repo is stored, too.

!!! Change the values according to your environment but do not commit the changes because they would become visible to other users !!!


Perform a build
===============
!!! Check for an existing gpg-agent service running in the task manager before build !!!
!!! If you re-do a build you have to kill this process manually because it locks the directory on windows !!!

* To perform a build make sure the set_properties.cmd contain the correct information (!version!).
* Open cmd window in the release-subdirectory (the directory where this file is located).
* Use the command-line: maven-release.cmd>release.log
* This pipes the output into a file release.log. This is usefull because the output might overrun the window buffer (at least on 
 windows systems) and in case of an error during build you might not find the necessary information of the failure.
* During release process a window of the gpg-agent is displayed. Enter the passphrase for the signing of the artifacts.
* Open the release.log with a logfile viewer (e.g. baretail) to see when the release has finished => you see a line with
   =========================
   RELEASE OF jbidibc PASSED
   =========================
* The artifact is now stored in the local repo and the tagged sources are under tags/<version-number>
* The version was automatically updated to the value %NEXT_DEV_VERSION% that was specified in the set_properties.cmd file.


Build failure
=============

If a build fails for whatever reason please execute the command-line: rollback-release.cmd>rollback.log

This reverts pending changes during the release process. Otherwise you would have to manually set all versions correct and
change the path in the <scm>-tag of the top-level pom!