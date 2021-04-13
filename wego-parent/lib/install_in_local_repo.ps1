"cancello le cartelle it/trieste, it/ts, it /wego nella repo maven"

$a = Join-Path -Path $Env:M2_REPO -ChildPath "\it\trieste"

if(Test-Path -Path $a) {
     Remove-Item -Recurse $a
}

$a = Join-Path -Path $Env:M2_REPO -ChildPath "\it\ts"
if(Test-Path -Path $a) {
     Remove-Item -Recurse $a
}

$a = Join-Path -Path $Env:M2_REPO -ChildPath "\it\wego"
if(Test-Path -Path $a) {
     Remove-Item -Recurse $a
}

mvn org.apache.maven.plugins:maven-install-plugin:2.5.1:install-file "-Dfile=beanshell-plus-1.0-SNAPSHOT.jar" "-Dpackaging=jar"

mvn org.apache.maven.plugins:maven-install-plugin:2.5.1:install-file "-Dfile=BlinkFish-1.0-SNAPSHOT.jar" "-Dpackaging=jar"

mvn org.apache.maven.plugins:maven-install-plugin:2.5.1:install-file "-Dfile=cf-utils-1.0-SNAPSHOT.jar" "-Dpackaging=jar"
mvn org.apache.maven.plugins:maven-install-plugin:2.5.1:install-file "-Dfile=dynamicodt-utils-1.0-SNAPSHOT.jar" "-Dpackaging=jar"
mvn org.apache.maven.plugins:maven-install-plugin:2.5.1:install-file "-Dfile=intalio-manager-2.0-SNAPSHOT.jar" "-Dpackaging=jar"

mvn org.apache.maven.plugins:maven-install-plugin:2.5.1:install-file "-Dfile=SaneLemon-1.0-SNAPSHOT.jar" "-Dpackaging=jar" 
mvn org.apache.maven.plugins:maven-install-plugin:2.5.1:install-file "-Dfile=webdav-server-1.0-SNAPSHOT.jar" "-Dpackaging=jar"

mvn org.apache.maven.plugins:maven-install-plugin:2.5.1:install-file "-Dfile=WelfaregoLiferayClient-1.0.jar" "-Dpackaging=jar"

