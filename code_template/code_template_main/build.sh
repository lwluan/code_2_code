#!/bin/bash
cd /home/$LOGNAME/build/code_template

# git config --global credential.helper store

git pull

rm -rf /home/$LOGNAME/build/code_template/code_template_main/lib/*
rm -rf /home/$LOGNAME/project/lib/*

start_file=/home/$LOGNAME/project/start.sh
if [ ! -f "$start_file" ]; then 
	ln -s /home/$LOGNAME/build/code_template/code_template_main/start.sh $start_file 
fi 

# cp -rf /home/$LOGNAME/build/code_template/src/main/resources/config/application.properties /home/$LOGNAME/project/
cp -rf /home/$LOGNAME/build/code_template/code_template_main/src/main/resources/log4j2.xml /home/$LOGNAME/project/

mvn dependency:copy-dependencies -DoutputDirectory=lib -DincludeScope=compile
cp /home/$LOGNAME/build/code_template/code_template_main/lib/*.jar /home/$LOGNAME/project/lib/

mvn clean install -D maven.test.skip=true
cp -rf /home/$LOGNAME/build/code_template/code_template_main/target/code_template_main.jar /home/$LOGNAME/project/lib/


kill_id=`jps |grep code_template_main.jar|awk '{print $1}'`
if [ 'xx' != 'xx'$kill_id ];then
	kill -9 $kill_id
fi

cd /home/$LOGNAME/project/
sh /home/$LOGNAME/project/start.sh



