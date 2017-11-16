#include <iostream>
#include <fstream>
#include <stdio.h>

// define buffer size in bytes
#define BUFFER_SIZE 100

// define the number of buffers to be written
#define NUM_BUFFERS 1000 * 1000

int main(int argc, char** argv) {
    std::cout << "Hello World" << std::endl;

    std::ifstream file("test_binary.bin", std::ios::in | std::ios::binary);

    char buffer[BUFFER_SIZE];

    for (auto i=0; i<NUM_BUFFERS; i++) {
        file.read(buffer, BUFFER_SIZE);

        /*
        for (auto j=0; j<BUFFER_SIZE; j++)
            printf("%d", buffer[j]);
        std::cout << std::endl;
        */
    }

    file.close();

    std::cout << "Bye Bye World" << std::endl;
}
