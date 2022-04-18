import os
import time
import shlex
import urllib3
import subprocess

def get_html_content(url):
    http = urllib3.PoolManager()
    response = http.request('GET', url)
    content = response.data.decode()
    response.release_conn()
    return content

def remove_pkg_definition():
    # remove pkg definition of java code files
    seed_folder_path = "/home/vanguard/projects/SAMutator/seeds/SpotBugs_Seeds"
    seed_paths = get_files_from_folder(seed_folder_path)
    for seed_path in seed_paths:
        seed_file = open(seed_path, "r")
        lines = seed_file.readlines()
        seed_file.close()
        new_code_lines = []
        for line in lines:
            if "package" not in line:
                new_code_lines.append(line)
        seed_file = open(seed_path, "w")
        seed_file.writelines(new_code_lines)
        seed_file.close()

def merge_dicts(*dict_args):
    """
    Given any number of dicts, shallow copy and merge into a new dict,
    precedence goes to key value pairs in latter dicts.
    """
    result = {}
    for dictionary in dict_args:
        result.update(dictionary)
    return result

def run_cmd(command):
    cmd_args = shlex.split(command)
    process = subprocess.Popen(cmd_args, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    stdout, stderr = process.communicate()
    while process.poll() is None:
        time.sleep(0.25)
    returncode = process.returncode
    return (returncode, stdout, stderr)

def get_files_from_folder(folder_path):
    file_list = []
    for file in os.listdir(folder_path):
        file_path = os.path.join(folder_path, file)
        if os.path.isdir(file_path):
            file_list.extend(get_files_from_folder(file_path))
        else:
            file_list.append(file_path)
    return file_list

def get_direct_files_from_folder(folder_path):
    file_list = []
    for file in os.listdir(folder_path):
        file_path = os.path.join(folder_path, file)
        if not os.path.isdir(file_path):
            file_list.append(file_path)
    return file_list

def run_infer(source_path, target_path):
    # infer run -o /tmp/out -- javac Test.java
    infer_path = "/home/vanguard/bin/infer/bin/infer"
    jar_list = get_files_from_folder("/home/vanguard/projects/SAMutator/tools/Infer_Dependency")
    jar_str = ".:"
    for jar_path in jar_list:
        jar_str = jar_str + jar_path + ":"
    jar_str = jar_str[0 : -1]
    cmd = infer_path + " run -o " + target_path + " -- javac -cp " + jar_str + " " + source_path
    (returncode, stdout, stderr) = run_cmd(cmd)
    if returncode != 0:
        print("Error Path: " + source_path)
        print(cmd)