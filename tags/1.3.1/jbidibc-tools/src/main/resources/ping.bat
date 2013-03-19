@echo off

cd /D "%~dp0"

set CP=.
for %%i in (lib\*.jar) do call cp.bat %%i
set CLASSPATH=%CP%

java -classpath %CLASSPATH% org.bidib.jbidibc.Ping %*
