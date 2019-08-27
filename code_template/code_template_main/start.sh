#!/bin/bash
cd /home/$LOGNAME/project
PROJECT_HOME="/home/$LOGNAME/project"
JAVA_HOME="/data/apps/jdk"

start(){

        for jar in $PROJECT_HOME/lib/*.jar; do
            CLASSPATH=$CLASSPATH:$jar
        done

        export CLASSPATH;
        echo $CLASSPATH

        JAVA_OPTS=" \
                -Xms512M \
                -Xmx512M \
                -Xmn340M \
                -XX:MaxDirectMemorySize=64m"

		nohup $JAVA_HOME/bin/java $JAVA_OPTS -jar lib/code_template_main.jar --spring.config.location=application.properties --logging.config=logback.xml $* > /dev/null 2>&1 &
		
        echo $! > $PROJECT_HOME/logs/AppMainStarter.pid

        echo -ne "AppAppStarter service starting        \e[1;32mOK\e[m\r\n"
}

help(){
        echo -ne "Usage: musesr \r\n"
        echo -ne "\t -c3p0 <property file path>: set absolute property file path,can't use with -bcp\r\n"
        echo -ne "\n"
}

case $1 in
"help")
        help
;;
*)
   start $*
;;
esac