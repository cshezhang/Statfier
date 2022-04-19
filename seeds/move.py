import os
import shutil

def get_files_from_folder(folder_path):
    file_list = []
    for file in os.listdir(folder_path):
        file_path = os.path.join(folder_path, file)
        if os.path.isdir(file_path):
            file_list.extend(get_files_from_folder(file_path))
        else:
            file_list.append(file_path)
    return file_list

def main():
    filelist = get_files_from_folder("/home/vanguard/project/SAMutator/seeds/old-Infer_Seeds")
    javalist = []
    for filepath in filelist:
        print(filepath)
        if ".java" in filepath:
            javalist.append(filepath)
    for javapath in javalist:
        print(javapath)
        javaname = javapath.split(os.sep)[-1]
        shutil.copyfile(javapath, "./" + javaname)

def mo():
    filelist = get_files_from_folder("/home/vanguard/project/SAMutator/seeds/Infer_Seeds")
    for i in range(1, 51):
        if(os.path.exists("/home/vanguard/project/SAMutator/seeds/Infer_Seeds" + os.sep + str(i))):
            continue
        os.mkdir("/home/vanguard/project/SAMutator/seeds/Infer_Seeds" + os.sep + str(i))
    for i in range(0, len(filelist)):
        javaname = filelist[i].split(os.sep)[-1]
        index = (i % 50) + 1
        shutil.copyfile(filelist[i], "./Infer_Seeds/" + str(index) + os.sep + javaname)

mo()
