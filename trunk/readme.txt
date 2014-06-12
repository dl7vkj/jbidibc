Preparation to build
====================

A working Maven 3 installation is required.
Install the artifact in directory 'additional' into the local maven repo. This can be performed with the provided script files in the directory.

Build
=====

Run the maven build in the bibid directory: 'mvn install' (or to rebuild all: 'mvn clean install').


Eclipse
=======

* Prepare:
Install the m2e tools (from Juno update site under General Purpose Tools) if not already installed.

Install the m2e-error-disabler-plugin
https://github.com/SonarSource/sonar-settings/tree/master/eclipse/m2e-error-disabler-plugin --> Follow installation guideline

Install TestNG Plugin
http://testng.org/doc/download.html


* Import project:
Use Eclipse Juno (m2e support added by default). Import the maven project: File -> Import -> Maven > Existing Maven Projects -> 
Root directory > Select the bidib directory > Select the projects -> Finish

Subversion
==========

The configuration file config for the subversion client is in the following directory : %USERPROFILE%\Application Data\Subversion or %USERPROFILE%\AppData\Roaming\Subversion (Win7)
Add the following configuration for some file-types in the section [auto-props]:

### Section for configuring automatic properties.
[auto-props]
*.java = svn:eol-style=native
*.properties = svn:eol-style=native
*.txt = svn:eol-style=native
*.xml = svn:eol-style=native
*.xsd = svn:eol-style=native


Maven release
=============

