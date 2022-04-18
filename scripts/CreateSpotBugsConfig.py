import os
import sys
from this import d


def get_dirs_from_folder(folder_path, dir_list):
    for file in os.listdir(folder_path):
        file_path = os.path.join(folder_path, file)
        if os.path.isdir(file_path):
            dir_list.append(file_path)

def main():
    dir_list = []
    get_dirs_from_folder("/home/vanguard/projects/SAMutator/seeds/SpotBugs_Seeds", dir_list)
    rule_config_path = os.getcwd() + os.sep + "SpotBugs_Rule_Config"
    if not os.path.exists(rule_config_path):
        os.mkdir(rule_config_path)
    for dir_path in dir_list:
        rule_name = dir_path.split(os.sep)[-1]
        xml_path = rule_config_path + os.sep + rule_name + ".xml"
        xml_file = open(xml_path, "w")
        content = """<?xml version="1.0" encoding="UTF-8"?>
<FindBugsFilter
    xmlns="https://github.com/spotbugs/filter/3.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="https://github.com/spotbugs/filter/3.0.0 https://raw.githubusercontent.com/spotbugs/spotbugs/3.1.0/spotbugs/etc/findbugsfilter.xsd">
    <Match>
        <Bug code=""" + "\"" + rule_name + "\"" + " />\n"
        content += "    </Match>\n</FindBugsFilter>"
        xml_file.write(content)
        xml_file.close()

main()