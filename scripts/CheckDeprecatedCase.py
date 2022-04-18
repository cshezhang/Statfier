import os
import sys

def main():
    seed_data = open("./PMDSeedData.csv", "r")
    content = seed_data.read()
    seed_data.close()
    deprecated_rules = open("./PMD_DeprecatedRules.txt", "r")
    lines = deprecated_rules.readlines()
    for tmp in lines:
        line = tmp.strip()
        if line in content:
            print(line)
    deprecated_rules.close()

main()