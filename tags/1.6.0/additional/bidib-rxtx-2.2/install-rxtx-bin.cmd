
call mvn install:install-file -Dfile=bidib-rxtx-binaries-2.2.jar -DpomFile=bidib-rxtx-binaries-2.2.pom -Dsources=bidib-rxtx-binaries-2.2-sources.jar -Djavadoc=bidib-rxtx-binaries-2.2-javadoc.jar 

rem call mvn gpg:sign-and-deploy-file -Dfile=bidib-rxtx-binaries-2.2.jar -DpomFile=bidib-rxtx-binaries-2.2.pom -Dsources=bidib-rxtx-binaries-2.2-sources.jar -Djavadoc=bidib-rxtx-binaries-2.2-javadoc.jar -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging 