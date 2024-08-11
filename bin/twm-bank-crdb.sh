APP_NAME="twm-bank-crdb-service"
JAVA_PARAM="-Xmx501m"

BIN_PATH=$TWM_HOME_PARENT/TWM/$APP_NAME/bin     #TWM-HOME-PARENT :: exported in .bashrc
cd $BIN_PATH/../target/
JAR_NAME=`ls *jar`
JAR_PATH=$BIN_PATH/../target/$JAR_NAME
JAVA_PATH=$HOME/.jdks/jdk17/bin/java

if [[ $# -eq 0 ]]; then
    EUREKA_URI="http://localhost:2012/eureka"
else
    EUREKA_URI="http://192.168.1.$1:2012/eureka"
fi

APP_PARAMS="-DEUREKA_URI=$EUREKA_URI"

echo "Starting '$APP_NAME' with java param: '$JAVA_PARAM', app param: '$APP_PARAMS', at '$JAR_PATH'"
$JAVA_PATH $JAVA_PARAM $APP_PARAMS -jar $JAR_PATH
