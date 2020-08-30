jarNames=( \
  "commons-io-2.6" \
  "json-20200518" \
)

descriptors=( \
  "org.apache.commons.io" \
  "org.json"
)

unusedJars=( \
  "javafx-base-14.0.2" \
  "javafx-controls-14.0.2" \
  "javafx-fxml-14.0.2" \
  "javafx-graphics-14.0.2" \
)

echo ">> COMPILING"

mvn --file pom.xml \
      clean \
      dependency:copy-dependencies \
      compile

echo ">> REMOVING EMPTY DEPENDENCIES"

for (( i=0; i<${#unusedJars[@]}; i++ ))
do
  echo removing ${unusedJars[$i]}
  rm target/dependency/${unusedJars[$i]}.jar
done

echo ">> COMPILING MODULE INFOS"

for (( i=0; i<${#jarNames[@]}; i++ ))
do

  echo Processing ${descriptors[$i]} in ${jarNames[$i]}.jar

  $JAVA14/bin/jdeps \
    --module-path target/dependency \
    --generate-module-info target/module-info \
    target/dependency/${jarNames[$i]}.jar

  $JAVA14/bin/javac \
    --module-path target/dependency \
    -d target/module-extensions/${descriptors[$i]} \
    --patch-module ${descriptors[$i]}=target/dependency/${jarNames[$i]}.jar \
    target/module-info/${descriptors[$i]}/module-info.java

  $JAVA14/bin/jar \
    uf target/dependency/${jarNames[$i]}.jar \
    -C target/module-extensions/${descriptors[$i]} module-info.class

done

echo ">> LINKING"

$JAVA14/bin/jlink \
  --module-path target/classes:target/dependency \
  --add-modules ru.covariance.mythkeeperpackager \
  --launcher launch=ru.covariance.mythkeeperpackager/ru.covariance.mythkeeperpackager.app.Launcher \
  --output target/linked \
  --strip-debug \
  --compress 2 \
  --no-header-files \
  --no-man-pages

echo ">> SPACE USAGE"

du -h target/linked

echo ">> PACKAGING"

$JAVA14/bin/jpackage \
  --name mythkeeper-packager \
  --dest target/package \
  --runtime-image target/linked \
  --module ru.covariance.mythkeeperpackager/ru.covariance.mythkeeperpackager.app.Launcher \
  --linux-shortcut
