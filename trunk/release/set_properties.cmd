set TAG_NAME=1.3
set NEXT_DEV_VERSION=1.4-SNAPSHOT

set PROJECT_NAME=jbidibc

set SVN_USERNAME=<your-sourceforge-username>
set SVN_PASSWORD=<your-sourceforge-password>

rem the local path to the directory where the release will be performed (clean checkout, build, create tag, build release version)
SET LOCAL_RELEASE_BASE=C:\release

SET JAVA_HOME=C:\Program Files\Java\jdk1.6.0_32
SET M2_HOME=C:\tools\apache-maven-3.0.4
SET ANT_HOME=C:\tools\apache-ant-1.8.4
SET SVN_HOME=C:\tools\svn-win32-1.6.15

SET SVN_BASE_PATH=https://svn.code.sf.net/p/jbidibc/code/branches/experimental-jbidibc
SET SVN_TAGS_PATH=https://svn.code.sf.net/p/jbidibc/code/tags

SET PATH=%M2_HOME%\bin;%JAVA_HOME%\bin;%SVN_HOME%\bin;%ANT_HOME%\bin;%PATH%

set MAVEN_OPTS=-Xmx1024m -XX:MaxPermSize=256m
