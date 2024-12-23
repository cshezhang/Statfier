#!/bin/bash
echo "Building process start..."

JAR_PATH=/home/huaien/projects/SAMutator/target/dependency
BIN_PATH=bin
SRC_PATH=/home/huaien/projects/SAMutator/src
 
# Path to the java file list
SRC_FILE_LIST_PATH=src/sources.list
 
# Generate a list included all java files
rm -f $SRC_PATH/sources.list
find $SRC_PATH/ -name *.java > $SRC_FILE_LIST_PATH
 
# Delete old compiled files, and generate bin directory
rm -rf $BIN_PATH/
mkdir $BIN_PATH/
 
# Generate dependency jar file list
for file in  ${JAR_PATH}/*.jar;
do
jarfile=${jarfile}:${file}
done

# copy config files
# cp src/*.properties bin/
# cp src/*.xml bin/ 

# Compile
javac -encoding UTF-8  -d $BIN_PATH/ -cp $jarfile @$SRC_FILE_LIST_PATH
 
#Add GC monitor, not suitable for production environment
# /usr/bin/java -Xms40G -Xmx50G -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -verbose:gc -Xloggc:/tmp/gc.log -cp $BIN_PATH$jarfile edu.polyu.AutomatedTester &
java -Xms32G -Xmx160G -cp $BIN_PATH$jarfile edu.polyu.AutomatedTester &
