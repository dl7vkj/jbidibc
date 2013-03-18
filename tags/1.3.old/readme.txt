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
Install the m2e-error-disabler-plugin
https://github.com/SonarSource/sonar-settings/tree/master/eclipse/m2e-error-disabler-plugin --> Follow installation guideline

Install TestNG Plugin
http://testng.org/doc/download.html


* Import project:
Use Eclipse Juno (m2e support added by default). Import the maven project: File -> Import -> Maven > Existing Maven Projects -> 
Root directory > Select the bidib directory > Select the projects -> Finish

Maven release
=============

