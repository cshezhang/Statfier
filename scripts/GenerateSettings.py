from Util import get_direct_files_from_folder

sonar_seed_path = "/Users/austin/project/SAMutator/seeds/SonarQube_Test"
sonar_project_name = "Statfier"
sonar_project_key = "sqp_b5cd7ba6cd143a589260158df861fcf43a20f5b9"

def main():
    seed_folder_paths = get_direct_files_from_folder(sonar_seed_path)
    for seed_folder_path in seed_folder_paths:
        contents = []
        contents.append("sonar.projectKey=" + sonar_project_name + "\nsonar.projectName=" + sonar_project_name + "\nsonar.projectVersion=1.0")
        contents.append("sonar.login=" + sonar_project_key)
        contents.append("sonar.sourceEncoding=UTF-8")
        contents.append("")

main()