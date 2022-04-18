import os
import urllib3
import subprocess

from bs4 import BeautifulSoup

bug_categories = ["annotation", "blocks", "design", "coding", "header", "imports", "javadoc", "metrics", "misc", "modifier", "naming", "regexp", "sizes", "whitespace"]
userdir = os.getcwd()
sep = os.sep
seed_folder_path = userdir  + File.separator + "CheckStyleSeed"

class CommandException(Exception):
    pass

def run_cmd(command):
    exitcode, output = subprocess.getstatusoutput(command)
    # if exitcode == 254:
        # print(output)
        # print("Command: " + command)
        # raise(CommandException())
    return exitcode

def get_html_content(url):
    http = urllib3.PoolManager()
    response = http.request('GET', url)
    content = response.data.decode()
    response.release_conn()
    return content

def get_files_from_folder(folder_path, list_name):
    for file in os.listdir(folder_path):
        file_path = os.path.join(folder_path, file)
        if os.path.isdir(file_path):
            get_files_from_folder(file_path, list_name)
        else:
            list_name.append(file_path) 
                
def main():
    if not os.path.exists("./Res"):
        os.mkdir("Res")
    file_list = []
    get_files_from_folder("CheckStyleDocSeeds", file_list)
    xml_list = []
    code_list = []
    for file in file_list:
        if ".xml" in file:
            xml_list.append(file)
        if ".code" in file:
            code_list.append(file)
    file_names = set()
    for xml_file in xml_list:
        file_names.add(xml_file[:-4])
    jar_path = "/home/vanguard/projects/SAMutator/tools/CheckStyle.jar"
    current_dir = os.getcwd()
    error_execution = 0
    for file_path in file_names:
        # print(file_path)
        xml_filepath = current_dir + os.sep + file_path + ".xml"
        java_filepath = current_dir + os.sep + file_path + ".java"
        report_path = current_dir + "/Res/" + file_path.split("/")[-1] + ".xml"
        cmd_args = "java -jar " + jar_path + " -c " + xml_filepath + " -f xml -o " + report_path + " " + java_filepath
        # print("Command: " + cmd_args)
        detected_bug_count = run_cmd(cmd_args)
        java_file = open(java_filepath, "r")
        lines = java_file.readlines()
        auto_bug_count = 0
        for line in lines:
            if "violation" in line.lower() and ("//" in line or "*" in line):
                auto_bug_count = auto_bug_count + 1
        if detected_bug_count == 254:
            error_execution = error_execution + 1
            continue
        report_file = open(report_path, "r")
        soup = BeautifulSoup(report_file, "xml")
        error_tags = soup.find_all("error")
        line_numbers = set()
        for error_tag in error_tags:
            line_number = int(error_tag.attrs["line"])
            line_numbers.add(line_number)
        detected_bug_count = len(line_numbers)
        if detected_bug_count != 0 and auto_bug_count != detected_bug_count and detected_bug_count != 254:
            print("---------------")
            print("Detected: " + str(detected_bug_count))
            print("Text: " + str(auto_bug_count))
            print("Java File Path: " + java_filepath)
            print("Report Path: " + report_path)
            print(cmd_args)
            print("---------------")
    print("Error Execution: " + str(error_execution))
        # java com.puppycrawl.tools.checkstyle.Main -c /sun_checks.xml -f xml \-o build/checkstyle_errors.xml Check.java
#         xml_file = open(xml_filepath, "r")
#         xml_content = xml_file.read().strip()
#         header = """<?xml version="1.0"?>
# <!DOCTYPE module PUBLIC
#     "-//Checkstyle//DTD Checkstyle Configuration 1.3//EN"
#     "https://checkstyle.org/dtds/configuration_1_3.dtd">
# <module name="Checker">
#   <property name="severity" value="error"/>
#   <property name="fileExtensions" value="java, properties, xml"/>
#   <module name="TreeWalker">
# """
#         xml_content = header + xml_content
#         footer = """
#   </module>
# </module>
#         """
#         xml_content = xml_content + footer
#         new_xml_file = open(xml_filepath, "w")
#         new_xml_file.write(xml_content)
#         new_xml_file.close()

# <module name="Checker">
#   <property name="severity" value="error"/>
#   <property name="fileExtensions" value="java, properties, xml"/>
#   <module name="TreeWalker">
#     <module name="ModifiedControlVariable"/>
#   </module>
# </module>

main()