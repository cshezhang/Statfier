import os
import random
import shutil

def split_list(num, srcList):
    group_number = int(len(srcList) / num)
    # Check whether exist remaining files
    last_len = len(srcList) % num
    groups = []
    for i in range(group_number):
        i = i + 1
        n = i * num
        m = n - num
        groups.append(srcList[m:n])
    if  last_len != 0:
        groups[-1].extend(srcList[-last_len:])
    return groups

def get_filepaths_from_folder():
    targetDirPath = '/home/vanguard/projects/SAMutator/seeds/PMD_Seeds'
    filepaths = []
    for (targetDir, dirnamess, filenames) in os.walk(targetDirPath):
        for filename in filenames:
            filepaths.append(targetDir + os.sep + filename)
    random.shuffle(filepaths)
    return filepaths

def main():
    filepaths = get_filepaths_from_folder()
    groups = split_list(int(len(filepaths) / 20), filepaths)
    for i in range(0, len(groups)):
        sub_folder_path = '/home/vanguard/projects/SAMutator/seeds/' + 'Sub_Seeds_' + str(i)
        if not os.path.exists(sub_folder_path):
            os.mkdir(sub_folder_path)
        for src_filepath in groups[i]:
            filename = src_filepath.split("/")[-1]
            shutil.copyfile(src_filepath, sub_folder_path + os.sep + filename)

main()
