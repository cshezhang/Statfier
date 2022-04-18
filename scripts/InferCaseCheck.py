import os
from re import L
import sys

from Util import get_files_from_folder, run_infer, merge_dicts, get_direct_files_from_folder

SOURCE_INFER_SEED_PATH = "C:\\Users\\Rainy\\IdeaProjects\\SourceInferSeeds"
INFER_TESTCASE_SEED_PATH = "/home/vanguard/projects/SAMutator/seeds/InferSeeds"
INFER_DOC_SEED_PATH = "/home/vanguard/projects/SAMutator/src/test/java/codetoanalyze/java"
issue_path_list = []
seed_path_list = []

# 1. Filename to Rule type; (Use infer doc)
# 2. Check compilation;
# 3. Infe testing

def get_seed_coveaged_rules():
    seed_path_list = []
    infer_bug_set = set()
    infer_bug_set_file = open(os.getcwd() + os.sep  + "InferBug.list", "r")
    lines = infer_bug_set_file.readlines()
    for line in lines:
        infer_bug_set.add(line.strip())
    infer_bug_set_file.close()
    file_path_list = get_files_from_folder(SOURCE_INFER_SEED_PATH)
    for file_path in file_path_list:
        if file_path.endswith(".java"):
            seed_path_list.append(file_path)
        if "issues.exp" in file_path:
            issue_path_list.append(file_path)
    rules = set()
    filename2bug = dict()
    for issue_file_path in issue_path_list:
        print("Issue Path: " + issue_file_path)
        issue_file = open(issue_file_path, "r")
        lines = issue_file.readlines()
        issue_file.close()
        for line in lines:
            if "params: " in line or "dependent_point_ids: " in line:
                continue
            if ", " in line:
                tokens = line.strip().split(", ")
                found = False
                rule = ""
                for token in tokens:
                    if token in infer_bug_set:
                        rule = token
                        found = True
                filename = tokens[0].split("/")[-1].split(".")[0]
                if found:
                    rules.add(rule)
                    filename2bug[filename] = rule
    return rules, seed_path_list, filename2bug

def get_doc_coveraged_rules():
    doc_rules = set()
    filename2bug = dict()
    path_list = get_direct_files_from_folder(INFER_DOC_SEED_PATH)
    doc_seedpath_list = []
    for doc_seedpath in path_list:
        if doc_seedpath.endswith(".java"):
            doc_seedpath_list.append(doc_seedpath)
            rule = doc_seedpath.split(os.sep)[-1].split(".")[0]
            doc_rules.add(rule)
            filename = doc_seedpath.split(os.sep)[-1].split(".")[0]
            filename2bug[filename] = rule
    return doc_rules, doc_seedpath_list, filename2bug

def main():
    (seed_coveraged_rules, seed_path_list, filename2bug1) = get_seed_coveaged_rules()
    print(filename2bug1)
    # (doc_coveraged_rules, doc_path_list, filename2bug2) = get_doc_coveraged_rules()
    # filename2bug = merge_dicts(filename2bug1, filename2bug2)
    # sum_coveraged_rules = seed_coveraged_rules.union(doc_coveraged_rules)
    # for rule in sum_coveraged_rules:
    #     print(rule)
    # print(len(seed_coveraged_rules))
    # print(len(sum_coveraged_rules))
    # for seedcase_path in seed_path_list:
    #     seedname = seedcase_path.split(os.sep)[-1].split(".")[0]
    #     output_path = os.getcwd() + os.sep + "infer_output" + os.sep + seedname
    #     run_infer(seedcase_path, output_path)
    # for doccase_path in doc_path_list:
    #     seedname = doccase_path.split(os.sep)[-1].split(".")[0]
    #     output_path = os.getcwd() + os.sep + "infer_output" + os.sep + seedname
    #     run_infer(doccase_path, output_path)


main()