@echo off

cd /D "%~dp0"
set CLASSPATH=jbidib*.jar

java -classpath %CLASSPATH% StartMacro %*
