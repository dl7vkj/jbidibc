@echo off

cd /D "%~dp0"

java -classpath ".;lib/*" org.bidib.jbidibc.GetNodeList %*
