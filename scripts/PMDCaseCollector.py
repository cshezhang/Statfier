import os
import random
import shutil
import urllib.request
import pandas as pd

from bs4 import BeautifulSoup

userdir = os.path.expanduser('~')
currentdir = os.getcwd()
sep = os.sep
save_path = userdir  + File.separator + "projects"  + File.separator + "SAMutator"  + File.separator + "seeds"  + File.separator + "PMD_Seeds" + sep

bug_categories = [
  "bestpractices",
  "codestyle",
  "design",
  "documentation",
  "errorprone",
  "multithreading",
  "performance",
  "security"
]

def getRuleNames():
  cate2rules = dict()
  namelink_source = "https://pmd.github.io/latest/pmd_rules_java_"
  for category in bug_categories:
    rule2remove = set()
    cate2rules[category] = set()
    namelink = namelink_source + category + ".html"
    # print("namelink: " + namelink)
    response = urllib.request.urlopen(namelink)
    if response.getcode() != 200:
      print("Name Html has an error!")
    html_doc = response.read()
    soup = BeautifulSoup(html_doc, "html.parser", from_encoding="utf-8")
    meta_tags = soup.find_all("meta")
    tokens = meta_tags[4].attrs["content"].split(",")
    for i in range(0, len(tokens)):
      tokens[i] = tokens[i][1:]
    tokens = tokens[1:]
    tags = soup.find_all("span")
    for i in range(0, len(tags)):
      tag = tags[i]
      if tag and tag.string == "Deprecated" and tag.parent.string == "Deprecated":
        rule2remove.add(str(tag.parent.previous_element.previous_element))
    rules = set()
    rules.update(tokens)
    cate2rules[category].update(rules - rule2remove)
    # print(len(rule2remove))
    # print(len(rules))
    # print(len(rules - rule2remove))
    # print("")
  return cate2rules

def getTestCases(cate2rules):
  bugs = set()
  file2rule = dict()
  filenames = []
  problem_cnts = []
  descriptions = []
  cates = []
  rules = []
  xml_file_paths = []
  fps = []
  if not os.path.exists(save_path):
    os.mkdir(save_path)
  for cate, rulesets in cate2rules.items():
    print(cate)
    for rule in rulesets:
      # xml_link = "https://raw.githubusercontent.com/pmd/pmd/81739da5caff948dbcd2136c17532b65c726c781/pmd-java/src/test/resources/net/sourceforge/pmd/lang/java/rule/" + cate + "/xml/" + rule + ".xml"
      # try:
      #   xml_response = urllib.request.urlopen(xml_link)
      # except urllib.error.HTTPError:
      #   # print("Cannot find XML file in the rule: " + cate + "  " + rule)
      #   fails.append((cate + "  " + rule, xml_link))
      # # case link: https://raw.githubusercontent.com/pmd/pmd/81739da5caff948dbcd2136c17532b65c726c781/pmd-java/src/test/resources/net/sourceforge/pmd/lang/java/rule/codestyle/xml/AbstractNaming.xml
      # xml_doc = xml_response.read()
      xml_folder_path = currentdir  + File.separator + "xml_folder"
      xml_file_path = xml_folder_path  + File.separator + cate  + File.separator + "xml"  + File.separator + rule + ".xml"
      if not os.path.exists(xml_file_path):
        continue
      xml_doc = open(xml_file_path)
      soup = BeautifulSoup(xml_doc, "xml", from_encoding="utf-8")
      test_codes = soup.find_all("test-code")
      for index in range(0, len(test_codes)):
        test_code = test_codes[index]
        description = test_code.find("description").string.lower() 
        problems_cnt_tag = test_code.find("expected-problems")
        problem_cnt = int(problems_cnt_tag.string)
        code = test_code.code
        filename = rule + str(index) + ".java"
        if code == None:
          continue
        filenames.append(filename)
        cates.append(cate)
        rules.append(rule)
        descriptions.append(description)
        xml_file_paths.append(xml_file_path)
        file2rule[filename] = cate + "_" + rule
        bugs.add(cate + "_" + rule)
        problem_cnts.append(problem_cnt)
        if "false" in description and "positive" in description and problem_cnt == 0:
          fps.append("true")
        else:
          fps.append("false")
        testfile = open(save_path + rule + str(index) + ".java", "w")
        testfile.write(str(code.get_text()))
        testfile.close()
  # if len(fails) > 0:
  #   print("failed len=" + str(len(fails)) + " and rules are:")
  #   for fail_info, link in fails:
  #     print(fail_info)
  #     print(link)
  dataframe = pd.DataFrame({"filename":filenames, "cate":cates, "rules":rules, "description":descriptions, "problem_cnt":problem_cnts, "FP":fps, "XML_Path":xml_file_paths})
  dataframe.to_csv("PMDSeedData.csv",index=False,sep=',')
  return bugs, file2rule


def get_filepaths_from_folder():
    targetDirPath = userdir  + File.separator + 'projects'  + File.separator + 'SAMutator'  + File.separator + 'seeds'+ sep + 'PMD_Seeds'
    filepaths = []
    for (targetDir, dirnamess, filenames) in os.walk(targetDirPath):
        for filename in filenames:
            filepaths.append(targetDir  + File.separator + filename)
    random.shuffle(filepaths)
    return filepaths

def main():
  cate2rules = getRuleNames()
  bugs, file2rule = getTestCases(cate2rules)
  # print(bugs)
  filepaths = get_filepaths_from_folder() 
  # print(filepaths)
  # print(file2rule)
  for bug in bugs:
    bug_dir_path = userdir  + File.separator + 'projects'  + File.separator + 'SAMutator'  + File.separator + 'seeds'+ sep + 'PMD_Seeds'  + File.separator + bug
    # print(bug_dir_path)
    if not os.path.exists(bug_dir_path):
      os.mkdir(bug_dir_path)
  # for k in file2rule.keys(): # entry example: HardCodedCryptoKey5.java   security_HardCodedCryptoKey
  #   print("entry:" + str(k) + "   " + str(file2rule[k]))
  for filepath in filepaths:
      # sub_folder_path = '/home/+ File.separator/projects/SAMutator/seeds/' + 'Sub_Seeds_' + str(i)
    filename = filepath.split(reg_sep)[-1]
    # print("filename: " + str(filename))
    # if filename not in file2rule.keys():
    #   print("Not in")
    target_folder = userdir  + File.separator + 'projects'  + File.separator + 'SAMutator'  + File.separator + 'seeds'+ sep + 'PMD_Seeds'  + File.separator + file2rule[filename]
    shutil.move(filepath, target_folder  + File.separator + filename)

main()