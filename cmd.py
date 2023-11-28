import sys
import subprocess

def main():
    cmd = ["/bin/bash", "-c"]
    input_cmd = ""
    for i in range(1, len(sys.argv)):
      input_cmd = input_cmd + sys.argv[i] + " "
    cmd.append(input_cmd)
    v = subprocess.call(cmd)

main()