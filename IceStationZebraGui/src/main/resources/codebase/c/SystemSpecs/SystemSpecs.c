#include <stdio.h>
#include <unistd.h>
#include <sys/sysinfo.h>

int main() {
    // Print the CPU architecture
    printf("CPU architecture: ");
    #if defined(__arm__)
    printf("ARM\n");
    #elif defined(__aarch64__)
    printf("ARM64\n");
    #elif defined(__x86_64__)
    printf("x86_64\n");
    #elif defined(__i386__)
    printf("x86\n");
    #else
    printf("Unknown\n");
    #endif

    // Number of processors
    long nprocs = sysconf(_SC_NPROCESSORS_ONLN);
    printf("Number of processors available: %ld\n", nprocs);

    // Total system memory
    struct sysinfo info;
    if (sysinfo(&info) == 0) {
        printf("Total system memory: %lu MB\n", info.totalram / 1024 / 1024);
        printf("Free system memory: %lu MB\n", info.freeram / 1024 / 1024);
    }

    // System uptime
    printf("System uptime: %ld seconds\n", info.uptime);

    return 0;
}
