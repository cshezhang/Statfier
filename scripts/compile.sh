#!/bin/bash
echo "Building process start..."
 
JAR_PATH=/home/lschen/projects/SAMutator/target/dependency
BIN_PATH=bin
SRC_PATH=/home/lschen/projects/SAMutator/src
 
# java文件列表目录，使用@文件名来管理java源文件，可以简化命令
SRC_FILE_LIST_PATH=src/sources.list
 
# 生所有的java文件列表
rm -f $SRC_PATH/sources.list
find $SRC_PATH/ -name *.java > $SRC_FILE_LIST_PATH
 
#删除旧的编译文件 生成bin目录
rm -rf $BIN_PATH/
mkdir $BIN_PATH/
 
#生成依赖jar包列表
for file in  ${JAR_PATH}/*.jar;
do
jarfile=${jarfile}:${file}
done
echo "jarfile = "$jarfile

# copy配置文件
# cp src/*.properties bin/
# cp src/*.xml bin/ 

#编译
/usr/bin/javac -encoding UTF-8  -d $BIN_PATH/ -cp $jarfile @$SRC_FILE_LIST_PATH
 
#运行，添加了GC监控的选项，不适合生产环境
# /usr/bin/java -Xms40G -Xmx50G -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -verbose:gc -Xloggc:/tmp/gc.log -cp $BIN_PATH$jarfile edu.polyu.AutomaticTester &
/usr/bin/java -Xms40G -Xmx50G -cp $BIN_PATH$jarfile edu.polyu.AutomaticTester &
