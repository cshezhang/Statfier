import os
import urllib3

from bs4 import BeautifulSoup

bug_categories = ["annotation", "blocks", "design", "coding", "header", "imports", "javadoc", "metrics", "misc", "modifier", "naming", "regexp", "sizes", "whitespace"]
userdir = os.getcwd()
sep = os.sep
seed_folder_path = userdir  + sep + "CheckStyleSeed"
code_counter = 0
not_covered_rule = 0

def get_html_content(url):
    http = urllib3.PoolManager()
    response = http.request('GET', url)
    content = response.data.decode()
    response.release_conn()
    return content

def parse_html_file(bug_category, content):
    global code_counter, not_covered_rule
    print("Processing Category: " + bug_category)
    rule_tags = []
    example_tags = []
    soup = BeautifulSoup(content, "html.parser")
    tags = soup.find_all()
    for tag in tags:
        if tag.name == "h2" and tag.get_text() != "Content" and tag.get_text() != "Overview":
            rule_tags.append(tag)
        if tag.name == "h3" and tag.get_text() == "Examples":
            example_tags.append(tag)
    # Following code is used to test rule coverage
    # if len(example_tags) != len(rule_tags) - 1:
    #     print("Error: " + bug_category + " Rule and example are not matched!")
    #     print("Example Size: " + str(len(example_tags)))
    #     print("Rule Size: " + str(len(rule_tags)))
    for rule_tag in rule_tags:
        example_tag = rule_tag.next_element
        foundExample = False
        while True:
            if example_tag.name == "h3" and example_tag.get_text().strip() == "Examples":
                foundExample = True
                break
            example_tag = example_tag.next_element
            if example_tag == None or example_tag.name == "h2":
                break
        if not foundExample:
            print(rule_tag.get_text() + " does not have examples!")
            continue
        code_tags = []
        code_tag = example_tag.next_element
        while True:
            # if code_tag.name == "div":
            #     a = 10
            #     print(code_tag.attrs)
            if code_tag.name == "div" and "class" in code_tag.attrs.keys() and "source" in code_tag.attrs["class"]:
                code_tags.append(code_tag)
            code_tag = code_tag.next_element
            if code_tag == None or code_tag.name == "h3":
                break
        rule2config = dict()
        rule2code = dict()
        config_codes = []
        java_codes = []
        # config2code = dict()
        # current_index = None
        for code_tag in code_tags:
            if "module name" in code_tag.get_text().lower():
                # current_index = code_tag
                # config2code[code_tag] = []
                config_codes.append(code_tag.get_text())
            else:
                java_codes.append(code_tag.get_text())
        rule_name = rule_tag.get_text().strip()
        rule2config[rule_name] = config_codes
        rule2code[rule_name] = java_codes
        code_counter  = code_counter + len(java_codes)
        if len(config_codes) != len(java_codes):
            if(len(java_codes) == 0):
                not_covered_rule = not_covered_rule + 1
                print("Config and Code Mismatch: " + rule_tag.get_text())
                print("Config Length: " + str(len(config_codes)) + " Java Code Length: " + str(len(java_codes)))
            continue
        for i in range(0, len(config_codes)):
            config_code = config_codes[i]
            java_code = java_codes[i]
            folder_path = userdir + sep + "CheckStyleDocSeeds" + sep + rule_name
            if not os.path.exists(folder_path):
                os.mkdir(folder_path)
            config_file = open("." + sep + "CheckStyleDocSeeds"  + sep + rule_name+ sep + str(i) + ".xml", "w", encoding="utf-8")
            java_file = open("." + sep + "CheckStyleDocSeeds"  + sep + rule_name + sep + str(i) + ".java", "w", encoding="utf-8")
            config_file.write(config_code)
            java_file.write(java_code)
            config_file.close()
            java_file.close()
                
def main():
    if not os.path.exists(seed_folder_path):
        os.mkdir(seed_folder_path)
    for bug_category in bug_categories:
        seed_file_path = seed_folder_path  + sep + bug_category + ".html"
        content = ""
        if os.path.exists(seed_file_path):
            seed_file = open(seed_file_path, "r")
            content = seed_file.read()
            seed_file.close()
        else:
            url = "https://checkstyle.sourceforge.io/config_" + bug_category + ".html"
            content = get_html_content(url)
            seed_file = open(seed_file_path, "w")
            seed_file.write(content)
            seed_file.close()
        parse_html_file(bug_category, content)
    print("Code=" + str(code_counter))
    print("Not Covered Rule:" + str(not_covered_rule))
main()