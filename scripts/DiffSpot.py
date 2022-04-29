import os
import json
import shutil
from unittest.result import failfast
from bs4 import BeautifulSoup

def get_files_from_folder(folder_path):
    file_list = []
    for file in os.listdir(folder_path):
        file_path = os.path.join(folder_path, file)
        if os.path.isdir(file_path):
            file_list.extend(get_files_from_folder(file_path))
        else:
            file_list.append(file_path)
    return file_list

def move_files():
    report_paths = get_files_from_folder("/home/vanguard/evaluation/SpotBugs_GuidedDiv_Seed5/SpotBugs_Results/")
    sp_report_path = "/home/vanguard/sp_reports"
    names = set()
    for report_path in report_paths:
        report_name = report_path.split(os.sep)[-1]
        if report_name in names:
            continue
        names.add(report_name)
        shutil.copyfile(report_path, sp_report_path + os.sep + report_name)

def parse_sp_report(path):
    report_file = open(path, "r")
    soup = BeautifulSoup(report_file.read(), "xml")
    bug_instances = soup.find_all("BugInstance")
    warn2cnt = dict()
    for bug_instance in bug_instances:
        warn = bug_instance["type"]
        if warn not in warn2cnt.keys():
            warn2cnt[warn] = 0
        warn2cnt[warn] = warn2cnt[warn] + 1
    return warn2cnt

def main():
    rule2issues = dict()
    seed_paths = []
    mutant_paths = []
    bug_types = []
    transforms = []
    spotbugs_result_path = "/home/vanguard/evaluation/SpotBugs_GuidedDiv_Seed5/results/"
    json_paths = get_files_from_folder(spotbugs_result_path)
    for json_path in json_paths:
        json_file = open(json_path, "r")
        json_data = json.loads(json_file.read())
        rule = json_data["Rule"]
        rule2issues[rule] = dict()
        results = json_data["Results"]
        for result in results:
            seq = result["Transform_Sequence"]
            rule2issues[rule][seq] = []
            bugs = result["Bugs"]
            for bug in bugs:
                seed_path = bug["Seed"]
                seed_paths.append(seed_path)
                mutant_path = bug["Mutant"]
                mutant_paths.append(mutant_path)
                bug_type = bug["BugType"]
                bug_types.append(bug_type)
                transforms.append(seq)
                rule2issues[rule][seq].append((seed_path, mutant_path, bug_type))
    fail_cnt = 0
    right_bug = 0
    error_bug = 0
    false_bug = 0
    for i in range(0, len(seed_paths)):
        seed_path = seed_paths[i]
        mutant_path = mutant_paths[i]
        bug_type = bug_types[i]
        transform = transforms[i]
        seed_report_path = "/home/vanguard/sp_reports/" + seed_path.split(os.sep)[-1].split(".")[0] + "_Result.xml"
        mutant_report_path = "/home/vanguard/sp_reports/" + mutant_path.split(os.sep)[-1].split(".")[0] + "_Result.xml"
        if(not os.path.exists(seed_report_path)) or (not os.path.exists(mutant_report_path)):
            fail_cnt = fail_cnt + 1
            continue
        seed_warn2cnt = parse_sp_report(seed_report_path)
        mutant_warn2cnt = parse_sp_report(mutant_report_path)
        fns = []
        fps = []
        for key in seed_warn2cnt.keys():
            if (key not in mutant_warn2cnt.keys()) or (seed_warn2cnt[key] > mutant_warn2cnt[key]):
                fns.append(key)
        for key in mutant_warn2cnt.keys():
            if key not in seed_warn2cnt.keys():
                fps.append(key)
            else:
                if (seed_warn2cnt[key] == mutant_warn2cnt[key] - 1 and transform != "[AddControlBranch]") or (seed_warn2cnt[key] < mutant_warn2cnt[key] - 1):
                    fps.append(key)
        if len(fns) != 0:
        # if len(fns) != 0 or len(fps) != 0:
            ok = True
            if transform == "[AddControlBranch]" and len(fps) == 1 and fps[0] == "DB_DUPLICATE_BRANCHES":
                ok = False
            if transform == "[TransferLocalVarToGlobal]" and len(fps) == 1 and fps[0] == "SS_SHOULD_BE_STATIC":
                ok = False
            if ok:
                print("Seed Path: " + seed_path)
                print("Mutant_Path: " + mutant_path)
                print("Transform: " + transform)
                print("FN: " + str(fns))
                print("FP: " + str(fps))
                right_bug = right_bug +1
                print("------------------------")
            else:
                false_bug = false_bug + 1
        else:
            error_bug = error_bug + 1
    print("Report Len: " + str(len(seed_paths)))
    print("Fail Len: " + str(fail_cnt))
    print("Right Bug: " + str(right_bug) + " Error Bug: " + str(error_bug) + " Tool FP: " + str(false_bug))
        
# move_files()
main()