
rem call mvn install:install-file -Dfile=bidib-rxtx-2.2-binaries.jar -DpomFile=bidib-rxtx-2.2-binaries.pom -Dclassifier=binaries

call mvn gpg:sign-and-deploy-file -Dfile=bidib-rxtx-2.2-binaries.jar -DpomFile=bidib-rxtx-2.2-binaries.pom -Dclassifier=binaries -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging 