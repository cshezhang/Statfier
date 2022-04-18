import sys
import subprocess

def main():
    cmd = ["/bin/bash", "-c"]
    input_cmd = ""
    f = open("res.log", "w")
    for i in range(1, len(sys.argv)):
      input_cmd = input_cmd + sys.argv[i] + " "
    f.write(input_cmd)
    cmd.append(input_cmd)
    v = subprocess.call(cmd)
    f.write("Python Exec: " + str(v))
    f.close()
main()