'''
Description: This is used to compare to result files of Java.
Author: Austin ZHANG
Date: 2021-11-11 10:21:16
'''
import os
import sys

from bs4 import BeautifulSoup
from subprocess import Popen, PIPE

userdir = os.getcwd()
sep = os.sep
spotbugs_path = userdir + sep + "SpotBugs" + sep + "bin" + sep + "spotbugs"

def get_files_from_folder(folder_path):
    file_list = []
    for file in os.listdir(folder_path):
        file_path = os.path.join(folder_path, file)
        if os.path.isdir(file_path):
            file_list.extend(get_files_from_folder(file_path))
        else:
            file_list.append(file_path)
    return file_list

def get_path(parent_name, child_name):
    file_list = get_files_from_folder(userdir + sep + "SingleTesting")
    file_list.extend(get_files_from_folder(userdir + sep + "mutants"))
    parent_path = ""
    child_path = ""
    for file_path in file_list:
        if parent_name + ".java" in file_path:
            parent_path = file_path
        if child_name + ".java" in file_path:
            child_path = file_path
    return (parent_path, child_path)

def invoke_command(cmd):
    print(cmd)
    process = Popen(cmd, stdout=PIPE, stderr=PIPE, shell=True)
    (stdout, stderr) = process.communicate()
    return_value = process.poll()
    if return_value:
        print(stderr.decode("utf-8"))
    else:
        print(stdout.decode("utf-8"))

def parse_spotbugs_report(path):
    print("Path: " + path)
    file = open(path, "r")
    soup = BeautifulSoup(file, "lxml")
    bug_instances = soup.find_all("buginstance")
    for bug_instance in bug_instances:
        bug_type = bug_instance.attrs["type"]
        print(bug_type, end=": ")
        src_lines = bug_instance.find("shortmessage").find_next_siblings("sourceline")
        print("Count: " + str(len(src_lines)) + ", Index:(", end="")
        for src_line in src_lines:
            if "start" in src_line.attrs.keys():
                print(src_line.attrs["start"], end=" ")
        print(")")

def main():
    parent_name = sys.argv[1]
    child_name = sys.argv[2]
    (parent_path, child_path) = get_path(parent_name, child_name)
    print("Parent Path: " + parent_path)
    print("Child Path: " + child_path)
    if not os.path.exists("." + sep + "Diff_Res" + sep + parent_name + sep):
        os.mkdir("." + sep + "Diff_Res" + sep + parent_name + sep)
    if not os.path.exists(userdir + sep + "Diff_Res" + sep + child_name + sep):
        os.mkdir(userdir + sep + "Diff_Res" + sep + child_name + sep)
    invoke_command("javac -d " + userdir + sep + "Diff_Res" + sep + parent_name + sep + " " + parent_path)
    invoke_command("javac -d " + userdir + sep + "Diff_Res" + sep + child_name + sep + " " + child_path)
    parent_res = userdir + sep + "SpotBugs_Results" + sep + parent_name + "_Result.xml"
    child_res = userdir + sep + "SpotBugs_Results" + sep + child_name + "_Result.xml"
    invoke_command(spotbugs_path + " -textui -xml:withMessages -output " + parent_res + " " + userdir + sep + "Diff_Res" + sep + parent_name + sep)
    invoke_command(spotbugs_path + " -textui -xml:withMessages -output " + child_res + " " + userdir + sep + "Diff_Res" + sep + child_name + sep)
    parse_spotbugs_report(parent_res)
    parse_spotbugs_report(child_res)
    
main()