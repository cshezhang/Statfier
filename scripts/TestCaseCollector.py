import os
import urllib.request

from bs4 import BeautifulSoup

save_path = os.getcwd() + os.sep + "PMD_Mutants" + os.sep + "iter0" + os.sep

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
    cate2rules[category] = []
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
    cate2rules[category].extend(tokens)
  return cate2rules

def getTestCases(cate2rules):
  fails = []
  if not os.path.exists(save_path):
    os.mkdir(save_path)
  for cate, rulesets in cate2rules.items():
    print(cate)
    for rule in rulesets:
      xml_link = "https://raw.githubusercontent.com/pmd/pmd/81739da5caff948dbcd2136c17532b65c726c781/pmd-java/src/test/resources/net/sourceforge/pmd/lang/java/rule/" + cate + "/xml/" + rule + ".xml"
      try:
        xml_response = urllib.request.urlopen(xml_link)
      except urllib.error.HTTPError:
        # print("Cannot find XML file in the rule: " + cate + "  " + rule)
        fails.append((cate + "  " + rule, xml_link))
      xml_doc = xml_response.read()
      soup = BeautifulSoup(xml_doc, "xml", from_encoding="utf-8")
      test_codes = soup.find_all("test-code")
      for index in range(0, len(test_codes)):
        test_code = test_codes[index]
        code = test_code.code
        if code == None:
          continue
        testfile = open(save_path + rule + str(index) + ".java", "w")
        testfile.write(str(code.get_text()))
        testfile.close()
  if len(fails) > 0:
    print("failed len=" + str(len(fails)) + " and rules are:")
    for fail_info, link in fails:
      print(fail_info)
      print(link)

def main():
  cate2rules = getRuleNames()
  getTestCases(cate2rules)

main()