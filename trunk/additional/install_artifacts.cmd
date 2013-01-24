

call mvn install:install-file -Dfile=rxtxcomm-2.2.pre2.jar -DpomFile=rxtxcomm-2.2.pre2.pom -Dsources=rxtxcomm-2.2.pre2-sources.jar

call mvn install:install-file -Dfile=RXTXcomm-2.2.jar -DpomFile=rxtxcomm-2.2.pom -Dsources=RXTXcomm-2.2-sources.jar

rem https://github.com/benalexau/xml-formatter/tree/master/releases/org/technicalsoftwareconfigurationmanagement/maven-plugin/tscm-maven-plugin/2.1.0.20111230154050
call mvn install:install-file -Dfile=tscm-maven-plugin-2.1.0.20111230154050.jar -DpomFile=tscm-maven-plugin-2.1.0.20111230154050.pom 
