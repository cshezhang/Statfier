#!/bin/bash
echo "Building process start..."
 
JAR_PATH=/home/vanguard/project/SAMutator/target/dependency
BIN_PATH=bin
SRC_PATH=/home/vanguard/project/SAMutator/src
 
# java文件列表目录，使用@文件名来管理java源文件，可以简化命令
SRC_FILE_LIST_PATH=src/sources.list
 
# 生所有的java文件列表
rm -f $SRC_PATH/sources.list
find $SRC_PATH/ -name *.java > $SRC_FILE_LIST_PATH
 
# 删除旧的编译文件 生成bin目录
rm -rf $BIN_PATH/
mkdir $BIN_PATH/
 
# Generate Jar dependency file list
for file in  ${JAR_PATH}/*.jar;
do
jarfile=${jarfile}:${file}
done
# echo "jarfile = "$jarfile

# Copy config files
# cp src/*.properties bin/
# cp src/*.xml bin/ 

# Compile
javac -encoding UTF-8  -d $BIN_PATH/ -cp $jarfile @$SRC_FILE_LIST_PATH
 
# 运行，添加了GC监控的选项，不适合生产环境
# /usr/bin/java -Xms40G -Xmx50G -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -verbose:gc -Xloggc:/tmp/gc.log -cp $BIN_PATH$jarfile edu.polyu.AutomaticTester &
java -Xms1G -Xmx2G -cp $BIN_PATH$jarfile edu.polyu.DiffSQAnalysis &
