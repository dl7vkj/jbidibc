set TAG_NAME=1.6.0
set NEXT_DEV_VERSION=1.6-SNAPSHOT

set PROJECT_NAME=jbidibc

set SVN_USERNAME=<your-sourceforge-username>
set SVN_PASSWORD=<your-sourceforge-password>
set GPG_PASSPHRASE=<your-gpg-passphrase>

rem the local path to the directory where the release will be performed (clean checkout, build, create tag, build release version)
SET LOCAL_RELEASE_BASE=D:\release

SET JAVA_HOME=C:\Program Files\Java\jdk1.7.0_25
SET M2_HOME=D:\tools\apache-maven-3.0.4
SET ANT_HOME=D:\tools\apache-ant-1.8.1
SET SVN_HOME=D:\tools\svn-win64-1.7.8

SET SVN_BASE_PATH=https://svn.code.sf.net/p/jbidibc/code/trunk
SET SVN_TAGS_PATH=https://svn.code.sf.net/p/jbidibc/code/tags

SET PATH=%M2_HOME%\bin;%JAVA_HOME%\bin;%SVN_HOME%;%ANT_HOME%\bin;%PATH%

set MAVEN_OPTS=-Xmx1024m -XX:MaxPermSize=256m
