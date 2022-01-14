import os
import urllib3

from bs4 import BeautifulSoup

bug_categories = ["annotation", "blocks", "design", "coding", "header", "imports", "javadoc", "metrics", "misc", "modifier", "naming", "regexp", "sizes", "whitespace"]
userdir = os.getcwd()
sep = os.sep
seed_folder_path = userdir + sep + "CheckStyleSeed"

def get_html_content(url):
    http = urllib3.PoolManager()
    response = http.request('GET', url)
    content = response.data.decode()
    response.release_conn()
    return content

def parse_html_file(bug_category, content):
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
        if len(code_tags) % 2 == 1:
            print("Config and Code Mismatch: " + rule_tag.get_text())
            continue
        config_codes = []
        java_codes = []
        for code_tag in code_tags:
            if "module name" in code_tag.get_text().lower():
                config_codes.append(code_tag.get_text())
            else:
                java_codes.append(code_tag.get_text())
        if len(config_codes) != len(java_codes):
            print("Config and Code Mismatch: " + rule_tag.get_text())
            continue
        # while tmp_tag != None and tmp_tag.name != "h3":
        #     if tmp_tag.name == "p":
        #         content = tmp_tag.get_text().lower()
        #         if "configure" in tmp_tag.get_text() or "configuration" in tmp_tag.get_text():
        #             config_tag = tmp_tag.next_sibling.next_sibling
        #             if config_tag.name != "div":
        #                  print("Error: Missing Config!")
        #             # print(str(config_tag.get_text()))
        #             config_codes.append(config_tag.get_text().strip())
        #             tmp1_tag = config_tag.next_sibling.next_sibling
        #             code_tag = tmp1_tag.next_sibling.next_sibling
        #             if code_tag.name != "div":
        #                 print("Error: Missing Code!")
        #             if "example" in tmp1_tag.get_text().lower():
        #                 # print(code_tag.get_text())
        #                 java_codes.append(code_tag.get_text())
        #     tmp_tag = tmp_tag.next_sibling
                
def main():
    if not os.path.exists(seed_folder_path):
        os.mkdir(seed_folder_path)
    for bug_category in bug_categories:
        seed_file_path = seed_folder_path + sep + bug_category + ".html"
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
        print("Category: " + bug_category)
        parse_html_file(bug_category, content)
        break

main()