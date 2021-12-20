###
 # @Description: 
 # @Author: Austin ZHANG
 # @Date: 2021-10-19 09:44:14
### 

mvn compile
mvn exec:java -Dexec.mainClass="edu.polyu.AutomaticTester" -Dexec.args="-Xms32G -Xmx50G -ea"

# SpotBugsResultPATH=/home/lschen/projects/SAMutator/SpotBugs_Results
# rm -rf $SpotBugsResultPATH/
# mkdir $SpotBugsResultPATH/
