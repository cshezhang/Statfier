#!/bin/bash
echo "Building process start..."

JAR_PATH=/PATH/TO/DEPENDENCY
BIN_PATH=bin
SRC_PATH=/PATH/TO/SRC
 
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

java -Xms16G -Xmx64G -cp $BIN_PATH$jarfile MainClassName &
