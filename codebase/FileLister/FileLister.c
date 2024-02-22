#include <stdio.h>
#include <stdlib.h>
#include <dirent.h>
#include <string.h>
#include <sys/stat.h>

void listFilesRecursively(char *basePath);

int main() {
    // Start from the root directory
    char *path = "/";

    printf("Starting directory listing from %s\n", path);

    // List all files and directories recursively
    listFilesRecursively(path);

    return 0;
}

void listFilesRecursively(char *basePath) {
    char path[1000];
    struct dirent *dp;
    DIR *dir = opendir(basePath);

    // Unable to open directory stream
    if (!dir)
        return;

    while ((dp = readdir(dir)) != NULL) {
        // Do not list "." and ".." entries
        if (strcmp(dp->d_name, ".") != 0 && strcmp(dp->d_name, "..") != 0) {
            // Construct new path from our base path
            strcpy(path, basePath);
            strcat(path, "/");
            strcat(path, dp->d_name);

            struct stat path_stat;
            stat(path, &path_stat);

            // Check if it's a directory or a file
            if (S_ISDIR(path_stat.st_mode)) {
                printf("Directory: %s\n", path);
                // Recursively call with the new path
                listFilesRecursively(path);
            } else {
                printf("File: %s\n", path);
            }
        }
    }

    closedir(dir);
}
