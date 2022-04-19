#include  <stdlib.h>
#include  <string.h>
#include  <stdio.h>
char cmd[255];
char *blank = " ";
int main(int argc, char * argv[]) {
    strcat(cmd, argv[1]);
    strcat(cmd, blank);
    strcat(cmd, argv[2]);
    strcat(cmd, blank);
    strcat(cmd, "\"");
    for(int i = 3; i < argc - 1; i++) {
        strcat(cmd, argv[i]);
        strcat(cmd, " ");
    }
    strcat(cmd, argv[argc - 1]);
    strcat(cmd, "\"");
//    printf("%s\n", cmd);
    system(cmd);
    return 0;
}