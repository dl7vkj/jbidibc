

rem call mvn install:install-file -Dfile=rxtxcomm-2.2.pre2.jar -DpomFile=rxtxcomm-2.2.pre2.pom -Dsources=rxtxcomm-2.2.pre2-sources.jar

rem call mvn install:install-file -Dfile=RXTXcomm-2.2.jar -DpomFile=rxtxcomm-2.2.pom -Dsources=RXTXcomm-2.2-sources.jar

rem https://github.com/benalexau/xml-formatter/tree/master/releases/org/technicalsoftwareconfigurationmanagement/maven-plugin/tscm-maven-plugin/2.1.0.20111230154050
rem call mvn install:install-file -Dfile=tscm-maven-plugin-2.1.0.20111230154050.jar -DpomFile=tscm-maven-plugin-2.1.0.20111230154050.pom 


rem call mvn gpg:sign-and-deploy-file -Dfile=RXTXcomm-2.2.jar -DpomFile=rxtxcomm-2.2.pom -Dsources=rxtxcomm-2.2-sources.jar -Djavadoc=rxtxcomm-2.2-javadoc.jar -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging 


call mvn install:install-file -Dfile=bidib-rxtx-bin-2.2-pre.jar -DpomFile=bidib-rxtx-bin-2.2-pre.pom -Dclassifier=binaries