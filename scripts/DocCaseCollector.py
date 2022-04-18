import re
import os
import urllib.request

from bs4 import BeautifulSoup

seed_path = os.getcwd() + os.sep + "PMD_Mutants" + os.sep + "iter0" + os.sep

def recover_code(codes):
  for code in codes:
    print(code)

def parseToCases(html_doc):
  seed_count = 0
  soup = BeautifulSoup(html_doc, "html.parser", from_encoding="utf-8")
  titles = soup.find_all("h2")
  examples = soup.find_all("p")
  for example in examples:
    if example.get_text() == "Example(s):":
      code = example.find_next_sibling()
      class_attrs = code.attrs["class"]
      if "language-java" in class_attrs and "highlighter-rouge" in class_attrs:
        span_nodes = code.find_all("span")
        code_tokens = []
        seed_file = open(seed_path + "seed" + str(seed_count) + ".java", "w")
        for span_node in span_nodes:
          code_token = span_node.get_text()
          code_tokens.append(code_token)
          if code_token == "{":
            seed_file.write(code_token + "\n")
            continue
          if code_token == "}":
            seed_file.write("\n" + code_token + "\n")
            continue
          if "..." in code_token:
            seed_file.write("// Code here\n")
            continue
          seed_file.write(code_token + " ")
        seed_file.close()
        seed_count = seed_count + 1
  print("Code Count=" + str(seed_count))

def main():
  response = urllib.request.urlopen("https://pmd.github.io/latest/pmd_rules_java_bestpractices.html")
  html_doc = response.read()
  return_code = response.getcode()
  if return_code == 200:
    parseToCases(html_doc)

main()