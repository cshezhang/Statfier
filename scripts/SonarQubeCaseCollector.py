import os
import sys
import urllib3

from bs4 import BeautifulSoup

userdir = os.getcwd()
sep = os.sep

def get_html_content(url):
    http = urllib3.PoolManager()
    response = http.request('GET', url)
    content = response.data.decode()
    response.release_conn()
    return content

def get_rule_names():
    rule_names = set()
    content = get_html_content("https://rules.sonarsource.com/java/")
    soup = BeautifulSoup(content, "html.parser")
    a_tags = soup.find_all("a")
    for tag in a_tags:
        if "href" in tag.attrs.keys():
            href_tag = tag.attrs["href"]
            if "/java/RSPEC" in href_tag:
                rule_names.add(href_tag)
    return rule_names

def crawl_code(rule_names):
    codes = []
    seed_path = userdir + sep + "SonarQubeSeeds"
    if not os.path.exists(seed_path):
        os.mkdir(seed_path)
    for rule_name in rule_names:
        url = "https://rules.sonarsource.com" + rule_name  # /java/RSPEC-5960
        # content = get_html_content(url)
        filename = rule_name[6:] + ".html"
        # filename = "RSPEC-5826" + ".html"
        seed_file = open(seed_path + sep + filename, "r")
        # seed_file.write(content)
        # seed_file.close()
        # print("Processing URL: " + url)
        soup = BeautifulSoup(seed_file, "html.parser")
        h2_tags = soup.find_all("h2")
        rule = rule_name[6:]
        code1 = []
        code2 = []
        notFound1 = True
        notFound2 = True
        for h2_tag in h2_tags:
            if "Compliant Solution" in h2_tag.get_text():
                notFound1 = False
                code_tag = h2_tag.next_sibling.next_sibling
                if code_tag.name == "pre":
                    code1.append(code_tag.get_text())  # right case
                else:
                    if code_tag.next_sibling.next_sibling.name == "pre":
                        code1.append(code_tag.next_sibling.next_sibling.get_text())
            if ("Noncompliant Code Example" in h2_tag.get_text()) or ("Sensitive Code Example" in h2_tag.get_text()):
                notFound2 = False
                code_tag = h2_tag.next_sibling.next_sibling
                if code_tag.name == "pre":
                    code2.append(code_tag.get_text())  # wrong case
                else:
                    if code_tag.next_sibling.next_sibling.name == "pre":
                        code2.append(code_tag.next_sibling.next_sibling.get_text())
        codes.append((rule, code1, code2))
        rule_folder = seed_path + sep + rule
        if not os.path.exists(rule_folder):
            os.mkdir(rule_folder)
        for i in range(0, len(code1)):
            right_code = code1[i]
            file1 = open(rule_folder + sep + "right" + str(i) + ".java", "w")
            file1.write(right_code)
            file1.close()
        for i in range(0, len(code2)):
            wrong_code = code2[i]
            file2 = open(rule_folder + sep + "wrong" + str(i) + ".java", "w")
            file2.write(wrong_code)
            file2.close()
        if notFound1 and notFound2:
            print("Special Rule1: " + rule + ", " + url)
        else:
            if len(code1) == 0 and len(code2) == 0:
                print("Special Rule2: " + rule + ", " + url)
    return codes
        
# Compliant Solution
# Noncompliant Code Example
def main():
    rule_names = get_rule_names()
    codes = crawl_code(rule_names)
    
main()