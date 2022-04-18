import os
import sys
import subprocess

from bs4 import BeautifulSoup

file2bug = dict()

detection_folder = "/home/vanguard/evaluation/Idea_SpotBugs_Testing/results/Check_SpotBugs_Results"
seed_folder_path = "/home/vanguard/projects/SAMutator/seeds/SpotBugs_Seeds"

def run_cmd(command):
    exitcode, output = subprocess.getstatusoutput(command)
    if exitcode != 0:
        print("Output: " + output)
        print("Command: " + command)
        sys.exit(0)
    return exitcode

def get_files_from_folder(folder_path):
    file_list = []
    for file in os.listdir(folder_path):
        file_path = os.path.join(folder_path, file)
        if os.path.isdir(file_path):
            file_list.extend(get_files_from_folder(file_path))
        else:
            file_list.append(file_path)
    return file_list

def createFile2Bug():
    seed_path_list = get_files_from_folder(seed_folder_path)
    for seed_path in seed_path_list:
        folder_name = seed_path.split("/")[-2]
        file2bug[seed_path] = folder_name
        
def parseSpotBugsXMLResult(xml_path):
    file  = open(xml_path, "r")
    soup = BeautifulSoup(file, "xml")
    bug_instances = soup.find_all("BugInstance")
    bug_codes = soup.find_all("BugCode")
    detected_bugs1 = []
    detected_bugs2 = []
    for bug_instance in bug_instances:
        bug_type = bug_instance.attrs["type"]
        detected_bugs1.append(bug_type)
    for bug_code in bug_codes:
        bug_abbrev = bug_code.attrs["abbrev"]
        detected_bugs2.append(bug_abbrev)
    filename = xml_path.split("/")[-1].split(".")[0][0:-7]
    for key in file2bug.keys():
        if key.endswith(filename + ".java"):
            code_file = open(key, "r")
            lines = code_file.readlines()
            code_file.close()
            needWarn = False
            tagged_bugs = file2bug[key]
            for line in lines:
                if ("ExpectWarning(\"" + tagged_bugs + "\")" in line) or ("DesireWarning(\"" + tagged_bugs + "\")" in line):
                     needWarn = True
            if needWarn and tagged_bugs not in detected_bugs1 and tagged_bugs not in detected_bugs2:
                if len(detected_bugs1) == 0 and len(detected_bugs2) == 0:
                    print("---------------------------------------------")
                    print("Filepath: " + key)
                    print("Tagged Bugs: " + tagged_bugs)
                    print("Bug Instances: " + str(detected_bugs1))
                    print("Bug Codes: " + str(detected_bugs2))
                    print("---------------------------------------------")
    
def execuateSpotBugs():
    cmd_file = open("./cmds.log", "r")
    lines = cmd_file.readlines()
    cmd_file.close()
    for line in lines:
        tokens = line.split()
        cmd = ""
        for i in range(0, len(tokens)):
            if i == 2:
                cmd = cmd + "-effort:max "
                continue
            if i == 3:
                continue
            cmd = cmd + tokens[i] + " "
        print(cmd)
        run_cmd(cmd)

def main():
    # execuateSpotBugs()
    createFile2Bug()
    result_file_list = get_files_from_folder(detection_folder)
    for result_file in result_file_list:
        parseSpotBugsXMLResult(result_file)
        
main()