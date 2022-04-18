'''
Description: This script is used to clean seeds.
Author: Vanguard
Date: 2021-11-01 10:52:59
'''

import os

def get_files_from_folder(folder_path, list_name):
    for file in os.listdir(folder_path):
        file_path = os.path.join(folder_path, file)
        if os.path.isdir(file_path):
            get_files_from_folder(file_path, list_name)
        else:
            list_name.append(file_path)

def process_file(java_path):
    new_lines = []
    java_file = open(java_path, "r")
    lines = java_file.readlines()
    for line in lines:
        # print("old line: " + line, end="")
        if "DetectorUnderTest" in line:
            continue
        else:
            new_lines.append(line)
    if os.path.exists(java_path):
        os.remove(java_path)
    java_file = open(java_path, "w")
    for line in new_lines:
        # print("line: " + str(line), end="")
        java_file.write(line)

def main():
    file_paths = []
    get_files_from_folder("./SpotBugs_Seeds", file_paths)
    for file_path in file_paths:
        if ".java" not in file_path:
            print("File: " + file_path)
            os.remove(file_path)
        # else:
        #     # print("processing: " + str(file_path))
        #     process_file(file_path)

main()
