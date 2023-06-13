#!/bin/bash
echo "Building process start..."
 
JAR_PATH=/home/huaien/projects/SAMutator1/target/dependency
BIN_PATH=bin
SRC_PATH=/home/huaien/projects/SAMutator1/src
 
SRC_FILE_LIST_PATH=src/sources.list
 
rm -f $SRC_PATH/sources.list
find $SRC_PATH/ -name *.java > $SRC_FILE_LIST_PATH
 
rm -rf $BIN_PATH/
mkdir $BIN_PATH/

for file in  ${JAR_PATH}/*.jar;
do
jarfile=${jarfile}:${file}
done
# echo "jarfile = "$jarfile

# cp src/*.properties bin/
# cp src/*.xml bin/ 

javac -encoding UTF-8  -d $BIN_PATH/ -cp $jarfile @$SRC_FILE_LIST_PATH
 
# /usr/bin/java -Xms40G -Xmx50G -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -verbose:gc -Xloggc:/tmp/gc.log -cp $BIN_PATH$jarfile edu.polyu.AutomaticTester &
java -Xms64G -Xmx128G -cp $BIN_PATH$jarfile org.detector.ComparisonEvaluation &
