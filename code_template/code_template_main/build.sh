#!/bin/bash

projectPath=/home/$LOGNAME/build/code_template
projectName=code_template_main
projectWebName=template_web
cd $projectPath


# git config --global credential.helper store
git pull

# ------ web build and cp ------- server build
# cd $projectPath/$projectWebName
# npm install
# npm run build
# cp -r $projectPath/$projectWebName/dist/* $projectPath/$projectName/src/main/resources/public/admin/

cd $projectPath

rm -rf $projectPath/$projectName/lib/*
rm -rf /home/$LOGNAME/project/lib/*

start_file=/home/$LOGNAME/project/start.sh
if [ ! -f "$start_file" ]; then 
	ln -s $projectPath/$projectName/start.sh $start_file 
fi 

# cp -rf $projectPath/src/main/resources/config/application.properties /home/$LOGNAME/project/
cp -rf $projectPath/$projectName/src/main/resources/logback.xml /home/$LOGNAME/project/

mvn dependency:copy-dependencies -DoutputDirectory=lib -DincludeScope=compile
cp $projectPath/$projectName/lib/*.jar /home/$LOGNAME/project/lib/

mvn clean install -D maven.test.skip=true
cp -rf $projectPath/$projectName/target/$projectName.jar /home/$LOGNAME/project/lib/


kill_id=`jps |grep $projectName.jar|awk '{print $1}'`
if [ 'xx' != 'xx'$kill_id ];then
	kill -9 $kill_id
fi

cd /home/$LOGNAME/project/
sh /home/$LOGNAME/project/start.sh



