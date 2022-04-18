import os

from Util import get_files_from_folder

userdir = os.getcwd()
sep = os.sep

def main():
    file_list = get_files_from_folder(userdir + sep + "CheckStyleDocSeeds")
    xml_list = []
    for file_path in file_list:
        if ".xml" in file_path and os.path.exists(file_path):
            os.remove(file_path)

main()