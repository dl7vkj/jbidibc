@echo off

cd /D "%~dp0"
set CLASSPATH=jbidibc*.jar

java -classpath %CLASSPATH% GetMacros %*
