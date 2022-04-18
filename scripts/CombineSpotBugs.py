import os
import sys

from Util import get_files_from_folder

spotbugs_testcase_path = "/home/vanguard/projects/SAMutator/seeds/Old_SpotBugs_Seeds"
spotbugs_doccase_path = "/home/vanguard/projects/SAMutator/seeds/SpotBugs_DocCases"

def printTags(file_path_list):
    seed_bugs = set()
    for file_path in file_path_list:
        seed_file = open(file_path, "r")
        lines = seed_file.readlines()
        for line in lines:
            if "@Override" in line or "@Retention" in line or "@Target" in line:
                continue
            if line.strip().startswith("@") and "Warning" in line and "(" in line:
                # print(line.strip())
                # sc = line.count("_")
                # if sc <= 1:
                #     continue
                rule_type = ""
                if "\"" in line:
                    rule_type = line[line.find("\"") + 1 : line.rfind("\"")]
                else:
                    rule_type = line.strip().split("(")[1][1:-2]
                if "," in rule_type:
                    rules = rule_type.split(",")
                    for rule in rules:
                        seed_bugs.add(rule.strip())
                else:
                    seed_bugs.add(rule_type)
            # if "@DesireWarning" in line or "ExpectWarning" in line:
            #     res.append(line.strip())
    return seed_bugs
                
def main():
    testcase_path_list = get_files_from_folder(spotbugs_testcase_path)
    doccase_path_list = get_files_from_folder(spotbugs_doccase_path)
    doc_bugs = set()
    for doccase in doccase_path_list:
        bug_type = doccase.split(os.sep)[-1].split(".")[0]
        doc_bugs.add(bug_type)
    seed_bugs = printTags(testcase_path_list)
    all_bugs = doc_bugs.union(seed_bugs)
    for bug in all_bugs:
        print(bug)
    print(len(all_bugs))
main()