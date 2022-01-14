import os
import sys

seed_path = "/home/vanguard/projects/infer/infer/tests/codetoanalyze/java"
rules = set()

def get_files_from_folder(path, list_name):
    for file in os.listdir(path):
        file_path = os.path.join(path, file)
        if os.path.isdir(file_path):
            get_files_from_folder(file_path, list_name)
        else:
            list_name.append(file_path)

def get_file_to_rule(filepaths):
    file2rule = dict()
    for filepath in filepaths:
        issue_file = open(filepath, "r")
        lines = issue_file.readlines()
        for line in lines:
            tokens = line.split(", ")
            if len(tokens) < 4:
                continue
            filename = tokens[0].split("/")[-1]
            rule = tokens[3].strip()
            if rule[0] < 'A' or rule[0] > 'Z':
                print(line)
                continue
            if filename not in file2rule.keys():
                file2rule[filename] = []
            file2rule[filename].append(rule)
            rules.add(rule)
    return file2rule

def main():
    files = []
    get_files_from_folder(seed_path, files)
    issue_pathes = []
    for file in files:
        if ".exp" in file:
            issue_pathes.append(file)
    get_file_to_rule(issue_pathes)
    for rule in rules:
        print(rule)
    print(len(rules))
main()