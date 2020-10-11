@echo off

set jarNames[0]=commons-io-2.6
set jarNames[1]=json-20200518

set descriptors[0]=org.apache.commons.io
set descriptors[1]=org.json

set unusedJars="javafx-base-14.0.2" "javafx-controls-14.0.2" "javafx-fxml-14.0.2" "javafx-graphics-14.0.2"

echo // CLEANING

call mvn --file pom.xml clean

echo // INSTALLING WIX

call mkdir target\wix
call powershell -Command "Invoke-WebRequest https://github.com/wixtoolset/wix3/releases/download/wix3112rtm/wix311.exe -OutFile target\wix\wix.exe"
call target\wix\wix.exe /install /quiet /norestart

echo // COMPILING

call mvn --file pom.xml dependency:copy-dependencies compile

echo // REMOVING UNUSED DEPS

for %%n in (%unusedJars%) do (
  echo // REMOVING %%n
  del target\dependency\%%n.jar
)

echo // COMPILING MODULE INFOS

for /l %%i in (0, 1, 1) do (
  call echo // PROCESSING %%descriptors[%%i]%% IN %%jarNames[%%i]%%

  call "%JAVA_HOME%bin\jdeps" ^
    --module-path target\dependency ^
    --generate-module-info target\module-info ^
    target\dependency\\%%jarNames[%%i]%%.jar

  call "%JAVA_HOME%bin\javac" ^
    --module-path target\dependency ^
    -d target\module-extensions\\%%descriptors[%%i]%% ^
    --patch-module %%descriptors[%%i]%%=target\dependency\\%%jarNames[%%i]%%.jar ^
    target\module-info\\%%descriptors[%%i]%%\module-info.java

  call "%JAVA_HOME%bin\jar" ^
    uf target\dependency\\%%jarNames[%%i]%%.jar ^
    -C target\module-extensions\\%%descriptors[%%i]%% module-info.class
)

echo // LINKING

call "%JAVA_HOME%bin\jlink" ^
  --module-path target\classes;target\dependency ^
  --add-modules ru.covariance.mythkeeperpackager ^
  --launcher launch=ru.covariance.mythkeeperpackager/ru.covariance.mythkeeperpackager.app.Launcher ^
  --output target/linked ^
  --strip-debug ^
  --compress 2 ^
  --no-header-files ^
  --no-man-pages

echo // PACKAGING

call "%JAVA_HOME%bin\jpackage" ^
  --name mythkeeper-packager ^
  --app-version 1.0.0 ^
  --dest target\package ^
  --runtime-image target\linked ^
  --module ru.covariance.mythkeeperpackager/ru.covariance.mythkeeperpackager.app.Launcher ^
  --win-dir-chooser ^
  --win-menu ^
  --win-per-user-install ^
  --win-shortcut
